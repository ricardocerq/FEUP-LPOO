package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.JButton;

import control.PlayerCommander;
import control.PlayerCommander.Role;
import logic.Pair;
import net.miginfocom.swing.MigLayout;
import network.CommunicationManager;
import network.PlayerConnectionListener;
import network.ReceiverListener;

public class LobbyMenu extends GenericGameScreen implements ReceiverListener, PlayerConnectionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4932309435677342547L;
	private boolean active = false;
	
	
	List<Pair<PlayerCommander, Role>> players = new LinkedList<Pair<PlayerCommander, Role>>();
	JButton playButton;
	/**
	 * Create the panel.
	 * @throws IOException 
	 */
	public LobbyMenu(Observer ob) throws IOException {
		super(ob);
		CommunicationManager.getInstance().addPlayerConnectionListener(this);
		CommunicationManager.getInstance().addReceiverListener(this);
		for(int i = 1 ;i <= 4; i++){
			players.add(new Pair<PlayerCommander, Role>(new PlayerCommander(i), Role.NONE));
		}
		
		JButton lobby = new GameButton(this,"Lobby",false);
		
		JButton backButton = new GameButton(this,"Back", true);
		
		playButton = new GameButton(this,"Play", true);
		
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				notifyObservers(GameWindow.Screen.MAIN);
			}
		});
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GameScreen.setPlayers(players);
				notifyObservers(GameWindow.Screen.GAME);
			}
		});
		
		setLayout(new MigLayout("", "[451.00,grow]", "[grow][grow][grow][grow][grow][grow][grow][grow][grow][grow]"));
		add(lobby, "cell 0 0,grow");
		
		Component horizontalGlue = Box.createHorizontalGlue();
		add(horizontalGlue, "flowx,cell 0 8,growx,aligny center");
		
		Component horizontalGlue_3 = Box.createHorizontalGlue();
		add(horizontalGlue_3, "cell 0 8,growx");
		
		Component horizontalGlue_4 = Box.createHorizontalGlue();
		add(horizontalGlue_4, "cell 0 8,growx");
		add(playButton, "cell 0 8,grow");
		
		
		Component horizontalGlue_6 = Box.createHorizontalGlue();
		add(horizontalGlue_6, "flowx,cell 0 9,growx");
		
		Component horizontalGlue_7 = Box.createHorizontalGlue();
		add(horizontalGlue_7, "cell 0 9,growx");
		
		Component horizontalGlue_8 = Box.createHorizontalGlue();
		add(horizontalGlue_8, "cell 0 9,growx");
		add(backButton, "cell 0 9,grow");
		
		Component horizontalGlue_5 = Box.createHorizontalGlue();
		add(horizontalGlue_5, "cell 0 8,growx");
		
		Component horizontalGlue_2 = Box.createHorizontalGlue();
		add(horizontalGlue_2, "cell 0 8,growx");
		
		Component horizontalGlue_1 = Box.createHorizontalGlue();
		add(horizontalGlue_1, "cell 0 8,growx,aligny center");
		
		Component horizontalGlue_9 = Box.createHorizontalGlue();
		add(horizontalGlue_9, "cell 0 9,growx");
		
		Component horizontalGlue_10 = Box.createHorizontalGlue();
		add(horizontalGlue_10, "cell 0 9,growx");
		
		Component horizontalGlue_11 = Box.createHorizontalGlue();
		add(horizontalGlue_11, "cell 0 9,growx");
		
		this.setDoubleBuffered(true);
		//timer.start();
		this.setFocusable(true);
		this.requestFocus();
		playButton.setEnabled(false);
	}
	@Override
	public Dimension switchOn() {
		active = true;
		//timer.start();
		return super.switchOn();
	}

	@Override
	public void switchOff() {
		active = false;
		//timer.stop();
		super.switchOff();
	}
	 @Override
	 public void paintComponent(Graphics g) {
		 super.paintComponent(g);
		 GameDraw.drawBackground(g, getWidth(), getHeight());
		 GameDraw.drawLobby(g,players, getWidth(), getHeight());
	 }
	@Override
	public boolean onReceive(int number, Serializable s) {
		System.out.println("Received " + number);
		
		if(!active)
			return false;
		if(number <= 0 || number > 4)
			return false;
		onPlayerChange();
		if(s instanceof String && s.equals("a")){
			if(players.get(number-1).second  == Role.ACTOR){
				if(numSnipers() == 0)
					players.get(number-1).second = Role.SNIPER;
			}else {
				players.get(number-1).second = Role.ACTOR;
			}
			if(playable()){
				playButton.setEnabled(true);
			}
			else {
				playButton.setEnabled(false);
			}
		}
		for(Pair<PlayerCommander, Role> p : players){
			System.out.println(p.first.getNumber() + ", " + p.second);
		}
		repaint();
		return true;
	}
	public boolean playable(){
		return numActors()>0 && numSnipers()>0;
	}
	public int numSnipers(){
		int sniperCount = 0;
		for(Pair<PlayerCommander, Role> p : players){
			if(p.second == Role.SNIPER)
				sniperCount++;
		}
		return sniperCount;
	}
	public int numActors(){
		int actorCount = 0;
		for(Pair<PlayerCommander, Role> p : players){
			if(p.second == Role.ACTOR)
				actorCount++;
		}
		return actorCount;
	}
	@Override
	public void onPlayerChange() {
		List<Integer> playerNums = CommunicationManager.getInstance().getPlayers();
		for(Integer i : playerNums){
			System.out.println(i);
		}
		
		for(Pair<PlayerCommander, Role> p : players){
			if(!playerNums.contains(p.first.getNumber()))
				p.second = Role.NONE;
			else{
				if(p.second == Role.NONE){
					if(numSnipers() == 0)
						p.second = Role.SNIPER;
					else p.second = Role.ACTOR;
				}
			}
			System.out.println(p.first.getNumber() + ", " + p.second);
		}
		if(playable()){
			System.out.println("ok");
			playButton.setEnabled(true);
		}
		else {
			System.out.println("not ok");
			playButton.setEnabled(false);
		}
		repaint();
	}

}
