/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity.
 */
public final class Constants {
    /*
     * Motors - CAN IDs
     */
    public static final int FRONT_RIGHT_DRIVE_MOTOR = 5; /* Module 1 */
    public static final int FRONT_LEFT_DRIVE_MOTOR = 6; /* Module 2 */
    public static final int REAR_LEFT_DRIVE_MOTOR = 7; /* Module 3 */
    public static final int REAR_RIGHT_DRIVE_MOTOR = 8; /* Module 4 */

    public static final int FRONT_RIGHT_PIVOT_MOTOR = 1; /* Module 1 */
    public static final int FRONT_LEFT_PIVOT_MOTOR = 2; /* Module 2 */
    public static final int REAR_LEFT_PIVOT_MOTOR = 3; /* Module 3 */
    public static final int REAR_RIGHT_PIVOT_MOTOR = 4; /* Module 4 */

    /*
     * Encoders - Analog Ports
     */
    public static final int FRONT_RIGHT_ANALOG_ENCODER = 0; /* Module 1 */
    public static final int FRONT_LEFT_ANALOG_ENCODER = 1; /* Module 2 */
    public static final int REAR_LEFT_ANALOG_ENCODER = 2; /* Module 3 */
    public static final int REAR_RIGHT_ANALOG_ENCODER = 3; /* Module 4 */
}
