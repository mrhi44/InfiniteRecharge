/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import frc.robot.Const;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.Solenoid;
import frc.robot.commands.IntakeWithJoystick;

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
public class Intake extends SubsystemBase {

  private final Solenoid intakeLift = new Solenoid(Const.Pneumatic.INTAKE);
  private final WPI_TalonSRX motor = new WPI_TalonSRX(Const.CAN.INTAKE_MOTOR);
  private double speed = 0;

  /**
   * Creates a new Intake with the settings in the constants file.
   */
  public Intake() {
    setSpeed(Const.Speed.INTAKE_SPEED);
    lift(true);
  }

  /**
   * Set the speed to run the intake at. Note that this does NOT 
   * actually run the intake, but set the speed that the intake will be run at.
   */
  public void setSpeed(double speed) {
    if (speed <= 1.0 && speed >= -1.0) {
      this.speed = speed;
    } else {
      throw new IllegalArgumentException("Speed out of bounds: " + speed);
    }
  }

  /**
   * Run the intake at the default speed.
   */
  public void run() {
    runAt(speed);
  }

  /**
   * Run the intake at the given speed.
   *
   * @param speed The speed in terms of current percentage.
   *              This will be passed directly into the motor
   *              with no checks.
   */
  public void runAt(double speed) {
    motor.set(speed);
  }

  /**
   * Stop the intake motor by running it at zero current.
   */
  public void stop() {
    runAt(0.0);
  }

  /**
   * Lift the intake, or put it down.
   *
   * @param liftIntake Whether or not the intake should be up or down.
   */
  public void lift(boolean liftIntake) {
    intakeLift.set(!liftIntake);
  }
}
