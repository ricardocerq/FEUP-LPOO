package logic;

import java.util.List;

import control.Command;
import control.Commander;

/**
 * @author Ricardo
 *
 */
public class Sniper extends MovingEntity{
	public enum SniperState{RELOADING, SHOOTING, STANDBY}
	private static int extraBullets = 1;
	public static final double HIT_DISTANCE = .5;
	private static final double SNIPER_LIGHT_RANGE_MAX = 1.5f;

	private static final double SNIPER_LIGHT_RANGE_MIN = .5f;
	private static final double SNIPER_SIZE_MAX = 2f;

	private static final double SNIPER_SIZE_MIN = 1.5f;

	/**
	 * get how many bullets the sniper has in addition to the number of other players
	 * @return number of extra bullets
	 */
	public static int getExtraBullets() {
		return extraBullets;
	}

	/**
	 * set how many bullets the sniper has in addition to the number of other players
	 * @param extraBullets new number of extra bulletss
	 */
	public static void setExtraBullets(int extraBullets) {
		Sniper.extraBullets = extraBullets;
	};
	int bullets;
	private double c = .01;
	private double c2 = .0125;
	private double force = 0;

	private double force2 = 0;

	private double k = .001;
	private double lightRange = SNIPER_LIGHT_RANGE_MAX;

	private double sniperSize = SNIPER_SIZE_MIN;

	private SniperState state = SniperState.STANDBY;

	float ticks;

	private double v = 0;

	private double v2 = 0;
	/**
	 * constructor for sniper
	 * @param pos initial position of sniper
	 * @param commander snipers commander
	 * @param max_speed maximum speed of sniper
	 * @param gep provider of the games other entities
	 * @param gmp provider of the map
	 * @param bullets initial bullets
	 */
	public Sniper(Vec2 pos,  Commander commander,float max_speed, GameEntityProvider gep, GameMapProvider gmp, int bullets) {
		super(pos, commander,max_speed, gep, gmp);
		this.bullets = bullets;
	}
	/* (non-Javadoc)
	 * @see logic.MovingEntity#applyForce(logic.Vec2)
	 */
	@Override
	public void applyForce(Vec2 force) {
		// TODO Auto-generated method stub

	}
	/* (non-Javadoc)
	 * @see logic.MovingEntity#applyVelocity(float)
	 */
	@Override
	public void applyVelocity(float time) {
		// TODO Auto-generated method stub

	}
	/* (non-Javadoc)
	 * @see logic.MovingEntity#attack()
	 */
	@Override
	public void attack(){
		if(bullets == 0 || state != SniperState.STANDBY){
			super.miss(this);
			return;
		}
		//AudioManager.getInstance().playSound(AudioManager.Sound.GUNSHOT, .6f);
		bullets--;
		super.attack();

		List<Entity> ents = gep.getEntities();
		Vec2 pos = this.getPos().add(new Vec2(0,.125f));
		for(Entity e : ents){
			if(e instanceof Actor){
				if(Vec2.getDist(pos, e.getPos()) < HIT_DISTANCE)
					e.hit(this);
			}
		}
		state = SniperState.SHOOTING;
		ticks = 0;
	}
	/**
	 * get the current number of bullets
	 * @return current number of bullets
	 */
	public int getBullets() {
		return bullets;
	}

	/**
	 * get the current range of the snipers light
	 * @return current light range
	 */
	public double getRange() {
		return lightRange;
	}
	/**
	 * get the current size of the sight
	 * @return size of sight
	 */
	public double getSize() {
		return sniperSize;
	}


	/**
	 * get the snipers state
	 * @return state of sniper
	 */
	public SniperState getState() {
		return state;
	}

	/* (non-Javadoc)
	 * @see logic.MovingEntity#hit(logic.Entity)
	 */
	@Override
	public void hit(Entity attacker) {
		super.hit(attacker);

	}

	/* (non-Javadoc)
	 * @see logic.MovingEntity#move(logic.Vec2)
	 */
	@Override
	public boolean move(Vec2 move) {
		super.move(move);
		Vec2 scaled = move.scalarMul(max_speed);
		this.getPos().addSet(scaled);
		if(this.getPos().getX() < 0)
			this.getPos().setX(0);
		else if(this.getPos().getX() > gmp.getMap().getWidth()-1)
			this.getPos().setX(gmp.getMap().getWidth()-1);
		if(this.getPos().getY() < 0)
			this.getPos().setY(0);
		else if(this.getPos().getY() >  gmp.getMap().getHeight()-1)
			this.getPos().setY(gmp.getMap().getHeight()-1);

		return true;
	}

	/* (non-Javadoc)
	 * @see logic.MovingEntity#processFrame(float)
	 */
	@Override
	protected void processFrame(float time) {
		int framesPerSecond = 60;
		final double millisecondsPerFrame = 1000/(double)framesPerSecond;
		final double oscCenter = state != SniperState.STANDBY ? SNIPER_LIGHT_RANGE_MIN : SNIPER_LIGHT_RANGE_MAX;
		final double localC = state != SniperState.STANDBY ? c2: c;
		force = - k * (lightRange - oscCenter) - localC * v;
		v += millisecondsPerFrame * force;
		lightRange += v * millisecondsPerFrame;

		final double oscCenter2 = state != SniperState.STANDBY ? SNIPER_SIZE_MAX : SNIPER_SIZE_MIN;
		force2 = - k * (sniperSize - oscCenter2) - localC * v2;
		v2 += millisecondsPerFrame * force2;
		sniperSize += v2 * millisecondsPerFrame;
		ticks += time;
		if(ticks > 10){
			state = SniperState.RELOADING;
			super.attackEnd();
		}
		if(ticks > 10)
			state = SniperState.STANDBY;
		else{
			if(state != SniperState.STANDBY){}
			//this.getPos().addSet(Vec2.randomVec());
		}
	}

	/* (non-Javadoc)
	 * @see logic.MovingEntity#stop()
	 */
	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see logic.MovingEntity#update(float)
	 */
	@Override
	public void update(float time) {
		commander.process(this.getPos(), null);
		Command c = commander.generateCommand();
		if (c != null){
			c.execute(this);
		}
		applyVelocity(time);
		processFrame(time);
		List<Entity> ents = gep.getEntities();
		Vec2 pos = this.getPos();//.add(new Vec2(0,.125f));
		for(Entity e : ents){
			if(e instanceof Actor){
				if(Vec2.getDist(pos, e.getPos()) < lightRange)
					((Actor) e).reveal();
			}
		}
	}

}
