package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import frc.robot.Const;

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
 *
 * @author Jordan Bancino
 */
public class Shooter extends SimpleMotorSubsystem {

    public final WPI_VictorSPX shooterMotor1 = new WPI_VictorSPX(Const.CAN.SHOOTER_MOTOR_1);
    public final WPI_VictorSPX shooterMotor2 = new WPI_VictorSPX(Const.CAN.SHOOTER_MOTOR_2);
    public final WPI_VictorSPX shooterMotorReversed = new WPI_VictorSPX(Const.CAN.SHOOTER_MOTOR_REVERSED);

<<<<<<< HEAD
    public final WPI_TalonSRX shooterHoodMotor = new WPI_TalonSRX(Const.CAN.SHOOTER_HOOD_MOTOR);
    shooterHoodMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);

=======
    public final WPI_TalonSRX hoodMotor = new WPI_TalonSRX(Const.CAN.SHOOTER_HOOD_MOTOR);
>>>>>>> 814075d4d91ae9924059729f8c8817f84c87a466

    /**
     * Configure the shooter and hood motor.
     */
    public Shooter() {
        super(Const.Speed.SHOOTER_SPEED);
        /* Make it do the super fancy blinky thingy. */
        hoodMotor.setSensorPhase(true);
        hoodMotor.config_kP(Const.PID.HOOD_SLOT, Const.PID.HOOD_P, Const.PID.HOOD_TIMEOUT);
        hoodMotor.config_kI(Const.PID.HOOD_SLOT, Const.PID.HOOD_I, Const.PID.HOOD_TIMEOUT);
        hoodMotor.config_kD(Const.PID.HOOD_SLOT, Const.PID.HOOD_D, Const.PID.HOOD_TIMEOUT);
        hoodMotor.selectProfileSlot(Const.PID.HOOD_SLOT, Const.PID.HOOD_SLOT);
        hoodMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Const.PID.HOOD_SLOT, Const.PID.HOOD_TIMEOUT);
    }

<<<<<<< HEAD
    public WPI_TalonSRX getMotor() {
        return shooterHoodMotor;
      }

      public void zeroPosition() {
        shooterHoodMotor.setSelectedSensorPosition(0);
      }

      public int getPosition() {
        /* Get hood motor Position from Talon SRX */
        return shooterHoodMotor.getSelectedSensorPosition(0);
      }
      
=======
    /**
     * Run the shooter motors at the given speed.
     *
     * @param speed The speed to run the shooter motors.
     */
>>>>>>> 814075d4d91ae9924059729f8c8817f84c87a466
    @Override
    public void runAt(double speed) {
        shooterMotor1.set(speed);
        shooterMotor2.set(speed);
        shooterMotorReversed.set(-speed);
    }

<<<<<<< HEAD
    public void stop() {
        setSpeed(0.0d);
      }
=======
    /**
     * Use the Talon SRX's MotionMagic to achieve the given position.
     *
     * @param position The position (in encoder counts) to set the hood to.
     */
    public void setHoodPosition(int position) {
        hoodMotor.set(ControlMode.MotionMagic, position);
    }

    /**
     * Run the hood motor, but at half the speed passed in here.
     *
     * @param speed The speed, scaled -1 to 1, to run the hood motor at.
     *              Note that this will be cut in half because the hood doesn't move
     *              very much or very fast.
     */
    public void setHoodSpeed(double speed) {
        hoodMotor.set(speed * 0.5);
    }

    /**
     * Get the hood's position.
     *
     * @return The hood position, in encoder counts.
     */
    public int getHoodPosition() {
        return hoodMotor.getSelectedSensorPosition(0);
    }

    /**
     * Set the hood position based on the provided distance away from the target.
     * 
     * @param distanceAwayFromTarget The distance the robot is away from the target.
     *                               The hood position will be calculated from this
     *                               and passed into the Motion Magic profile.
     */
    public void setHoodPosition(double distanceAwayFromTarget) {
        setHoodPosition(calculateHoodPosition(distanceAwayFromTarget));
    }

    /**
     * Calculate the hood position based on the distance the robot is from the target.
     *
     * @param distanceAwayFromTarget The distance the robot is away from the target.
     *                               The hood position will be calculated from this.
     * @return The encoder reading that represents the position that the hood should
     *         be set to for the given distance.
     */
    public int calculateHoodPosition(double distanceAwayFromTarget) {
        throw new UnsupportedOperationException("Distance calculation isn't supported yet.");
    }
>>>>>>> 814075d4d91ae9924059729f8c8817f84c87a466
}