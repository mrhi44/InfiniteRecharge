package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import frc.robot.Const;

/**
 * A simple motor subsystem for running one or more motors
 * at a variable, or set speed.
 *
 * @author Jordan Bancino
 */
public abstract class SimpleMotorSubsystem extends SubsystemBase {

    private double speed= 0;

    /**
     * Construct a simple motor subsystem.
     *
     * @param initialDefaultSpeed The initial default speed to run at.
     */
    public SimpleMotorSubsystem(double initialDefaultSpeed) {
        setSpeed(initialDefaultSpeed);
    }

    /**
     * Set the speed to run motor(s) at. Note that this does NOT 
     * actually run it/them, but set the speed that the motor(s) will be run at.
     */
    public final void setSpeed(double speed) {
      if (speed <= 1.0 && speed >= -1.0) {
        this.speed = speed;
      } else {
        throw new IllegalArgumentException("Speed out of bounds: " + speed);
      }
    }

    /**
     * Call runAt() with the default speed.
     */
    public final void run() {
        runAt(speed);
    }

    /**
     * Stop the motor(s) by running runAt(0.0).
     */
    public final void stop() {
        runAt(0.0);
    }

    /**
     * Run one or more motors at the given speed.
     *
     * @param speed The percentage of output (ranging from zero to 1)
     *              that the motor(s) should be run at.
     */
    public abstract void runAt(double speed);
}