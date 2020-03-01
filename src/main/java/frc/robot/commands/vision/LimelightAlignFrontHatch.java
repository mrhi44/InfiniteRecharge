/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.vision;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Const;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Shooter;
import net.bancino.robotics.jlimelight.LedMode;
import net.bancino.robotics.jlimelight.Limelight;
import net.bancino.robotics.swerveio.geometry.SwerveVector;

public class LimelightAlignFrontHatch extends CommandBase {

    DriveTrain drivetrain;
    Limelight limelight;
    Shooter shooter;
    double[] camtran;
    double fwd, str, rcw;
    boolean strafeGood, forwardGood, rotateGood, allGood;
    int hoodOffset;

    public LimelightAlignFrontHatch(DriveTrain drivetrain, Limelight limelight, Shooter shooter) {
        this.drivetrain = drivetrain;
        this.limelight = limelight;
        this.shooter = shooter;
        addRequirements(drivetrain, shooter);
    }

    @Override
    public void initialize() {
        drivetrain.setFieldCentric(false);
        limelight.setLedMode(LedMode.FORCE_ON);
        limelight.setPipeline(0);
    }

    @Override
    public void execute() {
        camtran = limelight.getCamTran();

        /** Assigns rotation and assigns acceptable offset conditions. */
        rcw = camtran[4] * Const.LimelightAlign.ROTATE_ADJUST_SPEED;
        if ((camtran[4] <= Const.LimelightAlign.ACCEPTED_OFFSET_BOUNDS) && (camtran[4] > -Const.LimelightAlign.ACCEPTED_OFFSET_BOUNDS)) {
            rcw = 0;
        }

        SwerveVector alignmentVector = new SwerveVector(0, 0, -rcw);
        //SwerveVector alignmentVector = new SwerveVector(str, fwd, rcw); for testing on swervio
        drivetrain.drive(alignmentVector);
        //shooter.setHoodPosition(camtran[2]);
    }


    @Override
    public void end(boolean interrupted) {
        drivetrain.setFieldCentric(true);
        limelight.setLedMode(LedMode.PIPELINE_CURRENT);
    }
    @Override
    public boolean isFinished() {
        return false;
    }
    
}