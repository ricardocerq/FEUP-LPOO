package pathfinding;

import java.util.ArrayList;

import pathfinding.NavGraph.Node;
import pathfinding.NavGraph.Path;
import logic.Map;
import logic.Vec2;

public class Trajectory {
	private ArrayList<Vec2> points = new  ArrayList<Vec2>();
	public Trajectory(Path path, Map map, int iterations, float bevel, float radius, float mindist){
		for(Node n : path.getNodes()){
			points.add(new Vec2(n.getX(), n.getY()));
		}
	}
	public int getClosest(Vec2 pos){
		float mindist = Float.MAX_VALUE;
		int min = -1;
		float currdist;
		for(int i = 0; i < points.size(); i++){
			if((currdist = Vec2.getDist(points.get(i), pos)) < mindist){
				min = i;
				mindist = currdist;
			}
		}
		return min;
	}
	public Vec2 getFirst(){
		return points.get(0);
	}
	public ArrayList<Vec2> getPoints() {
		return points;
	}
	public void removeFirst(){
		points.remove(0);
	}
}
