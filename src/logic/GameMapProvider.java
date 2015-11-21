package logic;

/**
 * common interface for all classes that provide access to the game map
 */
public interface GameMapProvider {
	/**
	 * get the map of the game
	 * @return map of the game
	 */
	public Map getMap();
}
