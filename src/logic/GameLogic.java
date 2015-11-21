package logic;

import java.util.List;
import java.util.Random;

public class GameLogic {
	public enum Winner {ACTORS, NONE, SNIPER}
	public static int FRAMES_PER_SECOND =  60;
	
	public static Random r = new Random();;
	public static float SECONDS_PER_FRAME = ((float)1)/FRAMES_PER_SECOND;
	
	/**
	 * perform the loop of the game
	 * @param g the state of the game
	 * @param time time passed
	 * @return winner of the game
	 */
	public static Winner gameLoop(GameState g, float time){
		for(Entity ent : g.getEntities()){
			ent.update(time);
		}
		Winner winner = winner(g);
		if(g.getWonState() == Winner.NONE)
			g.setWonState(winner);
		return g.getWonState();
	}
	
	
	
	/**
	 * provide sole access point for random number generator
	 * @return random number generator
	 */
	public static Random getRandom(){
		return r;
	}
	/**
	 * decide the winner of a given game state
	 * @param g gamestate to decide
	 * @return winner of the game
	 */
	public static Winner winner(GameState g){
		List<Entity> ents = g.getEntities();
		boolean playersDead = true;
		boolean sniperNoBullets = false;
		boolean actorsDead = true;
		for(Entity e : ents){
			if(e instanceof Sniper){
				if(((Sniper)e).getBullets() == 0)
					sniperNoBullets = true;
			}
			else if( e instanceof Actor){
				if(((Actor) e).getPlayerControlled() == 0){
					if(((Actor) e).getAlive())
						actorsDead = false;
				}
				else{
					if(((Actor) e).getAlive())
						playersDead = false;
				}
			}
		}
		if(playersDead)
			return Winner.SNIPER;
		if(sniperNoBullets || actorsDead)
			return Winner.ACTORS;
		else return Winner.NONE;
	}
}
