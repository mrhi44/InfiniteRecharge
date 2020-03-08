/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.vision;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Const;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Shooter;
import net.bancino.robotics.jlimelight.LedMode;
import net.bancino.robotics.jlimelight.Limelight;
import net.bancino.robotics.swerveio.SwerveDrive;
import net.bancino.robotics.swerveio.geometry.SwerveVector;

@SuppressWarnings("unused")
public class LimelightAlign extends CommandBase {

    private SwerveDrive drivetrain;
    private Limelight limelight;
    private Shooter shooter;
    private boolean doFrontHatch;
    private boolean isFinished = false;
    private boolean fwdIsGood = false, strIsGood = false, rcwIsGood = false;

    /*
     * The camtran cache stores a history of the forward and strafe camtran values
     * so that we can smooth out the trajectory of this command. The first size
     * index is the size of the cache and can therefore be set as needed. The second
     * size index is the size of the camtran and therefore should not be changed
     * unless the limelight API changes.
     * 
     * DO NOT modify the camtran cache directly, use a cache manipulation function.
     */
    private double[][] camtranCache = new double[10][6];
    /* DO NOT modify this variable, this is used by the camtranCache() function. */
    private int cacheIndex = -1;

    private double fwd, str, rcw;
    private double fwdSpeed, strSpeed, rcwSpeed;

    public LimelightAlign(SwerveDrive drivetrain, Limelight limelight, Shooter shooter, boolean doFrontHatch) {
        this.drivetrain = drivetrain;
        this.limelight = limelight;
        this.shooter = shooter;
        this.doFrontHatch = doFrontHatch;
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
    }

    @Override
    public void execute() {
        /** Camtran and rotation are always used. */
        double[] camtran = limelight.getCamTran();

        /**
         * Sets variables for doing the back hatch as well as the bounds in which
         * they're acceptably close.
         */
        rcw = limelight.getHorizontalOffset();
        if ((rcw <= Const.LimelightAlign.ACCEPTED_OFFSET_BOUNDS)
                && (rcw > -Const.LimelightAlign.ACCEPTED_OFFSET_BOUNDS)) {
            rcw = 0;
            rcwIsGood = true;
        }
        if (!doFrontHatch) {
            fwd = Math.abs(camtran[2]) - Const.LimelightAlign.DISTANCE_TO_TARGET;
            if ((fwd <= Const.LimelightAlign.ACCEPTED_OFFSET_BOUNDS)
                    && (fwd > -Const.LimelightAlign.ACCEPTED_OFFSET_BOUNDS)) {
                fwd = 0;
                fwdIsGood = true;
            }
            str = camtran[0];
            if ((str <= Const.LimelightAlign.ACCEPTED_OFFSET_BOUNDS)
                    && (str > -Const.LimelightAlign.ACCEPTED_OFFSET_BOUNDS)) {
                str = 0;
                strIsGood = true;
            }
        }

        if (limelight.hasValidTargets()) {
            /**
             * Multiply all of the values by their speed constants to get a decent speed
             * reference.
             */
            strSpeed = str * Const.LimelightAlign.STRAFE_ADJUST_SPEED;
            rcwSpeed = rcw * Const.LimelightAlign.ROTATE_ADJUST_SPEED;
            fwdSpeed = fwd * Const.LimelightAlign.FORWARD_ADJUST_SPEED;
        } else {
            strSpeed = rcwSpeed = fwdSpeed = 0;
            strIsGood = rcwIsGood = fwdIsGood = true;
            //isFinished = true;
        }

        // SwerveVector alignmentVector = new SwerveVector(str, fwd, rcw); for testing
        // on swervio
        SwerveVector alignmentVector = new SwerveVector(fwdSpeed, strSpeed, rcwSpeed);
        drivetrain.drive(alignmentVector);
        if (!doFrontHatch) {
            isFinished = fwdIsGood && strIsGood && rcwIsGood;
        } else {
            isFinished = rcwIsGood;
        }
        // shooter.setHoodPositionFromDistance(-camtran[2]);

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
