package control;

import logic.MovingEntity;

/**
 * represents a generic command to be issued to an entity
 */
public abstract class Command {
	/**
	 * execute the command
	 * @param me moving entity to execute the command on
	 */
	public abstract void execute(MovingEntity me);
}
