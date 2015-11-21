package control;

import logic.Vec2;

/**
 * represents a commander that does nothing, for testing purposes
 */
public class NullCommander implements Commander{

	int number = 0;
	
	/**
	 * constructor for class
	 * @param number player number
	 */
	public NullCommander(int number){
		this.number=number;
	}
	
	/* (non-Javadoc)
	 * @see control.Commander#generateCommand()
	 */
	@Override
	public Command generateCommand() {
		return new PosMoveCommand(0,0);
	}

	/**
	 * get the player number
	 * @return player number
	 */
	public int getNumber() {
		return number;
	}
	
	/* (non-Javadoc)
	 * @see control.Commander#process(logic.Vec2, logic.Vec2)
	 */
	@Override
	public void process(Vec2 pos, Vec2 velocity) {
		
	}

	/**
	 * set the player number
	 * @param number new player number
	 */
	public void setNumber(int number) {
		this.number = number;
	}

}
