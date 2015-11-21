package logic;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import logic.Tile;


/**
 * represents the map in the game
 */
public class Map {
	private ArrayList<ArrayList<Tile>> tiles;
	
	/**
	 * constructor for map
	 * @param width width of map in tiles
	 * @param height height of map in tiles
	 */
	public Map(int width, int height){
		tiles = new ArrayList<ArrayList<Tile>>();
		for(int y = 0; y < height ; y++){
			tiles.add(new ArrayList<Tile>());
			for(int x = 0; x < width; x++){
				if(y == 0 || y == height - 1 || x == 0 || x == width - 1)
					tiles.get(y).add(new Tile(Tile.TileType.WALL));
				else tiles.get(y).add(new Tile(Tile.TileType.FLOOR));
			}
		}
	}
	private int countLiveNeighbours(int x, int y){
		int count = 0;
		for(int j = y-1; j <= y+1; j++){
			for(int i = x-1; i <= x+1; i++){
				/*if(i != x || j != y){
					if(getTile(i,j).getType() == Tile.TileType.WALL)
						count++;
				}*/
				if((i == x || j == y)&& !(i == x && j == y)){
					if(getTile(i,j).getType() == Tile.TileType.WALL)
						count++;
				}
			}
		}
		return count;
	}
	/**
	 * get the height of the map in tiles
	 * @return height of the map
	 */
	public int getHeight(){
		return tiles.size();
	}
	private double getProb(int x, int y){
		//return countLiveNeighbours(x,y) * .1 + .05;
		return (Math.abs(x - getWidth()/2) + Math.abs(y - getHeight()/2))*.001 + .05;
	}
	/**
	 * get a tile from the map
	 * @param x x coordinate of tile
	 * @param y y coordinate of tile
	 * @return tile in x y position, null if invalid
	 */
	public Tile getTile(int x, int y){
		if(x < 0 || x >= getWidth() || y < 0 || y >= getHeight())
			return null;
		return tiles.get(y).get(x);
	}
	
	/**
	 * get the height of the map in tiles
	 * @return height of the map
	 */
	public int getWidth(){
		if(tiles.size() == 0)
			return 0;
		else return tiles.get(0).size();
	}
	/**
	 * randomly generate the map
	 */
	public void life(){
		Random r = GameLogic.getRandom();
		int totalWalls = (int)((getWidth()-1)*(getHeight()-1) * .25);

		do{
			for(int y = 1; y < getHeight() - 1; y++){
				for(int x = 1; x < getWidth() - 1; x++){
					getTile(x,y).setType(Tile.TileType.FLOOR);
				}
			}
			int numWalls = 0;
			while(numWalls < totalWalls){
				for(int y = 1; y < getHeight() - 1 && numWalls < totalWalls; y++){
					for(int x = 1; x < getWidth() - 1 && numWalls < totalWalls; x++){
						if(getTile(x,y).getType() == Tile.TileType.FLOOR){
							if(r.nextDouble() < getProb(x,y)){
								numWalls++;
								getTile(x,y).setType(Tile.TileType.WALL);
							}
						}
					}
				}
				for(int y = 1; y < getHeight() - 1; y++){
					for(int x = 1; x < getWidth() - 1; x++){
						if(getTile(x,y).getType() == Tile.TileType.WALL && countLiveNeighbours(x,y) == 0){
							numWalls--;
							getTile(x,y).setType(Tile.TileType.FLOOR);
						}
					}
				}
			} 
		}while(verify() == -1);

	}
	/**
	 * verify that the generated map is valid
	 * a map is valid if all tiles are accessible and if no wall is adjacent to four floors
	 * @return true if map is valid
	 */
	public int verify(){
		Point p = null;
		boolean found = false;
		List<Point> points = new LinkedList<Point>();
		for(int y = 1; y < getHeight() - 1; y++){
			for(int x = 1; x < getWidth() - 1; x++){
				if(getTile(x,y).getType() == Tile.TileType.FLOOR){
					if(!found){
						p = new Point(x,y);
						found = true;
					}
					points.add(new Point(x,y));
				}
			}
		}
		int count = points.size();
		if(!found)
			return -1;
		Stack<Point> stack = new Stack<Point>();
		stack.push(p);
		while(!stack.empty()){
			Point current = stack.pop();
			points.remove(current);
			for(int j = current.y-1; j <= current.y+1; j++){
				for(int i = current.x-1; i <= current.x+1; i++){
					if((i == current.x || j == current.y)&& !(i == current.x && j == current.y)){
						if(getTile(i,j).getType() == Tile.TileType.FLOOR){
							p = new Point(i,j);
							if(points.contains(p))
								stack.push(p);
						}
					}
				}
			}
		}
		if(!points.isEmpty())
			return -1;
		return count;
	}
	
}
