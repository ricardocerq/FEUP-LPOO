package gui;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Transparency;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import network.CommunicationManager;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import control.PlayerCommander;
import control.PlayerCommander.Role;
import logic.Actor;
import logic.Entity;
import logic.GameLogic;
import logic.GameState;
import logic.Pair;
import logic.Sniper;
import logic.Tile;

public class GameDraw {
	
	@Retention(RetentionPolicy.RUNTIME)
	private @interface DontLoad{
		
	}
	private static BufferedImage background;
	private static BufferedImage blackbottom;
	
	private static BufferedImage blackbottomleft;;
	
	private static BufferedImage blackbottomright;
	private static BufferedImage blackdefault;
	
	private static BufferedImage blackleft;
	private static BufferedImage blackright;
	
	private static BufferedImage blacktop;
	private static BufferedImage blacktopleft;
	
	
	
	private static BufferedImage blacktopright;
	private static BufferedImage bottom;
	
	private static BufferedImage bottomleft;
	
	private static BufferedImage bottomleftextra;
	private static BufferedImage bottomright;
	private static BufferedImage bottomrightextra;
	@DontLoad
	private static BufferedImage buffer;
	private static Graphics2D bufferg;
	private static BufferedImage crosshair;
	private static BufferedImage crosshair_shadow2;
	private static Date date = new Date();
	private static BufferedImage floor;
	static final int IMAGE_BASE = 24;
	static final int IMAGE_HEIGHT_OFF = 15;
	private static boolean imagesLoaded = false;
	
	private static BufferedImage left;
	@DontLoad
	private static BufferedImage mainBuffer;
	private static Graphics2D mainBufferg;
	private static Color[] playerColors = new Color[]{Color.GRAY, Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW};
	@DontLoad
	private static BufferedImage qr;
	private static int resX;
	private static int resY;
	private static BufferedImage right;
	private static BufferedImage rocks1;
	
	private static BufferedImage rocks2;
	private static BufferedImage rocks3;
	private static BufferedImage rocks4;
	
	private static boolean screenShake = false;
	private static BufferedImage shadowbottom;
	static Color shadowColor = new Color(0,0,0,100);
	private static BufferedImage shadowside;
	
	private static BufferedImage shadowtop;
	private static int shakeIntensity = 100   ;
	private static int shakeX = 0;
	private static int shakeY = 0;
	
	private static BufferedImage sheep;
	
	public static final int SHEEP_SIZE = 28;
	
	private static BufferedImage sheepportrait;
	
	private static BufferedImage sheepshadow;
	
	private static BufferedImage top;
	
	private static BufferedImage topleft;
	
	private static BufferedImage topleftleftnowall;
	
	//http://opengameart.org/content/a-blocky-dungeon
	private static BufferedImage topright;
	
	private static BufferedImage toprightrightnowall;
	
	private static BufferedImage walldecoration1;
	private static BufferedImage walldecoration2;
	private static BufferedImage walldecoration3;
	private static BufferedImage walldecoration4;

	
	public static void displayTileComponent(Graphics g, BufferedImage component, Point p, int size){
		g.drawImage(component, p.x, (int)(p.y-size*.625),  p.x + size,p.y + size, 0, 0, component.getWidth(), component.getHeight(), null);	
	}
	public static void draw(Graphics g, GameState gs, int initialX, int initialY, int size){
		if(mainBuffer == null || mainBuffer.getWidth() != resX || mainBuffer.getHeight() != resY){
			GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice device = env.getDefaultScreenDevice();
			GraphicsConfiguration config = device.getDefaultConfiguration();
			mainBuffer = config.createCompatibleImage(resX, resY, Transparency.TRANSLUCENT);
			//buffer = new BufferedImage(resX, resY, BufferedImage.TYPE_INT_ARGB);
			mainBufferg = (Graphics2D) mainBuffer.getGraphics();
			mainBufferg.setBackground(g.getColor());
		}
		
		if(!imagesLoaded)
			if(!loadImages())
				return;
		if(screenShake){
			System.out.println("yay");
			shakeX = GameLogic.r.nextInt(shakeIntensity) - shakeIntensity/2;
			shakeY = GameLogic.r.nextInt(shakeIntensity) - shakeIntensity/2;
		}else {
			shakeX = 0;
			shakeY = 0;
		}
		
		initialX += shakeX;
		initialY += shakeY;
		PriorityQueue<Entity> toPrint = getPrintQueue(gs);
		for(int j = 0; j < gs.getMap().getHeight(); j++){
			for(int i = 0; i < gs.getMap().getWidth(); i++)
				drawTileScreenFloor(g,gs, i, j, initialX, initialY, size);
		}
		for(int j = 0; j < gs.getMap().getHeight(); j++){
			for(int i = 0; i < gs.getMap().getWidth(); i++)
				drawTileScreenWalls(g,gs, i, j,initialX, initialY, size);

			for(int i = 0; i < gs.getMap().getWidth(); i++)
				drawTileScreenCorners(g,gs, i, j,initialX, initialY, size);

			while(!toPrint.isEmpty()){
				if((int)toPrint.peek().getY() == j){
					if(!(toPrint.peek() instanceof Sniper))
					drawEntityScreen(g, gs, toPrint.peek(), initialX, initialY,  size);
					toPrint.remove();
				} else break;
			}
		}
		for(Entity e : gs.getEntities()){
			if(e instanceof Sniper)
				drawEntityScreen(g, gs, e, initialX, initialY,  size);
		}
		if(gs.getWonState() != GameLogic.Winner.NONE){
			int size2 = Math.min(resX, resY);
			g.setFont(GameWindow.getGameFont().deriveFont(Font.PLAIN, (int)(size2 / 3)) );
			String winner;
			if(gs.getWonState() == GameLogic.Winner.SNIPER){
				winner = "Sniper Wins!";
			}else winner = "Actors Win!";
			Rectangle2D rect = g.getFontMetrics().getStringBounds(winner, g);
	    	double stringLen = rect.getWidth();
	    	int offset = size2/48;
	    	g.setColor(shadowColor);
			g.drawString(winner,(int)( ((float)resX)/2 - stringLen/2 + offset),(int)( ((float)resY)/2 + offset));
			g.setColor(Color.WHITE);
			g.drawString(winner, (int)(((float)resX)/2 - stringLen/2), (int)(((float)resY)/2));


			g.setFont(GameWindow.getGameFont().deriveFont(Font.PLAIN, (int)(size2/10)) );
			String message = "Actor Score : " + (int)(gs.getActorScore() * 100)+ " %";
			Rectangle2D rect2 = g.getFontMetrics().getStringBounds(message, g);
	    	double stringLen2 = rect2.getWidth();
	    	double stringHeight2 = rect2.getHeight();
	    	
	    	g.setColor(shadowColor);
			g.drawString(message,(int)( ((float)resX)/2 - stringLen2/2 + offset),(int)( ((float)resY)/2 +stringHeight2  + offset));
			g.setColor(Color.WHITE);
			g.drawString(message, (int)(((float)resX)/2 - stringLen2/2), (int)(((float)resY)/2 + stringHeight2));
			
			
			
		}
		//g.drawImage(mainBuffer, shakeX, shakeY, shakeX + resX, shakeY + resY, 0, 0, resX, resY, null);
		
	}
	///////
	public static void drawBackground(Graphics g, int resX, int resY){
		g.drawImage(background, 0, 0, null);
	}
	public static void drawCentered(Graphics g, GameState gs, int availableX, int availableY, int finalX, int finalY, int windowWidth, int windowHeight){
		int sizex = (finalX-availableX)/gs.getMap().getWidth();
		int sizey = (finalY-availableY)/gs.getMap().getHeight();
		int size = Math.min(sizex, sizey);
		int initialX = availableX/2 + (windowWidth - size*gs.getMap().getWidth())/2;
		int initialY = availableY/2 +(windowHeight - size*gs.getMap().getHeight())/2;
		
		draw(g, gs, initialX , initialY+ size * IMAGE_HEIGHT_OFF / IMAGE_BASE, size);
	}
	public static void drawCenteredLeft(Graphics g, GameState gs, int availableX, int availableY, int finalX, int finalY, int windowWidth, int windowHeight){
		int sizex = (finalX-availableX)/gs.getMap().getWidth();
		int sizey = (finalY-availableY)/gs.getMap().getHeight();
		int size = Math.min(sizex, sizey);
		int initialX = availableX;
		int initialY = availableY/2 +(windowHeight - size*gs.getMap().getHeight())/2;
		draw(g, gs, initialX, initialY, size);
	}
	public static void drawEntity(Graphics g,GameState gs, Entity t,  Point p, int size){
		if(t == null)
			return;
		if(!imagesLoaded)
			loadImages();
		//displayTileComponent(g,swordshieldHeroImage, p, size);
		//g.setColor(Color.BLUE);
		//g.fillOval(p.x - 5, p.y - 5, 10, 10);
		
		if(t instanceof Actor){
			int sheepsrcx;
			Actor.SheepState aState = ((Actor) t).getState();
			if(((Actor) t).getCommander() instanceof PlayerCommander){
				//System.out.println(aState);
				//System.out.println(((Actor) t).getVelocity().mag());
			}
			int number  = ((Actor) t).getPlayerControlled();
			
			if(!((Actor) t).getRevealed()){
				if(((Actor) t).getVelocity().mag() < 0.01)
					sheepsrcx = 1;
				else{
					sheepsrcx=  (int)((Actor) t).getFrameInfo();//1;//((Actor)t).g
					sheepsrcx %= 3;
				}
			}
			else{
				if(aState == Actor.SheepState.WALKING){
					sheepsrcx=  (int)((Actor) t).getFrameInfo();//1;//((Actor)t).g
					sheepsrcx %= 3;
				}
				else if(aState ==  Actor.SheepState.DEAD){
					sheepsrcx = 6;
				}
				else if(aState == Actor.SheepState.ATTACKING_STOPPED || aState == Actor.SheepState.ATTACKING_WALKING){
					sheepsrcx  = (int)((Actor) t).getFrameInfo();
					sheepsrcx = (sheepsrcx%3) + 3;
				}else sheepsrcx = 1;
			}
			sheepsrcx *= SHEEP_SIZE;
			
			int sheepsrcy = (int)(((Actor)t).getAngleX() / (Math.PI/4) + .5f);
			
			
			sheepsrcy %= 8;
			sheepsrcy *= SHEEP_SIZE;
			//System.out.println(sheepsrcy);
			float ratio = sheepshadow.getWidth() / sheepshadow.getHeight();
			g.drawImage(sheepshadow, (int)(p.x-size*.15*ratio), (int)((p.y+size*.1)-size*.15),  (int)(p.x+size*.15*ratio), (int)((p.y+size*.1)+size*.15), 0,0,sheepshadow.getWidth(), sheepshadow.getHeight(), null);
			g.drawImage(sheep, (int)(p.x-size*.5), (int)(p.y-size*.75),  (int)(p.x+size*.5), (int)(p.y+size*.25), sheepsrcx, sheepsrcy, sheepsrcx + SHEEP_SIZE ,sheepsrcy+SHEEP_SIZE, null);
			
			if(gs.getWonState() != GameLogic.Winner.NONE && number > 0 ){
				g.setColor(playerColors[number]);
				float x1 = p.x;
				float y1 = p.y - size*.5f;
				float height = size *.125f;
				g.fillPolygon(new int[] {(int) x1, (int)(x1 - size *.125f),(int)(x1 + size *.125f)}, new int[] {(int) y1 , (int)(y1 - height), (int)(y1 - height)}, 3);
				g.setFont(GameWindow.getGameFont().deriveFont(Font.PLAIN, (int)(size*.5)) );

				String s = "P" + number;
				double sW = g.getFontMetrics().getStringBounds(s, g).getWidth();
				g.drawString(s, (int)(x1 - sW/2), (int)(y1 - height*1.5));
			}
		
		}
		else if(t instanceof Sniper){
			float sniperSize = (float)((Sniper)t).getSize();
			g.drawImage(crosshair, (int)(p.x-size*.5*sniperSize), (int)(p.y-size*.5*sniperSize),  (int)(p.x+size*.5*sniperSize), (int)(p.y+size*.5*sniperSize), 0,0, crosshair.getWidth(), crosshair.getHeight(), null);
			//float ratio = ((float)crosshair_shadow2.getWidth()) / crosshair_shadow2.getHeight();
			float range = (float)((Sniper)t).getRange();
			//float cenas = 25f;
			//g.drawImage(crosshair_shadow2, (int)(p.x-size*cenas*ratio*range), (int)(p.y-size*cenas*range),  (int)(p.x+size*cenas*ratio*range), (int)(p.y+size*cenas*range), 0,0, crosshair_shadow2.getWidth(), crosshair_shadow2.getHeight(), null);
			
			if(buffer == null || buffer.getWidth() != resX || buffer.getHeight() != resY){
				GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
				GraphicsDevice device = env.getDefaultScreenDevice();
				GraphicsConfiguration config = device.getDefaultConfiguration();
				buffer = config.createCompatibleImage(resX, resY, Transparency.TRANSLUCENT);
				//buffer = new BufferedImage(resX, resY, BufferedImage.TYPE_INT_ARGB);
				if(bufferg != null){
					bufferg.dispose();
				}
					
				bufferg = (Graphics2D) buffer.getGraphics();
				bufferg.setBackground(shadowColor);
				bufferg.setColor(shadowColor);				
				bufferg.setComposite(AlphaComposite.DstOut);
			}
			//bufferg.setPaint(shadowColor);
			bufferg.clearRect(0, 0, resX, resY);
			
			//g.setColor(shadowColor);
			//g.fillRect(0, 0, resX, resY);
			//System.out.println(resX + ", " + resY);
			
			float dr =(float)(range * .125f);
			for(int i = 0; i < 5; i++, range += dr, dr/=2)
				bufferg.drawImage(crosshair_shadow2, (int)(p.x-size*range), (int)(p.y-size*range),  (int)(p.x+size*range), (int)(p.y+size*range), 0,0, crosshair_shadow2.getWidth(), crosshair_shadow2.getHeight(), null);
			g.drawImage(buffer,0,0,resX, resY, 0,0,buffer.getWidth(), buffer.getHeight(), null);
			g.setColor(Color.BLACK);
			g.setFont(GameWindow.getGameFont().deriveFont(Font.PLAIN, size/2));
	    	
			g.drawString(Integer.toString(((Sniper) t).getBullets()), (int)(p.x + size * .75), (int)(p.y + size *.75));
		}
		////////////
	}
	public static void drawEntityScreen(Graphics g,GameState gs,  Entity t, int initialX, int initialY, int size){
		drawEntity(g,gs, t, getRenderPosition(t.getX(), t.getY(),initialX,initialY,size), size);
	}
	
	public static void drawLobby(Graphics g, List<Pair<PlayerCommander, Role>>players,  int width, int height) {
		//drawQR(g);
		int i = 1;
		int dx = width /5;
		for(Pair<PlayerCommander, Role> p : players){
			drawPlayerLobby(g, p, width, height, dx * i,(int)(height *.4));
			i++;
		}
		
	}
	
	public static void drawPlayerLobby(Graphics g, Pair<PlayerCommander, Role>player,  int resX, int resY, int x, int y){
		int size = Math.min(resX, resY) /6;
		int offset = size / 16;
		
		g.setColor(shadowColor);
		
		g.fillRect(x-size+ offset, y-size+ offset, size*2 , size*2 );
		g.setColor(Color.WHITE);
		g.fillRect(x-size, y-size, size*2, size*2 );
		if(player.second == Role.ACTOR)
		g.drawImage(sheepportrait, x-size, y-size, x+size, y+size, 0,0,sheepportrait.getWidth(), sheepportrait.getHeight(), null);
		else if(player.second == Role.SNIPER)
			g.drawImage(crosshair, x-size, y-size, x+size, y+size, 0,0,crosshair.getWidth(), crosshair.getHeight(), null);
		else g.drawImage(qr, x-size, y-size, x+size, y+size, 0,0,qr.getWidth(), qr.getHeight(), null);
		g.setColor(shadowColor);
		
		g.setFont(GameWindow.getGameFont().deriveFont(Font.PLAIN, (int)(size*.75)));
		String text = "P" + player.first.getNumber();
		
		Rectangle2D rect = g.getFontMetrics().getStringBounds(text, g);
    	int stringLen = (int)rect.getWidth();
    	int stringHeight = (int)rect.getHeight();
		
    	g.drawString(text, x - stringLen/2 +offset, (int)(y + size + stringHeight*1.25 + offset));
    	
    	if(player.second == Role.NONE)
    	g.setColor(playerColors[player.first.getNumber()].darker().darker());
    	else g.setColor(playerColors[player.first.getNumber()]);
    	
		g.drawString(text, x - stringLen/2 , (int)(y + size + stringHeight*1.25));
	
	}
	public static void drawQR(Graphics g){
		g.drawImage(qr,0, 0, null);
	}
	public static void drawTileCorners(Graphics g, GameState gs, int tilex, int tiley, Point p, int size){
		if(gs.getMap().getTile(tilex, tiley).getType() == Tile.TileType.WALL){
			boolean displayedBottom = false;
			boolean displayedLeft = false;
			boolean displayedRight = false;
			boolean displayedTop= false;
			if(isFreeTile(gs, tilex, tiley+1)){
				displayedBottom = true;
			}
			if(isFreeTile(gs, tilex-1, tiley)){
				displayedLeft = true;
			}
			if(isFreeTile(gs, tilex+1, tiley)){
				displayedRight = true;
			}
			if(isFreeTile(gs, tilex, tiley-1)){
				displayedTop= true;
			}
			if( (isFreeTile(gs, tilex-1, tiley-1)&&!(displayedTop|| displayedLeft)|| (displayedTop&&displayedLeft))){
				displayTileComponent(g, topleft, p, size );
				if(displayedLeft){
					displayTileComponent(g, topleftleftnowall, p, size );
				}
			}
			if( (isFreeTile(gs, tilex+1, tiley-1)&&!(displayedTop || displayedRight)|| (displayedTop && displayedRight))){
				displayTileComponent(g, topright, p, size );
				if(displayedRight){
					displayTileComponent(g, toprightrightnowall, p, size );
				}
			}
			if( (isFreeTile(gs, tilex+1, tiley+1) &&!(displayedBottom || displayedRight) || (displayedBottom && displayedRight))){
				displayTileComponent(g, bottomright, p, size );
				if(displayedBottom)
					displayTileComponent(g, bottomrightextra, p, size );
			}
			if( (isFreeTile(gs, tilex-1, tiley+1)&&!(displayedBottom || displayedLeft) || (displayedBottom && displayedLeft) )){
				displayTileComponent(g, bottomleft, p, size );
				if(displayedBottom)
					displayTileComponent(g, bottomleftextra, p, size );
			}
		}
	}
	
	public static void drawTileFloor(Graphics g, GameState gs, int tilex, int tiley, Point p, int size){
		displayTileComponent(g, floor, p, size);
		if(gs.getMap().getTile(tilex, tiley).getType() == Tile.TileType.FLOOR){
			int hash = (tilex * tiley) + (tilex * tilex) + (tiley * tiley);
			hash *= date.hashCode();
			//hash = new Random(hash).nextInt();
			//System.out.println(hash % 15);
			if(hash % 15 == 1)
				displayTileComponent(g, rocks1, p, size);
			if(hash % 15 == 2)
				displayTileComponent(g, rocks2, p, size);
			if(hash % 15 == 3)
				displayTileComponent(g, rocks3, p, size);
			if(hash % 15 == 4)
				displayTileComponent(g, rocks4, p, size);
		}
		/*else if(gs.getMap().getTile(tilex, tiley).getType() == Tile.TileType.EXIT){
			if(gs.getPlayer() != null && gs.getPlayer().isArmed() && gs.getLiveDragons() == 0)
				displayTileComponent(g, exitOpen, p, size);
			else displayTileComponent(g, exitClosed, p, size);
		}*/
		if(!isFreeTile(gs, tilex-1, tiley)){
			boolean drawShadowtop = isFreeTile(gs, tilex-1, tiley-1);
			boolean drawShadowbottom = (isFreeTile(gs, tilex, tiley+1) &&!(isFreeTile(gs, tilex-1, tiley+1) || isFreeTile(gs, tilex, tiley)) || (isFreeTile(gs, tilex-1, tiley+1) && isFreeTile(gs, tilex, tiley)));//isFreeTile(gs, tilex+1, tiley-1);
			if(drawShadowtop)
				displayTileComponent(g, shadowtop, p, size);
			if(drawShadowbottom)
				displayTileComponent(g, shadowbottom, p, size);
		}
		
	}
	public static void drawTileScreenCorners(Graphics g, GameState gs, int tilex, int tiley, int initialX, int initialY, int size){
		drawTileCorners(g, gs, tilex, tiley, getRenderPosition(tilex, tiley ,initialX,initialY,size),size);
	}
	public static void drawTileScreenFloor(Graphics g, GameState gs, int tilex, int tiley, int initialX, int initialY, int size){
		drawTileFloor(g, gs, tilex, tiley, getRenderPosition(tilex, tiley ,initialX,initialY,size),size);
	}
	public static void drawTileScreenWalls(Graphics g, GameState gs, int tilex, int tiley, int initialX, int initialY, int size){
		drawTileWalls(g, gs, tilex, tiley, getRenderPosition(tilex, tiley ,initialX,initialY,size),size);
	}
	public static void drawTileWalls(Graphics g, GameState gs, int tilex, int tiley, Point p, int size){
		if(gs.getMap().getTile(tilex, tiley).getType() == Tile.TileType.WALL){
			boolean topFree = isFreeTile(gs, tilex, tiley-1);
			boolean bottomFree = isFreeTile(gs, tilex, tiley+1);
			boolean rightFree = isFreeTile(gs, tilex+1, tiley);
			boolean leftFree = isFreeTile(gs, tilex-1, tiley);
			//displayTileComponent(g, floor, p, size );
			if(!topFree){
				displayTileComponent(g, blacktop, p, size );
			}
			if(!leftFree){
				displayTileComponent(g, blackleft, p, size );
			}
			if(!rightFree){
				displayTileComponent(g, blackright, p, size );
			}
			if(!rightFree && !topFree){
				displayTileComponent(g, blacktopright, p, size );
			}
			if(!leftFree && !topFree){
				displayTileComponent(g, blacktopleft, p, size );
			}
			if(!bottomFree){
				displayTileComponent(g, blackbottom, p, size );
			}
			if(!bottomFree && ! rightFree){
				displayTileComponent(g, blackbottomright, p, size );
			}
			if(!bottomFree && ! leftFree){
				displayTileComponent(g, blackbottomleft, p, size );
			}
			
			displayTileComponent(g, blackdefault, p, size );
		
			if(isFreeTile(gs, tilex, tiley+1)){
				displayTileComponent(g, bottom, p, size );
				int hash = (tilex * tiley) + (tilex * tilex) + (tiley * tiley)+1;
				hash *= date.hashCode() * date.hashCode();
				//hash = new Random(hash).nextInt();
				if(hash % 7 == 1)
					displayTileComponent(g, walldecoration1, p, size);
				if(hash % 7 == 2)
					displayTileComponent(g, walldecoration2, p, size);
				if(hash % 7 == 3)
					displayTileComponent(g, walldecoration3, p, size);
				if(hash % 7 == 4)
					displayTileComponent(g, walldecoration4, p, size);
			}
			if(isFreeTile(gs, tilex-1, tiley)){
				displayTileComponent(g, left, p, size );
			}
			if(isFreeTile(gs, tilex, tiley-1)){
				displayTileComponent(g, top, p, size );
			}
			if(isFreeTile(gs, tilex+1, tiley)){
				displayTileComponent(g, right, p, size );
				p.x += size-.125*size;
				displayTileComponent(g, shadowside, p, size);
			}
		}
	}
	public static BufferedImage getPortrait() {
		if(!imagesLoaded)
			loadImages();
		return sheepportrait;
	}
	
	
	private static PriorityQueue<Entity> getPrintQueue(GameState gs) {
		Comparator<Entity> comparator = new Entity.EntityPositionComparator();
		PriorityQueue<Entity> out = new PriorityQueue<Entity>(comparator);
		List<Entity> ents = new LinkedList<Entity>(); 
		ents.addAll(gs.getEntities());
		for(int i = 0; i < ents.size(); i++){
			if(ents.get(i) instanceof Sniper){
				ents.remove(i);
				i--;
			}
		}
		out.addAll(ents);
		return out;
	}

	public static Point getRenderPosition(float f, float g,int initialX, int initialY, int size){
		return new Point((int)(f*size + initialX), (int)(g*size + initialY));
	}

	public static boolean isFreeTile(GameState g, int tilex, int tiley){
		Tile t = g.getMap().getTile(tilex, tiley);
		if(t == null)
			return false;
		if(t.getType() == Tile.TileType.WALL)
			return false;
		return true;
	}
	
	public static boolean loadImages(){
		if(imagesLoaded)
			return true;
		try{
			
			Field[] fields = GameDraw.class.getDeclaredFields();
			for(Field f : fields){
				if(f.getType().equals(BufferedImage.class) && f.getAnnotation(DontLoad.class) == null){
					f.set(null, ImageIO.read(GameDraw.class.getResourceAsStream("images/" + f.getName() + ".png")));
				}
			}
		} 
		catch(IOException | IllegalArgumentException | IllegalAccessException e){
			System.out.println("exiting");
			System.exit(0);
			return false;
		}	
		int shadowColor = new Color(255, 255, 255, 75).getRGB();
		int transparentColor = crosshair_shadow2.getRGB(0, 0);
		for(int y = 0; y < crosshair_shadow2.getHeight(); y++){
			for(int x = 0; x < crosshair_shadow2.getWidth(); x++){
				if(crosshair_shadow2.getRGB(x, y) != transparentColor){
					crosshair_shadow2.setRGB(x, y, shadowColor);
				}
			}
		}
		
		
		imagesLoaded = true;
		return true;
	}
	
	public static void setRes(int x, int y){
		resX = x;
		resY = y;
	}
	public static void setScreenShake(boolean s) {
		screenShake = s;
	}
	
	public static void writeQR() throws WriterException{
		QRCodeWriter writer = new QRCodeWriter();
		qr = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
		Graphics2D g1 = (Graphics2D) qr.getGraphics();
		BitMatrix bm = writer.encode(CommunicationManager.getInstance().connectionInfo(), BarcodeFormat.QR_CODE, 100, 100);
		for(int y = 0; y < bm.getHeight(); y++){
			for(int x = 0; x < bm.getWidth(); x++){
				if(bm.get(x, y))
					g1.setColor(Color.BLACK);
				else g1.setColor(Color.WHITE);
				g1.fillRect(x, y, 1,1);
			}
		}

	}
	
	private GameDraw(){}
}
