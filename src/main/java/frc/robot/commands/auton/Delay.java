/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auton;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class Delay extends CommandBase {
  private long startTime, duration;

  /**
   * A simple command, intended to be run in a sequence (not parallel), that
   * does nothing but wait the specified amount of time before finishing.
   *
   * @param duration The amount of time to wait in milliseconds.
   */
  public Delay(long duration) {
    this.duration = duration;
  }

  @Override
  public void initialize() {
    startTime = System.currentTimeMillis();
  }

  @Override
  public boolean isFinished() {
    return System.currentTimeMillis() - startTime >= duration;
  }
}
