/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.RobotContainer;

import net.bancino.robotics.swerveio.SwerveDrive;
import net.bancino.robotics.swerveio.SwerveDrive.DegreeOfFreedom;
import net.bancino.robotics.swerveio.encoder.AnalogEncoder;
import net.bancino.robotics.swerveio.encoder.Encoder;
import net.bancino.robotics.swerveio.module.SwerveModule;
import net.bancino.robotics.swerveio.pid.PIDController;
import net.bancino.robotics.swerveio.module.MK2SwerveModule;
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

  private static final int frontRightDriveCanId = RobotContainer.config().getInt("frontRightDriveCanId");
  private static final int frontLeftDriveCanId = RobotContainer.config().getInt("frontLeftDriveCanId");
  private static final int rearLeftDriveCanId = RobotContainer.config().getInt("rearLeftDriveCanId");
  private static final int rearRightDriveCanId = RobotContainer.config().getInt("rearRightDriveCanId");

  private static final int frontRightPivotCanId = RobotContainer.config().getInt("frontRightPivotCanId");
  private static final int frontLeftPivotCanId = RobotContainer.config().getInt("frontLeftPivotCanId");
  private static final int rearLeftPivotCanId = RobotContainer.config().getInt("rearLeftPivotCanId");
  private static final int rearRightPivotCanId = RobotContainer.config().getInt("rearRightPivotCanId");

  private static final int frontRightAnalogEncoder = RobotContainer.config().getInt("frontRightAnalogEncoder");
  private static final int frontLeftAnalogEncoder = RobotContainer.config().getInt("frontLeftAnalogEncoder");
  private static final int rearLeftAnalogEncoder = RobotContainer.config().getInt("rearLeftAnalogEncoder");
  private static final int rearRightAnalogEncoder = RobotContainer.config().getInt("rearRightAnalogEncoder");

  private static final double swerveModuleRampRate = RobotContainer.config().getDouble("swerveModuleRampRate");
  private static final double swerveModuleP = RobotContainer.config().getDouble("swerveModuleP");
  private static final double swerveModuleI = RobotContainer.config().getDouble("swerveModuleI");
  private static final double swerveModuleD = RobotContainer.config().getDouble("swerveModuleD");

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
    return new SwerveDrive.Builder().useDefaultKinematics(new ChassisDimension(new Length(29, Unit.INCHES)))
        .setGyro(gyro)
        /* This function adds the modules to the module map. */
        .setModuleMap((map) -> {
          map.put(SwerveModule.Location.FRONT_RIGHT,
              new MK2SwerveModule(frontRightDriveCanId, frontRightPivotCanId, frontRightAnalogEncoder, frontRightAngleOffset));
          map.put(SwerveModule.Location.FRONT_LEFT,
              new MK2SwerveModule(frontLeftDriveCanId, frontLeftPivotCanId, frontLeftAnalogEncoder, frontLeftAngleOffset));
          map.put(SwerveModule.Location.REAR_LEFT,
              new MK2SwerveModule(rearLeftDriveCanId, rearLeftPivotCanId, rearLeftAnalogEncoder, rearLeftAngleOffset));
          map.put(SwerveModule.Location.REAR_RIGHT,
              new MK2SwerveModule(rearRightDriveCanId, rearRightPivotCanId, rearRightAnalogEncoder, rearRightAngleOffset));

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
          swerve.setReversed(DegreeOfFreedom.FORWARD, true);
          swerve.setReversed(DegreeOfFreedom.STRAFE, true);
          swerve.setReversed(DegreeOfFreedom.ROTATION, true);
          // swerve.setFieldCentric(false);

          // swerve.setIdleAngle(0, false);

          swerve.startLogging(new DashboardSwerveLogger());
        });
  }
}
