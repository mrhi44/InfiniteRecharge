package frc.robot.commands;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Elevator;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;


public class ColorWheelRotation extends CommandBase {
    private final I2C.Port i2cPort = I2C.Port.kOnboard;
    private final ColorSensorV3 colorSensor = new ColorSensorV3(i2cPort);
    private final ColorMatch colorMatcher = new ColorMatch();
    private String colorString;
    private boolean colorFound = false;
    private boolean rotationComplete = false;
    
    Color detectedColor = colorSensor.getColor();
    ColorMatchResult match = colorMatcher.matchClosestColor(detectedColor);
    String desiredColor;
    Elevator elevator;
  
    public ColorWheelRotation(Elevator elevator) {
      addRequirements(elevator);
    }

    public void initialize() {
    rotationComplete = false;
      }

    public void execute() {
        if (rotationComplete == false){
            elevator.rotateColorWheel(25);
            rotationComplete = true;
        }

        /* Print values to the Smart Dashboard */
        SmartDashboard.putNumber("ColorSensor2/Red", colorSensor.getRed());
        SmartDashboard.putNumber("ColorSensor2/Green", colorSensor.getGreen());
        SmartDashboard.putNumber("ColorSensor2/Blue", colorSensor.getBlue());
        SmartDashboard.putString("ColorSensor2/Color", colorString);
        SmartDashboard.putString("ColorSensor2/desiredColor", desiredColor);
        SmartDashboard.putBoolean("ColorSensor2/ColorFound", colorFound);
        SmartDashboard.putBoolean("ColorSensor2/ ConditionMet", colorString.equals(desiredColor));
    }

    public void end(boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return rotationComplete;
    }
}