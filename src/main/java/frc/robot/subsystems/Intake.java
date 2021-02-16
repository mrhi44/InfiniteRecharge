/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import frc.robot.RobotContainer;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The Intake subsystem controls the intake of power cells.
 *
 * This subsystem consists of the following components:
 * - The intake motor (1x Talon SRX on CAN)
 * - Pneumatics to control the intake up and down.
 *
 * This subsystem should provide the following functions:
 * - Run a speed loop on the intake motor.
 * - Toggle the pneumatics to lift and lower the intake.
 *
 * @author Jordan Bancino
 */
public class Intake extends SimpleMotorSubsystem {

  private static final int pcmCanId = RobotContainer.config().getInt("pcmCanId");
  private static final int intakeUpId = RobotContainer.config().getInt("intakeUpId");
  private static final int intakeDownId = RobotContainer.config().getInt("intakeDownId");
  private static final int intakeCanId = RobotContainer.config().getInt("intakeCanId");
  private static final double intakeSpeed = RobotContainer.config().getDouble("intakeSpeed");

  private final Solenoid intakeUp = new Solenoid(pcmCanId, intakeUpId);
  private final Solenoid intakeDown = new Solenoid(pcmCanId, intakeDownId);
  private boolean intakeIsUp = false;
  private final WPI_VictorSPX motor = new WPI_VictorSPX(intakeCanId);

  /**
   * Creates a new Intake with the settings in the constants file.
   */
  public Intake() {
    super(intakeSpeed);
  }

  /**
   * Run the intake at the given speed.
   *
   * @param speed The speed in terms of current percentage.
   *              This will be passed directly into the motor
   *              with no checks.
   */
  @Override
  public void runAt(double speed) {
    motor.set(speed);
  }

  @Override
  public double getSpeed() {
    return motor.get();
  }

  /**
   * Lift the intake, or put it down.
   *
   * @param liftIntake Whether or not the intake should be up or down.
   */
  public void lift(boolean liftIntake) {
    intakeUp.set(liftIntake);
    intakeDown.set(!liftIntake);
    intakeIsUp = liftIntake;
  }

  public boolean isUp() {
    return intakeIsUp;
  }

  @Override
  public void periodic() {
    SmartDashboard.putBoolean("Subsystems/Intake/Up", isUp());
    SmartDashboard.putNumber("Subsystems/Intake/Speed", motor.get());
  }
}
