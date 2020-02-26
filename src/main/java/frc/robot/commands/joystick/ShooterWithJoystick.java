/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.joystick;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;

public class ShooterWithJoystick extends CommandBase {

    private Shooter shooter;
    private XboxController.Button shooterButton;
    private XboxController.Axis hoodAxis;
    private XboxController xbox;

    /** Creates a new ShooterWithJoystick, of course. */
    public ShooterWithJoystick(Shooter shooter, XboxController xbox, XboxController.Button shooterButton, XboxController.Axis hoodAxis) {
        this.shooter = shooter;
        this.xbox = xbox;
        this.shooterButton = shooterButton;
        this.hoodAxis = hoodAxis;
        addRequirements(shooter);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (xbox.getRawButton(shooterButton.value)) {
            shooter.run();
        } else {
            shooter.stop();
        }
        shooter.setHoodSpeed(xbox.getRawAxis(hoodAxis.value));
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

}