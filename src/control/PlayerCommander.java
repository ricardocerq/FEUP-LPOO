package control;

import java.io.Serializable;

import logic.Vec2;
import network.CommunicationManager;
import network.ReceiverListener;

/**
 * represents a commander controlled by the player
 */
public class PlayerCommander implements Commander, ReceiverListener, Comparable<PlayerCommander>{
	public enum Role {ACTOR, NONE, SNIPER}
	Command next = null;
	int number;
	Vec2 usernext = null;
	
	/**
	 * constructor for class
	 * @param number player number
	 */
	public PlayerCommander(int number){
		CommunicationManager.getInstance().addReceiverListener(this);
		this.number = number;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(PlayerCommander o) {
		return number - o.number;
	}
	/* (non-Javadoc)
	 * @see control.Commander#generateCommand()
	 */
	@Override
	public Command generateCommand() {
		if(next != null){
			Command returncommand =  next;
			next = null;
			return returncommand;
		}
			
		if(usernext == null){
			return new StopCommand();
		}
		return new PosMoveCommand((usernext).getX(),(usernext).getY());
	}

	/**
	 * get the players number
	 * @return the players number
	 */
	public int getNumber(){
		return number;
	}

	/* (non-Javadoc)
	 * @see network.ReceiverListener#onReceive(int, java.io.Serializable)
	 */
	@Override
	public boolean onReceive(int number, Serializable s) {
		if(number == this.number){
			if( s instanceof Vec2){
				usernext = (Vec2) s;
				return false;
			}
			if(s instanceof String && s.equals("a")){
				next = new AttackCommand();
				return false;
			}
		}
		return false;
	}

	

	/* (non-Javadoc)
	 * @see control.Commander#process(logic.Vec2, logic.Vec2)
	 */
	@Override
	public void process(Vec2 pos, Vec2 velocity) {
		
	}


}
