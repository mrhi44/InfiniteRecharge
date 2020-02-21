/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.AirCompressor;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Feed;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import net.bancino.robotics.swerveio.exception.SwerveException;
import net.bancino.robotics.swerveio.exception.SwerveRuntimeException;
import net.bancino.robotics.swerveio.command.SwerveDriveTeleop;
import net.bancino.robotics.swerveio.command.RunnableCommand;
import frc.robot.commands.ElevatorWithJoystick;
import frc.robot.commands.IntakeWithJoystick;
import frc.robot.commands.LimelightAlign;
import frc.robot.commands.ShooterWithJoystick;
import net.bancino.robotics.swerveio.gyro.NavXGyro;
import net.bancino.robotics.jlimelight.Limelight;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.PowerDistributionPanel;


/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {

  private final XboxController xbox0 = new XboxController(0);
  private final XboxController xbox1 = new XboxController(1);

  /* The robot's subsystems and commands are defined here */
  //private final AirCompressor compressor = new AirCompressor();
  private final DriveTrain drivetrain;
  //private final Elevator elevator = new Elevator();
  //private final Feed feed = new Feed();
  //private final Intake intake = new Intake();
  //private final Shooter shooter = new Shooter();

  /* Additional global objects can go here. */
  private final PowerDistributionPanel pdp = new PowerDistributionPanel(Const.CAN.POWER_DISTRIBUTION_PANEL);
  private final NavXGyro gyro = new NavXGyro(SPI.Port.kMXP);
  private final Limelight limelight = new Limelight();

  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    /* Construct our subsystems here if they throw exceptions. */
    try {
      drivetrain = new DriveTrain(gyro);
    } catch (SwerveException e) {
      throw new SwerveRuntimeException(e);
    }

    // Configure the button bindings
    configureButtonBindings();
    configureCommands();
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {

    JoystickButton xbox0LeftBumper = new JoystickButton(xbox0, XboxController.Button.kBumperLeft.value);
    xbox0LeftBumper.whenPressed(new RunnableCommand(() -> {
      drivetrain.getGyro().zero();
    }));
    /** Change the limelight stream to the main feed with an UP on xbox0's dpad. */
    POVButton xboxPOV0 = new POVButton(xbox0, 0);
    xboxPOV0.toggleWhenPressed(new RunnableCommand(() -> {
      limelight.setStreamingMode(net.bancino.robotics.jlimelight.StreamMode.PIP_MAIN);
    } ));
    /** Change the limelight stream to the secondary feed with a DOWN on the xbox0's dpad. */
    POVButton xboxPOV180 = new POVButton(xbox0, 180);
    xboxPOV180.toggleWhenPressed(new RunnableCommand(() -> {
      limelight.setStreamingMode(net.bancino.robotics.jlimelight.StreamMode.PIP_SECONDARY);
    }));

    /** Uses xbox0's X button to activate LimelightAlign while held. */
    JoystickButton xbox0X = new JoystickButton(xbox0, XboxController.Button.kX.value);
    xbox0X.whileHeld(new LimelightAlign(drivetrain, limelight));
  }

  private void configureCommands() {
    /* The intake uses the given hand's trigger and bumper. */
    //intake.setDefaultCommand(new IntakeWithJoystick(intake, feed, xbox0, GenericHID.Hand.kLeft, GenericHID.Hand.kRight));
    /* The drivetrain uses three axes: forward, strafe, and angular velocity, in that order. */
    SwerveDriveTeleop swerveDriveTeleop = new SwerveDriveTeleop(drivetrain, xbox0, XboxController.Axis.kLeftY, XboxController.Axis.kLeftX, XboxController.Axis.kRightX);
    swerveDriveTeleop.setThrottle(0.4);
    drivetrain.setDefaultCommand(swerveDriveTeleop);
    /** The elevator uses the y axis of the left joystick. */
    //elevator.setDefaultCommand(new ElevatorWithJoystick(elevator, xbox1, XboxController.Axis.kLeftY));
    /** The shooter uses the right trigger. */
    //shooter.setDefaultCommand(new ShooterWithJoystick(shooter, xbox1, XboxController.Axis.kRightTrigger));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // This command will run in autonomous
    return null;
  }
}
