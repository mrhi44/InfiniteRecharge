/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import frc.robot.Const;

/**
 * The intake subsystem is responsible for taking power cells into the 
 * robot feed.
 *
 * @author Jordan Bancino
 */
public class Intake extends SubsystemBase implements Stoppable, Runnable {

  private final WPI_VictorSPX motor;
  private double speed;

  /**
   * Creates a new Intake with the settings in the constants file.
   */
  public Intake() {
    motor = new WPI_VictorSPX(Const.CAN.INTAKE_MOTOR);
    setSpeed(Const.Intake.INTAKE_SPEED);
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
}
