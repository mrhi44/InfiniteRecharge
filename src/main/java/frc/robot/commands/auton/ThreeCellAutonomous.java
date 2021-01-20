/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auton;

import java.io.IOException;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.vision.LimelightAlign;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Feed;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Intake;
import net.bancino.robotics.swerveio.SwerveDrive;
import net.bancino.robotics.swerveio.command.PathweaverSwerveDrive;
import net.bancino.robotics.swerveio.command.PathweaverSwerveDrive.PathExecutionMode;
import net.bancino.robotics.jlimelight.Limelight;

/**
 * This command runs an autonomous mode that simply scores the three pre-loaded 
 * power cells. Nothing more, nothing less. It supports any path, just pass in the
 * path name to the constructor.
 *
 * @author Jordan Bancino
 */
public class ThreeCellAutonomous extends SequentialCommandGroup {
  
  private static final double feedWithShooterSpeed = RobotContainer.config().getDouble("feedWithShooterSpeed"));

  public ThreeCellAutonomous(String path, SwerveDrive swerve, Shooter shooter, Intake intake, Feed feed, Limelight limelight) throws IOException {
    super(
      new InstantCommand(() -> {
        shooter.run();
        intake.lift(true);
      }, shooter, intake),
      new PathweaverSwerveDrive(swerve, "paths/output/" + path + ".wpilib.json", PathExecutionMode.ROBOT_BACKWARDS),
      new LimelightAlign(swerve, limelight, shooter, true, 1000),
      new ParallelCommandGroup(
        new ShooterHoodWithLimelight(shooter, limelight),
        new SequentialCommandGroup(
          new WaitCommand(0.5),
          new InstantCommand(() -> feed.runAt(feedWithShooterSpeed, feed),
          new WaitCommand(4),
          new InstantCommand(() -> {
            shooter.stop();
            feed.stop();
          }, feed)  /* We don't require the shooter here because the hood command is using it. */
        )
      )
    );
  }
}
