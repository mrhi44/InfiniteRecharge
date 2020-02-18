/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.kinematics.SwerveDriveKinematics;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Const;
import frc.robot.subsystems.DriveTrain;
import net.bancino.robotics.swerveio.SwerveVector;

/**
 * Gets the BloberooX value from the raspberry pi and the onboard camera
 * and tracks it, and sucks it right up.
 * @author Ethan Snyder
 */
public class AutonBallGetter extends CommandBase {

    DriveTrain drivetrain;
    double bloberooX;
    double bloberooY;
    double fovAngle;
    double angleOfBlob;
    double rcw = 0;
    boolean isFinished = false;
    int scanTracker = 0;
    int maxTrackScans = 10;

    public AutonBallGetter(DriveTrain drivetrain) {
        this.drivetrain = drivetrain;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        isFinished = false;
        bloberooX = NetworkTableInstance.getDefault().getEntry("BloberooX").getDouble(0);
        bloberooY = NetworkTableInstance.getDefault().getEntry("BloberooY").getDouble(0);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        /** With every run through execute, update BloberooX, the backbone of this command. */
        bloberooX = NetworkTableInstance.getDefault().getEntry("BloberooX").getDouble(0);
        bloberooY = NetworkTableInstance.getDefault().getEntry("BloberooY").getDouble(0);
        /** If there is no value, start counting scans. */
        if (bloberooX == 0) {
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
        rcw = bloberooX * (Const.AutonBallGetter.CAMERA_ANGLE_X / Const.AutonBallGetter.CAMERA_RES_X);
        SwerveVector trackWithZ = new SwerveVector(0, 0, rcw);
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