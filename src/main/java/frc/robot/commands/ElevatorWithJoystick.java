/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Axis;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Elevator;

public class ElevatorWithJoystick extends CommandBase {

    Elevator elevator;
    XboxController xbox;
    XboxController.Axis axis, wheelAxis;
    double position;
    double basePosition;
    double manualOffset;
    double speedRef;

    public ElevatorWithJoystick(Elevator elevator, XboxController xbox, Axis axis, Axis wheelAxis) {
        this.xbox = xbox;
        this.elevator = elevator;
        this.axis = axis;
        this.wheelAxis = wheelAxis;
        addRequirements(elevator);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        manualOffset = elevator.getElevatorEncoder();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        elevator.setWheelSpeed(xbox.getRawAxis(wheelAxis.value));
        /**
         * TODO: Either remove the throttle or put it in the constants.
         */
        elevator.setElevatorSpeed(xbox.getRawAxis(axis.value) * 0.4);
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