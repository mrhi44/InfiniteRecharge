/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Feed;

public class IntakeWithJoystick extends CommandBase {

  private Intake intake;
  private Feed feed;
  private XboxController xbox;
  private GenericHID.Hand bumper;

  /**
   * Creates a new IntakeWithJoystick.
   */
  public IntakeWithJoystick(Intake intake, Feed feed, XboxController xbox, GenericHID.Hand bumper) {
    addRequirements(intake, feed);
    this.intake = intake;
    this.feed = feed;
    this.xbox = xbox;
    this.bumper = bumper;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    boolean xBoxBumper = xbox.getBumper(bumper);
    if (xBoxBumper) {
      feed.closeFeed(true);
      feed.run();
      intake.run();
    } else {
      intake.stop();
      feed.stop();
    }
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
