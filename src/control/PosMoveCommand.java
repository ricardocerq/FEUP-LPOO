package control;

import logic.MovingEntity;

/**
 * represents a normal move command
 */
public class PosMoveCommand extends MoveCommand{
	/**
	 * contructor for class
	 * @param x x coordinate for move
	 * @param y y coordinate for move
	 */
	PosMoveCommand(float x, float y) {
		super(x, y);
	}
	/* (non-Javadoc)
	 * @see control.Command#execute(logic.MovingEntity)
	 */
	@Override
	public void execute(MovingEntity me) {
		me.move(super.getDir());
	}
}
