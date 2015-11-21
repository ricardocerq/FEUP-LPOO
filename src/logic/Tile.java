package logic;

/**
 * @author Ricardo
 *
 */
public class Tile {
	/**
	 * @author Ricardo
	 *
	 */
	public enum TileType{
		EXIT, FLOOR, WALL,
	}
	private TileType type;

	/**
	 * default constructor for class
	 */
	public Tile(){
		this(TileType.FLOOR);
	}

	/**
	 * constructor for tile
	 * @param type type of tile
	 */
	public Tile(TileType type){
		this.type = type;
	}
	
	/**
	 * get the type of tile
	 * @return type of tile
	 */
	public TileType getType() {
		return type;
	}

	/**
	 * set the type of tile
	 * @param type type of tile to set
	 */
	public void setType(TileType type) {
		this.type = type;
	}
}
