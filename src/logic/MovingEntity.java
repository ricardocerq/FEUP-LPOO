package logic;

import java.util.LinkedList;

import control.Commander;
import control.MovingEntityListener;
import control.NullCommander;
import control.PlayerCommander;

/**
 *	represents a moving entity in the game
 */
public abstract class MovingEntity extends Entity{
	
	protected Commander commander;
	protected GameEntityProvider gep;
	
	protected GameMapProvider gmp;
	private LinkedList<MovingEntityListener> listeners = new LinkedList<MovingEntityListener>();
	
	protected float max_speed;
	
	/**
	 * constructor for class
	 * @param pos initial position for entity
	 * @param commander commander of the entity
	 * @param max_speed max speed of the entity
	 * @param gep object that provides access to other entities in the game
	 * @param gmpobject that provides access to the game map
	 */
	public MovingEntity(Vec2 pos, Commander commander,  float max_speed, GameEntityProvider gep, GameMapProvider gmp){
		super(pos);
		this.max_speed = max_speed;
		this.commander = commander;
		this.gep = gep;
		this.gmp = gmp;
	}
	
	/**
	 * add a listener interested in this entities events
	 * @param e new listener
	 */
	public  void addListener(MovingEntityListener e){
		listeners.add(e);
	}
	/**
	 * apply a force to this entity
	 * @param force applied force
	 */
	public abstract void applyForce(Vec2 force);
	
	/**
	 * apply this entities velocity to its position
	 * @param time time elapsed
	 */
	public abstract void applyVelocity(float time);
	
	/**
	 * called when this entity attacks
	 * 
	 */
	public void attack(){
		for(MovingEntityListener l : listeners){
			l.onAttackStart(this);
		}
	}
	
	/**
	 * called when this entity's attack ends
	 */
	protected void attackEnd(){
		for(MovingEntityListener l : listeners){
			l.onAttackEnd(this);
		}
	}
	
	/**
	 * get this entities commander
	 * @return commander of entity
	 */
	public Commander getCommander() {
		return commander;
	}
	
	/**
	 * get this entity's max speed
	 * @return max speed of entity
	 */
	public float getMaxSpeed() {
		return max_speed;
	}
	
	/**
	 * determine the controller of this entity
	 * @return 0 if entity is not player controlled else return the number of the player controlling the entity
	 */
	public int getPlayerControlled(){
		if(commander instanceof NullCommander)
			return ((NullCommander) commander).getNumber();
		if(commander instanceof PlayerCommander)
			return ((PlayerCommander)commander).getNumber();
		else return 0;
	}
	
	/* (non-Javadoc)
	 * @see logic.Entity#hit(logic.Entity)
	 */
	@Override
	public void hit(Entity attacker){
		for(MovingEntityListener l : listeners){
			l.onHit((MovingEntity)attacker, this);
		}
	}
	
	/**
	 * called when this entity is attacked but not affected by the attack
	 * @param attacker entity that tried to attack
	 * @return true
	 */
	public boolean miss(Entity attacker) {
		for(MovingEntityListener l : listeners){
			l.onMiss((MovingEntity)attacker, this);
		}
		return true;
	}
	
	/**
	 * called when this entity is moved
	 * @param move performed move
	 * @return true
	 */
	public boolean move(Vec2 move){
		for(MovingEntityListener l : listeners){
			l.onMove(this, move);
		}
		return true;
	}
	
	/**
	 * process the time elapsed
	 * @param time elapsed time
	 */
	protected abstract void processFrame(float time);
	
	/**
	 * change this entities commander
	 * @param commander new commander
	 */
	public void setCommander(Commander commander) {
		this.commander = commander;
	}
	
	/**
	 * set this entities velocity to 0
	 */
	public abstract void stop();
	
	/* (non-Javadoc)
	 * @see logic.Entity#update(float)
	 */
	@Override
	public abstract void update(float time);
	
}
