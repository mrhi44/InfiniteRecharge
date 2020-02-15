package frc.robot.commands;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Elevator;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;


public class ColorFinder extends CommandBase {
    private final I2C.Port i2cPort = I2C.Port.kOnboard;
    private final ColorSensorV3 colorSensor = new ColorSensorV3(i2cPort);
    private final ColorMatch colorMatcher = new ColorMatch();
    private boolean colorFound;
    private Elevator.WheelColor wheelColor;
    
    Color detectedColor = colorSensor.getColor();
    ColorMatchResult match = colorMatcher.matchClosestColor(detectedColor);
    String desiredColor;
    Elevator elevator;
  
    public ColorFinder() {
    }

    public void initialize() {
    colorFound = false;
      }

    public void execute() {
        if (colorFound = false){
            elevator.goToColor(wheelColor);
            colorFound = true;
        }
    }

    public void end(boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return colorFound;
        }
    }