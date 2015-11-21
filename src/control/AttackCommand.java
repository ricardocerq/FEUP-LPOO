package control;

import logic.MovingEntity;

/**
 *	represents a command tha tells an entity to attack
 */
public class AttackCommand extends Command{
	/* (non-Javadoc)
	 * @see control.Command#execute(logic.MovingEntity)
	 */
	@Override
	public void execute(MovingEntity me) {
		me.attack();
	}

}
