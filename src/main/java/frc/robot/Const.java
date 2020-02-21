package frc.robot;

/**
 * The constants class stores all of our robot wiring constants.
 */
public class Const {
    public static class CAN {
        public static final int FRONT_RIGHT_DRIVE_MOTOR = 5; /* Module 1 */
        public static final int FRONT_LEFT_DRIVE_MOTOR  = 6; /* Module 2 */
        public static final int REAR_LEFT_DRIVE_MOTOR   = 7; /* Module 3 */
        public static final int REAR_RIGHT_DRIVE_MOTOR  = 8; /* Module 4 */

        public static final int FRONT_RIGHT_PIVOT_MOTOR = 1; /* Module 1 */
        public static final int FRONT_LEFT_PIVOT_MOTOR  = 2; /* Module 2 */
        public static final int REAR_LEFT_PIVOT_MOTOR   = 3; /* Module 3 */
        public static final int REAR_RIGHT_PIVOT_MOTOR  = 4; /* Module 4 */

        public static final int ELEVATOR_MOTOR = 15;
        public static final int ELEVATOR_WHEEL = 16;

        public static final int FEED_MOTOR = 20;

        public static final int INTAKE_MOTOR = 11;

        public static final int SHOOTER_MOTOR_1        = 25;
        public static final int SHOOTER_MOTOR_2        = 26;
        public static final int SHOOTER_MOTOR_REVERSED = 27;
        public static final int SHOOTER_HOOD_MOTOR     = 28;

        public static final int POWER_DISTRIBUTION_PANEL = 59;
    }

    public static class PID {
        public static final double SWERVE_MODULE_RAMP_RATE = 0;
        public static final double SWERVE_MODULE_P         = 0.003;
        public static final double SWERVE_MODULE_I         = 0.00000155;
        public static final double SWERVE_MODULE_D         = 0;

        public static final double ELEVATOR_P = 0;
        public static final double ELEVATOR_I = 0;
        public static final double ELEVATOR_D = 0;

        public static final int HOOD_SLOT    = 0;
        public static final int HOOD_TIMEOUT = 10;
        public static final double HOOD_P    = 0;
        public static final double HOOD_I    = 0;
        public static final double HOOD_D    = 0;
    }

    public static class Pneumatic {
        public static final int CONTROL_MODULE    = 1;
        public static final int INTAKE_DOWN       = 2;
        public static final int INTAKE_UP         = 3;
        public static final int FEED_STOP_ENABLE  = 4;
        public static final int FEED_STOP_DISABLE = 5;
    }

    /**
     * Encoders - Analog Ports and position offsets
     */
    public static class Encoder {
        public static final int FRONT_RIGHT_ANALOG_ENCODER = 0; /* Module 1 */
        public static final int FRONT_LEFT_ANALOG_ENCODER  = 1; /* Module 2 */
        public static final int REAR_LEFT_ANALOG_ENCODER   = 2; /* Module 3 */
        public static final int REAR_RIGHT_ANALOG_ENCODER  = 3; /* Module 4 */

        /* The encoder offsets tell us where zero is for each motor. */
        public static final double FRONT_RIGHT_ENCODER_OFFSET = 146.27;
        public static final double FRONT_LEFT_ENCODER_OFFSET  = 134.60;
        public static final double REAR_LEFT_ENCODER_OFFSET   = 59.34;
        public static final double REAR_RIGHT_ENCODER_OFFSET  = 267.2;
    }

    /*
     * Constants pertaining to default speeds.
     */
    public static class Speed {
        public static final double INTAKE_SPEED  = 0.5;
        public static final double FEED_SPEED    = 0.5;
        public static final double SHOOTER_SPEED = 0.5;
        public static final double HOOD_SPEED    = 0.5;
        public static final double COLOR_WHEEL_FIXED_SPEED = 0.3;
    }

    public static class Elevator {
        public static final double MAX_HEIGHT = 0;
        public static final double COLOR_WHEEL_HEIGHT = 0;
        public static final double BOTTOM_HEIGHT = 0;
        public static final double INCREMENT = 2; //Max encoder counts per scan
        public static final int NUMBER_OF_COLOR_CHANGES = 25;
    }

    public static class AutonBallGetter {
        public static final double CAMERA_ANGLE_X = 61;
        public static final double CAMERA_ANGLE_Y = 31.16;
        public static final double CAMERA_RES_X = 320;
        public static final double CAMERA_RES_Y = 240;
        public static final double BALL_ADJUST_SPEED = 0.15; //To be multiplied by angle difference
    }

    public static class LimelightAlign {
        public static final double DISTANCE_TO_TARGET = 140; //In inches
        public static final double STRAFE_ADJUST_SPEED = 0.0025; //In inches
        public static final double FORWARD_ADJUST_SPEED = 0.02; //Again, in inches
        public static final double ROTATE_ADJUST_SPEED = 0.009; // Inches, oh yeah
        public static final double ACCEPTABLE_ERROR_STRAFE = 2; //Incheroos
        public static final double ACCEPTABLE_ERROR_FORWARD = 3; //Incheronis
        public static final double ACCEPTABLE_ERROR_ROTATE = 1; //Incherochos
    }
}