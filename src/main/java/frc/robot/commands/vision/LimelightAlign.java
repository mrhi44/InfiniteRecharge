/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.vision;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Shooter;
import frc.robot.util.RollingAverage;
import net.bancino.robotics.jlimelight.LedMode;
import net.bancino.robotics.jlimelight.Limelight;
import net.bancino.robotics.swerveio.SwerveDrive;
import net.bancino.robotics.swerveio.geometry.SwerveVector;

@SuppressWarnings("unused")
public class LimelightAlign extends CommandBase {

    /* Import parameters from the config file. */
    private static final int rollingAverageWindow = RobotContainer.config().getInt("rollingAverageWindow");
    private static final double distanceToTarget = RobotContainer.config().getDouble("limelightAlignDistanceToTarget");
    private static final double acceptedOffsetBounds = RobotContainer.config().getDouble("limelightAlignAcceptedOffsetBounds");
    private static final double strafeAdjustSpeed = RobotContainer.config().getDouble("limelightAlignStrafeAdjustSpeed");
    private static final double forwardAdjustSpeed = RobotContainer.config().getDouble("limelightAlignForwardAdjustSpeed");
    private static final double rotateAdjustSpeed = RobotContainer.config().getDouble("limelightAlignRotateAdjustSpeed");

    private SwerveDrive drivetrain;
    private Limelight limelight;
    private Shooter shooter;
    private boolean doFrontHatch;
    private boolean isFinished = false;
    private boolean fwdIsGood = false, strIsGood = false, rcwIsGood = false;

    private double fwd, str, rcw;
    private double fwdSpeed, strSpeed, rcwSpeed;

    private final long timeout;
    private long startTime = 0;
    private RollingAverage rollingAverageRCW, rollingAverageSTR, rollingAverageFWD  = new RollingAverage(rollingAverageWindow);

    public LimelightAlign(SwerveDrive drivetrain, Limelight limelight, Shooter shooter, boolean doFrontHatch, long timeout) {
        this.drivetrain = drivetrain;
        this.limelight = limelight;
        this.shooter = shooter;
        this.doFrontHatch = doFrontHatch;
        if (timeout < 0) {
            this.timeout = -1;
        } else {
            this.timeout = timeout;
        }
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        limelight.setPipeline(0);
        drivetrain.setFieldCentric(false);
        limelight.setLedMode(LedMode.FORCE_ON);
        if (doFrontHatch) {
            limelight.setPipeline(0);
            fwd = 0;
            fwdSpeed = 0;
            str = 0;
            strSpeed = 0;
        }
        startTime = System.currentTimeMillis();
    }

    @Override
    public void execute() {
        /**
         * Sets variables for doing the back hatch as well as the bounds in which
         * they're acceptably close.
         */
        rollingAverageRCW.add(limelight.getHorizontalOffset());
        rcw = rollingAverageRCW.get();
        if ((rcw <= acceptedOffsetBounds)
                && (rcw > -acceptedOffsetBounds)) {
            rcw = 0;
            rcwIsGood = true;
        }
        /* Sets forward and strafe depending on whether or not you're doing the back port. */
        if (!doFrontHatch) {
            double[] camtran = limelight.getCamTran();
            rollingAverageFWD.add(camtran[2]);
            fwd = Math.abs(rollingAverageFWD.get()) - distanceToTarget;
            if ((fwd <= acceptedOffsetBounds)
                    && (fwd > -acceptedOffsetBounds)) {
                fwd = 0;
                fwdIsGood = true;
            }
            rollingAverageSTR.add(camtran[0]);
            str = rollingAverageSTR.get();
            if ((str <= acceptedOffsetBounds)
                    && (str > -acceptedOffsetBounds)) {
                str = 0;
                strIsGood = true;
            }
        }

        /**
         * Multiply all of the values by their speed constants to get a decent speed
         * reference, only if a target is seen. If not, set the speed references to zero
         * and set the strafe, forward, and rotate checks to true.
         */
        if (limelight.hasValidTargets()) {
            strSpeed = str * strafeAdjustSpeed;
            rcwSpeed = rcw * rotateAdjustSpeed;
            fwdSpeed = fwd * forwardAdjustSpeed;
        } else {
            strSpeed = rcwSpeed = fwdSpeed = 0;
            strIsGood = rcwIsGood = fwdIsGood = true;
            //isFinished = true;
        }

        /**
         * If you're not doing the front port, quit when all three values are
         * within the threshold (or when there is no target.) If you're only doing
         * the front port, end the program when the rotation is good.
         */
        if (!doFrontHatch) {
            isFinished = fwdIsGood && strIsGood && rcwIsGood;
        } else {
            isFinished = rcwIsGood;
        }
        /** If all three conditions are within bounds or the timeout is hit, end the program. */
        if (timeout != -1 && System.currentTimeMillis() - startTime >= timeout) {
            isFinished = true;
            strSpeed = rcwSpeed = fwdSpeed = 0;
        }

        // SwerveVector alignmentVector = new SwerveVector(str, fwd, rcw); for testing
        // on swervio
        SwerveVector alignmentVector = new SwerveVector(fwdSpeed, strSpeed, rcwSpeed);
        drivetrain.drive(alignmentVector);

        /**
         * Put here for testing. SmartDashboard.putNumber("LimelightAlign/ForwardValue",
         * fwdSpeed); SmartDashboard.putNumber("LimelightAlign/StrafeValue", strSpeed);
         * SmartDashboard.putNumber("LimelightAlign/RotateValue", rcwSpeed);
         * 
         * SmartDashboard.putNumber("LimelightAlign/ForwardRaw", fwd);
         * SmartDashboard.putNumber("LimelightAlign/StrafeRaw", str);
         * SmartDashboard.putNumber("LimelightAlign/RotateRaw", rcw);
         */
    }

    @Override
    public void end(boolean interrupted) {
        limelight.setLedMode(LedMode.PIPELINE_CURRENT);
        //drivetrain.setFieldCentric(true);
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
