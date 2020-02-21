/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Const;
import frc.robot.subsystems.DriveTrain;
import net.bancino.robotics.jlimelight.Limelight;
import net.bancino.robotics.swerveio.geometry.SwerveVector;
import net.bancino.robotics.swerveio.gyro.NavXGyro;

public class LimelightAlign extends CommandBase {

    DriveTrain drivetrain;
    NavXGyro navXGyro;
    Limelight limelight;
    double[] camtran;
    double fwd, str, rcw;
    boolean strafeGood, forwardGood, rotateGood, allGood;

    public LimelightAlign(DriveTrain drivetrain, NavXGyro navXGyro, Limelight limelight) {
        this.drivetrain = drivetrain;
        this.navXGyro = navXGyro;
        this.limelight = limelight;
    }

    @Override
    public void initialize() {
        double[] camtran = limelight.getCamTran();
    }

    @Override
    public void execute() {
        camtran = limelight.getCamTran();

        str = camtran[0] * Const.LimelightAlign.STRAFE_ADJUST_SPEED;
        fwd = camtran[1] * Const.LimelightAlign.FORWARD_ADJUST_SPEED;
        rcw = camtran[5] * Const.LimelightAlign.ROTATE_ADJUST_SPEED;

        SwerveVector alignmentVector = new SwerveVector(fwd, -str, rcw);
        drivetrain.drive(alignmentVector);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
    
}