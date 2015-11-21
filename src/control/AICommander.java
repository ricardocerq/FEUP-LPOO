package control;

import java.util.Random;

import pathfinding.NavGraph;
import pathfinding.PathFinder;
import pathfinding.Trajectory;
import pathfinding.NavGraph.Node;
import pathfinding.NavGraph.Path;
import logic.GameLogic;
import logic.Map;
import logic.Vec2;

/**
 *	commander controlled by AI
 */
public class AICommander implements Commander{
	public enum AIState {EXECUTING,FORMULATING, INIT, WAITING};
	public final static float MAX_DIST = 1000000000;
	public final static int MAX_WAIT_FRAMES = 120;
	public final static float MIN_DIST = .15f;
	public final static int MIN_WAIT_FRAMES = 60;
	private Map map;
	private NavGraph navgraph;
	private Vec2 next;
	private AIState state = AIState.WAITING;
	private Trajectory trajectory;
	private int waitFrames=GameLogic.getRandom().nextInt(120)+120;
	
	/**
	 * constructor for class
	 * @param map
	 * @param navgraph
	 */
	public AICommander(Map map, NavGraph navgraph){
		this.map = map;
		this.navgraph = navgraph;
	}
	/* (non-Javadoc)
	 * @see control.Commander#generateCommand()
	 */
	@Override
	public Command generateCommand() {
		//return new StopCommand();	
		if(next == null)
			return new StopCommand();	
		return new ForceMoveCommand(next.getX(),next.getY());
	}
	/**
	 * get the current trajectory
	 * @return
	 */
	public Trajectory getTrajectory() {
		return trajectory;
	}
	/* (non-Javadoc)
	 * @see control.Commander#process(logic.Vec2, logic.Vec2)
	 */
	public void process(Vec2 pos, Vec2 velocity){
		//System.out.println(state);
		if(state == AIState.FORMULATING){
			Node src = navgraph.getClosest(pos.getX(), pos.getY());
			Node dst = navgraph.getNodes().get(new Random().nextInt(navgraph.getNodes().size()));
			Path path = PathFinder.genPath(navgraph, src, dst);
			if(path != null){
				PathFinder.smoothPath(path, map, (float)0.1);
				trajectory  = new Trajectory(path, map, 100,(float).5,(float)0.25, (float) .05);
				state = AIState.INIT;
			}
			next = null;
			return;
		
			
		}
		else if(state == AIState.EXECUTING || state == AIState.INIT){
			/*int newindex =  trajectory.getClosest(pos);
			Vec2 direction =  trajectory.getPoints().get(newindex).sub(pos);
			if(direction.mag() > MAX_DIST){
				state = AIState.FORMULATING;
			}*/
			if(trajectory.getPoints().size() == 0){
				state = AIState.WAITING;
				waitFrames = new Random().nextInt(MAX_WAIT_FRAMES-MIN_WAIT_FRAMES) + MIN_WAIT_FRAMES;
				next = null;
				return;
			}
			Vec2 direction =  trajectory.getFirst().sub(pos);
			float dist;
			if((dist = direction.mag()) > MAX_DIST){
				state = AIState.FORMULATING;
				return;
			}else if(dist < MIN_DIST){
				trajectory.removeFirst();
				if(trajectory.getPoints().size() == 0){
					state = AIState.WAITING;
					waitFrames = new Random().nextInt(MAX_WAIT_FRAMES-MIN_WAIT_FRAMES) + MIN_WAIT_FRAMES;
					next = null;
					return;
				}
				next =  trajectory.getFirst().sub(pos);
			}
			else next = direction;
			if(state == AIState.EXECUTING){
				next = direction.sub(velocity);
				next.scalarMulSet(.025f);
			}
			
			state =  AIState.EXECUTING;
		}
		else if(state == AIState.WAITING){
			if(waitFrames <= 0)
				state = AIState.FORMULATING;
			else waitFrames--;
		}
	}

}
