package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.SPI;

/**
 * The Gyro allows field centric navigation.
 * 
 * @author Jordan Bancino
 */
public class Gyro extends SubsystemBase {

  /* Create the AHRS NavX Gyro */
  private final AHRS navxGyro = new AHRS(SPI.Port.kMXP);

  /**
   * Zero the gyro.
   */
  public Gyro() {
    zero();
  }

  /**
   * Get the continuous angle of the gyro, accumulative.
   * 
   * @return The continuous angle straight from the gyro.
   */
  public double getAngle() {
    return navxGyro.getAngle();
  }

  /**
   * Get the actual yaw value, this is not continuous, it reports an actual angle.
   * 
   * @return The direction in degrees that the gyro is facing.
   */
  public double getYaw() {
    return navxGyro.getYaw();
  }

  public void zero() {
    System.out.println("NavX Zero-ed!");
    navxGyro.zeroYaw();
  }

  public boolean isConnected() {
    return navxGyro.isConnected();
  }

  @Override
  public void periodic() {
    SmartDashboard.putNumber("Gyro/Yaw", getYaw());
  }
}
