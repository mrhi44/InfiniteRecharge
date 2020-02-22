/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Axis;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Const;
import frc.robot.subsystems.Shooter;

public class ShooterWithJoystick extends CommandBase {

    Shooter shooter;
    Axis kRightTrigger;
    XboxController xbox1;

    /** Creates a new ShooterWithJoystick, of course. */
    public ShooterWithJoystick(Shooter shooter, XboxController xbox1, Axis kRightTrigger) {
        this.shooter = shooter;
        this.xbox1 = xbox1;
        this.kRightTrigger = kRightTrigger;
        addRequirements(shooter);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (xbox1.getRawAxis(kRightTrigger.value) > 0.1) {
            shooter.runAt(Const.Speed.SHOOTER_SPEED);
        } else {
            shooter.runAt(0);
        }

    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

}