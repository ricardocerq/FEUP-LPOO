package control;

import logic.Vec2;

/**
 *	represents an object that can issue commands to a game entity
 */
public interface Commander {
	/**
	 * generate the next command
	 * @return next command
	 */
	public Command generateCommand();
	/**
	 * process the current state of the entity to make a decision
	 * @param pos current position
	 * @param velocity current velocity
	 */
	public void process(Vec2 pos,Vec2 velocity);
}
