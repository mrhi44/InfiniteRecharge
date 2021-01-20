package frc.robot.subsystems;

import frc.robot.RobotContainer;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The Feed subsystem controls the belt that will feed
 * power cells into the shooter.
 *
 * This subsystem consists of the following components:
 * - Belt feeder (1x Spark Max motor controller)
 *
 * This subsystem should provide the following functions:
 * - Run the belt feed.
 *
 * @author Jordan Bancino
 */
public class Feed extends SimpleMotorSubsystem {

    private static final int feedCanId = RobotContainer.config().getInt("feedCanId");
    private static final double feedWithIntakeSpeed = RobotContainer.config().getDouble("feedWithIntakeSpeed");

    private final CANSparkMax feedMotor = new CANSparkMax(feedCanId, MotorType.kBrushless);

    public Feed() {
        super(feedWithIntakeSpeed);
    }

    @Override
    public void runAt(double speed) {
        feedMotor.set(speed);
    }

    @Override
    public double getSpeed() {
        return feedMotor.get();
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Subsystems/Feed/Speed", feedMotor.get());
    }
}