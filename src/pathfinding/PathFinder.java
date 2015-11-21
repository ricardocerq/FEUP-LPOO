package pathfinding;

import java.util.LinkedList;

import pathfinding.NavGraph.Node;
import pathfinding.NavGraph.Path;
import logic.Map;
import logic.Tile;

public class PathFinder {
	public static Path genPath(NavGraph graph, Node src, Node goal){
		Path out = null;
		for(Node n : graph.getNodes()){
			n.setH(n.calculateH(goal));
			n.setVisited(false);
			n.setProccessed(false);
			n.setParent(null);
			n.setBestG(Float.MAX_VALUE);
		}
		LinkedList<Node> open = new LinkedList<Node>();
		open.add(src);
		src.setVisited(true);
		src.setBestG(0);
		src.setParent(null);
		
		while(open.size() != 0){
			Node current = null;
			int toRemove = -1;
			for(int i = 0; i < open.size(); i++){
				if(current == null || open.get(i).compareTo(current) <= 0){
					current = open.get(i);
					toRemove = i;
				}
			}
			if(current == goal)
				break;
			open.remove(toRemove);
			for(NavGraph.Edge edge : current.getEdges()){
				if(!edge.getDest().isProccessed()){
					float newCost = current.getBestG() + edge.getG();
					if(current.getParent() != null){
						float olddirx = (current.getX() - current.getParent().getX());
						float olddiry = (current.getY() - current.getParent().getY());
						float newdirx = (edge.getDest().getX() - current.getX());
						float newdiry = (edge.getDest().getY() - current.getY());
						if(Math.abs(olddirx - newdirx ) >= 0.01|| Math.abs(olddiry - newdiry ) >= 0.01)
							newCost*=1;
					}
					if(edge.getDest().getBestG() > newCost){
						edge.getDest().setBestG(newCost);
						edge.getDest().setParent(current);
						if(!edge.getDest().isVisited())
							open.add(edge.getDest());
					}
				}
			}
			current.setProccessed(true);
		}
		if(goal.getParent() == null)
			return out;
		out = new Path();
		Node n = goal;
		do{
			out.addNode(n);
			n = n.getParent();
		}while(n != null);
		return out;
	}
	
	public static boolean isTraversable(Map m, float x1, float y1, float x2, float y2, float radius){
		float curx = x1;
		float cury = y1;
		float dirx = x2 - x1;
		float diry = y2 - y1;
		float step = (float) 0.1;
		float dist = (float) Math.sqrt(dirx*dirx+diry*diry);
		dirx /= dist; diry /= dist;
		float sidex = diry * radius;
		float sidey = -dirx * radius;
		dirx *= step; diry *= step;
		int it = (int)(dist / step);
		for(int i = 0; i < it; i++){
			curx =  x1 + dirx*i;
			cury = y1 + diry*i;
			float x, y;
			x = curx;
			y = cury;
			if(m.getTile((int)(x), (int)(y)) == null || m.getTile((int)(x), (int)(y)).getType() == Tile.TileType.WALL)
				return false;
			x = curx + sidex;
			y = cury + sidey;
			if(m.getTile((int)(x), (int)(y)) == null || m.getTile((int)(x), (int)(y)).getType() == Tile.TileType.WALL)
				return false;
			x = curx - sidex;
			y = cury - sidey;
			if(m.getTile((int)(x), (int)(y)) == null || m.getTile((int)(x), (int)(y)).getType() == Tile.TileType.WALL)
				return false;
		}
		return true;
	}
	
	public static boolean isTraversable(Node src, Node dst, Map m, float radius){
		float srcx = src.getX();
		float srcy = src.getY();
		float dstx = dst.getX();
		float dsty = dst.getY();
		return isTraversable(m, srcx, srcy, dstx, dsty, radius);
	}

	private static boolean sameDirection(Node node, Node node2, Node node3) {
		float dir1x = node2.getX() - node.getX();
		float dir1y = node2.getY() - node.getY();
		float dir2x = node3.getX() - node2.getX();
		float dir2y = node3.getY() - node2.getY();
		if(Math.abs(dir1x - dir2x) <= 0.01 && Math.abs(dir1y - dir2y) <= 0.01)
			return true;
		return false;
	}
	public static void smoothPath(Path p, Map m, float radius){
		boolean changed = false;
		do{
			changed = false;
			for(int i = 1; i < p.getNodes().size()-1; i++){
				if(isTraversable(p.getNodes().get(i-1), p.getNodes().get(i+1), m, radius) &&!sameDirection(p.getNodes().get(i-1), p.getNodes().get(i),p.getNodes().get(i+1))){
					p.getNodes().remove(i);
					i--;
					changed = true;
				}
			}
		}while (changed);
	}
}
