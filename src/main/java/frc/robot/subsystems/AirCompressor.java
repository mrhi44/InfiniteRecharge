package frc.robot.subsystems;

import frc.robot.Const;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Compressor;

/**
 * The Elevator subsystem controls the elevator mechanism
 *
 * This subsystem consists of the following components:
 * - The Pneumatic Compressor
 *
 * This subsystem should provide the following functions:
 * - Turn the compressor on and off
 *
 * @author Jordan Bancino
 */
public class AirCompressor extends SubsystemBase {

    private final Compressor compressor;

    /**
     * Create an air compressor on the specified control module.
     *
     * @param controlModule The control module to create the compressor on.
     */
    public AirCompressor(int controlModule) {
        compressor = new Compressor(controlModule);
        compressor.start();
    }

    /**
     * Enable/disable closed-loop control.
     *
     * @param autoControl Whether or not to enable the closed-loop control.
     */
    public void automaticControl(boolean autoControl) {
        compressor.setClosedLoopControl(autoControl);
    }

    /**
     * Enable closed-loop control;
     */
    public void start() {
        compressor.start();
    }

    /**
     * Disable closed-loop control.
     */
    public void stop() {
        compressor.stop();
    }
}