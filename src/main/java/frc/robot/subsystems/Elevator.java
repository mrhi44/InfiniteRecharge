package frc.robot.subsystems;

import frc.robot.commands.DriveWithJoystick;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.XboxController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.SpeedController;
import com.revrobotics.CANSparkMax;


public class Elevator extends SubsystemBase{

    private final CANSparkMax elevatorMotor = new CANSparkMax(0, MotorType.kBrushless);//(DeviceID, MotorType)

    public Elevator(){
        
    }
    public void stop(){
        
    }
}