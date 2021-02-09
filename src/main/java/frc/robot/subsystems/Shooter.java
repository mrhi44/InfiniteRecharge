package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import frc.robot.RobotContainer;
import net.bancino.robotics.swerveio.pid.DefaultPIDController;

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

    private static final int shooter1CanId = RobotContainer.config().getInt("shooter1CanId");
    private static final int shooter2CanId = RobotContainer.config().getInt("shooter2CanId");
    //private static final int shooter3CanId = RobotContainer.config().getInt("shooter3CanId");
    private static final int shooterHoodCanId = RobotContainer.config().getInt("shooterHoodCanId");

    private static final double hoodP = RobotContainer.config().getDouble("hoodP");
    private static final double hoodI = RobotContainer.config().getDouble("hoodI");
    private static final double hoodD = RobotContainer.config().getDouble("hoodD");

    private static final double shooterSpeed = RobotContainer.config().getDouble("shooterSpeed");
    private static final double hoodOutputLimit = RobotContainer.config().getDouble("hoodOutputLimit");

    private static final int hoodSlot = RobotContainer.config().getInt("hoodSlot");
    private static final int hoodTimeout = RobotContainer.config().getInt("hoodTimeout");
    private static final int maxHoodPosition = RobotContainer.config().getInt("maxHoodPosition");

    private final WPI_VictorSPX shooterMotor1 = new WPI_VictorSPX(shooter1CanId);
    private final WPI_VictorSPX shooterMotor2 = new WPI_VictorSPX(shooter2CanId);
   // private final WPI_VictorSPX shooterMotor3 = new WPI_VictorSPX(shooter3CanId);

    private final WPI_TalonSRX hoodMotor = new WPI_TalonSRX(shooterHoodCanId);
    private final DefaultPIDController hoodPid = new DefaultPIDController(hoodP, hoodI, hoodD);
    private int hoodSetpoint = 0;
    private double hoodPidOutput = 0;

    /**
     * Configure the shooter and hood motor.
     */
    public Shooter() {
        super(shooterSpeed);
        /* Make it do the super fancy blinky thingy. */
        hoodMotor.setSensorPhase(true);
        hoodMotor.setInverted(true);
        hoodMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, hoodSlot, hoodTimeout);

        hoodPid.setOutputLimits(hoodOutputLimit);
        hoodPid.setSetpointRange(maxHoodPosition);
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
        //shooterMotor3.set(speed);
    }

    @Override
    public double getSpeed() {
        return shooterMotor1.get();
    }

    /**
     * Use the a PID position loop to obtain the inputted position.
     *
     * @param position The position (in encoder counts) to set the hood to.
     */
    public void setHoodPosition(int setpoint) {
        this.hoodSetpoint = setpoint;
        double hoodPosition = getHoodPosition();
        this.hoodPidOutput = hoodPid.getOutput(hoodPosition < 200 ? 0 : hoodPosition, setpoint);
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
        if (speed > hoodOutputLimit) {
            speed = hoodOutputLimit;
        } else if (speed < -hoodOutputLimit) {
            speed = -hoodOutputLimit;
        }
        hoodMotor.set(speed);
    }

    /**
     * Get the hood's position.
     *
     * @return The hood position, in encoder counts.
     */
    public double getHoodPosition() {
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
     * target using a linear function. This is a polynomial found by
     * epxerimentation. We measured the ideal hood position for distances
     * incremented in a foot and generated this polynomial by curve fitting.
     *
     * @param distance The distance the robot is away from the target. The hood
     *                 position will be calculated from this.
     * @return The encoder reading that represents the position that the hood should
     *         be set to for the given distance.
     */
    public int calculateHoodPosition(double distance) {
        return (int) ((0.0043 * Math.pow(distance, 3)) - (2.1315 * Math.pow(distance, 2)) + (336.75 * distance)
                - 8432.4);
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