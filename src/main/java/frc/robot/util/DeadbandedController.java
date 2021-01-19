package frc.robot.util;

/**
 * A controller that has a small deadband. This can help if joysticks are touchy
 * and/or don't stick to zero (feeding random values in the neutral position).
 * 
 * @author Jordan Bancino
 * @version 1.0.0
 * @since 1.0.0
 */
public interface DeadbandedController {

    /**
     * The default deadband. This is considered a sane default for dealing with
     * joystick errors and should be used by classes that implement this interface
     * as the default starting deadband which can later be changed by the user.
     */
    public static final double DEFAULT_DEADBAND = 0.2;

    /**
     * Set a deadband for this controller.
     * 
     * @param deadband The deadband to set. If the joystick input is within plus or
     *                 minus this value, then the result of the deadband function
     *                 will be zero. This value should be within -1 and 1.
     * @throws IllegalArgumentException If the deadband is out of bounds -1 and 1.
     */
    public void setDeadband(double deadband);

    /**
     * Get the deadband that is currently set on this controller.
     * 
     * @return The deadband being used in this controller's calculations.
     */
    public double getDeadband();

    /**
     * Deadband the input with the set deadband for this controller.
     * 
     * @param rawInput The raw input to apply the deadband to.
     * @return Zero if the raw input is within plus or minus the deadband,
     *         otherwise, the scaled value from deadband to 1.
     */
    public default double deadband(double rawInput) {
        return deadband(rawInput, getDeadband());
    }

    /**
     * Deadband the input with a custom deadband.
     * 
     * @param rawInput The raw input to apply the deadband to.
     * @param deadband The deadband to use for the modification.
     * @return Zero if the raw input is within plus or minus the deadband,
     *         otherwise, the scaled value from deadband to 1.
     */
    public static double deadband(double rawInput, double deadband) {
        if (!bounded(rawInput)) {
            throw new IllegalArgumentException("Input out of bounds: " + rawInput);
        }
        if (!bounded(deadband)) {
            throw new IllegalArgumentException("Deadband out of bounds: " + deadband);
        }
        double modInput = 0;
        if (rawInput < 0) {
            if (rawInput <= -deadband) {
                modInput = (rawInput + deadband) / (1 - deadband);
            }
        } else {
            if (rawInput >= deadband) {
                modInput = (rawInput - deadband) / (1 - deadband);
            }
        }
        return modInput;
    }

    /**
     * Check whether or not an input is bounded within the acceptable bounds for
     * this interface.
     * 
     * @param input The input to check.
     * @return Whether or not the input is within -1 and +1, both being included in
     *         the check.
     */
    public static boolean bounded(double input) {
        return input <= 1 && input >= -1;
    }
}