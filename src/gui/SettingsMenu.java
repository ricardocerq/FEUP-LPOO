package gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;
import audio.AudioManager;
import logic.Sniper;

public class SettingsMenu extends GenericGameScreen {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3418183567620892147L;
	/**
	 * Create the panel.
	 * @throws IOException 
	 */
	public SettingsMenu(Observer ob) throws IOException {
		super(ob);
		/*JPanel panel = new JPanel(){
			private static final long serialVersionUID = 3243848245799525803L;
			 @Override
			 public void paintComponent(Graphics g) {
			 }
		};
		
		
		JButton btnOptions = new GameButton(this,"Options", false);
		JButton btnBack = new GameButton(this,"Back", true);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(191)
					.addComponent(btnOptions, GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
					.addGap(190))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(198)
					.addComponent(btnBack, GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE)
					.addGap(197))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(100)
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addGap(100))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(43)
					.addComponent(btnOptions,  GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(panel, GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
					.addGap(18)
					.addComponent(btnBack, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
					.addGap(26))
		);
		
		GameButton button = new GameButton(this, "Sheep" , false);
		button.setAlignmentX(0.5f);
		
		GameSlider actorSlider = new GameSlider(this, 1, 150, 15);
		
		actorSlider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if(arg0.getSource() instanceof JSlider)
				GameScreen.setNumActors(((JSlider)arg0.getSource()).getValue());
			}
		});
		
		GameButton button_1 = new GameButton(this, "Extra Bullets", false);
		button_1.setAlignmentX(0.5f);
		
		GameSlider bulletSlider = new GameSlider(this, 0, 150, 1);
		
		bulletSlider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if(arg0.getSource() instanceof JSlider)
				Sniper.setExtraBullets(((JSlider)arg0.getSource()).getValue());
			}
		});
		
		JButton btnNewButton = new GameButton(this, "Volume", false);
		
		JSlider slider = new GameSlider(this, 0,100,(int)(AudioManager.getInstance().getVolume()*100));
		
		slider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				if(e.getSource() instanceof JSlider){
					float number = (float)((JSlider)e.getSource()).getValue();
					AudioManager.getInstance().setVolume(number / 100);
					System.out.println("setting volume" + number / 100);
				}
			}
		});
		GameButton musicButton = new GameButton(this, "Music", true, true);
		musicButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				AudioManager.getInstance().handleLoops(!musicButton.getClicked());
			}
			
		});
		
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//AudioManager.getInstance().handleLoops(!musicButton.getClicked());
				notifyObservers(GameWindow.Screen.MAIN);
			}
		});
		
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_panel.createSequentialGroup()
						.addGap(93)
						.addComponent(button, GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
						.addGap(93))
					.addGroup(gl_panel.createSequentialGroup()
						.addGap(25)
						.addComponent(actorSlider, GroupLayout.DEFAULT_SIZE, 565, Short.MAX_VALUE)
						.addContainerGap())
					.addGroup(gl_panel.createSequentialGroup()
						.addGap(90)
						.addComponent(button_1, GroupLayout.DEFAULT_SIZE, 419, Short.MAX_VALUE)
						.addGap(91))
					.addGroup(gl_panel.createSequentialGroup()
						.addGap(90)
						.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
						.addGap(90))
					.addGroup(gl_panel.createSequentialGroup()
						.addGap(14)
						.addComponent(bulletSlider, GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
						.addGap(13))
					.addGroup(gl_panel.createSequentialGroup()
						.addGap(14)
						.addComponent(slider, GroupLayout.DEFAULT_SIZE, 573, Short.MAX_VALUE)
						.addGap(13))
					.addGroup(gl_panel.createSequentialGroup()
						.addGap(271)
						.addComponent(musicButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGap(270))
			);
			gl_panel.setVerticalGroup(
				gl_panel.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_panel.createSequentialGroup()
						.addGap(5)
						.addComponent(button, GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(actorSlider, GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(button_1, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
						.addGap(11)
						.addComponent(bulletSlider, GroupLayout.DEFAULT_SIZE, 36, Short.MAX_VALUE)
						.addGap(13)
						.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
						.addGap(11)
						.addComponent(slider, GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE)
						.addGap(2)
						.addComponent(musicButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addContainerGap())
		);
		panel.setLayout(gl_panel);
		groupLayout.setAutoCreateGaps(true);
		groupLayout.setAutoCreateContainerGaps(true);
		setLayout(groupLayout);
		this.setPreferredSize(new Dimension(800,500));*/
		
		
		setLayout(new MigLayout("", "[500,grow,fill]", "[grow,fill][44.00,grow][184.00,grow,fill][-20.00,grow,fill][48.00,grow][44.00,grow,fill]"));
		
		JButton optionsButton = new GameButton(this, "Options", false);
		add(optionsButton, "cell 0 0");
		
		JPanel panel = new JPanel(){
			private static final long serialVersionUID = 3243848245799525803L;
			 @Override
			 public void paintComponent(Graphics g) {
			 }
		};
		
		JButton sheepButton = new GameButton(this,"Sheep", false);
		JSlider sheepSlider = new GameSlider(this, 1, 250, 15);
		JButton extraBulletsButton = new GameButton(this, "Extra Bullets", false);
		JSlider bulletsSlider = new GameSlider(this, 0, 250, 1);
		JButton btnMapWidth = new GameButton(this,"Map Width", false);
		JSlider widthSlider = new GameSlider(this, 7, 25, 20);
		JButton btnMapHeight = new GameButton(this,"Map Height", false);
		JSlider heightSlider = new GameSlider(this, 7, 25, 13);
		JButton Volume = new GameButton(this,"Volume", false);
		JSlider volumeSlider = new GameSlider(this, 0,100,(int)(AudioManager.getInstance().getVolume()*100));
		JButton backButton = new GameButton(this, "Back", true);
		JButton musicButton = new GameButton(this, "Music", true, true);
		
		setLayout(new MigLayout("", "[500,grow,fill]", "[grow,fill][238.00,grow 150,fill][44.00,grow,fill]"));
		
		add(optionsButton, "cell 0 0,grow");
		
		add(panel, "cell 0 1,grow");
		panel.setLayout(new MigLayout("", "[25.00%:n:25.00%][25.00%:50.00%:25.00%,grow,right][25.00%:25.00%:25.00%,grow,fill][grow]", "[grow,fill][grow,fill][grow,fill][grow,fill][grow,fill][-33.00,grow,fill][grow,fill]"));
		
		panel.add(sheepButton, "cell 1 1,growx,aligny center");
		
		panel.add(sheepSlider, "cell 2 1,grow");
		
		panel.add(extraBulletsButton, "cell 1 2,grow");
		
		
		panel.add(bulletsSlider, "flowx,cell 2 2,growx,aligny center");
		
		
		panel.add(btnMapWidth, "cell 1 3,grow");
		
		
		panel.add(widthSlider, "flowx,cell 2 3,grow");
		
		
		panel.add(btnMapHeight, "cell 1 4,grow");
		
		
		panel.add(heightSlider, "flowx,cell 2 4,growx,aligny center");
		
		
		panel.add(Volume, "cell 1 5,grow");
		
		
		volumeSlider.setValue(100);
		panel.add(volumeSlider, "flowy,cell 2 5,grow");
		
		panel.add(musicButton, "cell 1 6 2 1,growx,aligny center");
		
		Component horizontalGlue = Box.createHorizontalGlue();
		add(horizontalGlue, "flowx,cell 0 2,growx");
		
		Component horizontalGlue_2 = Box.createHorizontalGlue();
		add(horizontalGlue_2, "cell 0 2,growx");
		
		Component horizontalGlue_4 = Box.createHorizontalGlue();
		add(horizontalGlue_4, "cell 0 2,growx");
		
		
		add(backButton, "cell 0 2,grow");
		
		Component horizontalGlue_5 = Box.createHorizontalGlue();
		add(horizontalGlue_5, "cell 0 2,growx");
		
		Component horizontalGlue_3 = Box.createHorizontalGlue();
		add(horizontalGlue_3, "cell 0 2,growx");
		
		Component horizontalGlue_1 = Box.createHorizontalGlue();
		add(horizontalGlue_1, "cell 0 2,growx");
		
		
		
		
		sheepSlider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if(arg0.getSource() instanceof JSlider)
				GameScreen.setNumActors(((JSlider)arg0.getSource()).getValue());
			}
		});
		
		
		
		
		bulletsSlider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if(arg0.getSource() instanceof JSlider)
				Sniper.setExtraBullets(((JSlider)arg0.getSource()).getValue());
			}
		});
		heightSlider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if(arg0.getSource() instanceof JSlider)
				GameScreen.setMapHeight(((JSlider)arg0.getSource()).getValue());
			}
		});
		widthSlider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent arg0) {
				if(arg0.getSource() instanceof JSlider)
					GameScreen.setMapWidth(((JSlider)arg0.getSource()).getValue());
			}
		});
		
		volumeSlider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent e) {
				if(e.getSource() instanceof JSlider){
					float number = (float)((JSlider)e.getSource()).getValue();
					AudioManager.getInstance().setVolume(number / 100);
				}
			}
		});
		musicButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				AudioManager.getInstance().handleLoops(!((GameButton)musicButton).getClicked());
			}
			
		});
		
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//AudioManager.getInstance().handleLoops(!musicButton.getClicked());
				notifyObservers(GameWindow.Screen.MAIN);
			}
		});
		
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
