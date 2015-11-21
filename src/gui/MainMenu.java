package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Observer;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;


public class MainMenu extends GenericGameScreen{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6090894998739332157L;

	
	
	public MainMenu(Observer ob) throws IOException {
		super(ob);
		
		
		GameButton title = new GameButton(this, ">> Sheep Party <<", false);
		
		
		GameButton playButton = new GameButton(this,"Play", true);
		
		
		GameButton settingsButton = new GameButton(this,"Settings", true);
		
		
		GameButton quitButton = new GameButton(this, "Quit", true);
		
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(175)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(quitButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
						.addComponent(settingsButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
						.addComponent(playButton, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
						.addComponent(title, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
					.addGap(175))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(25)
					.addComponent(title, GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
					.addGap(50)
					.addComponent(playButton, GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
					.addGap(25)
					.addComponent(settingsButton, GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
					.addGap(25)
					.addComponent(quitButton, GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE)
					.addGap(50))
		);
		setLayout(groupLayout);
		//this.setMinimumSize(new Dimension(200,500));
		this.setPreferredSize(new Dimension(800,500));
		
		quitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				notifyObservers(GameWindow.Screen.CLOSE);
			}
		});
		playButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				notifyObservers(GameWindow.Screen.LOBBY);
			}
		});
		settingsButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				notifyObservers(GameWindow.Screen.SETTINGS);
			}
		});
		setFocusable(false);
	}

	@Override
	public Dimension switchOn() {
		return super.switchOn();
	}

	@Override
	public void switchOff() {
		super.switchOff();
	}
	 @Override
	 public void paintComponent(Graphics g) {
		 super.paintComponent(g);
		 GameDraw.drawBackground(g, getWidth(), getHeight()); 
	 }
}
