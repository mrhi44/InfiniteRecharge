/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.joystick;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Const;
import frc.robot.subsystems.Shooter;
import net.bancino.robotics.jlimelight.LedMode;
import net.bancino.robotics.jlimelight.Limelight;

public class ShooterWithJoystick extends CommandBase {

    private Shooter shooter;
    private XboxController.Button shooterButton;
    private XboxController.Axis hoodAxis;
    private XboxController xbox;
    private Limelight limelight;

    private boolean manualHoodControl = false;

    private double[] camtranHistory = new double[10];
    private int historyPointer = 0;

    private double positionRef = 0;

    /** Creates a new ShooterWithJoystick, of course. */
    public ShooterWithJoystick(Shooter shooter, Limelight limelight, XboxController xbox,
            XboxController.Button shooterButton, XboxController.Axis hoodAxis) {
        this.shooter = shooter;
        this.limelight = limelight;
        this.xbox = xbox;
        this.shooterButton = shooterButton;
        this.hoodAxis = hoodAxis;
        addRequirements(shooter);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        SmartDashboard.putBoolean("Commands/ShooterWithJoystick/Manual Hood Control", manualHoodControl);
        SmartDashboard.putNumber("Commands/ShooterWithJoystick/Manual Hood Reference", positionRef);
        /*
         * The hood can be manually controlled by the hood axis. Here, this speed
         * reference is translated into a position reference that can be moved up and
         * down. This will not let the hood overrun the bounds set in the constants
         * class.
         */
        if (manualHoodControl) {
            double speedRef = -xbox.getRawAxis(hoodAxis.value);
            positionRef = positionRef + (speedRef * Const.Shooter.HOOD_POSITION_INCREMENT);
            if (positionRef > Const.Shooter.MAX_HOOD_POSITION) {
                positionRef = Const.Shooter.MAX_HOOD_POSITION;
            } else if (positionRef < Const.Shooter.MIN_HOOD_POSITION) {
                positionRef = Const.Shooter.MIN_HOOD_POSITION;
            }
        }
        double[] camtran = limelight.getCamTran();
        double distance = -1;
        if (camtran.length > 2) {
            distance = camtran[2];
            SmartDashboard.putNumber("Commands/ShooterWithJoystick/Limelight 3D Distance", distance);
        }

        /**
         * If the shooter button is activated, run the shooter and adjust the hood based
         * on the Limelight's camtran reading.
         */
        if (xbox.getRawButton(shooterButton.value)) {
            shooter.run();
            /* Force the Limelight on to compute the hood position. */
            limelight.setLedMode(LedMode.FORCE_ON);
            if (limelight.hasValidTargets() && !manualHoodControl) {
                if (historyPointer < camtranHistory.length) {
                    
                    if (distance != -1) {
                        camtranHistory[historyPointer] = Math.abs(distance);
                        historyPointer++;
                    }
                } else {
                    double sum = 0;
                    for (int i = 0; i < historyPointer; i++) {
                        sum += camtranHistory[i];
                    }
                    shooter.setHoodPositionFromDistance(sum / historyPointer);
                }
            } else {
                historyPointer = 0;
                shooter.setHoodPosition((int) positionRef);
            }
            //shooter.setHoodPosition(8000);
        } else {
            shooter.stop();
            limelight.setLedMode(LedMode.PIPELINE_CURRENT);

            historyPointer = 0;
            shooter.setHoodPosition((int) positionRef);
            //shooter.setHoodPosition(0);
        }
    }

    public void setManualHoodControl(boolean manualHoodControl) {
        this.manualHoodControl = manualHoodControl;
    }

    public boolean hoodManuallyControlled() {
        return manualHoodControl;
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

}