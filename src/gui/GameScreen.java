package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Observer;

import javax.swing.Timer;

import control.PlayerCommander;
import audio.AudioManager;
import logic.GameLogic;
import logic.GameState;
import logic.Pair;



public class GameScreen extends GenericGameScreen{
	/**
	 * 
	 */
	private static final long serialVersionUID = 764578129380153922L;
	GameState gs;
	Timer timer;
	private boolean paused = true;
	private static int mapWidth = 20;
	private static int mapHeight = 13;
	public static void setMapWidth(int mapWidth) {
		GameScreen.mapWidth = mapWidth;
	}
	public static void setMapHeight(int mapHeight) {
		GameScreen.mapHeight = mapHeight;
	}
	static List<Pair<PlayerCommander, PlayerCommander.Role>> players = new LinkedList<Pair<PlayerCommander, PlayerCommander.Role>>();
	static int numActors = 15;
	
	
	public static int getNumActors() {
		return numActors;
	}
	public static void setNumActors(int numActors) {
		GameScreen.numActors = numActors;
	}
	public static void setPlayers(List<Pair<PlayerCommander, PlayerCommander.Role>> p){
		players = p;
	}
	public GameScreen(Observer ob) throws IOException{
		super(ob);
		AudioManager.getInstance().loop(AudioManager.Sound.MUSIC_LPOO, 0.7f);
		@SuppressWarnings("unused")
		GameEventHandler gh = GameEventHandler.getInstance();
		gs = new GameState();
		
		this.setBackground(new Color(47,40,58));
		this.setPreferredSize(new Dimension(800,600));
	
		GameControl control = new GameControl(this, gs);
		this.addMouseListener(control);
		this.addMouseMotionListener(control);
		this.addKeyListener(control);
		//this.addKeyListener(this);
		timer = new Timer(1000/GameLogic.FRAMES_PER_SECOND, new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!paused){
					GameLogic.gameLoop(gs, 1);
				}
			
				repaint();
			}
		});
		this.setDoubleBuffered(true);
		//timer.start();
		this.setFocusable(true);
		this.requestFocus();
		//System.out.println(path.getNodes().size());
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		GameDraw.setRes(getWidth(), getHeight());
		GameDraw.drawCentered(g, gs, 0,0,getWidth(),getHeight(),getWidth(),getHeight());
	}
	public int getTileSize(){
		int sizex = (getWidth()-0)/gs.getMap().getWidth();
		int sizey = (getHeight()-0)/gs.getMap().getHeight();
		int size = Math.min(sizex, sizey);
		return size;
	}
	public int getInitialX(){
		return (getWidth() - getTileSize() * gs.getMap().getWidth())/2;
	}
	public int getInitialY(){
		return  (getHeight() - getTileSize()* gs.getMap().getHeight())/2;
	}
	@Override
	public Dimension switchOn() {
		this.requestFocus();
		Dimension d = super.switchOn();
		gs.gen(mapWidth, mapHeight, numActors, players);
		paused = false;
		repaint();
		timer.start();
		return d;
	}
	@Override
	public void switchOff() {
		super.switchOff();
		paused = true;
		timer.stop();
	}
	
}
