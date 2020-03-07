/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.joystick;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.subsystems.Feed;
import frc.robot.subsystems.Intake;

public class IntakeWithJoystick extends CommandBase {

  private Intake intake;
  private Feed feed;
  private XboxController xbox;
  private XboxController.Button intakeButton;

  /**
   * Creates a new IntakeWithJoystick.
   */
  public IntakeWithJoystick(Intake intake, Feed feed, XboxController xbox, XboxController.Button intakeButton) {
    addRequirements(intake);
    this.intake = intake;
    this.feed = feed;
    this.xbox = xbox;
    this.intakeButton = intakeButton;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (xbox.getRawButton(intakeButton.value) && !intake.isUp() && feed.getSpeed() > 0) {
      intake.run();
    } else {
      intake.stop();
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
