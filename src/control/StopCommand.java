package control;

import logic.MovingEntity;

/**
 * 	causes the entity to stop
 */
public class StopCommand extends Command {
	/**
	 * constructor for class
	 */
	StopCommand(){
	}
	/* (non-Javadoc)
	 * @see control.Command#execute(logic.MovingEntity)
	 */
	@Override
	public void execute(MovingEntity me) {
		me.stop();
	}
}
