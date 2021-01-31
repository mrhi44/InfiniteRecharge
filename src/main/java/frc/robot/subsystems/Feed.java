package frc.robot.subsystems;

import frc.robot.RobotContainer;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

/**
 * The Feed subsystem controls the belt that will feed power cells into the
 * shooter.
 *
 * This subsystem consists of the following components: - Belt feeder (1x Spark
 * Max motor controller)
 *
 * This subsystem should provide the following functions: - Run the belt feed.
 *
 * @author Jordan Bancino
 */
public class Feed extends SubsystemBase {

    private static final int feedRiseCanId = RobotContainer.config().getInt("feedRiseCanId");
    private static final int feedRunCanId = RobotContainer.config().getInt("feedRunCanId");

    private final CANSparkMax feedRunMotor = new CANSparkMax(feedRunCanId, MotorType.kBrushless);
    private final CANSparkMax feedRiseMotor = new CANSparkMax(feedRiseCanId, MotorType.kBrushless);

    public void setRun(double speed) {
        feedRunMotor.set(speed);

    }

    public void setRise(double speed) {
        feedRiseMotor.set(speed);
    }

    public double getRise() {
        return feedRiseMotor.get();
    }

    public double getRun() {
        return feedRunMotor.get();
    }

    public void stopRun() {
        feedRunMotor.stopMotor();
    }

    public void stopRise() {
        feedRiseMotor.stopMotor();
    }

    public void stop() {
        stopRun();
        stopRise();
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Subsystems/Feed/Speed", feedRunMotor.get());
    }
}