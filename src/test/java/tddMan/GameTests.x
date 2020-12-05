package tddMan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;

import tddMan.Character.GhostCharacter;
import tddMan.Fruit.AppleFruit;
import tddMan.GameState.Direction;

public class GameTests {
	
	@Test
	public void eatFruit() throws InterruptedException, IOException{
		Game game = new Game();
		game.Start();

		AppleFruit apple = new AppleFruit(13, 16);
		game.SpawnFruit(apple);
		
		Integer initialPoints = game.GetPoints();

		game.Course.TurnLeft(game.Player);

		Integer pointsAfterEatingApple = game.GetPoints();
		
		assertTrue(initialPoints + apple.GetPoints() == pointsAfterEatingApple);
	}

	@Test
	public void collideWithGhost() throws InterruptedException, IOException{
		Game game = new Game();
		game.Start();

		assertEquals(game.IsInProgress(), true);
		
		GhostCharacter ghost = new GhostCharacter(13, 16);
		game.SpawnGhost(ghost);
		
		game.Course.TurnRight(ghost);
		
		assertEquals(game.IsInProgress(), false);
	}
	
	@Test
	public void playerIllegalMove() throws InterruptedException, IOException {
		Game game = new Game();
		game.Start();
		
		Integer startingXPosition = game.Player.GetXPos();
		Integer startingYPosition = game.Player.GetYPos();
		
		Integer assumedXPosition = null;
		Integer assumedYPosition = null;
		
		assertEquals(game.IsInProgress(), true);
		
		game.Course.TurnUp(game.Player);
		assumedXPosition = startingXPosition;

		assertEquals(game.Player.GetXPos(), assumedXPosition);
	}
	
	@Test
	public void playerMoveInAllDirections() throws InterruptedException, IOException{
		Game game = new Game();
		game.Start();
		
		game.Player.SetXPos(14);
		game.Player.SetYPos(9);
		
		Integer startingXPosition = game.Player.GetXPos();
		Integer startingYPosition = game.Player.GetYPos();
		
		Integer assumedXPosition = null;
		Integer assumedYPosition = null;
		
		assertEquals(game.IsInProgress(), true);
		
		game.Course.TurnRight(game.Player);
		assumedXPosition = startingXPosition + 1;

		assertEquals(game.Player.GetXPos(), assumedXPosition);
		game.Course.TurnLeft(game.Player);

		game.Course.TurnLeft(game.Player);
		assumedXPosition = startingXPosition - 1;

		assertEquals(game.Player.GetXPos(), assumedXPosition);
		game.Course.TurnRight(game.Player);

		game.Course.TurnUp(game.Player);
		assumedYPosition = startingYPosition - 1;
		
		assertEquals(game.Player.GetYPos(), assumedYPosition);
		game.Course.TurnDown(game.Player);

		game.Course.TurnDown(game.Player);
		assumedYPosition = startingYPosition + 1;
		
		assertEquals(game.Player.GetYPos(), assumedYPosition);
		game.Course.TurnUp(game.Player);

		assertEquals(game.IsInProgress(), true);
	}
}
