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
import net.bancino.robotics.jlimelight.LedState;
import net.bancino.robotics.jlimelight.Limelight;
import net.bancino.robotics.swerveio.geometry.SwerveVector;

public class LimelightAlignBackHatch extends CommandBase {

    DriveTrain drivetrain;
    Limelight limelight;
    Shooter shooter;
    double[] camtran;
    double fwd, str, rcw;

    public LimelightAlignBackHatch(DriveTrain drivetrain, Limelight limelight, Shooter shooter) {
        this.drivetrain = drivetrain;
        this.limelight = limelight;
        this.shooter = shooter;
        addRequirements(drivetrain, shooter);
    }

    @Override
    public void initialize() {
        drivetrain.setFieldCentric(false);
        limelight.setLedMode(LedState.FORCE_ON);
        limelight.setPipeline(1);
    }

    @Override
    public void execute() {
        camtran = limelight.getCamTran();

        /** Assigns strafe and assigns acceptable offset conditions. */
        str = camtran[0] * Const.LimelightAlign.STRAFE_ADJUST_SPEED;
        if ((str <= Const.LimelightAlign.ACCEPTED_OFFSET_BOUNDS) && (str > Const.LimelightAlign.ACCEPTED_OFFSET_BOUNDS)) {
            str = 0;
        }
        /** Assigns rotation and assigns acceptable offset conditions. */
        rcw = camtran[4] * Const.LimelightAlign.ROTATE_ADJUST_SPEED;
        if ((rcw <= Const.LimelightAlign.ACCEPTED_OFFSET_BOUNDS) && (rcw > Const.LimelightAlign.ACCEPTED_OFFSET_BOUNDS)) {
            rcw = 0;
        }
        /** Assigns forward and assigns acceptable offset conditions. */
        fwd = (Math.abs(camtran[2]) - Const.LimelightAlign.DISTANCE_TO_TARGET) * Const.LimelightAlign.FORWARD_ADJUST_SPEED;
        if ((fwd <= Const.LimelightAlign.ACCEPTED_OFFSET_BOUNDS) && (fwd > Const.LimelightAlign.ACCEPTED_OFFSET_BOUNDS)) {
            fwd = 0;
        }

        SwerveVector alignmentVector = new SwerveVector(fwd, -str, rcw);
        //SwerveVector alignmentVector = new SwerveVector(str, fwd, rcw); for testing on swervio
        drivetrain.drive(alignmentVector);
        shooter.setHoodPosition(camtran[2]);
    }


    @Override
    public void end(boolean interrupted) {
        drivetrain.setFieldCentric(true);
        limelight.setLedMode(LedState.FORCE_OFF);
    }
    @Override
    public boolean isFinished() {
        return false;
    }
    
}