/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.io.File;
import java.io.IOException;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.AirCompressor;
import frc.robot.subsystems.DriveTrain;
import frc.robot.subsystems.Feed;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import net.bancino.robotics.swerveio.SwerveDrive;
import net.bancino.robotics.swerveio.command.SwerveDriveTeleop;
import net.bancino.robotics.swerveio.command.PathweaverSwerveDrive;
import frc.robot.commands.joystick.FeedWithJoystick;
import frc.robot.commands.joystick.IntakeWithJoystick;
import frc.robot.commands.joystick.ShooterWithJoystick;
import frc.robot.commands.vision.AutonBallGetter;
import frc.robot.commands.vision.LimelightAlign;
import frc.robot.commands.auton.ThreeCellAutonomous;
import net.bancino.robotics.swerveio.gyro.NavXGyro;
import net.bancino.robotics.jlimelight.Limelight;
import net.bancino.robotics.jlimelight.StreamMode;
import frc.robot.util.DeadbandedXboxController;
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

  /* Store the robot configuration. */
  private static Config cachedConfig;

  public static Config config() {
    if (cachedConfig == null) {
      try {
        cachedConfig = new Config();
      } catch (IOException e) {
        DriverStation.reportWarning("Failed to load configuration file.", false);
        throw new RuntimeException(e);
      }
    }
    return cachedConfig;
  }

  private static final double drivetrainThrottle = config().getDouble("drivetrainThrottle");
  private static final int pdpCanId = config().getInt("pdpCanId");

  /* Operator Interface */
  private final DeadbandedXboxController xbox0 = new DeadbandedXboxController(0);
  private final DeadbandedXboxController xbox1 = new DeadbandedXboxController(1);

  /* Global objects */
  private final PowerDistributionPanel pdp = new PowerDistributionPanel(pdpCanId);
  private final CameraServer camServer = CameraServer.getInstance();
  private final NavXGyro gyro = new NavXGyro(SPI.Port.kMXP);
  private final Limelight limelight = new Limelight();

  /* The robot's subsystems */
  private final AirCompressor compressor = new AirCompressor();
  private final SwerveDrive drivetrain = DriveTrain.create(gyro);
  private final Feed feed = new Feed();
  private final Intake intake = new Intake();
  private final Shooter shooter = new Shooter();

  private final SendableChooser<Command> autonCommands = new SendableChooser<>();

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    xbox0.setJoystickDeadband(GenericHID.Hand.kLeft, 0.3);
    xbox0.setJoystickDeadband(GenericHID.Hand.kRight, 0.2);

    limelight.setStreamMode(StreamMode.PIP_SECONDARY);

    // Configure the button bindings
    configureButtonBindings();
    configureCommands();

    // Configure autonomous modes
    File pathDir = new File(Filesystem.getDeployDirectory(), "paths/output");
    File[] paths = pathDir.listFiles((file) -> file.getName().endsWith(".wpilib.json"));

    for (int i = 0; i < paths.length; i++) {
      File path = paths[i];
      try {
        String name = path.getName().replaceAll(".wpilib.json", "");
        Command auto =  new PathweaverSwerveDrive(drivetrain, path, PathweaverSwerveDrive.PathExecutionMode.NORMAL, false);
        if (i == 0) {
          autonCommands.setDefaultOption(name, auto);
        } else {
          autonCommands.addOption(name, auto);
        }
      } catch (IOException e) {
        DriverStation.reportError("Unable to load path: " + path.getName(), false);
        e.printStackTrace();
      }
    }
    SmartDashboard.putData("Autonomous", autonCommands);
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
    xbox0Start.whenPressed(new InstantCommand(() -> {
      gyro.zero();
    }));

    /* Toggle field-centric drive (should only be used if we lose the gyro during a match) */
    JoystickButton xbox0Back = new JoystickButton(xbox0, XboxController.Button.kBack.value);
    xbox0Back.whenPressed(new InstantCommand(() -> {
      drivetrain.setFieldCentric(!drivetrain.isFieldCentric());
    }));
    

    /* Change the limelight's camera stream mode. */
    POVButton xbox0POV0 = new POVButton(xbox0, 0);
    xbox0POV0.toggleWhenPressed(new InstantCommand(() -> {
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
    xbox0POV180.toggleWhenPressed(new InstantCommand(() -> {
      /**
       * TODO: Implement this here
       */
    }));

    /** Uses xbox0's X button to activate LimelightAlign (Back Hatch) while held. */
    JoystickButton xbox0X = new JoystickButton(xbox0, XboxController.Button.kX.value);
    xbox0X.whileHeld(new LimelightAlign(drivetrain, limelight, shooter, false, -1));

    /** Uses xbox0's A button to activate LimelightAlign (Front Hatch) while held. */
    JoystickButton xbox0A = new JoystickButton(xbox0, XboxController.Button.kA.value);
    xbox0A.whileHeld(new LimelightAlign(drivetrain, limelight, shooter, true, -1));

    /** Usex xbox0's B button to activate AutonBallGetter while held. */
    JoystickButton xbox0B = new JoystickButton(xbox0, XboxController.Button.kB.value);
    xbox0B.whileHeld(new AutonBallGetter(drivetrain));

    /* Toggle the intake. */
    POVButton xbox1POV0 = new POVButton(xbox1, 0);
    xbox1POV0.whenPressed(new InstantCommand(() -> {
      intake.lift(true);
    }));
    POVButton xbox1POV180 = new POVButton(xbox1, 180);
    xbox1POV180.whenPressed(new InstantCommand(() -> {
      intake.lift(false);
    }));

   
  }

  private void configureCommands() {
    /*
     * The drivetrain uses three axes: forward, strafe, and angular velocity, in
     * that order.
     */
    SwerveDriveTeleop swerveDriveTeleop = new SwerveDriveTeleop(drivetrain, xbox0, XboxController.Axis.kLeftY,
        XboxController.Axis.kLeftX, XboxController.Axis.kRightX);
    swerveDriveTeleop.setThrottle(drivetrainThrottle);
    drivetrain.setDefaultCommand(swerveDriveTeleop);

  

    /* The intake uses the given hand's bumper. */
    intake.setDefaultCommand(new IntakeWithJoystick(intake, feed, xbox1, XboxController.Button.kA));
    
    /* The feed will use the left bumper and the A button for reverse. Notice the overlap; The feed will run at the same time as the intake. */
    feed.setDefaultCommand(new FeedWithJoystick(feed, shooter, xbox1, XboxController.Button.kA, XboxController.Button.kB, XboxController.Button.kX, XboxController.Button.kBumperRight));

    /** The shooter uses the right bumper. */
    ShooterWithJoystick shooterWithJoystick = new ShooterWithJoystick(shooter, limelight, xbox1, XboxController.Button.kBumperLeft, XboxController.Axis.kRightY);
    JoystickButton xbox1Y = new JoystickButton(xbox1, XboxController.Button.kY.value);
    xbox1Y.whenPressed(new InstantCommand(() -> {
      shooterWithJoystick.setManualHoodControl(!shooterWithJoystick.hoodManuallyControlled());
    }));
    shooter.setDefaultCommand(shooterWithJoystick);
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    return autonCommands.getSelected();
  }
}
