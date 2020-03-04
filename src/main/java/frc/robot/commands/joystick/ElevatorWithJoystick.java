/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.joystick;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Const;
import frc.robot.subsystems.Elevator;

public class ElevatorWithJoystick extends CommandBase {

    private Elevator elevator;
    private XboxController xbox;
    private XboxController.Axis axis, wheelAxis;
    private XboxController.Button positionOverride;

    public ElevatorWithJoystick(Elevator elevator, XboxController xbox, XboxController.Axis axis, XboxController.Axis wheelAxis, XboxController.Button positionoverride) {
        this.xbox = xbox;
        this.elevator = elevator;
        this.axis = axis;
        this.wheelAxis = wheelAxis;
        this.positionOverride = positionOverride;
        addRequirements(elevator);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

        double speedRef = -xbox.getRawAxis(axis.value);
        double currentPosition = elevator.getElevatorEncoder();

        /* Run the elevator, but only if it's within the encoder bounds (if the override button isn't being pressed). */
        boolean checkBounds = xbox.getRawButton(positionOverride.value);
        if (checkBounds && currentPosition >= Const.Elevator.MAX_HEIGHT && speedRef > 0) {
            elevator.setElevatorSpeed(0);
        } else if (checkBounds && currentPosition <= Const.Elevator.BOTTOM_HEIGHT && speedRef < 0) {
            elevator.setElevatorSpeed(0);
        } else {
            elevator.setElevatorSpeed(speedRef);
        }

        /* Run the elevator adjustment wheel at a given speed, determined by the height of the elevator.
         * If the elevator is above 70% of it's maximum height, we can safely assume that we are running
         * the endgame bar speed, otherwise, we're running the color wheel speed.
         */
        speedRef = 0;
        if (currentPosition > Const.Elevator.MAX_HEIGHT * 0.7) {
            speedRef = Const.Speed.ENDGAME_BAR_SPEED;
        } else {
            speedRef = Const.Speed.COLOR_WHEEL_FIXED_SPEED;
        }
        if (xbox.getRawAxis(wheelAxis.value) < 0) {
            speedRef *= -1;
        } else if (xbox.getRawAxis(wheelAxis.value) == 0) {
            speedRef = 0;
        }
        elevator.setWheelSpeed(speedRef);
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