/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.Const;

import java.io.File;
import java.util.HashMap;
import java.io.IOException;

import frc.robot.commands.DriveWithJoystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import net.bancino.robotics.swerveio.SwerveDrive;
import net.bancino.robotics.swerveio.encoder.SparkMaxEncoder;
import net.bancino.robotics.swerveio.encoder.AnalogEncoder;
import net.bancino.robotics.swerveio.encoder.AbstractEncoder;
import net.bancino.robotics.swerveio.module.AbstractSwerveModule;
import net.bancino.robotics.swerveio.module.MK2SwerveModule;
import net.bancino.robotics.swerveio.SwerveModule;
import net.bancino.robotics.swerveio.exception.SwerveException;
import net.bancino.robotics.swerveio.log.DashboardSwerveLogger;
import net.bancino.robotics.swerveio.log.csv.CSVPIDSwerveLogger;
import net.bancino.robotics.swerveio.si.Length;
import net.bancino.robotics.swerveio.si.Unit;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain extends SwerveDrive {
  /* These are in inches, but unit really doesn't matter. */
  public static final Length BASE_WIDTH = new Length(29, Unit.INCHES);
  public static final Length BASE_LENGTH = new Length(29, Unit.INCHES);
  /* Our 1:1 Encoder. */
  public static final double COUNTS_PER_PIVOT_REVOLUTION = 360;

  /* The encoder offsets tell us where zero is for each motor. */
  public static final double FRONT_RIGHT_ENCODER_OFFSET = 146.27;
  public static final double FRONT_LEFT_ENCODER_OFFSET = 134.60;
  public static final double REAR_LEFT_ENCODER_OFFSET = 59.34;
  public static final double REAR_RIGHT_ENCODER_OFFSET = 267.2;

  private static AbstractEncoder frontRightEncoder = new SparkMaxEncoder(SparkMaxEncoder.EncoderMode.ANALOG, FRONT_RIGHT_ENCODER_OFFSET);
  private static AbstractEncoder frontLeftEncoder = new SparkMaxEncoder(SparkMaxEncoder.EncoderMode.ANALOG, FRONT_LEFT_ENCODER_OFFSET);
  private static AbstractEncoder rearLeftEncoder = new SparkMaxEncoder(SparkMaxEncoder.EncoderMode.ANALOG, REAR_LEFT_ENCODER_OFFSET);
  private static AbstractEncoder rearRightEncoder = new SparkMaxEncoder(SparkMaxEncoder.EncoderMode.ANALOG, REAR_RIGHT_ENCODER_OFFSET);

  //private static AbstractEncoder frontRightEncoder = new AnalogEncoder(Const.Encoder.FRONT_RIGHT_ANALOG_ENCODER, FRONT_RIGHT_ENCODER_OFFSET);
  //private static AbstractEncoder frontLeftEncoder = new AnalogEncoder(Const.Encoder.FRONT_LEFT_ANALOG_ENCODER, FRONT_LEFT_ENCODER_OFFSET);
  //private static AbstractEncoder rearLeftEncoder = new AnalogEncoder(Const.Encoder.REAR_LEFT_ANALOG_ENCODER, REAR_LEFT_ENCODER_OFFSET);
  //private static AbstractEncoder rearRightEncoder = new AnalogEncoder(Const.Encoder.REAR_RIGHT_ANALOG_ENCODER, REAR_RIGHT_ENCODER_OFFSET);

  /**
   * Create the SwerveDrive with the default settings and the robot map.
   */
  public DriveTrain(XboxController xbox, Gyro gyro) throws SwerveException {
    super(BASE_WIDTH, BASE_LENGTH, COUNTS_PER_PIVOT_REVOLUTION, () -> {
      var modules = new HashMap<SwerveModule, AbstractSwerveModule>();
      modules.put(SwerveModule.FRONT_RIGHT,
          new MK2SwerveModule(Const.CAN.FRONT_RIGHT_DRIVE_MOTOR, Const.CAN.FRONT_RIGHT_PIVOT_MOTOR, frontRightEncoder));
      modules.put(SwerveModule.FRONT_LEFT,
          new MK2SwerveModule(Const.CAN.FRONT_LEFT_DRIVE_MOTOR, Const.CAN.FRONT_LEFT_PIVOT_MOTOR, frontLeftEncoder));
      modules.put(SwerveModule.REAR_LEFT,
          new MK2SwerveModule(Const.CAN.REAR_LEFT_DRIVE_MOTOR, Const.CAN.REAR_LEFT_PIVOT_MOTOR, rearLeftEncoder));
      modules.put(SwerveModule.REAR_RIGHT,
          new MK2SwerveModule(Const.CAN.REAR_RIGHT_DRIVE_MOTOR, Const.CAN.REAR_RIGHT_PIVOT_MOTOR, rearRightEncoder));
      return modules; /* Return the module map for the constructor's use. */
    }, (module) -> {
      module.getPivotPIDController().setOutputRampRate(0);
    });

    setDefaultCommand(new DriveWithJoystick(this, gyro, xbox));

    zeroDriveEncoders();

    setIdleAngle(0, false);

    startLogging(new DashboardSwerveLogger());

    File logFile = new File("/home/lvuser/pid.csv");
    try {
      logFile.createNewFile();
      startLogging(100, new CSVPIDSwerveLogger(logFile, SwerveModule.FRONT_LEFT));
    } catch (IOException e) {
      System.out.println("Error Creating Robot CSV: " + e);
      e.printStackTrace();
    }
  }
}
