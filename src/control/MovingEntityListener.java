package control;

import java.util.LinkedList;
import java.util.List;

import logic.MovingEntity;
import logic.Vec2;

/**
 * represents an object interested in the events associated with the game's entities
 */
public abstract class MovingEntityListener {
	private static List<MovingEntityListener> list = new LinkedList<MovingEntityListener>();
	
	/**
	 * add all current listeners to entity
	 * @param e entity on which to perform operation
	 */
	public final static void addListeners(MovingEntity e){
		for(MovingEntityListener l : list){
			e.addListener(l);
		}
	}
	
	/**
	 * constructor for class
	 */
	public MovingEntityListener(){
		list.add(this);
	}
	
	/**
	 * called when an entity ends its attack
	 * @param attacker entity that ended its attack
	 */
	public abstract void onAttackEnd(MovingEntity attacker);
	
	/**
	 * called when an entity begins its attack
	 * @param attacker entity that begun its attack
	 */
	public abstract void onAttackStart(MovingEntity attacker);
	
	/**
	 * called when an entity is hit with an attack
	 * @param attacker entity who initiated the attack
	 * @param attacked
	 */
	public abstract void onHit(MovingEntity attacker, MovingEntity attacked);
	
	/**
	 * called when an entity misses its target
	 * @param attacker entity that tried to attack
	 * @param attacked entity that escaped the attack
	 */
	public abstract void onMiss(MovingEntity attacker, MovingEntity attacked);
	
	/**
	 * called when the entity moves
	 * @param mover entity that moved
	 * @param move move made by the entity
	 */
	public abstract void onMove(MovingEntity mover, Vec2 move);
}
