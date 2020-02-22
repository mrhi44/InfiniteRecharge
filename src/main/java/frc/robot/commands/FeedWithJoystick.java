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
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

public class FeedWithJoystick extends CommandBase {

    private Shooter shooter;
    private Feed feed;
    private Intake intake;
    private GenericHID.Hand intakeBumper, shooterBumper;
    private XboxController xbox;
    private long currentTime, timePressed = 0;
    private boolean firstPress = true;

    /** Creates a new ShooterWithJoystick, of course. */
    public FeedWithJoystick(Intake intake, Feed feed, Shooter shooter, XboxController xbox, GenericHID.Hand intakeBumper, GenericHID.Hand shooterBumper) {
        this.shooter = shooter;
        this.feed = feed;
        this.intake = intake;
        this.xbox = xbox;
        this.intakeBumper = intakeBumper;
        this.shooterBumper = shooterBumper;
        addRequirements(shooter, feed, intake);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        currentTime = System.currentTimeMillis();

        if (xbox.getBumper(shooterBumper)) {
            shooter.run();

            if (firstPress) {
                firstPress = false;
                timePressed = currentTime;
            }

            if (currentTime - timePressed >= Const.Time.SHOOTER_FEED_DELAY_MS) {
                //feed.run();
                //feed.closeFeed(false);
            }
        } 
        if (xbox.getBumper(intakeBumper)) {
            //feed.closeFeed(true);
            double speed = Const.Speed.FEED_SPEED;
            if (xbox.getAButton()) {
                speed *= -1;
            }
            feed.runAt(speed);
            intake.run();
        }

        if (!xbox.getBumper(intakeBumper) && !xbox.getBumper(shooterBumper)) {
            shooter.stop();
            feed.stop();
            intake.stop();
            firstPress = true;
        }

    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

}