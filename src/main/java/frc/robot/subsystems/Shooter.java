package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
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
 */
public class Shooter extends SimpleMotorSubsystem {

    public final WPI_VictorSPX shooterMotor1 = new WPI_VictorSPX(Const.CAN.SHOOTER_MOTOR_1);
    public final WPI_VictorSPX shooterMotor2 = new WPI_VictorSPX(Const.CAN.SHOOTER_MOTOR_2);
    public final WPI_VictorSPX shooterMotorReversed = new WPI_VictorSPX(Const.CAN.SHOOTER_MOTOR_REVERSED);

    public Shooter() {
        super(Const.Speed.SHOOTER_SPEED);
    }

    @Override
    public void runAt(double speed) {
        shooterMotor1.set(speed);
        shooterMotor2.set(speed);
        shooterMotorReversed.set(-speed);
    }
}