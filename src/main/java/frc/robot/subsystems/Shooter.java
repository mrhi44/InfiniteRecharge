package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * The Shooter subsystem controls the shooting mechanism
 * that will shoot power cells.
 *
 * This subsystem consists of the following components:
 * - The fly wheel (3x Victor SPX controllers on CAN, note that one will be reversed)
 * - The hood (1x Talon SRX controller with SRX magnetic encoder.)
 *
 * This subsystem should provide the following functions:
 * - Calculate the hood position from distance from the target
 * - Control the hood via a position loop
 * - Run the fly wheel.
 */
public class Shooter extends SubsystemBase {
    public Shooter() {

    }
}