package control;

import logic.Vec2;

/**
 * represents a command that will move an entity
 */
public abstract class MoveCommand extends Command {
	private Vec2 dir;
	
	/**
	 * constructor for class
	 * @param x x coordinate of move
	 * @param y y coordinate of move
	 */
	MoveCommand(float x, float y){
		dir = new Vec2(x,y);
	}
	/**
	 * get the vector of the command
	 * @return direction of command
	 */
	public Vec2 getDir() {
		return dir;
	}
	/**
	 * get the x coordinate of command
	 * @return x coordinate of command
	 */
	public float getX() {
		return dir.getX();
	}
	/**
	 * get the y coordinate of command
	 * @return y coordinate of command
	 */
	public float getY() {
		return dir.getY();
	}
	/**
	 * set the x coordinate of command
	 * @param x new x coordinate of command
	 */
	public void setX(float x) {
		this.dir.setX(x);
	}
	/**
	 * set the y coordinate of command
	 * @param y new y coordinate of command
	 */
	public void setY(float y) {
		this.dir.setY(y);
	}
	
}
