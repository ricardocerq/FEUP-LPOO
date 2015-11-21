package logic;

import java.util.Comparator;

/**
 * represents an entity in the game
 * can be a sniper or sheep
 */
public abstract class Entity {	

	/**
	 * allows the comparison of two entity for drawing
	 */
	public static class EntityPositionComparator implements Comparator<Entity> {
		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(Entity e1, Entity e2) {   	
			if(e1 == null && e2 == null)
				return 0;
			if(e1 == null)
				return -1;
			if(e2==null)
				return 1;
			if(e1.getY() < e2.getY() )
				return -1;
			if(e1.getY() > e2.getY())
				return 1;
			if(e1.getX() < e2.getX())
				return -1;
			if(e1.getX() > e2.getX())
				return 1;
			return 0;
		}
	}
	private Vec2 pos;
	
	/**
	 * constructor for entity
	 * @param pos initial position of entity
	 */
	public Entity(Vec2 pos){
		this.pos = pos;
	}

	/**
	 * get the current position of entity
	 * @return position of entity
	 */
	public Vec2 getPos(){
		return pos;
	}

	/**
	 * get the x coordinate of the entity
	 * @return x coordinate of the entity
	 */
	public float getX() {
		return pos.getX();
	}

	/**
	 * get the y coordinate of the entity
	 * @return y coordinate of the entity
	 */
	public float getY() {
		return pos.getY();
	}
	/**
	 * called when the current entity is hit with an attack
	 * @param attacker entity who attacked this one
	 */
	public abstract void hit(Entity attacker);
	
	/**
	 * change the position of this entity
	 * @param pos new position
	 */
	public void setPos(Vec2 pos){
		this.pos = pos;
	}
	/**
	 * change the x coordinate of this entity
	 * @param x new x coordinate
	 */
	public void setX(float x) {
		this.pos.setX(x);
	}
	/**
	 * change the y coordinate of this entity
	 * @param y new y coordinate
	 */
	public void setY(float y) {
		pos.setY(y);
	}

	/**
	 * update the entity
	 * @param time time passed since last update
	 */
	public abstract void update(float time);
	
	
}
