/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Const;
import frc.robot.subsystems.Feed;
import frc.robot.subsystems.Shooter;

public class ShooterWithJoystick extends CommandBase {

    private Shooter shooter;
    private Feed feed;
    private GenericHID.Hand bumper;
    private XboxController xbox;
    private long currentTime, timePressed = 0;
    private boolean firstPress = true;

    /** Creates a new ShooterWithJoystick, of course. */
    public ShooterWithJoystick(Shooter shooter, Feed feed, XboxController xbox, GenericHID.Hand bumper) {
        this.shooter = shooter;
        this.feed = feed;
        this.xbox = xbox;
        this.bumper = bumper;
        addRequirements(shooter, feed);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        currentTime = System.currentTimeMillis();

        if (xbox.getBumper(bumper)) {
            shooter.run();

            if (firstPress) {
                firstPress = false;
                timePressed = currentTime;
            }

            if (currentTime - timePressed >= Const.Time.SHOOTER_FEED_DELAY_MS) {
                feed.run();
                feed.closeFeed(false);
            }
        } else {
            shooter.stop();
            feed.stop();
            feed.closeFeed(true);
            firstPress = true;
        }

    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

}