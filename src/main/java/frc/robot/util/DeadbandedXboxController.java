package frc.robot.util;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;

/**
 * A deadbanded Xbox controller.
 * 
 * @author Jordan Bancino
 * @version 1.0.1
 * @since 1.0.0
 */
public class DeadbandedXboxController extends XboxController implements DeadbandedController {
    private double deadband, leftTriggerDeadband, rightTriggerDeadband, leftJoystickDeadband, rightJoystickDeadband;

    /**
     * Create a deadbanded controller with the port and default deadband.
     * @param port The port to create the controller on.
     */
    public DeadbandedXboxController(int port) {
        this(port, DEFAULT_DEADBAND);
    }

    /**
     * Create a deadbanded controller with the port and initial deadband.
     * @param port The port to create the controller on.
     * @param initialDeadband The initial deadband to apply to all axes of this controller.
     */
    public DeadbandedXboxController(int port, double initialDeadband) {
        super(port);
        setDeadband(initialDeadband);
        setTriggerDeadband(GenericHID.Hand.kLeft, initialDeadband);
        setTriggerDeadband(GenericHID.Hand.kRight, initialDeadband);
        setJoystickDeadband(GenericHID.Hand.kLeft, initialDeadband);
        setJoystickDeadband(GenericHID.Hand.kRight, initialDeadband);
    }

    @Override
    public void setDeadband(double deadband) {
        if (DeadbandedController.bounded(deadband)) {
            this.deadband = deadband;
        } else {
            throw new IllegalArgumentException("Deadband out of bounds: " + deadband);
        }
    }

    @Override
    public double getDeadband() {
        return deadband;
    }

    /**
     * Get the deadband being applied to the given trigger.
     * @param hand The hand to get the trigger deadband for.
     * @return The trigger deadband for the given hand.
     */
    public double getTriggerDeadband(GenericHID.Hand hand) {
        switch (hand) {
            case kLeft:
                return leftTriggerDeadband;
            case kRight:
                return rightTriggerDeadband;
            default: 
                throw new IllegalArgumentException("They didn't have a constant for" + hand + " when I wrote this library!");
        }
    }

    /**
     * Set the deadband for the given trigger.
     * @param hand The hand to set the trigger deadband for.
     * @param deadband The deadband to set for the given trigger.
     */
    public void setTriggerDeadband(GenericHID.Hand hand, double deadband) {
        if (DeadbandedController.bounded(deadband)) {
            switch (hand) {
                case kLeft:
                    leftTriggerDeadband = deadband;
                    break;
                case kRight:
                    rightTriggerDeadband = deadband;
                    break;
                default: 
                    throw new IllegalArgumentException("They didn't have a constant for" + hand + " when I wrote this library!");
            }
        } else {
            throw new IllegalArgumentException("Deadband out of bounds: " + deadband);
        }
    }

    /**
     * Get the deadband being applied to the given joystick.
     * @param hand The hand to get the joystick deadband for.
     * @return The joystick deadband for the given hand.
     */
    public double getJoystickDeadband(GenericHID.Hand hand) {
        switch (hand) {
            case kLeft:
                return leftJoystickDeadband;
            case kRight:
                return rightJoystickDeadband;
            default: 
                throw new IllegalArgumentException("They didn't have a constant for" + hand + " when I wrote this library!");
        }
    }

    /**
     * Set the deadband for the given joystick.
     * @param hand The hand to set the trigger deadband for.
     * @param deadband The deadband to set for the given trigger.
     */
    public void setJoystickDeadband(GenericHID.Hand hand, double deadband) {
        if (DeadbandedController.bounded(deadband)) {
            switch (hand) {
                case kLeft:
                    leftJoystickDeadband = deadband;
                    break;
                case kRight:
                    rightJoystickDeadband = deadband;
                    break;
                default: 
                    throw new IllegalArgumentException("They didn't have a constant for" + hand + " when I wrote this library!");
            }
        } else {
            throw new IllegalArgumentException("Deadband out of bounds: " + deadband);
        }
    }

    @Override
    public double getTriggerAxis(GenericHID.Hand hand) {
        return DeadbandedController.deadband(super.getTriggerAxis(hand), getTriggerDeadband(hand));
    }

    @Override
    public double getX(GenericHID.Hand hand) {
        return DeadbandedController.deadband(super.getX(hand), getJoystickDeadband(hand));
    }

    @Override
    public double getY(GenericHID.Hand hand) {
        return DeadbandedController.deadband(super.getX(hand), getJoystickDeadband(hand));
    }

    @Override
    public double getRawAxis(int axis) {
        double deadband;
        if (axis == XboxController.Axis.kLeftTrigger.value) {
            deadband = leftTriggerDeadband;
        } else if (axis == XboxController.Axis.kRightTrigger.value) {
            deadband = rightTriggerDeadband;
        } else if (axis == XboxController.Axis.kLeftX.value || axis == XboxController.Axis.kLeftY.value) {
            deadband = leftJoystickDeadband;
        } else if (axis == XboxController.Axis.kRightX.value || axis == XboxController.Axis.kRightY.value) {
            deadband = rightJoystickDeadband;
        } else {
            deadband = getDeadband();
        }
        return DeadbandedController.deadband(super.getRawAxis(axis), deadband);
    }
}