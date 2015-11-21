package logic;

import java.util.List;

/**
 * interface for any class that provides access to the games entities
 */
public interface GameEntityProvider {
	/**
	 * get the entities
	 * @return entities of the game
	 */
	public List<Entity> getEntities();
}
