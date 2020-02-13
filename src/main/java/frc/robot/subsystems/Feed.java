package frc.robot.subsystems;

import frc.robot.Const;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.Solenoid;

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
 */
public class Feed extends SubsystemBase {

    private final CANSparkMax feedMotor = new CANSparkMax(Const.CAN.FEED_MOTOR, MotorType.kBrushless);
    private final Solenoid feedStop = new Solenoid(Const.Pneumatic.CONTROL_MODULE, Const.Pneumatic.FEED_STOP);

    public Feed() {

    }
}