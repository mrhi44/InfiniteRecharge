/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auton;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;
import net.bancino.robotics.jlimelight.LedMode;
import net.bancino.robotics.jlimelight.Limelight;

public class ShooterHoodWithLimelight extends CommandBase {
  private Limelight limelight;
  private Shooter shooter;
  /**
   * Creates a new ShooterHoodWIthLimelight.
   */
  public ShooterHoodWithLimelight(Shooter shooter, Limelight limelight) {
    this.shooter = shooter;
    this.limelight = limelight;
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(shooter);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    limelight.setLedMode(LedMode.FORCE_ON);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (limelight.hasValidTargets()) {
      shooter.setHoodPositionFromDistance(Math.abs(limelight.getCamTran()[2]));
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    limelight.setLedMode(LedMode.PIPELINE_CURRENT);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }
}
