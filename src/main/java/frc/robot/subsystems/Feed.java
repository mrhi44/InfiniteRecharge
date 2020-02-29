package frc.robot.subsystems;

import frc.robot.Const;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
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

    public Feed() {
        super(Const.Speed.FEED_SPEED);
    }

    @Override
    public void runAt(double speed) {
        feedMotor.set(speed);
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Subsystems/Feed/Speed", feedMotor.get());
    }
}