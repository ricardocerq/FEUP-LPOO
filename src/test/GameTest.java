package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.*;
import pathfinding.NavGraph;
import pathfinding.PathFinder;
import pathfinding.Trajectory;
import control.NullCommander;
import control.PlayerCommander;
import control.PlayerCommander.Role;
import junit.framework.TestCase;
import logic.Actor;
import logic.Entity;
import logic.GameLogic;
import logic.GameState;
import logic.Map;
import logic.MovingEntity;
import logic.Pair;
import logic.Sniper;
import logic.Tile;
import logic.Vec2;

public class GameTest extends TestCase{
	static GameState gs = new GameState();
	static LinkedList<Pair<PlayerCommander, Role>> players = new LinkedList<Pair<PlayerCommander, Role>>();
	static int height = 13;
	static int width = 20;
	@Before
	public void setUp() throws Exception {
		players.clear();
		players.add(new Pair<PlayerCommander, Role>(new PlayerCommander(1), Role.NONE));
		players.add(new Pair<PlayerCommander, Role>(new PlayerCommander(2), Role.NONE));
		players.add(new Pair<PlayerCommander, Role>(new PlayerCommander(3), Role.NONE));
		players.add(new Pair<PlayerCommander, Role>(new PlayerCommander(4), Role.NONE));
	}
	@Test
	public static void testGen(){
		int numActors = 0;
		int numPlayers = 0;
		Random r = new Random();
		assertEquals(players.size(), 4);
		for(int i = 0; i < 10; i++){
			numActors = r.nextInt(50) + 1;
			gs.gen(width, height, numActors, players);
			Map m = gs.getMap();
			int count = 0;
			for(int y = 0; y < gs.getMap().getHeight(); y++){
				for(int x = 0; x < gs.getMap().getWidth(); x++){
					if(y == 0 || x == 0 || x == gs.getMap().getWidth() -1 || y == gs.getMap().getHeight()-1)
						assertEquals(gs.getMap().getTile(x, y).getType(), Tile.TileType.WALL);
					else{
						if(gs.getMap().getTile(x, y).getType() ==  Tile.TileType.WALL){
							assertTrue(gs.getMap().getTile(x-1, y).getType() ==  Tile.TileType.WALL || 
									gs.getMap().getTile(x+1, y).getType() ==  Tile.TileType.WALL ||
									gs.getMap().getTile(x, y-1).getType() ==  Tile.TileType.WALL ||
									gs.getMap().getTile(x, y+1).getType() ==  Tile.TileType.WALL 
									);
						}else {
							count++;
						}
					}
				}
			}
			assertTrue("count : " + count + " verify : " + m.verify(), m.verify() == count);
			assertEquals(gs.getEntities().size(), numActors);
			int verPlayers = 0;
			int verSnipers = 0;
			int verActors = 0;
			for(Entity e : gs.getEntities()){
				if(e instanceof Sniper){
					verSnipers++;
				}
				else if(e instanceof Actor){
					verActors++;
				}
				if(e instanceof MovingEntity){
					if(((MovingEntity) e).getCommander() instanceof PlayerCommander)
						verPlayers++;
				}
			}
			assertEquals(verPlayers, numPlayers);
			assertEquals(verSnipers, 0);
			assertEquals(verActors,numActors);
		}
	}
	@Test
	public static void testActor(){
		int numActors = 0;
		gs.gen(width, height, numActors, players);
		float x;
		float y;
		Random r = GameLogic.getRandom();
		do{
			x = r.nextFloat() * (gs.getMap().getWidth() - 2) + 1;
			y = r.nextFloat() * (gs.getMap().getHeight() - 2) + 1;
		}while(gs.getMap().getTile((int)x, (int)y).getType() == Tile.TileType.WALL);
		
		Actor a1 = new Actor(new Vec2(x,y), new NullCommander(1), .025f, gs, gs);
		Actor a2 = new Actor(new Vec2(x,y), new NullCommander(0), .025f, gs, gs);
		gs.addEntity(a1);
		gs.addEntity(a2);
		assertEquals(GameLogic.Winner.NONE, GameLogic.winner(gs));
		a1.attack();
		assertEquals(Actor.SheepState.DEAD, a2.getState());
		assertEquals(Actor.SheepState.ATTACKING_STOPPED, a1.getState());
		assertFalse(a2.getRevealed());
		assertFalse(a1.getRevealed());
		assertFalse(Float.isNaN(a1.getAngleX()));
		assertFalse(Float.isNaN(a2.getAngleX()));
		Vec2 move = Vec2.randomVec();
		move.normalize();
		Vec2 lastPos = a1.getPos();
		
		a1.move(move);
		float error = 0.01f;
		assertTrue("ds: " + Vec2.getDist(a1.getPos(), lastPos), Vec2.getDist(a1.getPos(), lastPos) <= a1.getMaxSpeed()  + error);
		assertEquals(GameLogic.Winner.ACTORS, GameLogic.winner(gs));
		assertEquals(1.0, gs.getActorScore(), error);
	}
	
	@Test
	public static void testSniper(){
		int numActors = 0;
		int numPlayers = 1;
		players.set(0,new Pair<PlayerCommander, Role>(new PlayerCommander(1), Role.SNIPER));
		gs.gen(width, height, numActors, players);
		float x;
		float y;
		Random r = GameLogic.getRandom();
		do{
			x = r.nextFloat() * (gs.getMap().getWidth() - 2) + 1;
			y = r.nextFloat() * (gs.getMap().getHeight() - 2) + 1;
		}while(gs.getMap().getTile((int)x, (int)y).getType() == Tile.TileType.WALL);
		
		Actor a1 = new Actor(new Vec2(x,y), new NullCommander(1), .025f, gs, gs);
		Actor a2 = new Actor(new Vec2(x,y), new NullCommander(0), .025f, gs, gs);
		
		float x2;
		float y2;
		do{
			x2 = r.nextFloat() * (gs.getMap().getWidth() - 2) + 1;
			y2 = r.nextFloat() * (gs.getMap().getHeight() - 2) + 1;
		}while(gs.getMap().getTile((int)x2, (int)y2).getType() == Tile.TileType.WALL && Vec2.getDist(x2, y2, x, y) < 5);
		
		
		Actor a3 = new Actor(new Vec2(x2,y2), new NullCommander(0), .025f, gs, gs);
		gs.addEntity(a1);
		gs.addEntity(a2);
		gs.addEntity(a3);
		
		assertEquals(GameLogic.Winner.NONE, GameLogic.winner(gs));
		a1.attack();
		assertEquals(Actor.SheepState.DEAD, a2.getState());
		assertEquals(Actor.SheepState.ATTACKING_STOPPED, a1.getState());
		assertFalse(a2.getRevealed());
		assertFalse(a1.getRevealed());
		assertEquals(GameLogic.Winner.NONE, GameLogic.winner(gs));
		
		Sniper s = null;
		for(Entity e : gs.getEntities()){
			if(e instanceof Sniper){
				s = (Sniper)e;
				break;
			}
		}
		assertNotEquals(null, s);
		
		int initialBullets = numPlayers-1 + Sniper.getExtraBullets();
		assertEquals(initialBullets, s.getBullets());
		
		s.setPos(new Vec2(x, y));
		
		s.update(1);
		
		assertTrue(a2.getRevealed());
		assertTrue(a1.getRevealed());
		
		s.attack();
		
		assertEquals(initialBullets-1 , s.getBullets());
		
		assertEquals(Sniper.SniperState.SHOOTING , s.getState());
		 
		assertEquals(Actor.SheepState.DEAD, a1.getState());
		assertEquals(Actor.SheepState.DEAD, a2.getState());
		
		assertFalse(a1.getRevealed());
		assertTrue(a2.getRevealed());
		
		GameLogic.gameLoop(gs,1);
		
		assertTrue(a1.getRevealed());
		
		Vec2 move = Vec2.randomVec();
		move.normalize();
		Vec2 lastPos = s.getPos();
		
		s.move(move);
		float error = 0.01f;
		assertTrue("ds: " + Vec2.getDist(s.getPos(), lastPos), Vec2.getDist(s.getPos(), lastPos) <= s.getMaxSpeed()  + error);
		
		assertEquals(GameLogic.Winner.SNIPER, GameLogic.winner(gs));
	}
	
	@Test
	public static void testPathFinding(){
		int numActors = 0;
		Random r = new Random();
		assertEquals(players.size(), 4);
		for(int i = 0; i < 10; i++){
			numActors = r.nextInt(50) + 1;
			gs.gen(width, height, numActors, players);
			NavGraph graph = new NavGraph(gs.getMap());
			for(int j = 0; j < 10; j++){
				NavGraph.Node src = graph.getNodes().get(r.nextInt(graph.getNodes().size()));
				NavGraph.Node dst = null;
				do{
					dst = graph.getNodes().get(r.nextInt(graph.getNodes().size()));
				}while(src==dst);
				NavGraph.Path p = PathFinder.genPath(graph, src, dst);
				assertNotEquals(null, p);
				PathFinder.smoothPath(p, gs.getMap(),.1f);
				assertNotEquals(0, p.getNodes().size());

				Trajectory t = new Trajectory(p, gs.getMap(), 100,(float).5,(float)0.25, (float) .05);

				assertNotEquals(0, t.getPoints().size());
			}
			
		}
	}
	
	@Test
	public static void testAI(){
		gs.gen(width, height, 50, players);
		for(int i  = 0 ;i < 10000; i++){
			GameLogic.gameLoop(gs, 1);
			for(Entity e : gs.getEntities()){
				if(e instanceof Actor)
					assertTrue(((Actor) e).getAlive());
			}
		}
	}
}
