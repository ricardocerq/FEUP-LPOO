package pathfinding;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import logic.Map;
import logic.Tile;

public class NavGraph {
	public static class Edge {
		private Node dest;
		private float g;
		private Node src;

		Edge(Node src, Node dest){
			this.src = src;
			this.dest = dest;
			this.g = src.calculateG(dest);
		}
		
		public Node getDest() {
			return dest;
		}
		public float getG() {
			return g;
		}

		public Node getSrc() {
			return src;
		}
		public void setG(float g) {
			this.g = g;
		}
	}
	public static class Node implements Comparable<Node>{
		private float bestG;
		private List<Edge> edges = new LinkedList<Edge>();
		private float h;
		private Node parent;
		private boolean proccessed = false;
		private boolean visited = false;
		private float x, y;
		
		public Node(float x, float y){
			this.x = x; this.y = y;
		}
		public void addEdge(Node node) {
			this.edges.add(new Edge(this, node));
		}
		public float calculateG(Node dest){
			return (float) Math.sqrt((dest.x  - this.x)*(dest.x  - this.x) + (dest.y  - this.y)*(dest.y - this.y));
		}
		public float calculateH(Node goal){
			float xdist = Math.abs (this.x - goal.x);
			float ydist = Math.abs (this.y - goal.y);
			return (xdist+ydist);
			//return  HOR_COST * (xdist+ ydist) + (DIAG_COST - 2* HOR_COST) * Math.min(xdist, ydist);
		}
		@Override
		public int compareTo(Node entry) {
			if(this.getBestG() + this.getH() < entry.getBestG() + entry.getH())
				return -1;
			if(this.getBestG() + this.getH() > entry.getBestG() + entry.getH())
				return 1;
			if(this.getBestG() > entry.getBestG())
				return -1;
			if(this.getBestG() < entry.getBestG())
				return 1;
			return 0;
		}
		public float getBestG() {
			return bestG;
		}
		public List<Edge> getEdges() {
			return edges;
		}
		public float getH() {
			return h;
		}
		
		public Node getParent() {
			return parent;
		}
		public float getX() {
			return x;
		}
		public float getY() {
			return y;
		}
		public boolean isProccessed() {
			return proccessed;
		}
		public boolean isVisited() {
			return visited;
		}
		public void setBestG(float bestG) {
			this.bestG = bestG;
		}
		public void setH(float h) {
			this.h = h;
		}
		public void setParent(Node parent) {
			this.parent = parent;
		}
		public void setProccessed(boolean proccessed) {
			this.proccessed = proccessed;
		}
		public void setVisited(boolean visited) {
			this.visited = visited;
		}
		public void setX(float x) {
			this.x = x;
		}
		public void setY(float y) {
			this.y = y;
		}
	}
	public static class Path{
		private ArrayList<Node> nodes= new ArrayList<Node>();
		public Path(){
			
		}
		public void addNode(Node node){
			nodes.add(0, node);
		}
		@SuppressWarnings("unchecked")
		public Object clone(){
			Path out = new Path();
			out.nodes = (ArrayList<Node>) nodes.clone();
			return out;
		}
		public ArrayList<Node> getNodes(){
			return nodes;
		}
		
	}
	
	public static final float ERROR = (float) 0.001;
	
	public static final float RADIUS = .1f;

	public static final int SIDE_NODES_PER_TILE = 7;
	private ArrayList<Node> nodes = new ArrayList<Node>();
	public NavGraph(Map map){
		for(int y  =  1; y < map.getHeight() - 1; y++){
			for(int x = 1; x < map.getWidth() - 1; x++){
				if(map.getTile(x, y).getType() == Tile.TileType.FLOOR){
					for(int numy = 0 ; numy < SIDE_NODES_PER_TILE; numy++){
						for(int numx = 0 ; numx < SIDE_NODES_PER_TILE; numx++){
							if(validNode(x + ((float)numx +.5f) /SIDE_NODES_PER_TILE,y + ((float)numy+.5f)/SIDE_NODES_PER_TILE, map))
								addNode(new Node(x + ((float)numx +.5f) /SIDE_NODES_PER_TILE ,y + ((float)numy+.5f)/SIDE_NODES_PER_TILE));
			
						}
					}
					//addNode(new Node(x,y));
				}
			}
		}
		for(int i = 0; i < nodes.size()-1; i++){
			for(int j = i+1; j < nodes.size(); j++){
				
				if((Math.abs(nodes.get(i).getX() -  nodes.get(j).getX()) <= ERROR &&  Math.abs(nodes.get(i).getY() - nodes.get(j).getY()) <= ((float)1)/SIDE_NODES_PER_TILE+ ERROR) || Math.abs(nodes.get(i).getY() - nodes.get(j).getY()) <= ERROR &&  Math.abs(nodes.get(i).getX() - nodes.get(j).getX()) <= ((float)1)/SIDE_NODES_PER_TILE+ERROR){
					addEdge(nodes.get(i), nodes.get(j));
					addEdge(nodes.get(j), nodes.get(i));
				}
			}
		}
	}
	
	public void addEdge(Node src, Node dest){
		src.addEdge(dest);
	}
	public void addNode(Node node) {
		this.nodes.add(node);
	}
	
	public Node getClosest(float x, float y){
		Node min = null;
		float mindist = Float.MAX_VALUE;
		float currdist;
		for(Node n : nodes){
			if((currdist = (float)Math.sqrt((x-n.getX())*(x-n.getX()) + (y-n.getY())*(y-n.getY())) )< mindist){
				min = n;
				mindist = currdist;
			}
		}
		return min;
	}
	public ArrayList<Node> getNodes() {
		return nodes;
	}
	private boolean validNode(float x, float y, Map map) {
		if(map.getTile((int)(x-RADIUS), (int)(y-RADIUS)).getType() != Tile.TileType.FLOOR)
			return false;
		if(map.getTile((int)(x-RADIUS), (int)(y+RADIUS)).getType() != Tile.TileType.FLOOR)
			return false;
		if(map.getTile((int)(x+RADIUS), (int)(y-RADIUS)).getType() != Tile.TileType.FLOOR)
			return false;
		if(map.getTile((int)(x+RADIUS), (int)(y+RADIUS)).getType() != Tile.TileType.FLOOR)
			return false;
		return true;
	}
}
