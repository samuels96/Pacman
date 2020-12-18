package tddMan.Graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.TextField;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;
import javax.swing.JFrame;

import tddMan.Course;
import tddMan.Game;
import tddMan.Block.Block;
import tddMan.Block.BlockInteractiveInterface;
import tddMan.Character.GhostCharacter;
import tddMan.Movement.Direction;

class MyCanvas extends JComponent {

	private Game game;
	private Course course;
	private Block[] courseBlocks;

	public MyCanvas(Game game) {
		this.game = game;
		this.course = game.GetCourse();
		this.courseBlocks = this.course.GetFlatCourse();
	}

	public void DrawBlock(Graphics g, BlockInteractiveInterface blockObj) {
		Graphics2D g2d = (Graphics2D) g;

		if (blockObj.getClass().equals(GhostCharacter.class)) {
			int offset = 0;

			Iterator<GhostCharacter> ghostStackIterator = ((GhostCharacter)blockObj).GetOverlayedGhostIterator();
			while(ghostStackIterator.hasNext()){
				GhostCharacter gh = ghostStackIterator.next();
				switch(gh.GetCurrentDirection()){
					case RIGHT:
						g2d.drawImage(gh.GraphicsGetImg(), (gh.GetXPos() * 32) - offset, gh.GetYPos() * 32, null);
						break;
					case LEFT:
						g2d.drawImage(gh.GraphicsGetImg(), (gh.GetXPos() * 32) + offset, gh.GetYPos() * 32, null);
						break;
					case UP:
						g2d.drawImage(gh.GraphicsGetImg(), gh.GetXPos() * 32, (gh.GetYPos() * 32) + offset, null);
						break;
					case DOWN:
						g2d.drawImage(gh.GraphicsGetImg(), gh.GetXPos() * 32, (gh.GetYPos() * 32) - offset, null);
						break;
					case NONE:
						g2d.drawImage(gh.GraphicsGetImg(), offset + gh.GetXPos() * 32, offset + (gh.GetYPos() * 32), null);
						break;
				}
				offset += 4;
			}
		}
		else
			g2d.drawImage(blockObj.GraphicsGetImg(), blockObj.GetXPos() * 32, blockObj.GetYPos() * 32, null);
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
