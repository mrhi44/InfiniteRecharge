package frc.robot.subsystems;

import frc.robot.Const;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The Feed subsystem controls the belt that will feed
 * power cells into the shooter.
 *
 * This subsystem consists of the following components:
 * - Belt feeder (1x Spark Max motor controller)
 * - Pneumatic power cell-stopper.
 *
 * This subsystem should provide the following functions:
 * - Run the belt feed.
 * - Toggle the power cell-stopper
 *
 * @author Jordan Bancino
 */
public class Feed extends SimpleMotorSubsystem {

    private final CANSparkMax feedMotor = new CANSparkMax(Const.CAN.FEED_MOTOR, MotorType.kBrushless);
    private final Solenoid feedStopEnable = new Solenoid(Const.CAN.PNEUMATIC_CONTROL_MODULE, Const.Pneumatic.FEED_STOP_ENABLE);
    private final Solenoid feedStopDisable = new Solenoid(Const.CAN.PNEUMATIC_CONTROL_MODULE, Const.Pneumatic.FEED_STOP_DISABLE);
    private boolean feedStopped = false;

    public Feed() {
        super(Const.Speed.FEED_SPEED);
        closeStopper(false);
    }

    @Override
    public void runAt(double speed) {
        feedMotor.set(speed);
    }

    public void closeStopper(boolean closeFeed) {
        feedStopEnable.set(closeFeed);
        feedStopDisable.set(!closeFeed);
        feedStopped = closeFeed;
    }

    public boolean stopperEngaged() {
        return feedStopped;
    }

    @Override
    public void periodic() {
        SmartDashboard.putBoolean("Subsystems/Feed/Stopper Engaged", stopperEngaged());
        SmartDashboard.putNumber("Subsystems/Feed/Speed", feedMotor.get());
    }
}