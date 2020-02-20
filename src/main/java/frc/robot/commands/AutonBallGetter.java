/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Const;
import frc.robot.subsystems.DriveTrain;
import net.bancino.robotics.swerveio.geometry.SwerveVector;
import net.bancino.robotics.swerveio.gyro.NavXGyro;

/**
 * Gets the BloberooX value from the raspberry pi and the onboard camera
 * and tracks it, and sucks it right up.
 * @author Ethan Snyder
 */
public class AutonBallGetter extends CommandBase {

    DriveTrain drivetrain;
    NavXGyro navXGyro;
    double[] bloberooX, defaultBloberoo;
    double ballAngle, gyroAngle, angleDiff;
    boolean isFinished = false;
    int scanTracker = 0;
    int maxTrackScans = 10;

    public AutonBallGetter(DriveTrain drivetrain, NavXGyro navXGyro) {
        this.drivetrain = drivetrain;
        this.navXGyro = navXGyro;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        isFinished = false;
        bloberooX = NetworkTableInstance.getDefault().getEntry("BloberooX").getDoubleArray(defaultBloberoo);
        gyroAngle = navXGyro.getAngle();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        /** With every run through execute, update BloberooX, the backbone of this command. */
        bloberooX = NetworkTableInstance.getDefault().getEntry("BloberooX").getDoubleArray(defaultBloberoo);
        /** If there is no value, start counting scans. */
        if (bloberooX.length == 0) {
            scanTracker++;
        } else {
            scanTracker = 0;
        }
        /** After a set amount of scans, exit the commmand. */
        if (scanTracker >= maxTrackScans) {
            scanTracker = 0;
            isFinished = true;
        }
        /** Some math to find the real, actual angle of the blob from the camera. */
        ballAngle = bloberooX[0] * (Const.AutonBallGetter.CAMERA_ANGLE_X / Const.AutonBallGetter.CAMERA_RES_X);
        angleDiff = gyroAngle - ballAngle;
        SwerveVector trackWithZ = new SwerveVector(0, 0, (angleDiff * Const.AutonBallGetter.BALL_ADJUST_SPEED));
        drivetrain.drive(trackWithZ);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return isFinished;
    }

}