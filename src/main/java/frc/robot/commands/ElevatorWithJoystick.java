/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Const;
import frc.robot.subsystems.Elevator;

public class ElevatorWithJoystick extends CommandBase {

    Elevator elevator;
    XboxController xbox1;
    double position;
    double basePosition;
    double manualOffset;
    double speedRef;

    public ElevatorWithJoystick(Elevator elevator, XboxController xbox1) {
        this.elevator = elevator;
        this.xbox1 = xbox1;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        manualOffset = elevator.getElevatorEncoder();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        speedRef = xbox1.getRawAxis(1);
        /**
         * Sets the cases for each button press. Y will unlock the position loop and
         * allow free control.
         */
        if (xbox1.getYButton()) {
            elevator.setElevatorSpeed(speedRef);
            manualOffset = 0;
            basePosition = elevator.getElevatorEncoder();
        } else {
            if (xbox1.getXButton()) {
                basePosition = Const.Elevator.MAX_HEIGHT;
            } else if (xbox1.getAButton()) {
                basePosition = Const.Elevator.COLOR_WHEEL_HEIGHT;
            } else if (xbox1.getBButton()) {
                basePosition = Const.Elevator.BOTTOM_HEIGHT;
            }

            /**
             * Creates a manual offset based off of current position and the (modified)
             * joystick input.
             */
            manualOffset = manualOffset + (Const.Elevator.INCREMENT * speedRef);

            /**
             * These two statements serve to moderate the movement of the elevator
             * from a sudden change from manual movement to a setpoint.
             */
            if ((manualOffset + basePosition) < Const.Elevator.BOTTOM_HEIGHT) {
                manualOffset = (Const.Elevator.BOTTOM_HEIGHT - basePosition);
            }
            if ((manualOffset + basePosition) > Const.Elevator.MAX_HEIGHT) {
                manualOffset = (Const.Elevator.MAX_HEIGHT - basePosition);
            }

            /** Finally, the position is both the manual offset and the setpoint. */
            position = basePosition + manualOffset;

            /** Feed that sweet sweet position into the actual motor control. */
            elevator.setElevatorPosition(position);
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}