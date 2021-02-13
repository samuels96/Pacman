package tddMan.Graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.awt.TextField;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComponent;
import javax.swing.JFrame;

import tddMan.Game;
import tddMan.Course.Course;
import tddMan.Block.Block;
import tddMan.Block.BlockGraphicsResources;
import tddMan.Block.BlockInteractiveInterface;
import tddMan.Character.GhostCharacter;
import tddMan.Movement.Direction;

class MyCanvas extends JComponent {

	private Game game;
	private Course course;
	private Block[] courseBlocks;

	private Boolean ghostFrameFlag = false;

	public MyCanvas(Game game) {
		this.game = game;
		this.course = game.GetCourse();
		this.courseBlocks = this.course.GetFlatCourse();
	}

	public void DrawBlock(Graphics g, BlockInteractiveInterface blockObj) {
		Graphics2D g2d = (Graphics2D) g;

		if(!ghostFrameFlag)
			ghostFrameFlag = true;
		else 
			ghostFrameFlag = false;

		if (blockObj.getClass().equals(GhostCharacter.class)) {
			int offset = 0;

			Iterator<GhostCharacter> ghostStackIterator = ((GhostCharacter)blockObj).GetOverlayedGhostIterator();
			while(ghostStackIterator.hasNext()){
				GhostCharacter gh = ghostStackIterator.next();
				switch(gh.GetCurrentDirection()){
					case RIGHT:
						g2d.drawImage(gh.GraphicsGetImg(), (gh.GetXPos() * 32) - (ghostFrameFlag ? offset : offset+6), gh.GetYPos() * 32, null);
						break;
					case LEFT:
						g2d.drawImage(gh.GraphicsGetImg(), (gh.GetXPos() * 32) + (ghostFrameFlag ? offset : offset-6), gh.GetYPos() * 32, null);
						break;
					case UP:
						g2d.drawImage(gh.GraphicsGetImg(), gh.GetXPos() * 32, (gh.GetYPos() * 32) + (ghostFrameFlag ? offset : offset-6), null);
						break;
					case DOWN:
						g2d.drawImage(gh.GraphicsGetImg(), gh.GetXPos() * 32, (gh.GetYPos() * 32) - (ghostFrameFlag ? offset : offset+6), null);
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
		g.drawString("Score: " + game.GetPoints(), 0, 730);

		g.drawString("Lives: ", 200, 730);
		for(int i = 1; i <= game.GetPlayer().GetLives(); i++){
			g.drawImage(BlockGraphicsResources.imgDict_pac.get(Direction.RIGHT).get(1), 232 + (i * 40), 705, null);
		}
		if(game.GetPlayer().GetLives() == 0){
			g.setFont(new Font("TimesRoman", Font.BOLD, 60)); 
			g.drawString("GAME OVER", 250, 360);

			g.setFont(new Font("TimesRoman", Font.PLAIN, 20)); 
			g.drawString("Press Enter To Play Again", 320, 438);
		}

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
			case KeyEvent.VK_ENTER:
				if(!game.GetPlayer().HasLivesLeft()){
					game.ResetInstance();
				}
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

		setSize(880, 780);
		setLocationRelativeTo(null);
        setVisible(true);  
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
