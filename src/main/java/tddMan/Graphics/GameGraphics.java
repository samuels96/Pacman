package tddMan.Graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.io.File;
import java.io.IOException;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.awt.TextField;
import java.awt.image.BufferedImage;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.plaf.FontUIResource;

import tddMan.Course;
import tddMan.Game;
import tddMan.SuperPower;
import tddMan.Block.Block;
import tddMan.Block.BlockInteractiveInterface;
import tddMan.Block.FreeBlockObj;
import tddMan.Block.SolidBlockObj;
import tddMan.Character.GhostCharacter;
import tddMan.Character.GhostType;
import tddMan.Character.PlayerCharacter;
import tddMan.Fruit.AppleFruit;
import tddMan.Movement.Direction;

class MyCanvas extends JComponent {

	private Game game;
	private Course course;
	private Block[] courseBlocks;

	private BufferedImage img_blackBlock;
	private BufferedImage img_blueBlock;
	private BufferedImage img_pacDefault;
	private Hashtable<Direction, ArrayList<BufferedImage>> imgDict_pac = new Hashtable<Direction, ArrayList<BufferedImage>>();
	private Hashtable<GhostType, Hashtable<Direction, ArrayList<BufferedImage>>> imgDict_ghost = new Hashtable<GhostType, Hashtable<Direction, ArrayList<BufferedImage>>>();
	private ArrayList<BufferedImage> imgArr_ghostFlashing = new ArrayList<BufferedImage>();
	private BufferedImage img_fruit;
	private BufferedImage img_superPower;
	private BufferedImage img_notImplemented;

	private volatile ArrayList<BufferedImage> pacAnimationImg = new ArrayList<BufferedImage>();

	private volatile int pacAnimation = 0;
	private int blinkyAnimation = 0;
	private int inkyAnimation = 0;
	private int pinkyAnimation = 0;
	private int clydeAnimation = 0;

	private void ghostImageLoad(Hashtable<GhostType, Hashtable<Direction, ArrayList<BufferedImage>>> imgDict, GhostType ghostType) throws IOException {

			String ghostName = ghostType.toString().toLowerCase();
			String rootFolder = "graphicResources/ghosts/" + ghostName + "/";

			imgDict.put(ghostType, new Hashtable<Direction, ArrayList<BufferedImage>>());

			for(Direction dir : Direction.values()){
				if(dir == Direction.NONE)
					imgDict.get(ghostType).put(dir, new ArrayList<BufferedImage>(Arrays.asList(
						ImageIO.read(new File(rootFolder + ghostName + ".png")),
						ImageIO.read(new File(rootFolder + ghostName + ".png"))
					)));
				else
					imgDict.get(ghostType).put(dir, new ArrayList<BufferedImage>(Arrays.asList(
						ImageIO.read(new File(rootFolder + ghostName + "1_" + dir.toString().toLowerCase() + ".png")),
						ImageIO.read(new File(rootFolder + ghostName + "2_" + dir.toString().toLowerCase() + ".png"))
					)));
			}
		}

	public MyCanvas(Game game) {
		this.game = game;
		this.course = game.GetCourse();
		this.courseBlocks = this.course.GetFlatCourse();
		try {
			img_blackBlock = ImageIO.read(new File("graphicResources/blackBlock.png"));
			img_blueBlock = ImageIO.read(new File("graphicResources/blueBlock.png"));
			img_pacDefault = ImageIO.read(new File("graphicResources/pac/pac1.png"));

			for(GhostType ghost : GhostType.values()){
				ghostImageLoad(imgDict_ghost, ghost);
			}

			for(Direction dir : Direction.values()){
				if(dir == Direction.NONE)
					imgDict_pac.put(dir, new ArrayList<BufferedImage>(Arrays.asList(
						img_pacDefault,
						img_pacDefault,
						img_pacDefault
					)));
				else
					imgDict_pac.put(dir, new ArrayList<BufferedImage>(Arrays.asList(
						img_pacDefault,
						ImageIO.read(new File("graphicResources/pac/pac2_" + dir.toString().toLowerCase() + ".png")),
						ImageIO.read(new File("graphicResources/pac/pac3_" + dir.toString().toLowerCase() + ".png"))
					)));
			}

			imgArr_ghostFlashing.add(ImageIO.read(new File("graphicResources/ghosts/flash1.png")));
			imgArr_ghostFlashing.add(ImageIO.read(new File("graphicResources/ghosts/flash2.png")));

			img_fruit = ImageIO.read(new File("graphicResources/fruit.png"));
			img_superPower = ImageIO.read(new File("graphicResources/superPower.png"));
			img_notImplemented = ImageIO.read(new File("graphicResources/ni.png"));

			pacAnimationImg = imgDict_pac.get(Direction.NONE);
		} catch (IOException e) {
		}
	}

	public void DrawBlock(Graphics g, BlockInteractiveInterface blockObj) {
		Graphics2D g2d = (Graphics2D) g;

		if (blockObj.getClass().equals(SolidBlockObj.class)) {
			g2d.drawImage(blockObj.GetImg(), blockObj.GetXPos() * 32, blockObj.GetYPos() * 32, null);
		} else {
			if (blockObj.getClass().equals(AppleFruit.class)) {
				g2d.drawImage(img_fruit, blockObj.GetXPos() * 32, blockObj.GetYPos() * 32, null);
			}

			else if (blockObj.getClass().equals(PlayerCharacter.class)) {
				pacAnimationImg = imgDict_pac.get(Game.playerDirection);

				if(pacAnimation >= 2)
					pacAnimation = 0;
				else
					pacAnimation++;

				g2d.drawImage(pacAnimationImg.get(pacAnimation), blockObj.GetXPos() * 32, blockObj.GetYPos() * 32, null);
			} 
			else if (blockObj.getClass().equals(GhostCharacter.class)) {
				GhostCharacter currGhost = (GhostCharacter)blockObj;
				ArrayList<BufferedImage> imgArr;
				int animation = 0;

				if(game.GetPlayer().HasSuperPower() && currGhost.isOutOfSpawnArea()){
					imgArr = imgArr_ghostFlashing;
				}
				else
					imgArr = imgDict_ghost.get(currGhost.GetGhostType())
									.get((currGhost.GetCurrentDirection()));

				switch(currGhost.GetGhostType()){
					case Blinky:
						animation = blinkyAnimation;
						if (++blinkyAnimation > 1)
							blinkyAnimation = 0;
						break;
					case Inky:
						animation = inkyAnimation;
						if (++inkyAnimation > 1)
							inkyAnimation = 0;
						break;
					case Clyde:
						animation = clydeAnimation;
						if (++clydeAnimation > 1)
							clydeAnimation = 0;
						break;
					case Pinky:
						animation = pinkyAnimation;
						if (++pinkyAnimation > 1)
							pinkyAnimation = 0;
						break;
				}


				ArrayList<GhostCharacter> overlayedGhosts = ((GhostCharacter)blockObj).GetOverlayGhostsStack();

				int offset = 0;
				for(GhostCharacter gh : overlayedGhosts){
					if(imgArr != imgArr_ghostFlashing){
						imgArr = imgDict_ghost.get(gh.GetGhostType())
										.get((gh.GetCurrentDirection()));
					}

					switch(gh.GetCurrentDirection()){
						case RIGHT:
							g2d.drawImage(imgArr.get(animation), (gh.GetXPos() * 32) - offset, gh.GetYPos() * 32, null);
							break;
						case LEFT:
							g2d.drawImage(imgArr.get(animation), (gh.GetXPos() * 32) + offset, gh.GetYPos() * 32, null);
							break;
						case UP:
							g2d.drawImage(imgArr.get(animation), gh.GetXPos() * 32, (gh.GetYPos() * 32) + offset, null);
							break;
						case DOWN:
							g2d.drawImage(imgArr.get(animation), gh.GetXPos() * 32, (gh.GetYPos() * 32) - offset, null);
							break;
						case NONE:
							g2d.drawImage(imgArr.get(animation), offset + gh.GetXPos() * 32, offset + (gh.GetYPos() * 32), null);
							break;
					}
					offset += 4;
				}


			} else if (blockObj.getClass().equals(GhostCharacter.class)) {
				g2d.drawImage(img_notImplemented, blockObj.GetXPos() * 32, blockObj.GetYPos() * 32, null);
			} else if (blockObj.getClass().equals(SuperPower.class)) {
				g2d.drawImage(img_superPower, blockObj.GetXPos() * 32, blockObj.GetYPos() * 32, null);
			}
			else{
				g2d.drawImage(null, blockObj.GetXPos() * 32, blockObj.GetYPos() * 32, null);
			}
		}
	}
    

	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, getWidth(), getHeight());

		for(Block bl : courseBlocks){
			DrawBlock(g, bl.BlockObject);
		}

		g.setFont(new Font("TimesRoman", Font.PLAIN, 25)); 
		g.setColor(Color.WHITE);
		g.drawString("Score: " + game.GetPoints(), 0, 25);
	}
}

class CustomKeyListener implements KeyListener{
	
	private Game game;

	public void keyTyped(KeyEvent e) {
	}
	
	public CustomKeyListener(Game game) {
		this.game = game;
	}

	public void keyPressed(KeyEvent e) {
		switch(e.getExtendedKeyCode()) {
			case KeyEvent.VK_LEFT:
				game.ChangeDirection(Direction.LEFT);
				break;
			case KeyEvent.VK_DOWN:
				game.ChangeDirection(Direction.DOWN);
				break;
			case KeyEvent.VK_RIGHT:
				game.ChangeDirection(Direction.RIGHT);
				break;
			case KeyEvent.VK_UP:
				game.ChangeDirection(Direction.UP);
				break;
			case KeyEvent.VK_P:
				if(game.pause)
					game.Unpause();
				else
					game.Pause();
				break;
		}

	}

	public void keyReleased(KeyEvent e) {
	}
}


public class GameGraphics extends JFrame {

	public GameGraphics(Game game) {
		initUI(game);
	}

	private void initUI(Game game) {

		MyCanvas mc = new MyCanvas(game);

		TextField textField  = new TextField(10);
		textField.addKeyListener(new CustomKeyListener(game));
		add(textField);
		add(mc);

		setSize(1000, 800);
		setLocationRelativeTo(null);
        setVisible(true);  
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
