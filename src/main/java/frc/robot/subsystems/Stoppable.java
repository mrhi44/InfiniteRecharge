package frc.robot.subsystems;

/**
 * An interface that indicates that a subsystem is stoppable.
 *
 * @author Jordan Bancino
 */
public interface Stoppable {
    /*
     * Stop the entire subsystem immediately. This should
     * send the stop command to all components of the substem,
     * though it may return before all the compoents are stopped.
     */
    public void stop();
}