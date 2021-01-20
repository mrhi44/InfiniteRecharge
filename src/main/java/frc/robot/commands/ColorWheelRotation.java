package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Elevator;
import com.revrobotics.ColorSensorV3;


public class ColorWheelRotation extends CommandBase {

    private static final int elevatorColorChanges = RobotContainer.config().getInt("elevatorColorChanges");
    private static final double colorWheelSpeed = RobotContainer.config().getDouble("colorWheelSpeed");

    private Elevator elevator;
    private ColorSensorV3 colorSensor;
    private Elevator.WheelColor currentColor, previousColor;
    private int revCount;
    private boolean rotationComplete;
    
    public ColorWheelRotation(Elevator elevator) {
        this.colorSensor = elevator.getColorSensor();
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
            if (revCount >= elevatorColorChanges) {
                /* Reset everything to the starting configuration for the next run. */
                elevator.setWheelSpeed(0);
                revCount = 0;
                previousColor = null;
                rotationComplete = true;
            } else {
                elevator.setWheelSpeed(colorWheelSpeed);
            }
        }
    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return rotationComplete;
    }
}