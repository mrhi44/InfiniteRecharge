/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.io.IOException;

import edu.wpi.first.cameraserver.CameraServer;
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
import frc.robot.commands.vision.AutonBallGetter;
import frc.robot.commands.vision.LimelightAlign;
import frc.robot.commands.ColorFinder;
import frc.robot.commands.ColorWheelRotation;
import net.bancino.robotics.swerveio.gyro.NavXGyro;
import net.bancino.robotics.jlimelight.Limelight;
import net.bancino.robotics.jlimelight.StreamMode;
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
  private final CameraServer camServer = CameraServer.getInstance();
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

    limelight.setStreamMode(StreamMode.PIP_SECONDARY);

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
    /* Zero the gyro when the start button is pressed. */
    JoystickButton xbox0Start = new JoystickButton(xbox0, XboxController.Button.kStart.value);
    xbox0Start.whenPressed(new RunnableCommand(() -> {
      drivetrain.getGyro().zero();
    }));

    /* Toggle field-centric drive (should only be used if we lose the gyro during a match) */
    JoystickButton xbox0Back = new JoystickButton(xbox0, XboxController.Button.kBack.value);
    xbox0Back.whenPressed(new RunnableCommand(() -> {
      drivetrain.setFieldCentric(!drivetrain.isFieldCentric());
    }));
    
    /* Rotate the color wheel 3 times with wheel on elevator*/
    JoystickButton xbox1B = new JoystickButton(xbox0, XboxController.Button.kB.value);
    xbox1B.whileHeld(new ColorWheelRotation(elevator));

    /* Change the limelight's camera stream mode. */
    POVButton xbox0POV0 = new POVButton(xbox0, 0);
    xbox0POV0.toggleWhenPressed(new RunnableCommand(() -> {
      if (limelight.getStreamMode() == StreamMode.PIP_MAIN) {
        limelight.setStreamMode(StreamMode.PIP_SECONDARY);
      } else if (limelight.getStreamMode() == StreamMode.PIP_SECONDARY) {
        limelight.setStreamMode(StreamMode.PIP_MAIN);
      } else {
        /* This shouldn't happen, it should be either main or secondary. */
        limelight.setStreamMode(StreamMode.STANDARD);
      }
    }));
    /* Change the camera source on the dashboard between Limelight and Raspberry PI */
    POVButton xbox0POV180 = new POVButton(xbox0, 180);
    xbox0POV180.toggleWhenPressed(new RunnableCommand(() -> {
      /**
       * TODO: Implement this here
       */
    }));

    /** Uses xbox0's X button to activate LimelightAlign (Back Hatch) while held. */
    JoystickButton xbox0X = new JoystickButton(xbox0, XboxController.Button.kX.value);
    xbox0X.whileHeld(new LimelightAlign(drivetrain, limelight, shooter, false));

    /** Uses xbox0's A button to activate LimelightAlign (Front Hatch) while held. */
    JoystickButton xbox0A = new JoystickButton(xbox0, XboxController.Button.kA.value);
    xbox0A.whileHeld(new LimelightAlign(drivetrain, limelight, shooter, true));

    /** Usex xbox0's B button to activate AutonBallGetter while held. */
    JoystickButton xbox0B = new JoystickButton(xbox0, XboxController.Button.kB.value);
    xbox0B.whileHeld(new AutonBallGetter(drivetrain));

    /* Toggle the intake. */
    POVButton xbox1POV0 = new POVButton(xbox1, 0);
    xbox1POV0.whenPressed(new RunnableCommand(() -> {
      intake.lift(true);
    }));
    POVButton xbox1POV180 = new POVButton(xbox1, 180);
    xbox1POV180.whenPressed(new RunnableCommand(() -> {
      intake.lift(false);
    }));

    /* Toggle the elevator lock. */
    JoystickButton xbox1X = new JoystickButton(xbox1, XboxController.Button.kX.value);
    xbox1X.whenPressed(new RunnableCommand(() -> {
      elevator.setLocked(!elevator.isLocked());
    }));

    JoystickButton xbox1Start = new JoystickButton(xbox1, XboxController.Button.kStart.value);
    xbox1Start.whenPressed(new RunnableCommand(() -> {
      elevator.zeroElevatorEncoder();
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
    swerveDriveTeleop.setFlipped(SwerveDriveTeleop.Axis.FWD, false);
    swerveDriveTeleop.setFlipped(SwerveDriveTeleop.Axis.STR, true);
    swerveDriveTeleop.setFlipped(SwerveDriveTeleop.Axis.RCW, true);
    /* The joystick is deadbanded, no need to deadband here. */
    swerveDriveTeleop.setDeadband(0.3);
    drivetrain.setDefaultCommand(swerveDriveTeleop);

    /** The elevator uses the y axis of the left joystick. */
    elevator.setDefaultCommand(new ElevatorWithJoystick(elevator, xbox1, XboxController.Axis.kLeftY, XboxController.Axis.kLeftX, XboxController.Button.kBack));

    /* The intake uses the given hand's bumper. */
    intake.setDefaultCommand(new IntakeWithJoystick(intake, xbox1, XboxController.Button.kBumperRight));
    
    /* The feed will use the left bumper and the A button for reverse. Notice the overlap; The feed will run at the same time as the intake. */
    feed.setDefaultCommand(new FeedWithJoystick(feed, xbox1, XboxController.Button.kBumperRight, XboxController.Button.kA));

    /** The shooter uses the right bumper. */
    shooter.setDefaultCommand(new ShooterWithJoystick(shooter, xbox1, XboxController.Button.kBumperLeft, XboxController.Axis.kRightY));
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
