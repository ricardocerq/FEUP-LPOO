package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import javax.swing.JPanel;


public class GenericGameScreen extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8729328797031140351L;

	public Dimension switchOn(){
		this.setVisible(true);
		this.requestFocus();
		return this.getPreferredSize();
	}
	public void switchOff(){
		this.setVisible(false);
	}
	private List<Observer> observers = new ArrayList<Observer>();
	
	public GenericGameScreen(Observer window) throws IOException {
		addObserver(window);
		setFocusable(true);
		this.setVisible(false);
	}
	public void addObserver(Observer o){
		observers.add(o);
	}
	public void notifyObservers(Object obj){
		for(Observer o : observers){
			o.update(null, obj);
		}
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
	}
}
