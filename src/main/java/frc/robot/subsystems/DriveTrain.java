/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.Const;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import net.bancino.robotics.swerveio.SwerveBuilder;
import net.bancino.robotics.swerveio.SwerveDrive;
import net.bancino.robotics.swerveio.SwerveMeta;
import net.bancino.robotics.swerveio.DegreeOfFreedom;
import net.bancino.robotics.swerveio.encoder.AnalogEncoder;
import net.bancino.robotics.swerveio.encoder.AbstractEncoder;
import net.bancino.robotics.swerveio.module.AbstractSwerveModule;
import net.bancino.robotics.swerveio.pid.AbstractPIDController;
import net.bancino.robotics.swerveio.module.MK2SwerveModule;
import net.bancino.robotics.swerveio.SwerveModule;
import net.bancino.robotics.swerveio.exception.SwerveException;
import net.bancino.robotics.swerveio.log.DashboardSwerveLogger;
import net.bancino.robotics.swerveio.geometry.Length;
import net.bancino.robotics.swerveio.geometry.Unit;
import net.bancino.robotics.swerveio.geometry.SquareChassis;
import net.bancino.robotics.swerveio.gyro.AbstractGyro;
import net.bancino.robotics.swerveio.kinematics.DefaultSwerveKinematics;
import net.bancino.robotics.swerveio.kinematics.SwerveKinematicsProvider;

/**
 * The drivetrain subsystem drives the robot! (wow!).
 *
 * This subsystem consists of the following components:
 * - Swerve module (4x drive + pivot motor)
 *
 * This subsystem should provide the following functions:
 * - Run the drivetrain with joystick
 * - Run the drivetrain autonomously
 *
 * @author Jordan Bancino
 */
public class DriveTrain {

  /**
   * Create a new instance of a swerve drive.
   * @param gyro The gyro to use in field-centric calculations
   * @return A new SwerveDrive drivetrain based on our configuration.
   * @throws SwerveException If there is an error creating the swerve drive.
   */
  public static SwerveDrive create(AbstractGyro gyro) throws SwerveException {
    return new SwerveBuilder()
      .setKinematicsProvider(new DefaultSwerveKinematics(new SquareChassis(new Length(29, Unit.INCHES))))
      .setGyro(gyro)
      /* This function adds the modules to the module map. */
      .setModuleMap((map) -> {
        AbstractEncoder frontRightEncoder = new AnalogEncoder(Const.Encoder.FRONT_RIGHT_ANALOG_ENCODER, Const.Encoder.FRONT_RIGHT_ENCODER_OFFSET);
        AbstractEncoder frontLeftEncoder = new AnalogEncoder(Const.Encoder.FRONT_LEFT_ANALOG_ENCODER, Const.Encoder.FRONT_LEFT_ENCODER_OFFSET);
        AbstractEncoder rearLeftEncoder = new AnalogEncoder(Const.Encoder.REAR_LEFT_ANALOG_ENCODER, Const.Encoder.REAR_LEFT_ENCODER_OFFSET);
        AbstractEncoder rearRightEncoder = new AnalogEncoder(Const.Encoder.REAR_RIGHT_ANALOG_ENCODER, Const.Encoder.REAR_RIGHT_ENCODER_OFFSET);

        map.put(SwerveModule.FRONT_RIGHT, new MK2SwerveModule(Const.CAN.FRONT_RIGHT_DRIVE_MOTOR,
            Const.CAN.FRONT_RIGHT_PIVOT_MOTOR, frontRightEncoder));
        map.put(SwerveModule.FRONT_LEFT,
            new MK2SwerveModule(Const.CAN.FRONT_LEFT_DRIVE_MOTOR, Const.CAN.FRONT_LEFT_PIVOT_MOTOR, frontLeftEncoder));
        map.put(SwerveModule.REAR_LEFT,
            new MK2SwerveModule(Const.CAN.REAR_LEFT_DRIVE_MOTOR, Const.CAN.REAR_LEFT_PIVOT_MOTOR, rearLeftEncoder));
        map.put(SwerveModule.REAR_RIGHT,
            new MK2SwerveModule(Const.CAN.REAR_RIGHT_DRIVE_MOTOR, Const.CAN.REAR_RIGHT_PIVOT_MOTOR, rearRightEncoder));
  
      /* This function is run on every module. */
      }, (module) -> {
        AbstractPIDController modulePid = module.getPivotPIDController();
        modulePid.setOutputRampRate(Const.PID.SWERVE_MODULE_RAMP_RATE);
        modulePid.setP(Const.PID.SWERVE_MODULE_P);
        modulePid.setI(Const.PID.SWERVE_MODULE_I);
        modulePid.setD(Const.PID.SWERVE_MODULE_D);
      })
      /* This function is run when the swerve drive object is created. It performs additional setup. */
      .create((swerve) -> {
        swerve.setReversed(DegreeOfFreedom.FORWARD, true);
        swerve.setReversed(DegreeOfFreedom.STRAFE, true);
        swerve.setReversed(DegreeOfFreedom.ROTATION, true);
        swerve.zeroDriveEncoders();
        //swerve.setFieldCentric(false);

        //swerve.setIdleAngle(0, false);

        swerve.startLogging(new DashboardSwerveLogger());
      });
  }
}
