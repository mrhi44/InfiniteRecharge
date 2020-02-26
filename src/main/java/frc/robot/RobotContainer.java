/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.io.IOException;

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
import net.bancino.robotics.swerveio.command.PathweaverSwerveDrive;
import net.bancino.robotics.swerveio.command.RunnableCommand;
import frc.robot.commands.joystick.ElevatorWithJoystick;
import frc.robot.commands.joystick.FeedWithJoystick;
import frc.robot.commands.joystick.IntakeWithJoystick;
import frc.robot.commands.joystick.ShooterWithJoystick;
import frc.robot.commands.vision.LimelightAlignBackHatch;
import frc.robot.commands.vision.LimelightAlignFrontHatch;
import net.bancino.robotics.swerveio.gyro.NavXGyro;
import net.bancino.robotics.jlimelight.Limelight;
import net.bancino.robotics.liboi.DeadbandedXboxController;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.PowerDistributionPanel;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
@SuppressWarnings("unused")
public class RobotContainer {

  private final DeadbandedXboxController xbox0 = new DeadbandedXboxController(0);
  private final DeadbandedXboxController xbox1 = new DeadbandedXboxController(1);

  /* The robot's subsystems and commands are defined here */
  private final AirCompressor compressor = new AirCompressor();
  private final DriveTrain drivetrain;
  private final Elevator elevator = new Elevator();
  private final Feed feed = new Feed();
  private final Intake intake = new Intake();
  private final Shooter shooter = new Shooter();

  /* Additional global objects can go here. */
  private final PowerDistributionPanel pdp = new PowerDistributionPanel(Const.CAN.POWER_DISTRIBUTION_PANEL);
  private final NavXGyro gyro = new NavXGyro(SPI.Port.kMXP);
  private final Limelight limelight = new Limelight();

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
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
   * Use this method to define your button->command mappings. Buttons can be
   * created by instantiating a {@link GenericHID} or one of its subclasses
   * ({@link edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then
   * passing it to a {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
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
    }));
    /**
     * Change the limelight stream to the secondary feed with a DOWN on the xbox0's
     * dpad.
     */
    POVButton xboxPOV180 = new POVButton(xbox0, 180);
    xboxPOV180.toggleWhenPressed(new RunnableCommand(() -> {
      limelight.setStreamingMode(net.bancino.robotics.jlimelight.StreamMode.PIP_SECONDARY);
    }));

    /** Uses xbox0's X button to activate LimelightAlignBackHatch while held. */
    JoystickButton xbox0X = new JoystickButton(xbox0, XboxController.Button.kX.value);
    xbox0X.whileHeld(new LimelightAlignBackHatch(drivetrain, limelight, shooter));

    /** Uses xbox0's A button to activate LimelightAlignFrontHatch while held. */
    JoystickButton xbox0A = new JoystickButton(xbox0, XboxController.Button.kA.value);
    xbox0A.whileHeld(new LimelightAlignFrontHatch(drivetrain, limelight, shooter));

    /* Toggle the intake. */
    JoystickButton xbox1B = new JoystickButton(xbox1, XboxController.Button.kB.value);
    xbox1B.whenPressed(new RunnableCommand(() -> {
      intake.lift(!intake.isUp());
    }));

    /* Toggle the elevator lock. */
    JoystickButton xbox1X = new JoystickButton(xbox1, XboxController.Button.kX.value);
    xbox1X.whenPressed(new RunnableCommand(() -> {
      elevator.setLocked(!elevator.isLocked());
    }));

    /* Toggle the feed stop. */
    JoystickButton xbox1Y = new JoystickButton(xbox1, XboxController.Button.kY.value);
    xbox1Y.whenPressed(new RunnableCommand(() -> {
      feed.closeStopper(!feed.stopperEngaged());
    }));
  }

  private void configureCommands() {
    /*
     * The drivetrain uses three axes: forward, strafe, and angular velocity, in
     * that order.
     */
    SwerveDriveTeleop swerveDriveTeleop = new SwerveDriveTeleop(drivetrain, xbox0, XboxController.Axis.kLeftY,
        XboxController.Axis.kLeftX, XboxController.Axis.kRightX);
    swerveDriveTeleop.setThrottle(Const.Speed.DRIVETRAIN_THROTTLE);
    /* The joystick is deadbanded, no need to deadband here. */
    swerveDriveTeleop.setDeadband(0);
    drivetrain.setDefaultCommand(swerveDriveTeleop);

    /** The elevator uses the y axis of the left joystick. */
    elevator.setDefaultCommand(new ElevatorWithJoystick(elevator, xbox1, XboxController.Axis.kLeftY, XboxController.Axis.kRightX));

    /* The intake uses the given hand's bumper. */
    intake.setDefaultCommand(new IntakeWithJoystick(intake, xbox1, XboxController.Button.kBumperLeft));
    
    /* The feed will use the left bumper and the A button for reverse. Notice the overlap; The feed will run at the same time as the intake. */
    feed.setDefaultCommand(new FeedWithJoystick(feed, xbox1, XboxController.Button.kBumperLeft, XboxController.Button.kA));

    /** The shooter uses the right bumper. */
    shooter.setDefaultCommand(new ShooterWithJoystick(shooter, xbox1, XboxController.Button.kBumperRight, XboxController.Axis.kRightY));
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // This command will run in autonomous
    try {
      drivetrain.getGyro().zero();
      return new PathweaverSwerveDrive(drivetrain, "paths/output/Kettering.wpilib.json", PathweaverSwerveDrive.PathExecutionMode.ROBOT_BACKWARDS);
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}
