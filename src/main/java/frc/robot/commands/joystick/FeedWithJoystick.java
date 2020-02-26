/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.joystick;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Const;
import frc.robot.subsystems.Feed;

public class FeedWithJoystick extends CommandBase {

    private Feed feed;
    private XboxController.Button runButton, reverseButton;
    private XboxController xbox;

    public FeedWithJoystick(Feed feed, XboxController xbox, XboxController.Button runButton, XboxController.Button reverseButton) {
        this.feed = feed;
        this.xbox = xbox;
        this.runButton = runButton;
        this.reverseButton = reverseButton;

        addRequirements(feed);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

        if (xbox.getRawButton(runButton.value)) {
            double speed = Const.Speed.FEED_SPEED;
            if (xbox.getRawButton(reverseButton.value)) {
                speed *= -1;
            }
            feed.runAt(speed);
        } else {
            feed.stop();
        }
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

}