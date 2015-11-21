package logic;

import java.util.List;

import control.Command;
import control.Commander;
import control.PlayerCommander;

/**
 * represents the sheep
 *
 */
public class Actor extends MovingEntity{ 
	public enum SheepState{ATTACKING, ATTACKING_STOPPED, ATTACKING_WALKING, DEAD,NORMAL,STOPPED, WALKING}
	private boolean alive = true;
	public final double ATTACK_RANGE = .5;
	private Vec2 dir = Vec2.randomVec();
	private float frameTicks;;
	private boolean revealed = false;
	private SheepState state = SheepState.NORMAL;
	private float storedAngle;
	private Vec2 velocity = new Vec2();
	
	
	/**
	 * construtor for Actor
	 * @param pos position of sheep
	 * @param commander commander of sheep
	 * @param max_speed maximum speed of sheep
	 * @param gep entity provider for sheep
	 * @param gmp map provider for sheep
	 */
	public Actor(Vec2 pos, Commander commander, float max_speed, GameEntityProvider gep, GameMapProvider gmp){
		super(pos, commander, max_speed, gep, gmp);
		this.max_speed = max_speed;
	}
	private boolean addToPos(Vec2 addPos) {
		if((state == SheepState.DEAD&& revealed))
			return false;
		Vec2 newPos = getPos().add(addPos);
		if(gmp.getMap() == null || gmp.getMap().getTile((int) newPos.getX(),(int) newPos.getY()) == null || gmp.getMap().getTile((int) newPos.getX(),(int) newPos.getY()).getType() == Tile.TileType.WALL && commander instanceof PlayerCommander)
		{
			//this.setPos(newPos.addSet(new Vec2(-addPos.getY(),addPos.getX())));
			return false;
		}
		float dist = newPos.sub(getPos()).mag();
		addToTicks(dist);
		this.setPos(newPos);
		return true;
	}
	private void addToTicks(float dist){
		/*float val = dist*4;
		float newVal;
		if(add)
			newVal = frameTicks + val;
		else newVal  = frameTicks - val;
		if((add && frameTicks % 3 == 2 && newVal % 3 == 0) ||(!add && frameTicks% 3 == 0 && newVal % 3 == 2)){
			if(add)
				frameTicks -= val;
			else frameTicks += val;
			add = ! add;
		}
		else frameTicks = newVal;*/
		frameTicks += dist*4;
	}
	
	/* (non-Javadoc)
	 * @see logic.MovingEntity#applyForce(logic.Vec2)
	 */
	@Override
	public void	applyForce(Vec2 force){
		if(state == SheepState.DEAD && revealed)
			return;
		this.velocity.addSet(force);
		float v = velocity.mag();
		if(v > max_speed){
			this.velocity.scalarMulSet(max_speed/v);
		}
		if(velocity.magSquared() != 0)
			dir = velocity.getNormalized();
	}
	/* (non-Javadoc)
	 * @see logic.MovingEntity#applyVelocity(float)
	 */
	@Override
	public void applyVelocity(float time){
		if(commander instanceof PlayerCommander)
			return;
		addToPos(velocity.scalarMul(time));
		if(velocity.mag() > 0.01)
			super.move(velocity.scalarMul(time));
		//this.getPos().addSet(velocity.scalarMul(time));
	}
	/* (non-Javadoc)
	 * @see logic.MovingEntity#attack()
	 */
	@Override
	public void attack() {
		if(state == SheepState.NORMAL){
			super.attack();
			frameTicks = 0;
			state = SheepState.ATTACKING;
			revealed = false;
			List<Entity> entities = gep.getEntities();
			for(Entity a : entities){
				if(a == this )
					continue;
				if(a instanceof Actor){
					if(((Actor)a).getPlayerControlled() > 0)
						continue;
					if(state == SheepState.DEAD && getRevealed())
						continue;
				}
				Vec2 dist = a.getPos().sub(this.getPos());
				if(dist.mag() < ATTACK_RANGE ){
					a.hit(this);
					//a.state = SheepState.DEAD;
				}
			}
			//AudioManager.getInstance().playSound(AudioManager.Sound.SHEEP1, 1f);
		}
	}
	/**
	 * determine whether sheep is alive
	 * @return true if sheep is alive
	 */
	public boolean getAlive(){
		return state != SheepState.DEAD; 
	}
	/**
	 * @return angle of sheep relative to x axis
	 */
	public float getAngleX(){
		float angle;
		float mag = dir.mag();
		if(mag > 0.0000001){
		angle = (float) (Math.acos(dir.getX()/dir.mag()));
		if(dir.getY() > 0)
			angle =(float) (2*Math.PI - angle);
		storedAngle = angle;
		}
		return storedAngle;
	}
	/**
	 * @return frame information of actor
	 */
	public float getFrameInfo() {
		return frameTicks;
	}
	
	/**
	 * @return whether sheep is revealed
	 */
	public boolean getRevealed(){
		return revealed;
	}
	
	/**
	 * @return state of sheep
	 */
	public SheepState getState(){
		if(state == SheepState.DEAD)
			return state;
		if(velocity.mag() < .001){
			if(state == SheepState.ATTACKING)
				return SheepState.ATTACKING_STOPPED;
			else return SheepState.STOPPED;
		}else {
			if(state == SheepState.ATTACKING)
				return SheepState.ATTACKING_WALKING;
			else return SheepState.WALKING;
		}
	}
	
	/**
	 * @return velocity of actor
	 */
	public Vec2 getVelocity(){
		return velocity;
	}
	/* (non-Javadoc)
	 * @see logic.MovingEntity#hit(logic.Entity)
	 */
	@Override
	public void hit(Entity attacker) {
		if(state == SheepState.DEAD && revealed)
			return;
		if(state != SheepState.DEAD){
			super.hit(attacker);
			state = SheepState.DEAD;
			alive = false;
			revealed = false;
		}else ((MovingEntity)attacker).miss(this);
	}
	/**
	 * @return whether sheep is alive
	 */
	public boolean isAlive() {
		return alive;
	}
	
	/* (non-Javadoc)
	 * @see logic.MovingEntity#move(logic.Vec2)
	 */
	@Override
	public boolean move(Vec2 move){
		super.move(move);
		/*float dist = move.mag();
		//System.out.println("before: "+ getX() + ", "+ getY());
		if(dist <= max_speed){
			//System.out.println("true");
			this.getPos().addSet(move);
			//return true;
		}
		else{
			//System.out.println("false");
			this.getPos().addSet(move.scalarMul(max_speed/dist));
			//return false;
		}
		//System.out.println("after: "+ getX() + ", "+ getY());
		return true;*/
		if(state == SheepState.DEAD && revealed)
			return false;
		Vec2 scaled = move.scalarMul(max_speed);
		addToPos(scaled);
		this.velocity = scaled;
		//this.getPos().addSet(scaled);
		this.dir = move.getNormalized();
		return true;
		
	}
	/* (non-Javadoc)
	 * @see logic.MovingEntity#processFrame(float)
	 */
	@Override
	protected void processFrame(float time) {
		if(state == SheepState.ATTACKING){
			frameTicks += time/10;
			if(frameTicks > GameLogic.FRAMES_PER_SECOND/((float)20)){
				frameTicks = 0;
				state = SheepState.NORMAL;
				super.attackEnd();
			}else{	
			}
		}
		//frameTicks += time;
	}
	/**
	 * reveal this actor
	 */
	public void reveal(){
		revealed = true;
	}
	/**
	 * change value of alive
	 * @param alive new value for alive
	 */
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	/* (non-Javadoc)
	 * @see logic.MovingEntity#stop()
	 */
	public void stop() {
		this.velocity.setXY(0, 0);
	}
	/*public void moveUpdate(){
		commander.process(this.getPos(), this.getVelocity());
		Command c = commander.generateCommand();
		if (c != null){
			System.out.println(((MoveCommand)c).getX() + ", "+((MoveCommand)c).getY());
			c.execute(this);
		}
	}*/
	
	
	/* (non-Javadoc)
	 * @see logic.MovingEntity#update(float)
	 */
	@Override
	public void update(float time){
		commander.process(this.getPos(), this.getVelocity());
		Command c = commander.generateCommand();
		if (c != null){
			c.execute(this);
		}
		applyVelocity(time);
		processFrame(time);
	}
}
