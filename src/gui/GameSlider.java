package gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JSlider;

public class GameSlider extends JSlider implements MouseListener, MouseMotionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7166274001249686831L;
	Component parent;
	float barWidthscale = .9f;
	float barHeightscale = .25f;
	float catchX = .1f;
	float catchY = 1f;
	Color back = new Color(0,0,0,50);
	Color selectedColor = new Color(255,255,255,255);
	Color hoveredColor = new Color(225,225,225,255);
	Color normalColor = new Color(200,200,200,255);
	
	boolean hovered = false;
	boolean selected = false;
	public GameSlider(Component parent, int min, int max, int value) {
        super(min, max, value);
        this.parent = parent;
    	setAlignmentX(Component.CENTER_ALIGNMENT);
    	this.addMouseListener(this);
    	this.addMouseMotionListener(this);
    	this.setFocusable(true);
    	super.setEnabled(false);
	}
	@Override
    protected void paintComponent(Graphics g) {
    	parent.repaint();
    	//super.paintComponent(g);
    	g.setColor(back);
    	int initialX = getWidth() /2  - (int)(getWidth() * barWidthscale) /2;
    	int initialY = getHeight() /2  + (int)(getHeight() * barHeightscale) /4 ;
    	int maxX = (int)(getWidth() * barWidthscale);
    	int maxY =  (int)(getHeight() * barHeightscale);
    	
    	int valueX = maxX * (this.getValue() - this.getMinimum())/(this.getMaximum() - this.getMinimum());
    	
        g.fillRect( initialX ,initialY  , maxX, maxY );
        int offset = Math.min(getWidth(), getHeight())/128;
        offset = (int) (maxX * .005f);
       // g.fillPolygon(new int[]{initialX+ valueX+ offset, (int)(initialX+ valueX - maxX* .01f)+ offset, (int)(initialX+valueX + maxX* .01f)+ offset}, new int[]{initialY + offset,(int)(initialY - maxY * .75f+ offset),(int)(initialY - maxY* .75f+ offset)}, 3);
        g.setFont(GameWindow.getGameFont().deriveFont(Font.PLAIN, (int)(Math.min(getWidth(), getHeight())*.25) ));
    	
        
        String number = Integer.toString(getValue());
        Rectangle2D rect = g.getFontMetrics().getStringBounds(number, g);
    	int stringLen = (int)rect.getWidth();
    	int stringHeight = (int)rect.getHeight();
    	int stringX =  initialX + valueX - stringLen/2;
    	int stringY = initialY + maxY + 3*stringHeight /4 ;
    	
    	
        g.drawString(number, stringX+ offset, stringY+offset);
        
        
        if(selected)
        	g.setColor(selectedColor);
        else if(hovered) 
        	g.setColor(hoveredColor);
        else g.setColor(normalColor);
        
        g.fillRect( initialX ,initialY  , valueX , maxY);
        
        
       //g.fillPolygon(new int[]{initialX+ valueX, (int)(initialX+ valueX - maxX* .01f), (int)(initialX+valueX + maxX* .01f)}, new int[]{initialY ,(int)(initialY - maxY * .75f),(int)(initialY - maxY* .75f)}, 3);
        // g.fillPolygon(new int[]{0, 0,maxX}, new int[]{0, maxY, 0}, 3);
        g.drawString(number, stringX, stringY);
        
	}
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseEntered(MouseEvent arg0) {
			hovered = true;
			repaint();
		
	}
	@Override
	public void mouseExited(MouseEvent arg0) {
			hovered = false;
			repaint();
	}
	@Override
	public void mousePressed(MouseEvent arg0) {
		/*int middleX = getWidth() /2  - (int)(getWidth() * barWidthscale) /2 + (int)(getWidth() * barWidthscale)* (this.getValue() - this.getMinimum())/(this.getMaximum() - this.getMinimum());
		int middleY = (int)(getHeight() * barHeightscale) /2;
		int minX =  middleX - (int)(catchX * getWidth());
		int maxX =  middleX + (int)(catchX * getWidth());
		int minY =  middleY - (int)(catchY * getHeight());
		int maxY =  middleY + (int)(catchY * getHeight());
		if(arg0.getX() >= minX && arg0.getX() <= maxX && arg0.getY() >= minY && arg0.getY() <= maxY  ){
			selected = true;
			mouseDragged(arg0);
			repaint();
		}*/
		if(hovered){
			selected = true;
			mouseDragged(arg0) ;
		}
	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		selected = false;
		repaint();
		
	}
	@Override
	public void mouseDragged(MouseEvent arg0) {
		if(selected){
			int initialX = getWidth() /2  - (int)(getWidth() * barWidthscale) /2;
	    	int maxX = initialX + (int)(getWidth() * barWidthscale);
	    	int value = (int)(((float)(arg0.getX() - initialX) * (this.getMaximum() - this.getMinimum())) / (maxX - initialX) + .5f) + this.getMinimum();
	    	if(value < this.getMinimum())
	    		this.setValue(this.getMinimum());
	    	else if(value > this.getMaximum())
	    		this.setValue(this.getMaximum());
	    	else this.setValue(value);
	    		//this.setValue(25);
	    	this.repaint();
	    	
		}
	}
	@Override
	public void mouseMoved(MouseEvent arg0) {
		
	}
}
