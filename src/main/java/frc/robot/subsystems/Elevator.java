package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * The Elevator subsystem controls the elevator mechanism
 *
 * This subsystem consists of the following components:
 * - The elevator motor (1x Spark Max using internal NEO encoder.)
 * - The adjustment wheel (1x Talon SRX/Victor SPX controller on either CAN or PWM)
 *
 * This subsystem should provide the following functions:
 * - Run a position loop on the elevator 
 * - Run a speed loop on the elevator
 * - Run a speed loop on the adjustment wheel
 */
public class Elevator extends SubsystemBase {
    public Elevator() {

    }
}