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
public class Intake extends SimpleMotorSubsystem {

  private final Solenoid intakeUp = new Solenoid(Const.Pneumatic.INTAKE_UP);
  private final Solenoid intakeDown = new Solenoid(Const.Pneumatic.INTAKE_DOWN);
  private final WPI_TalonSRX motor = new WPI_TalonSRX(Const.CAN.INTAKE_MOTOR);

  /**
   * Creates a new Intake with the settings in the constants file.
   */
  public Intake() {
    super(Const.Speed.INTAKE_SPEED);
    lift(true);
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

  /**
   * Lift the intake, or put it down.
   *
   * @param liftIntake Whether or not the intake should be up or down.
   */
  public void lift(boolean liftIntake) {
    intakeUp.set(liftIntake);
    intakeDown.set(!liftIntake);
  }
}
