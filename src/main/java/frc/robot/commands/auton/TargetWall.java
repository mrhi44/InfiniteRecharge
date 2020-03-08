/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auton;

import java.io.IOException;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Feed;
import frc.robot.subsystems.Shooter;
import net.bancino.robotics.swerveio.SwerveDrive;
import net.bancino.robotics.swerveio.command.PathweaverSwerveDrive;
import net.bancino.robotics.jlimelight.Limelight;
import net.bancino.robotics.liboi.command.RunnableCommand;

/**
 * This command runs if we are starting on the wall closest to the target.
 */
public class TargetWall extends SequentialCommandGroup {
  public TargetWall(String path, SwerveDrive swerve, Shooter shooter, Feed feed, Limelight limelight) throws IOException {
    super(
      new RunnableCommand(() -> shooter.run(), shooter),
      new PathweaverSwerveDrive(swerve, "paths/output/" + path + ".wpilib.json"),
      new ParallelCommandGroup(
        new ShooterHoodWithLimelight(shooter, limelight),
        new SequentialCommandGroup(
          new Delay(500),
          new RunnableCommand(() -> feed.run(), feed),
          new Delay(4000),
          new RunnableCommand(() -> {
            shooter.stop();
            feed.stop();
          }, shooter, feed)
        )
      )
    );
  }
}
