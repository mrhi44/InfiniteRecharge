package frc.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Elevator;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;


public class ColorFinder extends CommandBase {
    private Elevator.WheelColor wheelColor;
    Elevator elevator;

    public void initialize(){
        switch (DriverStation.getInstance().getGameSpecificMessage()) {
            case "B":
            wheelColor = Elevator.WheelColor.BLUE;
            case "R":
            wheelColor = Elevator.WheelColor.RED;
            case "Y":
            wheelColor = Elevator.WheelColor.YELLOW;
            case "G":
            wheelColor = Elevator.WheelColor.GREEN;
        }
    }
  
    public ColorFinder(Elevator elevator) {
        addRequirements(elevator);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return elevator.goToColor(elevator.colorToTargetColor(wheelColor));
        }
    }