package gui;

import control.MovingEntityListener;
import audio.AudioManager;
import logic.Actor;
import logic.MovingEntity;
import logic.Sniper;
import logic.Vec2;

public class GameEventHandler extends MovingEntityListener{
	private static GameEventHandler instance = null;
	private float volume = .8f;
	public static GameEventHandler getInstance(){
		if(instance == null)
			instance = new GameEventHandler();
		return instance;
	}
	
	private GameEventHandler(){
		super();
	};
	
	@Override
	public void onAttackStart(MovingEntity attacker) {
		if(attacker instanceof Sniper){
			AudioManager.getInstance().playSound(AudioManager.Sound.GUNSHOT, volume);
			GameDraw.setScreenShake(true);
		}
		
		
	}

	@Override
	public void onAttackEnd(MovingEntity attacker) {
		if(attacker instanceof Sniper){
			GameDraw.setScreenShake(false);
		}
	}

	@Override
	public void onHit(MovingEntity attacker, MovingEntity attacked) {
		if(attacker instanceof Actor && attacked instanceof Actor){
			AudioManager.getInstance().playSound(AudioManager.Sound.SHEEP1, volume);
		}
	}
		
	@Override
	public void onMove(MovingEntity mover, Vec2 move) {
		
	}

	@Override
	public void onMiss(MovingEntity attacker, MovingEntity attacked) {
		if(attacker instanceof Actor && attacked instanceof Actor){
			//AudioManager.getInstance().playSound(AudioManager.Sound.SHEEP2, volume);
		}
		else{
			if(attacker instanceof Sniper)
				AudioManager.getInstance().playSound(AudioManager.Sound.GUNCLICK, volume);
		}
	}
	
}
