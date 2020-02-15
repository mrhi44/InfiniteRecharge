package frc.robot.subsystems;

import frc.robot.Const;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;

import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.ControlType;

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
    private final CANPIDController elevatorPID = new CANPIDController(elevatorMotor);
    private String startingColor = colorSensor.getColor().toString();
    private WheelColor targetColor = null;
    private WheelColor wheelColor;
    private int revCount = 0;

    private static enum WheelColor {
        RED, GREEN, BLUE, YELLOW
    };

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
        elevatorPID.setReference(position, ControlType.kSmartMotion);
    }

    /**
     * Velocity loop control for the elevator.
     * 
     * @param speed Speed reference for elevator.
     */
    public void setElevatorVelocity(double speed) {
        elevatorMotor.set(speed);
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
     * Used to rotate the wheel a set amount of times.
     * 
     * @param rotationCount Amount of times to rotate the wheel.
     */
    public void rotateColorWheel(int rotationCount) {
        /** Counts each new color, meaning the cheese slices. */
        if (colorSensor.getColor().toString() != startingColor) {
            revCount++;
            startingColor = colorSensor.getColor().toString();
        }
        /** If we haven't reached our rotation count, keep spinning, dude. */
        if (revCount * 8 == rotationCount) {
            wheelMotor.set(0);
        } else {
            wheelMotor.set(Const.Speed.COLOR_WHEEL_FIXED_SPEED);
        }
    }

    /**
     * A convertor to feed color sensor data through to the enum WheelColor.
     * 
     * @param color Raw color output data from the Color Sensor.
     * @return The enum, RED, GREEN, BLUE, RED, to match that color.
     */
    public WheelColor convertToWheelColor(Color color) {
        switch (color.toString()) {
        case "Red":
            wheelColor = WheelColor.RED;
        case "Green":
            wheelColor = WheelColor.GREEN;
        case "Blue":
            wheelColor = WheelColor.BLUE;
        case "Yellow":
            wheelColor = WheelColor.YELLOW;
        }
        return wheelColor;
    }

    /**
     * Finds the color, offset by 2 counterclockwise, away from the given color.
     * @param color The color you're really looking to put under the sensor. Input
     * as a single character: R, G, B, Y.
     * @return The offset color, the one the robot will be reading. Returns as a
     * WheelColor.
     */
    public WheelColor colorToTargetColor(WheelColor color) {
        switch (color) {
        case RED:
            targetColor = WheelColor.BLUE;
        case GREEN:
            targetColor = WheelColor.YELLOW;
        case BLUE:
            targetColor = WheelColor.RED;
        case YELLOW:
            targetColor = WheelColor.GREEN;
        }
        return targetColor;
    }

    /**
     * Spins the wheelMotor until you're on you're targeted color.
     * @param offsetColor The desired color (Offset, not the actual game message color.)
     */
    public void goToColor(WheelColor offsetColor) {
        if (convertToWheelColor(colorSensor.getColor()) != offsetColor) {
            wheelMotor.set(Const.Speed.COLOR_WHEEL_FIXED_SPEED);
        } else {
            wheelMotor.set(0);
        }
    }
}