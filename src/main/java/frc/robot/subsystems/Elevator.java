package frc.robot.subsystems;

import frc.robot.Const;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.I2C;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import com.revrobotics.ColorSensorV3;

/**
 * The Elevator subsystem controls the elevator mechanism
 *
 * This subsystem consists of the following components:
 * - The elevator motor (1x Spark Max using internal NEO encoder.)
 * - The adjustment wheel (1x Talon SRX/Victor SPX controller on either CAN or PWM)
 * - The color sensor for the color wheel
 *
 * This subsystem should provide the following functions:
 * - Run a position loop on the elevator 
 * - Run a speed loop on the elevator
 * - Run a speed loop on the adjustment wheel
 * - Run a position loop for the color wheel (both matching color and rotations)
 */
public class Elevator extends SubsystemBase {

    private final CANSparkMax elevatorMotor = new CANSparkMax(Const.CAN.ELEVATOR_MOTOR, MotorType.kBrushless);
    private final WPI_VictorSPX wheelMotor = new WPI_VictorSPX(Const.CAN.ELEVATOR_WHEEL);
    private final ColorSensorV3 colorSensor = new ColorSensorV3(I2C.Port.kOnboard);

    public Elevator() {

    }
}