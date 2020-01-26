/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.io.File;
import java.util.HashMap;
import java.io.IOException;

import frc.robot.Constants;
import frc.robot.commands.DriveWithJoystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import net.bancino.robotics.swerveio.SwerveDrive;
import net.bancino.robotics.swerveio.encoder.SparkMaxEncoder;
import net.bancino.robotics.swerveio.encoder.AnalogEncoder;
import net.bancino.robotics.swerveio.module.AbstractSwerveModule;
import net.bancino.robotics.swerveio.module.MK2SwerveModule;
import net.bancino.robotics.swerveio.SwerveModule;
import net.bancino.robotics.swerveio.exception.SwerveException;
import net.bancino.robotics.swerveio.log.DashboardSwerveLogger;
import net.bancino.robotics.swerveio.log.csv.CSVPIDSwerveLogger;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DriveTrain extends SwerveDrive {

  /*
     * Motors - CAN IDs
     */
    public static final int FRONT_RIGHT_DRIVE_MOTOR = 5; /* Module 1 */
    public static final int FRONT_LEFT_DRIVE_MOTOR = 6; /* Module 2 */
    public static final int REAR_LEFT_DRIVE_MOTOR = 7; /* Module 3 */
    public static final int REAR_RIGHT_DRIVE_MOTOR = 8; /* Module 4 */

    public static final int FRONT_RIGHT_PIVOT_MOTOR = 1; /* Module 1 */
    public static final int FRONT_LEFT_PIVOT_MOTOR = 2; /* Module 2 */
    public static final int REAR_LEFT_PIVOT_MOTOR = 3; /* Module 3 */
    public static final int REAR_RIGHT_PIVOT_MOTOR = 4; /* Module 4 */

    /*
     * Encoders - Analog Ports
     */
    public static final int FRONT_RIGHT_ANALOG_ENCODER = 0; /* Module 1 */
    public static final int FRONT_LEFT_ANALOG_ENCODER = 1; /* Module 2 */
    public static final int REAR_LEFT_ANALOG_ENCODER = 2; /* Module 3 */
    public static final int REAR_RIGHT_ANALOG_ENCODER = 3; /* Module 4 */

  /* These are in centimeters, but unit really doesn't matter. */
  public static final double BASE_WIDTH = 29;
  public static final double BASE_LENGTH = 29;
  /* Our 1:1 Encoder. */
  public static final double COUNTS_PER_PIVOT_REVOLUTION = 360;

  /* The encoder offsets tell us where zero is for each motor. */
  public static final double FRONT_RIGHT_ENCODER_OFFSET = 54.65;
  public static final double FRONT_LEFT_ENCODER_OFFSET = 196.27;
  public static final double REAR_LEFT_ENCODER_OFFSET = 351.45;
  public static final double REAR_RIGHT_ENCODER_OFFSET = 160.66;

  private static AnalogEncoder frontRightEncoder = new AnalogEncoder(FRONT_RIGHT_ANALOG_ENCODER, FRONT_RIGHT_ENCODER_OFFSET);
  private static AnalogEncoder frontLeftEncoder = new AnalogEncoder(FRONT_LEFT_ANALOG_ENCODER, FRONT_LEFT_ENCODER_OFFSET);
  private static AnalogEncoder rearLeftEncoder = new AnalogEncoder(REAR_LEFT_ANALOG_ENCODER, REAR_LEFT_ENCODER_OFFSET);
  private static AnalogEncoder rearRightEncoder = new AnalogEncoder(REAR_RIGHT_ANALOG_ENCODER, REAR_RIGHT_ENCODER_OFFSET);

  /**
   * Create the SwerveDrive with the default settings and the robot map.
   */
  public DriveTrain(XboxController xbox, Gyro gyro) throws SwerveException {
    super(BASE_WIDTH, BASE_LENGTH, COUNTS_PER_PIVOT_REVOLUTION, () -> {
      var modules = new HashMap<SwerveModule, AbstractSwerveModule>();
      modules.put(SwerveModule.FRONT_RIGHT,
          new MK2SwerveModule(FRONT_RIGHT_DRIVE_MOTOR, FRONT_RIGHT_PIVOT_MOTOR, frontRightEncoder));
      modules.put(SwerveModule.FRONT_LEFT,
          new MK2SwerveModule(FRONT_LEFT_DRIVE_MOTOR, FRONT_LEFT_PIVOT_MOTOR, frontLeftEncoder));
      modules.put(SwerveModule.REAR_LEFT,
          new MK2SwerveModule(REAR_LEFT_DRIVE_MOTOR, REAR_LEFT_PIVOT_MOTOR, rearLeftEncoder));
      modules.put(SwerveModule.REAR_RIGHT,
          new MK2SwerveModule(REAR_RIGHT_DRIVE_MOTOR, REAR_RIGHT_PIVOT_MOTOR, rearRightEncoder));
      return modules; /* Return the module map for the constructor's use. */
    }, (module) -> {
      module.getPivotPIDController().setOutputRampRate(0);
    });

    setDefaultCommand(new DriveWithJoystick(this, gyro, xbox));

    zeroDriveEncoders();

    //setIdleAngle(0, false);

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
