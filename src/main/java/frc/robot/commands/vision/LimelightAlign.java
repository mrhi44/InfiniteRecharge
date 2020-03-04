/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.vision;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Const;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Shooter;
import net.bancino.robotics.jlimelight.LedMode;
import net.bancino.robotics.jlimelight.Limelight;
import net.bancino.robotics.swerveio.geometry.SwerveVector;

public class LimelightAlign extends CommandBase {

    DriveTrain drivetrain;
    Limelight limelight;
    Shooter shooter;

    boolean doFrontHatch;

    double[] camtran;
    double fwd, str, rcw;
    double fwdSpeed, strSpeed, rcwSpeed;
    double jitterX = 0;

    public LimelightAlign(DriveTrain drivetrain, Limelight limelight, Shooter shooter, boolean doFrontHatch) {
        this.drivetrain = drivetrain;
        this.limelight = limelight;
        this.shooter = shooter;
        this.doFrontHatch = doFrontHatch;
        addRequirements(drivetrain, shooter);
    }

    @Override
    public void initialize() {
        drivetrain.setFieldCentric(false);
        limelight.setLedMode(LedMode.FORCE_ON);
        if (doFrontHatch) {
            limelight.setPipeline(0);
            fwd = 0;
            fwdSpeed = 0;
            str = 0;
            strSpeed = 0;
        } else {
            limelight.setPipeline(1);
        }
    }

    @Override
    public void execute() {
        /** Camtran and rotation are always used. */
        camtran = limelight.getCamTran();
        /**
         * Assigns rotation value and its acceptable bounds. Rotation is computed no
         * matter what, for both front and back hatches.
         */
        rcw = limelightAntiJitter(limelight.getHorizontalOffset());
        if ((rcw <= Const.LimelightAlign.ACCEPTED_OFFSET_BOUNDS) && (rcw > -Const.LimelightAlign.ACCEPTED_OFFSET_BOUNDS)) {
            rcw = 0;
        }
        /**
         * Sets variables for doing the back hatch as well as the bounds in which
         * they're acceptably close.
         */
        if (!doFrontHatch) {
            fwd = limelightAntiJitter(Math.abs(camtran[2])) - Const.LimelightAlign.DISTANCE_TO_TARGET;
            if ((fwd <= Const.LimelightAlign.ACCEPTED_OFFSET_BOUNDS) && (fwd > -Const.LimelightAlign.ACCEPTED_OFFSET_BOUNDS)) {
                fwd = 0;
            }
            str = limelightAntiJitter(camtran[0]);
            if ((str <= Const.LimelightAlign.ACCEPTED_OFFSET_BOUNDS) && (str > -Const.LimelightAlign.ACCEPTED_OFFSET_BOUNDS)) {
                str = 0;
            }
        }
        /**
         * Multiply all of the values by their speed constants to get a decent speed
         * reference.
         */
        strSpeed = str * Const.LimelightAlign.STRAFE_ADJUST_SPEED;
        rcwSpeed = rcw * Const.LimelightAlign.ROTATE_ADJUST_SPEED;
        fwdSpeed = fwd * Const.LimelightAlign.FORWARD_ADJUST_SPEED;

        SwerveVector alignmentVector = new SwerveVector(fwdSpeed, strSpeed, -rcwSpeed);
        // SwerveVector alignmentVector = new SwerveVector(str, fwd, rcw); for testing on swervio
        drivetrain.drive(alignmentVector);
        // shooter.setHoodPosition(camtran[2]
        SmartDashboard.putNumber("LimelightAlign/ForwardValue", fwdSpeed);
        SmartDashboard.putNumber("LimelightAlign/StrafeValue", strSpeed);
        SmartDashboard.putNumber("LimelightAlign/RotateValue", rcwSpeed);
        
        SmartDashboard.putNumber("LimelightAlign/ForwardRaw", fwd);
        SmartDashboard.putNumber("LimelightAlign/StrafeRaw", str);
        SmartDashboard.putNumber("LimelightAlign/RotateRaw", rcw);
    }

    @Override
    public void end(boolean interrupted) {
        limelight.setLedMode(LedMode.PIPELINE_CURRENT);
        drivetrain.setFieldCentric(true);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    /**
     * Keeps an input from jumping and jittering, specifically limelight input.
     * 
     * @param x Limelight input to jitter-proof.
     * @return A double, probably the same one you put in, but jitterproofed.
     */
    public double limelightAntiJitter(double x) {
        /** Handles first time run. */
        if (jitterX == 0) {
            jitterX = x;
            /**
             * If the difference between scans is greater than a threshold value, set it to
             * last scan.
             */
        } else if (Math.abs(x - jitterX) > Const.LimelightAlign.JITTER_VARIATION_THRESHOLD) {
            x = jitterX;
        } else {
            jitterX = 0;
        }
        return x;
    }
}