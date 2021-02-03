/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.RobotContainer;

import net.bancino.robotics.swerveio.SwerveDrive;
//import net.bancino.robotics.swerveio.SwerveDrive.DegreeOfFreedom;
import net.bancino.robotics.swerveio.module.SwerveModule;
import net.bancino.robotics.swerveio.pid.PIDController;
import net.bancino.robotics.swerveio.module.MK3SwerveModule;
import net.bancino.robotics.swerveio.log.DashboardSwerveLogger;
import net.bancino.robotics.swerveio.geometry.Length;
import net.bancino.robotics.swerveio.geometry.Length.Unit;
import net.bancino.robotics.swerveio.geometry.ChassisDimension;
import net.bancino.robotics.swerveio.gyro.Gyro;

/**
 * The drivetrain subsystem drives the robot! (wow!).
 *
 * This subsystem consists of the following components: - Swerve module (4x
 * drive + pivot motor)
 *
 * This subsystem should provide the following functions: - Run the drivetrain
 * with joystick - Run the drivetrain autonomously
 *
 * @author Jordan Bancino
 */
public class DriveTrain {

  private static final double drivetrainWidth = RobotContainer.config().getDouble("drivetrainWidth");
  private static final double drivetrainLength = RobotContainer.config().getDouble("drivetrainLength");

  private static final int frontRightDriveCanId = RobotContainer.config().getInt("frontRightDriveCanId");
  private static final int frontLeftDriveCanId = RobotContainer.config().getInt("frontLeftDriveCanId");
  private static final int rearLeftDriveCanId = RobotContainer.config().getInt("rearLeftDriveCanId");
  private static final int rearRightDriveCanId = RobotContainer.config().getInt("rearRightDriveCanId");

  private static final int frontRightPivotCanId = RobotContainer.config().getInt("frontRightPivotCanId");
  private static final int frontLeftPivotCanId = RobotContainer.config().getInt("frontLeftPivotCanId");
  private static final int rearLeftPivotCanId = RobotContainer.config().getInt("rearLeftPivotCanId");
  private static final int rearRightPivotCanId = RobotContainer.config().getInt("rearRightPivotCanId");

  private static final int frontRightEncoder = RobotContainer.config().getInt("frontRightEncoder");
  private static final int frontLeftEncoder = RobotContainer.config().getInt("frontLeftEncoder");
  private static final int rearLeftEncoder = RobotContainer.config().getInt("rearLeftEncoder");
  private static final int rearRightEncoder = RobotContainer.config().getInt("rearRightEncoder");

  private static final double swerveModuleRampRate = RobotContainer.config().getDouble("swerveModuleRampRate");
  private static final double swerveModuleP = RobotContainer.config().getDouble("swerveModuleP");
  private static final double swerveModuleI = RobotContainer.config().getDouble("swerveModuleI");
  private static final double swerveModuleD = RobotContainer.config().getDouble("swerveModuleD");

  private static final double swerveDriveRampRate = RobotContainer.config().getDouble("swerveDriveRampRate");
  private static final double swerveDriveAngleP = RobotContainer.config().getDouble("swerveDriveAngleP");
  private static final double swerveDriveAngleI = RobotContainer.config().getDouble("swerveDriveAngleI");
  private static final double swerveDriveAngleD = RobotContainer.config().getDouble("swerveDriveAngleD");

  private static final double frontRightAngleOffset = RobotContainer.config().getDouble("frontRightAngleOffset");
  private static final double frontLeftAngleOffset = RobotContainer.config().getDouble("frontLeftAngleOffset");
  private static final double rearLeftAngleOffset = RobotContainer.config().getDouble("rearLeftAngleOffset");
  private static final double rearRightAngleOffset = RobotContainer.config().getDouble("rearRightAngleOffset");

  /**
   * Create a new instance of a swerve drive.
   * 
   * @param gyro The gyro to use in field-centric calculations
   * @return A new SwerveDrive drivetrain based on our configuration.
   * @throws IllegalArgumentException If there is an error creating the swerve
   *                                  drive.
   */
  public static SwerveDrive create(Gyro gyro) throws IllegalArgumentException {
    return new SwerveDrive.Builder()
    	.setRampRate(swerveDriveRampRate)
        .useDefaultKinematics(
            new ChassisDimension(new Length(drivetrainWidth, Unit.INCHES), new Length(drivetrainLength, Unit.INCHES)))
        .setGyro(gyro).setAnglePID(swerveDriveAngleP, swerveDriveAngleI, swerveDriveAngleD)
        /* This function adds the modules to the module map. */
        .setModuleMap((map) -> {
          map.put(SwerveModule.Location.FRONT_RIGHT, new MK3SwerveModule(frontRightDriveCanId, frontRightPivotCanId,
              frontRightEncoder, frontRightAngleOffset));
          map.put(SwerveModule.Location.FRONT_LEFT,
              new MK3SwerveModule(frontLeftDriveCanId, frontLeftPivotCanId, frontLeftEncoder, frontLeftAngleOffset));
          map.put(SwerveModule.Location.REAR_LEFT,
              new MK3SwerveModule(rearLeftDriveCanId, rearLeftPivotCanId, rearLeftEncoder, rearLeftAngleOffset));
          map.put(SwerveModule.Location.REAR_RIGHT,
              new MK3SwerveModule(rearRightDriveCanId, rearRightPivotCanId, rearRightEncoder, rearRightAngleOffset));

          /* This function is run on every module. */
        }, (module) -> {
          PIDController modulePid = module.getPivotPIDController();
          modulePid.setOutputRampRate(swerveModuleRampRate);
          modulePid.setP(swerveModuleP);
          modulePid.setI(swerveModuleI);
          modulePid.setD(swerveModuleD);
        })
        /*
         * This function is run when the swerve drive object is created. It performs
         * additional setup.
         */
        .build((swerve) -> {
          // swerve.setReversed(DegreeOfFreedom.FORWARD, true);
          // swerve.setReversed(DegreeOfFreedom.STRAFE, true);
          // swerve.setReversed(DegreeOfFreedom.ROTATION, true);
          // swerve.setFieldCentric(false);

          // swerve.setIdleAngle(0, false);

          swerve.startLogging(new DashboardSwerveLogger());
        });
  }
}
