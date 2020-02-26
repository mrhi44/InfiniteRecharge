package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

import com.revrobotics.ColorSensorV3;

public class ColorWheelDebug extends CommandBase {

    ColorSensorV3 colorSensorV3;
    String colorString;
    Boolean colorFound;

    public ColorWheelDebug(ColorSensorV3 colorSensorV3){
        this.colorSensorV3 = colorSensorV3;
    }

    public void execute() {
        Color detectedColor = colorSensorV3.getColor();
        int proximity = colorSensorV3.getProximity();

        SmartDashboard.putNumber("Red", detectedColor.red);
        SmartDashboard.putNumber("Green", detectedColor.green);
        SmartDashboard.putNumber("Blue", detectedColor.blue);
        SmartDashboard.putNumber("Proximity", proximity);
    }

    public boolean isFinished() {
        return false;
    }
}

