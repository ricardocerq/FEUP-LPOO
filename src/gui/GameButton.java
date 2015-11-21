package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;

public class GameButton extends JButton implements MouseMotionListener, MouseListener{
	 /**
	 * 
	 */
	private static final long serialVersionUID = 4269976026167595994L;
	Font font;
	Component parent;
	Color mainColor = new Color(255,255,255,255);
	Color secColor = new Color(200,200,200,255);
	Color toggledColor = new Color(100,100,100,100);
	Color shadowColor = new Color(0,0,0,50);
	boolean hovered = false;
	boolean clickable;
	private boolean clicked;
	private boolean toggleable;
	public GameButton(Component parent, String label, boolean clickable, boolean toggle){
			super(label);
	        this.parent = parent;
	        this.clickable = clickable;
	        this.toggleable = toggle;
	        //this.clicked = toggle;
	    	this.addMouseListener(this);
	    	this.addMouseMotionListener(this);
	    	setAlignmentX(Component.CENTER_ALIGNMENT);
	}
	
		public GameButton(Component parent, String label, boolean clickable) {
	       this(parent, label, clickable, false);
	    }

	    @Override
	    protected void paintComponent(Graphics g) {
	    	parent.repaint();
	        //super.paintComponent(g);
	    	
	    	g.setFont(GameWindow.getGameFont().deriveFont(Font.PLAIN, (int)(Math.min(getWidth(), getHeight())*.75) ));
	    	//g.setFont(new Font(text, horizontalAlignment, alignmentX));
	    	Rectangle2D rect = g.getFontMetrics().getStringBounds(this.getText(), g);
	    	int stringLen = (int)rect.getWidth();
	    	int stringHeight = (int)rect.getHeight();
	    	int startX = this.getWidth()/2 - stringLen/2;
	    	int startY = this.getHeight() / 2 + stringHeight/2;
	    	g.setColor(shadowColor);
	    	g.drawString(this.getText(), startX + stringHeight/16, startY+ stringHeight/16);
	    	
	    	if(!this.isEnabled())
	    		g.setColor(toggledColor);
	    	
	    	else if(clicked && toggleable)
	    		g.setColor(toggledColor);
	    	else if(hovered)
	    		g.setColor(mainColor);
	    	else g.setColor(secColor);
	    	
	    	if(clicked){
	    		startX +=stringHeight/32;
	    		startY +=stringHeight/32;
	    	}
	    		
	    	g.drawString(this.getText(), startX, startY);
	    	
	    }

	    @Override
	    public Dimension getPreferredSize() {
	        Dimension size = super.getPreferredSize();
	        return size;
	    }

		@Override
		public void mouseDragged(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			if(clickable){
				hovered = true;
				repaint();
			}
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			if(clickable){
				hovered = false;
				repaint();
			}
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			if(toggleable){
				clicked = ! clicked;
				repaint();
				super.fireActionPerformed(new ActionEvent(this, 0, "Click"));
			}
			else if(hovered){
				clicked = true;
				repaint();
			}
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			if(!toggleable)
				clicked = false;
			repaint();
		}
		public boolean getClicked(){
			return clicked;
		}
}
