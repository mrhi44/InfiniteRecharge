package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;

import com.revrobotics.ColorSensorV3;


public class ColorWheelDebug extends CommandBase {
    String colorString;
    Boolean colorFound;
    private final I2C.Port i2cPort = I2C.Port.kOnboard;
    private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);

    public ColorWheelDebug(){
    }

    public void execute() {
        Color detectedColor = m_colorSensor.getColor();
        int proximity = m_colorSensor.getProximity();

        SmartDashboard.putNumber("Red", detectedColor.red);
        SmartDashboard.putNumber("Green", detectedColor.green);
        SmartDashboard.putNumber("Blue", detectedColor.blue);
        SmartDashboard.putNumber("Proximity", proximity);
    }

    public boolean isFinished() {
        return false;
    }
}

