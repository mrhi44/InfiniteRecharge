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
import net.bancino.robotics.swerveio.pid.DefaultPIDController;

@SuppressWarnings("unused")
public class LimelightAlign extends CommandBase {

    /* Import parameters from the config file. */
    private static final int rollingAverageWindow = RobotContainer.config().getInt("rollingAverageWindow");
    private static final double rampRate = RobotContainer.config().getDouble("limelightAlignRampRate");
    private static final double maxOutput = RobotContainer.config().getDouble("limelightAlignPIDMax");
    private static final double desiredDistancetoTarget = RobotContainer.config().getDouble("limelightFWDSetpoint");
    private static final double desiredAngletoTarget = RobotContainer.config().getDouble("limelightRCWSetpoint");
    private static final double desiredStrafetoTarget = RobotContainer.config().getDouble("limelightSTRSetpoint");
    private static final double forwardP = RobotContainer.config().getDouble("limelightAlignFwdP");
    private static final double forwardI = RobotContainer.config().getDouble("limelightAlignFwdI");
    private static final double rotateP = RobotContainer.config().getDouble("limelightAlignRcwP");
    private static final double rotateI = RobotContainer.config().getDouble("limelightAlignRcwI");
    private static final double strafeP = RobotContainer.config().getDouble("limelightAlignStrP");
    private static final double strafeI = RobotContainer.config().getDouble("limelightAlignStrI");

    private SwerveDrive drivetrain;
    private Limelight limelight;
    private Shooter shooter;
    private boolean doFrontHatch;
    private boolean isFinished = false;
    private boolean fwdIsGood = false, strIsGood = false, rcwIsGood = false;

    /** Variables to hold distance and the vector speed on that axis. */
    private double fwd, str, rcw;
    private double fwdSpeed, strSpeed, rcwSpeed;

    /** Timeout variables */
    private final long timeout;
    private long startTime = 0;

    /** Makes a new RollingAverage for every axis that uses the average limelight readings over so many readings. */
    private RollingAverage rollingAverageFWD = new RollingAverage(rollingAverageWindow);
    private RollingAverage rollingAverageRCW = new RollingAverage(rollingAverageWindow);
    private RollingAverage rollingAverageSTR = new RollingAverage(rollingAverageWindow);

    /** Make a PID controller for every axis that LimelightAlign corrects. P and I doubles defined in config file. */
    private DefaultPIDController pidFWD = new DefaultPIDController(forwardP, forwardI, 0);
    private DefaultPIDController pidRCW = new DefaultPIDController(rotateP, rotateI, 0);
    private DefaultPIDController pidSTR = new DefaultPIDController(strafeP, strafeI, 0);

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
        /** Setting the ramp rate for each axis. Defined in config file. */
        pidFWD.setOutputRampRate(rampRate);
        pidRCW.setOutputRampRate(rampRate);
        pidSTR.setOutputRampRate(rampRate);

        /** Setting the maximum output value each axis. Defined in config file. */
        pidFWD.setOutputLimits(maxOutput);
        pidRCW.setOutputLimits(maxOutput);
        pidSTR.setOutputLimits(maxOutput);

        limelight.setPipeline(0);
        drivetrain.setFieldCentric(false);
        limelight.setLedMode(LedMode.FORCE_ON);
        /** For the front hatch, there's no need for head-on alignment where forward and strafe would matter. */
        if (doFrontHatch) {
            fwd = 0;
            fwdSpeed = 0;
            str = 0;
            strSpeed = 0;
        }
        startTime = System.currentTimeMillis();
    }

    @Override
    public void execute() {

        /** If there's no target, no bueno. Exit the command. */
        isFinished = limelight.hasValidTargets();

        /** 
         * RCW must be done for front and back hatches, so it gets calculated here before any other axis.
         * Values are put into the rolling average first to smooth out jumpy readings, and then put into the PID
         * loop for more efficient readings. (For all axis').
         */
        rollingAverageRCW.add(limelight.getHorizontalOffset());
        rcw = pidRCW.getOutput(rollingAverageRCW.get(), desiredAngletoTarget);

        /**
         * Sets forward and strafe depending on whether or not you're doing the back port.
         * Forwards and strafe were set equal to zero in init.
         */
        if (!doFrontHatch) {
            double[] camtran = limelight.getCamTran();
            /** Forwards calculations. */
            rollingAverageFWD.add(camtran[2]);
            fwd = pidFWD.getOutput(rollingAverageFWD.get(), desiredDistancetoTarget);
            /** Strafe calculations. */
            rollingAverageSTR.add(camtran[0]);
            str = pidSTR.getOutput(rollingAverageSTR.get(), desiredStrafetoTarget);
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

        /** If the timeout is hit, end the command. */
        if (timeout != -1 && System.currentTimeMillis() - startTime >= timeout) {
            isFinished = true;
        }

        /** Robot go vroom on the vector with swerve drive. */
        SwerveVector alignmentVector = new SwerveVector(fwdSpeed, strSpeed, rcwSpeed);
        drivetrain.drive(alignmentVector);
    }

    @Override
    public void end(boolean interrupted) {
        limelight.setLedMode(LedMode.PIPELINE_CURRENT);
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
