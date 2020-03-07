package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Const;
import frc.robot.subsystems.Elevator;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.I2C;


public class ColorWheelRotation extends CommandBase {

    Elevator elevator;
    public Elevator.WheelColor currentColor, previousColor;
    private final ColorSensorV3 colorSensor = new ColorSensorV3(I2C.Port.kOnboard);
    public int revCount;
    public boolean rotationComplete;
    
    public ColorWheelRotation(Elevator elevator) {
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
                previousColor = currentColor;
            }
            if (revCount >= Const.Elevator.NUMBER_OF_COLOR_CHANGES) {
                /* Reset everything to the starting configuration for the next run. */
                elevator.setWheelSpeed(0);
                revCount = 0;
                currentColor = null;
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
