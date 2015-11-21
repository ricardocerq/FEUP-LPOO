package gui;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class GameWindow extends JFrame implements Observer{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1903142717890981086L;
	
	public enum Screen{GAME, MAIN, SETTINGS, LOBBY, CLOSE};
	
	private GenericGameScreen gameScreen;
	private GenericGameScreen mainMenu;
	private GenericGameScreen lobbyMenu;
	private GenericGameScreen settingsMenu;
	private static Font gameFont;
	private static GameWindow instance;
	
	public static GameWindow getInstance(){
		if(instance == null)
			try {
				instance = new GameWindow();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return instance;
	}
	
	protected GameWindow() throws IOException{
		super("Sheep Party");
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e2) {
			e2.printStackTrace();
		}
		this.setIconImage(GameDraw.getPortrait());
		InputStream is = GameButton.class.getResourceAsStream("images/battlenet.ttf");
		try {
			
			gameFont = Font.createFont(Font.TRUETYPE_FONT, is);
		
		} catch (FontFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		getContentPane().setLayout(new CardLayout());
		mainMenu = new MainMenu(this);
		this.getContentPane().add(mainMenu);
		
		gameScreen = new GameScreen(this);
		this.getContentPane().add(gameScreen);
		
		settingsMenu = new SettingsMenu(this);
		this.getContentPane().add(settingsMenu);
		
		lobbyMenu = new LobbyMenu(this);
		this.getContentPane().add(lobbyMenu);
		
		this.setPreferredSize(mainMenu.switchOn());
		
		//this.setPreferredSize(gameScreen.getPreferredSize());
		centerWindow();
		
		
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		if(!(arg1 instanceof Screen))
			return;
		for(Component comp : this.getContentPane().getComponents()){
			if(comp instanceof GenericGameScreen){
				((GenericGameScreen) comp).switchOff();
			}else 
				comp.setVisible(false);
		}
		switch((Screen)arg1){
		case GAME:
			((GenericGameScreen) gameScreen).switchOn();
			break;
		case MAIN:
			((GenericGameScreen) mainMenu).switchOn();
			break;
		case SETTINGS:
			((GenericGameScreen) settingsMenu).switchOn();
			break;
		case LOBBY:
			((GenericGameScreen) lobbyMenu).switchOn();
			break;
		case CLOSE:
			this.processWindowEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
			break;
		}

	}
	public void centerWindow(){
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension initial = new Dimension((int)(screen.getWidth() /2 - getPreferredSize().getWidth()/2),(int)( screen.getHeight() /2 -getPreferredSize().getHeight()/2));
		this.setLocation(initial.width, initial.height);
	}

	public static Font getGameFont() {
		return gameFont;
	}
	
	
}
