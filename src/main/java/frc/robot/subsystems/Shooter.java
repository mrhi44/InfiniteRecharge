package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import frc.robot.Const;

import net.bancino.robotics.swerveio.pid.MiniPID;

/**
 * The Shooter subsystem controls the shooting mechanism that will shoot power
 * cells.
 *
 * This subsystem consists of the following components: - The fly wheel (3x
 * Victor SPX controllers on CAN, note that one will be reversed) - The hood (1x
 * Talon SRX controller with SRX magnetic encoder.)
 *
 * This subsystem should provide the following functions: - Calculate the hood
 * position from distance from the target - Control the hood via a position loop
 * - Run the fly wheel.
 *
 * @author Jordan Bancino
 */
public class Shooter extends SimpleMotorSubsystem {

    public final WPI_VictorSPX shooterMotor1 = new WPI_VictorSPX(Const.CAN.SHOOTER_MOTOR_1);
    public final WPI_VictorSPX shooterMotor2 = new WPI_VictorSPX(Const.CAN.SHOOTER_MOTOR_2);

    public final WPI_TalonSRX hoodMotor = new WPI_TalonSRX(Const.CAN.SHOOTER_HOOD_MOTOR);
    private final MiniPID hoodPid = new MiniPID(Const.PID.HOOD_P, Const.PID.HOOD_I, Const.PID.HOOD_D);
    private int hoodSetpoint = 0;
    private double hoodPidOutput = 0;

    /**
     * Configure the shooter and hood motor.
     */
    public Shooter() {
        super(Const.Speed.SHOOTER_SPEED);
        /* Make it do the super fancy blinky thingy. */
        hoodMotor.setSensorPhase(true);
        hoodMotor.setInverted(true);
        hoodMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, Const.PID.HOOD_SLOT,
                Const.PID.HOOD_TIMEOUT);

        hoodPid.setOutputLimits(Const.Shooter.HOOD_OUTPUT_LIMIT);
        hoodPid.setSetpointRange(Const.Shooter.MAX_HOOD_POSITION);
    }

    /**
     * Run the shooter motors at the given speed.
     *
     * @param speed The speed to run the shooter motors.
     */
    @Override
    public void runAt(double speed) {
        shooterMotor1.set(-speed);
        shooterMotor2.set(-speed);
    }

    /**
     * Use the a PID position loop to obtain the inputted position.
     *
     * @param position The position (in encoder counts) to set the hood to.
     */
    public void setHoodPosition(int setpoint) {
        this.hoodSetpoint = setpoint;
        this.hoodPidOutput = hoodPid.getOutput(getHoodPosition(), setpoint);
        hoodMotor.set(-hoodPidOutput);
    }

    /**
     * Run the hood motor.
     *
     * @param speed The speed to run the hood at. Note that this method will
     *              automatically correct the speed if it is out of bounds (See
     *              Const.Shooter.HOOD_OUTPUT_LIMIT)
     */
    public void setHoodSpeed(double speed) {
        if (speed > Const.Shooter.HOOD_OUTPUT_LIMIT) {
            speed = Const.Shooter.HOOD_OUTPUT_LIMIT;
        } else if (speed < -Const.Shooter.HOOD_OUTPUT_LIMIT) {
            speed = -Const.Shooter.HOOD_OUTPUT_LIMIT;
        }
        hoodMotor.set(speed);
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
    public void setHoodPositionFromDistance(double distanceAwayFromTarget) {
        setHoodPosition(calculateHoodPosition(distanceAwayFromTarget));
    }

    /**
     * Calculate the hood position based on the distance the robot is from the
     * target.
     *
     * @param distance The distance the robot is away from the target.
     *                               The hood position will be calculated from this.
     * @return The encoder reading that represents the position that the hood should
     *         be set to for the given distance.
     */
    public int calculateHoodPosition(double distance) {
        double trenchDistance = 19.0 * 12.0;
        double lineDistance = 10.0 * 12.0;
        int trenchCount = Const.Shooter.HOOD_ENCODER_DISTANCE_MAP.get(trenchDistance);
        int lineCount = Const.Shooter.HOOD_ENCODER_DISTANCE_MAP.get(lineDistance);
        double slope = (trenchCount - lineCount) / (trenchDistance - lineDistance);
        return (int) (slope * (distance - trenchDistance) + trenchCount);
    }


    @Override
    public void periodic() {
        SmartDashboard.putNumber("Subsystems/Shooter/Hood Position", getHoodPosition());
        SmartDashboard.putNumber("Subsystems/Shooter/Hood Setpoint", hoodSetpoint);
        SmartDashboard.putNumber("Subsystems/Shooter/Hood PID Output", hoodPidOutput);
        SmartDashboard.putNumber("Subsystems/Shooter/Hood Speed", hoodMotor.get());
        SmartDashboard.putNumber("Subsystems/Shooter/Shooter Speed", shooterMotor1.get());
    }
}