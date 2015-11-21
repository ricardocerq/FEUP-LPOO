package logic;

import java.util.ArrayList;
import java.util.List;

import pathfinding.NavGraph;
import control.AICommander;
import control.Commander;
import control.MovingEntityListener;
import control.PlayerCommander;

/**
 *	represents the state of the game
 *
 */
public class GameState implements GameEntityProvider, GameMapProvider {
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private Map map;
	
	private GameLogic.Winner wonState = GameLogic.Winner.NONE;
	
	/**
	 * constructor for class
	 */
	public GameState(){
		
	}
	
	/**
	 * add an entity to the game
	 * @param ent
	 */
	public void addEntity(Entity ent) {
		if(ent instanceof MovingEntity)
			MovingEntityListener.addListeners((MovingEntity)ent);
		entities.add(ent);
	}

	/**
	 * add a sheep in a random position with a given commander
	 * @param a commander for the new sheep
	 */
	public void addRandomActor(Commander a){
		float x,y;
		do{
			x = GameLogic.getRandom().nextFloat() * (getMap().getWidth() - 2) + 1;
			y = GameLogic.getRandom().nextFloat() * (getMap().getHeight() - 2) + 1;
		}while(getMap().getTile((int)x, (int)y).getType() == Tile.TileType.WALL);
		
		addEntity(new Actor(new Vec2(x,y), a, .025f, this, this));
	}
	/**
	 * generate the map
	 * @param width width of map, in tiles
	 * @param height height of map,in tiles
	 * @param actors number of actors in the game
	 * @param players the list of players and their roless
	 */
	public void gen(int width, int height, int actors, List<Pair<PlayerCommander, PlayerCommander.Role>> players){
		this.entities.clear();
		wonState =  GameLogic.Winner.NONE;
		setMap(new Map(width, height));
		getMap().life();
		NavGraph nav = new NavGraph(getMap());
		
		for(int i = actors; i > 0; i--){
			addRandomActor(new AICommander(getMap(), nav));
		}
		int numActors = 0;
		for(Pair<PlayerCommander, PlayerCommander.Role> p : players){
			if(p.second == PlayerCommander.Role.ACTOR){
				numActors++;
			}
		}
		for(Pair<PlayerCommander, PlayerCommander.Role> p : players){
			if(p.second == PlayerCommander.Role.ACTOR){
				addRandomActor(p.first);
			}else if(p.second == PlayerCommander.Role.SNIPER){
				addEntity(new Sniper(new Vec2(getMap().getWidth()/2,getMap().getHeight()/2),p.first, .1f, this, this, numActors+Sniper.getExtraBullets()));
			}
		}
	}
	
	/**
	 * get the score of the actors, which is the ratio of dead sheep and the total number
	 * @return actor score
	 */
	public float getActorScore(){
		int count = 0;
		int totalCount = 0;
		if (wonState == GameLogic.Winner.ACTORS)
			return 1;
		for(Entity e : entities){
			if(e instanceof Actor){
				if(((Actor) e).getPlayerControlled() <= 0){
					totalCount++;
					if(!((Actor) e).getAlive())
						count++;
				}
			}
		}
		return ((float)count) / totalCount;
	}
	/* (non-Javadoc)
	 * @see logic.GameEntityProvider#getEntities()
	 */
	@Override
	public List<Entity> getEntities(){
		return entities;
	}
	/* (non-Javadoc)
	 * @see logic.GameMapProvider#getMap()
	 */
	@Override
	public Map getMap() {
		return map;
	}
	/**
	 * return the won state of the game
	 * @return won state
	 */
	public GameLogic.Winner getWonState() {
		return wonState;
	}
	
	/**
	 * change the map of the game
	 * @param map
	 */
	public void setMap(Map map) {
		this.map = map;
	}

	/**
	 * change the won state of the game
	 * @param wonState
	 */
	public void setWonState(GameLogic.Winner wonState) {
		this.wonState = wonState;
	}
}
