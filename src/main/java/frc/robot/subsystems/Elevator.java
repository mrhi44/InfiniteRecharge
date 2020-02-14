package frc.robot.subsystems;

import frc.robot.Const;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;

import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.ControlType;

/**
 * The Elevator subsystem controls the elevator mechanism
 *
 * This subsystem consists of the following components: - The elevator motor (1x
 * Spark Max using internal NEO encoder.) - The adjustment wheel (1x Talon
 * SRX/Victor SPX controller on either CAN or PWM) - The color sensor for the
 * color wheel
 *
 * This subsystem should provide the following functions: - Run a position loop
 * on the elevator - Run a speed loop on the elevator - Run a speed loop on the
 * adjustment wheel - Run a position loop for the color wheel (both matching
 * color and rotations)
 */
public class Elevator extends SubsystemBase {

    private final CANSparkMax elevatorMotor = new CANSparkMax(Const.CAN.ELEVATOR_MOTOR, MotorType.kBrushless);
    private final WPI_VictorSPX wheelMotor = new WPI_VictorSPX(Const.CAN.ELEVATOR_WHEEL);
    private final ColorSensorV3 colorSensor = new ColorSensorV3(I2C.Port.kOnboard);
    private final CANPIDController elevatorPID = new CANPIDController(elevatorMotor);
    private String targetColor = "";
    private int revCount = 0;
    private boolean done = false;


    public Elevator() {

    }

    /**
     * Position loop control for the elevator.
     * 
     * @param position Encoder reference for elevator setpoint.
     */
    public void setElevatorPosition(double position) {
        elevatorPID.setP(Const.PID.ELEVATOR_P);
        elevatorPID.setI(Const.PID.ELEVATOR_I);
        elevatorPID.setReference(position, ControlType.kPosition);
    }

    /**
     * Velocity loop control for the elevator.
     * 
     * @param velocity Speed reference for elevator.
     */
    public void setElevatorVelocity(double velocity) {
        elevatorPID.setP(Const.PID.ELEVATOR_P);
        elevatorPID.setI(Const.PID.ELEVATOR_I);
        elevatorPID.setReference(velocity, ControlType.kVelocity);
    }

    /**
     * Sets the color wheels speed.
     * 
     * @param speed The speed, between -1 and 1, of the motor.
     */
    public void setWheelSpeed(double speed) {
        wheelMotor.set(speed);
    }

    /**
     * Spins color wheel to meet color and revolution needs.
     * 
     * @param targetRevs Number of revolutions desired.
     * @param colorMode  Set true if we need to match the color on the wheel.
     */
    public void spinColorWheel(int targetRevs, boolean colorMode) {
        /** Counts revs with every color change. */
        String color = colorSensor.getColor().toString();
        if (colorSensor.getColor().toString() != color) {
            revCount++;
            color = colorSensor.getColor().toString();
        }
        /** Only counting revolutions, no color. */
        if (!colorMode) {
            while (revCount < targetRevs * 8) {
                wheelMotor.set(Const.Speed.COLOR_WHEEL_FIXED_SPEED);
            }
        /** Counting revolutions with the color. */
        } else if (colorMode) {
            if (color != colorToTargetColor() && !done) {
                wheelMotor.set(Const.Speed.COLOR_WHEEL_FIXED_SPEED);
            } else {
                done = true;
                while (revCount < targetRevs * 8) {
                    wheelMotor.set(Const.Speed.COLOR_WHEEL_FIXED_SPEED);
                }
            }
        }
    }

    /**
     * Serves only the purpose of finding the offset targeted color for the function
     * spinColorWheel().
     */
    public String colorToTargetColor() {
        String color = DriverStation.getInstance().getGameSpecificMessage();
        switch (color) {
        case "R":
            targetColor = "B";
        case "G":
            targetColor = "Y";
        case "B":
            targetColor = "R";
        case "Y":
            targetColor = "G";
        }
        return targetColor;
    }
}