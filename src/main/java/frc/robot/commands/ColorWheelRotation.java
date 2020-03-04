package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Elevator;

public class ColorWheelRotation extends CommandBase {
    Elevator elevator;
  
    public ColorWheelRotation(Elevator elevator) {
        this.elevator = elevator;
        addRequirements(elevator);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return elevator.rotateColorWheel();
    }
}
