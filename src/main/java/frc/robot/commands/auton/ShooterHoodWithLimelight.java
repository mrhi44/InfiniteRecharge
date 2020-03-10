/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auton;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;
import frc.robot.util.RollingAverage;
import net.bancino.robotics.jlimelight.LedMode;
import net.bancino.robotics.jlimelight.Limelight;

public class ShooterHoodWithLimelight extends CommandBase {
  private final Limelight limelight;
  private final RollingAverage rollingAverage = new RollingAverage(15);
  private final Shooter shooter;

  /**
   * Autonomously run the shooter hood position loop based on feedback from
   * the Limelight. This also uses a simple moving average to stabilize the
   * Limelight output before passing it into the hood position loop, so
   * movements will be much more consistent and controlled, if slightly laggy.
   *
   * @param shooter The shooter to use.
   * @param limelight The Limelight to use.
   */
  public ShooterHoodWithLimelight(Shooter shooter, Limelight limelight) {
    this.shooter = shooter;
    this.limelight = limelight;
    addRequirements(shooter);
  }

  @Override
  public void initialize() {
    limelight.setLedMode(LedMode.FORCE_ON);
  }

  @Override
  public void execute() {
    if (limelight.hasValidTargets()) {
      rollingAverage.add(limelight.getCamTran()[2]);
      shooter.setHoodPositionFromDistance(rollingAverage.get());
    }
  }

  @Override
  public void end(boolean interrupted) {
    limelight.setLedMode(LedMode.PIPELINE_CURRENT);
  }

  @Override
  public boolean isFinished() {
    return false;
  }
}
