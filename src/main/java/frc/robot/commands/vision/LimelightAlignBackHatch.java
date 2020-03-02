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

public class LimelightAlignBackHatch extends CommandBase {

    DriveTrain drivetrain;
    Limelight limelight;
    Shooter shooter;
    double[] camtran;
    double fwd, str, rcw;
    double fwdSpeed, strSpeed, rcwSpeed;

    public LimelightAlignBackHatch(DriveTrain drivetrain, Limelight limelight, Shooter shooter) {
        this.drivetrain = drivetrain;
        this.limelight = limelight;
        this.shooter = shooter;
        addRequirements(drivetrain, shooter);
    }

    @Override
    public void initialize() {
        drivetrain.setFieldCentric(false);
        limelight.setLedMode(LedMode.FORCE_ON);
        limelight.setPipeline(1);
    }

    @Override
    public void execute() {
        camtran = limelight.getCamTran();

        /** Assigns strafe and assigns acceptable offset conditions. */
        str = camtran[0];
        if ((str <= Const.LimelightAlign.ACCEPTED_OFFSET_BOUNDS) && (str > -Const.LimelightAlign.ACCEPTED_OFFSET_BOUNDS)) {
            str = 0;
        }
        /** Assigns rotation and assigns acceptable offset conditions. */
        rcw = limelight.getHorizontalOffset();
        if ((rcw <= Const.LimelightAlign.ACCEPTED_OFFSET_BOUNDS) && (rcw > -Const.LimelightAlign.ACCEPTED_OFFSET_BOUNDS)) {
            rcw = 0;
        }
        /** Assigns forward and assigns acceptable offset conditions. */
        fwd = Math.abs(camtran[2]) - Const.LimelightAlign.DISTANCE_TO_TARGET;
        if ((fwd <= Const.LimelightAlign.ACCEPTED_OFFSET_BOUNDS) && (fwd > -Const.LimelightAlign.ACCEPTED_OFFSET_BOUNDS)) {
            fwd = 0;
        }

        /** Multiply all of the values by their speed constants to get a decent speed reference. */
        strSpeed = str * Const.LimelightAlign.STRAFE_ADJUST_SPEED;
        rcwSpeed = rcw * Const.LimelightAlign.ROTATE_ADJUST_SPEED;
        fwdSpeed = fwd * Const.LimelightAlign.FORWARD_ADJUST_SPEED;

        SwerveVector alignmentVector = new SwerveVector(fwdSpeed, strSpeed, -rcwSpeed);
        //SwerveVector alignmentVector = new SwerveVector(str, fwd, rcw); for testing on swervio
        drivetrain.drive(alignmentVector);
        //shooter.setHoodPosition(camtran[2]);

        /** 
        SmartDashboard.putNumber("LimelightAlign/ForwardValue", fwdSpeed);
        SmartDashboard.putNumber("LimelightAlign/StrafeValue", strSpeed);
        SmartDashboard.putNumber("LimelightAlign/RotateValue", rcwSpeed);

        SmartDashboard.putNumber("LimelightAlign/ForwardRaw", fwd);
        SmartDashboard.putNumber("LimelightAlign/StrafeRaw", str);
        SmartDashboard.putNumber("LimelightAlign/RotateRaw", rcw);
        */
    }


    @Override
    public void end(boolean interrupted) {
        drivetrain.setFieldCentric(true);
        limelight.setLedMode(LedMode.PIPELINE_CURRENT);
    }
    @Override
    public boolean isFinished() {
        return false;
    }
    
}