/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.HashMap;

import frc.robot.Constants;
import frc.robot.commands.DriveWithJoystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import net.bancino.robotics.swerveio.SwerveDrive;
import net.bancino.robotics.swerveio.encoder.SparkMaxEncoder;
import net.bancino.robotics.swerveio.module.AbstractSwerveModule;
import net.bancino.robotics.swerveio.module.MK2SwerveModule;
import net.bancino.robotics.swerveio.SwerveModule;
import net.bancino.robotics.swerveio.exception.SwerveException;

public class DriveTrain extends SwerveDrive {
  /* These are in centimeters, but unit really doesn't matter. */
  public static final double BASE_WIDTH = 51;
  public static final double BASE_LENGTH = 56;
  /* Our 1:1 Encoder. */
  public static final double COUNTS_PER_PIVOT_REVOLUTION = 360;

  /* The encoder offsets tell us where zero is for each motor. */
  public static final double FRONT_RIGHT_ENCODER_OFFSET = 91.054678;
  public static final double FRONT_LEFT_ENCODER_OFFSET = 326.337857;
  public static final double REAR_LEFT_ENCODER_OFFSET = 179.121075;
  public static final double REAR_RIGHT_ENCODER_OFFSET = 26.71;

  private static SparkMaxEncoder frontRightEncoder = new SparkMaxEncoder(true, FRONT_RIGHT_ENCODER_OFFSET);
  private static SparkMaxEncoder frontLeftEncoder = new SparkMaxEncoder(true, FRONT_LEFT_ENCODER_OFFSET);
  private static SparkMaxEncoder rearLeftEncoder = new SparkMaxEncoder(true, REAR_LEFT_ENCODER_OFFSET);
  private static SparkMaxEncoder rearRightEncoder = new SparkMaxEncoder(true, REAR_RIGHT_ENCODER_OFFSET);

  /**
   * Create the SwerveDrive with the default settings and the robot map.
   */
  public DriveTrain(XboxController xbox, Gyro gyro) throws SwerveException {
    super(BASE_WIDTH, BASE_LENGTH, COUNTS_PER_PIVOT_REVOLUTION, () -> {
      var modules = new HashMap<SwerveModule, AbstractSwerveModule>();
      modules.put(SwerveModule.FRONT_RIGHT,
          new MK2SwerveModule(Constants.FRONT_RIGHT_DRIVE_MOTOR, Constants.FRONT_RIGHT_PIVOT_MOTOR, frontRightEncoder));
      modules.put(SwerveModule.FRONT_LEFT,
          new MK2SwerveModule(Constants.FRONT_LEFT_DRIVE_MOTOR, Constants.FRONT_LEFT_PIVOT_MOTOR, frontLeftEncoder));
      modules.put(SwerveModule.REAR_LEFT,
          new MK2SwerveModule(Constants.REAR_LEFT_DRIVE_MOTOR, Constants.REAR_LEFT_PIVOT_MOTOR, rearLeftEncoder));
      modules.put(SwerveModule.REAR_RIGHT,
          new MK2SwerveModule(Constants.REAR_RIGHT_DRIVE_MOTOR, Constants.REAR_RIGHT_PIVOT_MOTOR, rearRightEncoder));
      return modules; /* Return the module map for the constructor's use. */
    }, (module) -> {
      module.setPivotClosedLoopRampRate(0);
    });

    setDefaultCommand(new DriveWithJoystick(this, gyro, xbox));
  }
}
