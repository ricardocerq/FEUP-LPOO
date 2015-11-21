package logic;

import gui.GameDraw;
import gui.GameWindow;

import java.io.IOException;
import java.net.BindException;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.google.zxing.WriterException;

import audio.AudioManager;
import network.CommunicationManager;


public class Game {
	public static void main(String[] args) throws IOException {
		System.setProperty("sun.java2d.opengl", "true");
		System.err.println("OK");
		if(!GameDraw.loadImages())
			System.exit(1);

		try{
			AudioManager.init();
		}catch(IOException e){
			e.printStackTrace();
		}

		try{
			CommunicationManager.setThisIP();
		}catch(IOException e){
			System.out.println("No Connection!");
			System.exit(1);
		}
		CommunicationManager.setIsServer(true);
		try{
			CommunicationManager c = CommunicationManager.getInstance();
			if(c != null)
				c.openAll();
		}catch (BindException e){
			System.err.println("Port in use !!");
			System.exit(1);
		}
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					GameDraw.writeQR();
					GameWindow.getInstance();
				
				} catch (WriterException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void connectionError() {
		JOptionPane.showMessageDialog(GameWindow.getInstance(), "Could not connect!");
		GameWindow.getInstance().dispose();
	}
}
