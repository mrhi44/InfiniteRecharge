package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Const;
import frc.robot.subsystems.Elevator;
import com.revrobotics.ColorSensorV3;


public class ColorWheelRotation extends CommandBase {

    Elevator elevator;
    ColorSensorV3 colorSensor;
    public Elevator.WheelColor currentColor, previousColor;
    public int revCount;
    public boolean rotationComplete;
    
    public ColorWheelRotation(Elevator elevator, ColorSensorV3 colorSensor) {
        this.colorSensor = colorSensor;
        this.elevator = elevator;
        addRequirements(elevator);
    }
        public void initialize(){
            revCount = 0;
            rotationComplete = false;
        }

        public void execute(){
            currentColor = elevator.toWheelColor(colorSensor.getColor());
            if (previousColor != currentColor){
                revCount++;
                previousColor = elevator.toWheelColor(colorSensor.getColor());
                previousColor = currentColor;
            }
            if (revCount >= Const.Elevator.NUMBER_OF_COLOR_CHANGES) {
                /* Reset everything to the starting configuration for the next run. */
                elevator.setWheelSpeed(0);
                revCount = 0;
                previousColor = null;
                rotationComplete = true;
            } else {
                elevator.setWheelSpeed(Const.Speed.COLOR_WHEEL_FIXED_SPEED);
            }
        }
    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return rotationComplete;
    }
}