package frc.robot.commands;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Const;
import frc.robot.subsystems.Elevator;
import edu.wpi.first.wpilibj.DriverStation;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;


public class ColorWheelWithJoystick extends CommandBase {
    private final I2C.Port i2cPort = I2C.Port.kOnboard;
    private final ColorSensorV3 colorSensor = new ColorSensorV3(i2cPort);
    private final ColorMatch colorMatcher = new ColorMatch();
    private final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
    private final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
    private final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
    private final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);
    private String colorString;
    private boolean colorFound = false;
    private boolean rotationComplete = false;
    private Elevator.WheelColor wheelColor;

    edu.wpi.first.wpilibj.PWMVictorSPX pwm = new edu.wpi.first.wpilibj.PWMVictorSPX(0);
    
    Color detectedColor = colorSensor.getColor();
    ColorMatchResult match = colorMatcher.matchClosestColor(detectedColor);
    String desiredColor;
    String gameSpecificColor = DriverStation.getInstance().getGameSpecificMessage();
    Elevator elevator;
  
    public ColorWheelWithJoystick() {
    }

    public void initialize() {
      switch (gameSpecificColor){
        case "R":
       wheelColor = Elevator.WheelColor.BLUE;
        case "B":
        wheelColor = Elevator.WheelColor.RED;
        case "G":
        wheelColor = Elevator.WheelColor.YELLOW;
        case "Y":
        wheelColor = Elevator.WheelColor.GREEN;
      }

        /*if (gameSpecificColor != null){
        colorMatcher.addColorMatch(kBlueTarget);
        colorMatcher.addColorMatch(kGreenTarget);
        colorMatcher.addColorMatch(kRedTarget);
        colorMatcher.addColorMatch(kYellowTarget);
        //Color detectedColor = colorSensor.getColor();
        //ColorMatchResult match = colorMatcher.matchClosestColor(detectedColor);

          switch (gameSpecificColor)
          {
            case "B" :
              //Blue case code
              desiredColor = "R";
              pwm.setSpeed(Const.Speed.COLOR_WHEEL_FIXED_SPEED); //either negative or postive for ccw or cw
              break;
            case "G" :
              //Green case code
              desiredColor = "Y";
              pwm.setSpeed(Const.Speed.COLOR_WHEEL_FIXED_SPEED);
         
              break;
            case "R" :
              //Red case code
              desiredColor = "B";
              pwm.setSpeed(Const.Speed.COLOR_WHEEL_FIXED_SPEED);
                break;
            
            case "Y" :
              //Yellow case code
              desiredColor = "G";
              pwm.setSpeed(Const.Speed.COLOR_WHEEL_FIXED_SPEED);
                break;
            
            default :
              //This is corrupt data
              pwm.setSpeed(0.0d);
              break;
          }
        } else {
          //Code for no data received yet
          pwm.setSpeed(0.0d);
        }*/
      }

    public void execute() {

        if (desiredColor == null || rotationComplete == false){
            elevator.rotateColorWheel(3);
            rotationComplete = true;
        } else {
          elevator.goToColor(wheelColor);
          colorFound = true;
        }

        /** Print values to the Smart Dashboard */
        SmartDashboard.putNumber("ColorSensor2/Red", colorSensor.getRed());
        SmartDashboard.putNumber("ColorSensor2/Green", colorSensor.getGreen());
        SmartDashboard.putNumber("ColorSensor2/Blue", colorSensor.getBlue());
        SmartDashboard.putString("ColorSensor2/Color", colorString);
        SmartDashboard.putString("ColorSensor2/desiredColor", desiredColor);
        SmartDashboard.putBoolean("ColorSensor2/ColorFound", colorFound);
        SmartDashboard.putBoolean("ColorSensor2/ ConditionMet", colorString.equals(desiredColor));
    }

    public void end(boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return colorFound;
    }
}