package control;

import logic.MovingEntity;

/**
 * represents a move command that exerts force on an object
 */
public class ForceMoveCommand extends MoveCommand{
	/**
	 * constructor
	 * @param x x coordinate of force
	 * @param y y coordinate of force
	 */
	ForceMoveCommand(float x, float y) {
		super(x, y);
	}
	/* (non-Javadoc)
	 * @see control.Command#execute(logic.MovingEntity)
	 */
	@Override
	public void execute(MovingEntity me) {
		me.applyForce(this.getDir());
	}
}
