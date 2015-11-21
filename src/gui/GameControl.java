package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import control.PlayerCommander;
import logic.Entity;
import logic.GameState;
import logic.MovingEntity;
import logic.Sniper;
import logic.Vec2;

public class GameControl implements MouseListener, MouseMotionListener, KeyListener, ActionListener{
	private GameScreen j;
	private GameState gs;
	private boolean[] buttons = new boolean[]{false,false,false, false, false};
	int count = 0;
	public GameControl(GameScreen j, GameState gs){
		this.j = j;
		this.gs = gs;
	}
	
	private void issueCommand(){
		MovingEntity s = null;
		List<Entity> ents = gs.getEntities();
		for(Entity ent: ents){
			if(ent instanceof MovingEntity && ((MovingEntity) ent).getCommander() instanceof PlayerCommander){
				s = (MovingEntity)ent;
				if(s instanceof Sniper)
				break;
					//((PlayerCommander)((MovingEntity)ent).getCommander()).onReceive(((PlayerCommander)((MovingEntity)ent).getCommander()).getNumber(), (Serializable)new Vec2(((float)deltaX)/10, ((float)deltaY) /10));
			}
		}
		if(s == null)
			return;
		Vec2 move = new Vec2();
		
		if(buttons[0])
			move.setY(move.getY() - 1);
		if(buttons[1])
			move.setX(move.getX() - 1);
		if(buttons[2])
			move.setY(move.getY() + 1);
		if(buttons[3])
			move.setX(move.getX() + 1);
		if(move.mag() != 0)
			move.normalize();
		
		((PlayerCommander)s.getCommander()).onReceive(((PlayerCommander)s.getCommander()).getNumber(), move);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			j.notifyObservers(GameWindow.Screen.LOBBY);
		}
		
		if(e.getKeyCode() == KeyEvent.VK_W){
			
			buttons[0] = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_A){
			
			buttons[1] = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_S){
			
			buttons[2] = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_D){
			buttons[3] = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE){
			MovingEntity s = null;
			List<Entity> ents = gs.getEntities();
			for(Entity ent: ents){
				if(ent instanceof MovingEntity && ((MovingEntity) ent).getCommander() instanceof PlayerCommander){
					s = (MovingEntity)ent;
					if(s instanceof Sniper)
					break;
						//((PlayerCommander)((MovingEntity)ent).getCommander()).onReceive(((PlayerCommander)((MovingEntity)ent).getCommander()).getNumber(), (Serializable)new Vec2(((float)deltaX)/10, ((float)deltaY) /10));
				}
			}
			if(s == null)
				return;
			((PlayerCommander)s.getCommander()).onReceive(((PlayerCommander)s.getCommander()).getNumber(), "a");
			buttons[4] = true;
		}
		issueCommand();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_W){
			buttons[0] = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_A){
			buttons[1] = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_S){
			buttons[2] = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_D){
			buttons[3] = false;
		}
		
		issueCommand();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}
	
	int x, y;
	@Override
	public void mouseMoved(MouseEvent e) {
		/*int deltaX, deltaY;
		
		deltaX = e.getX() - x;
		deltaY = e.getY() - y;
		List<Entity> ents = gs.getEntities();
		for(Entity ent: ents){
			if(ent instanceof MovingEntity){
				if(((MovingEntity)ent).getCommander() instanceof PlayerCommander){
					((PlayerCommander)((MovingEntity)ent).getCommander()).onReceive(((PlayerCommander)((MovingEntity)ent).getCommander()).getNumber(), (Serializable)new Vec2(((float)deltaX)/10, ((float)deltaY) /10));
				}
			}
		}
		System.out.println("moved");
		x = e.getX();
		y = e.getY();
		j.repaint();*/
	}

	@Override
	public void mouseClicked(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {
		/*tracking = true;
		x = e.getX();
		y = e.getY();*/
	}

	@Override
	public void mouseExited(MouseEvent e) {
		/*tracking = false;
		List<Entity> ents = gs.getEntities();
		for(Entity ent: ents){
			if(ent instanceof MovingEntity){
				if(((MovingEntity)ent).getCommander() instanceof PlayerCommander){
					((PlayerCommander)((MovingEntity)ent).getCommander()).onReceive(((PlayerCommander)((MovingEntity)ent).getCommander()).getNumber(), (Serializable)new Vec2());
				}
			}
		}*/
	}

	@Override
	public void mousePressed(MouseEvent e) {
		//GameState g = j.getEditing();
//		int size = j.getTileSize();
//		//float x = (((j.gs.getMap().getWidth())*(NavGraph . SIDE_NODES_PER_TILE) - 1)* size + 2*(NavGraph.SIDE_NODES_PER_TILE)*e.getX() - (j.getWidth())*(NavGraph . SIDE_NODES_PER_TILE)) /(2*(NavGraph.SIDE_NODES_PER_TILE)*size);
//		//float y = ((((GameDraw.IMAGE_BASE)*(j.gs.getMap().getHeight()) - 2 *(GameDraw.IMAGE_HEIGHT_OFF))*(NavGraph.SIDE_NODES_PER_TILE) - GameDraw.IMAGE_BASE)* size + 2*(GameDraw.IMAGE_BASE)*(NavGraph.SIDE_NODES_PER_TILE)*e.getY()- (GameDraw.IMAGE_BASE)*(j.getHeight())*(NavGraph.SIDE_NODES_PER_TILE))/(2*(GameDraw.IMAGE_BASE)*(NavGraph.SIDE_NODES_PER_TILE)*size);
//		float x = ((float)e.getX()- j.getInitialX()) / size;
//		float y = ((float)e.getY() - j.getInitialY()) / size;
//		//x-= .5/NavGraph.SIDE_NODES_PER_TILE;
//		y-= 2.5/NavGraph.SIDE_NODES_PER_TILE;
//		if(e.getButton() == MouseEvent.BUTTON1 || e.getButton() == MouseEvent.BUTTON3){
//			Node closest = null;
//			float dist = Float.MAX_VALUE; 
//			for(Node n : j.nav.getNodes()){
//				float newdist = (float)Math.sqrt((x-n.getX())*(x-n.getX())+(y-n.getY())*(y-n.getY()));
//				if(newdist < dist){
//					dist = newdist;
//					closest = n;
//				}
//			}
//			if(e.getButton() == MouseEvent.BUTTON1)
//				j.src = closest;
//			else j.dst = closest;
//			j.calcPath();
//		}
//		j.repaint();
//		
		/*List<Entity> ents = gs.getEntities();
		for(Entity ent: ents){
			if(ent instanceof MovingEntity){
				if(((MovingEntity)ent).getCommander() instanceof PlayerCommander){
					((PlayerCommander)((MovingEntity)ent).getCommander()).onReceive(((PlayerCommander)((MovingEntity)ent).getCommander()).getNumber(), "a");
				}
			}
		}*/
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}
	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}
