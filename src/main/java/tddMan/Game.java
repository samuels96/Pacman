package tddMan;

import java.awt.EventQueue;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import tddMan.Block.BlockFactory;
import tddMan.Block.BlockInteractionStatus;
import tddMan.Block.BlockInteractiveInterface;
import tddMan.Block.BlockInteractionStatus.Status;
import tddMan.Character.CharacterFactory;
import tddMan.Character.FleeingGhostBehavior;
import tddMan.Character.GhostCharacter;
import tddMan.Character.GhostType;
import tddMan.Character.NormalGhostBehavior;
import tddMan.Character.PlayerCharacter;
import tddMan.Character.RespawnGhostBehavior;
import tddMan.Fruit.CherryFruit;
import tddMan.Fruit.Fruit;
import tddMan.Fruit.FruitFactory;
import tddMan.Graphics.GameGraphics;
import tddMan.Movement.Direction;
import tddMan.Movement.Movement;

public class Game {
	private Course course;
	private PlayerCharacter player;
	Integer highscore;
	private GameGraphics graphics;

	public static Direction playerDirection = Direction.NONE;

	public volatile static Boolean stop = false;
	public volatile static Boolean pause = false;

	Thread gameLoopThread = null;

	public Course GetCourse() {
		return this.course;
	}

	public Integer GetHighscore() {
		return highscore;
	}

	public PlayerCharacter GetPlayer() {
		return this.player;
	}

	public void ChangeDirection(Direction dir) {
		if (dir == playerDirection)
			return;

		if (Movement.CanCharacterMakeStepInDirection(course, player, dir.toString()) == true)
			playerDirection = dir;
	}

	public static void OnCollisionWithGhost() {
		stop = true;
	}

	public static void Reset() {
		playerDirection = Direction.NONE;
		stop = false;
		pause = false;
	}

	public void LaunchGraphics() {
		gameLoopThread.start();
		final Game thisGame = this;

		EventQueue.invokeLater(new Runnable() {

			public void run() {

				graphics.setVisible(true);
			}
		});
	}

	private class gameLoop implements Runnable {

		Game game;

		public gameLoop(Game game) {
			this.game = game;
		}

		public void run() {

			Status status = null;
			Status ghostStatus = null;
			Direction currDir = Direction.NONE;

			NormalGhostBehavior normalGhostBehavior = new NormalGhostBehavior();
			FleeingGhostBehavior fleeingGhostBehavior = new FleeingGhostBehavior();
			RespawnGhostBehavior respawnGhostBehavior = new RespawnGhostBehavior();

			GhostCharacter Blinky = CharacterFactory.CreateGhost(GhostType.Blinky, 11, 10);
			GhostCharacter Inky = CharacterFactory.CreateGhost(GhostType.Inky, 12, 10);
			GhostCharacter Clyde = CharacterFactory.CreateGhost(GhostType.Clyde, 14, 10);
			GhostCharacter Pinky = CharacterFactory.CreateGhost(GhostType.Pinky, 15, 10);

			/*
			Movement.TurnRight(course, Blinky);
			Movement.TurnUp(course, Blinky);
			Movement.TurnUp(course, Inky);
			Movement.TurnLeft(course, Clyde);
			Movement.TurnLeft(course, Clyde);
			Movement.TurnUp(course, Clyde);
			Movement.TurnUp(course, Clyde);
			Movement.TurnUp(course, Blinky);
			Movement.TurnUp(course, Inky);
			Movement.TurnRight(course, Clyde);
			Movement.TurnRight(course, Clyde);
			Movement.TurnRight(course, Inky);
			Movement.TurnRight(course, Blinky);
			Movement.TurnRight(course, Blinky);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			graphics.repaint();
			try {
				Thread.sleep(1000000000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			*/

			GhostCharacter[] ghosts = {Blinky, Inky, Clyde, Pinky};
			//GhostCharacter[] ghosts = {Blinky};
			
			//course.PlaceObjectOnCourseBlock(player);

			new Timer().scheduleAtFixedRate(new TimerTask()
			{
				public void run()
				{
					if(CherryFruit.Spawnable)
						while(course.PlaceObjectOnCourseBlock(FruitFactory.CreateCherry((int)((Math.random()*100) % 27), 
																			(int)((Math.random()*100) % 22))) != Status.NO_COLLISION){}

				}
			}, 3000, 3000);

			while(stop != true) {
				if(pause != true) {
					try {
						Thread.sleep(110);
					} catch (InterruptedException e) {
					}

					switch (playerDirection) {
						case LEFT:
								status = Movement.TurnLeft(course, player);
								break;
						case RIGHT:
								status = Movement.TurnRight(course, player);
								break;
						case UP:
								status = Movement.TurnUp(course, player);
								break;
						case DOWN:
								status = Movement.TurnDown(course, player);
								break;
						default:
							break;
					}
					if(status == Status.GHOST_EAT_PLAYER){
						OnCollisionWithGhost();
						break;
					}
					
					for(GhostCharacter ghost : ghosts) {
						if(ghost.IsRespawning()){
							ghost.SetBehaviorStrategy(respawnGhostBehavior);
						}
						else if(player.HasSuperPower() && ghost.isOutOfSpawnArea()) {
							ghost.SetBehaviorStrategy(fleeingGhostBehavior);
						}
						else{
							ghost.SetBehaviorStrategy(normalGhostBehavior);
						}

						ghost.SetNextDirection(game);

						switch (ghost.GetCurrentDirection()) {
							case LEFT:
									ghostStatus = Movement.TurnLeft(course, ghost);
									break;
							case RIGHT:
									ghostStatus = Movement.TurnRight(course, ghost);
									break;
							case UP:
									ghostStatus = Movement.TurnUp(course, ghost);
									break;
							case DOWN:
									ghostStatus = Movement.TurnDown(course, ghost);
									break;
							case NONE:
									ghostStatus = Movement.NoTurn(course, ghost);
									break;
							default:
								break;
						}
						if(ghostStatus == Status.GHOST_EAT_PLAYER){
							OnCollisionWithGhost();
						}
					}
				}
				graphics.repaint();
			}
		}
	}

	Game(){
		pause = true;
		stop = true;

		BlockFactory.loadSolidBlockImg();
		course = new Course();
		player = CharacterFactory.CreatePlayer(13, 16);

		graphics = new GameGraphics(this);

		gameLoopThread = new Thread(new gameLoop(this));
	}
	
	public Boolean IsInProgress() {
		return (stop != true);
	}
	
	public void Pause() {
		pause = true;
	}

	public void Unpause() {
		pause = false;
	}

	public void Stop() {
		stop = true;
		try {
			gameLoopThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void Start() throws InterruptedException, IOException {
		pause = false;
		stop = false;
		
		//course.PlaceObjectOnCourseBlock(player);
	}

	public Integer GetPoints() {
		return player.GetPoints();
	}

	
	private Boolean spawnBlockObject(BlockInteractiveInterface bObj) {
		try {
			if( course.PlaceObjectOnCourseBlock(bObj) != BlockInteractionStatus.Status.ILLEGAL_INTERACTION )
				return true;
		}
		catch (Exception e) {
			return false;
		}
		return false;
	}

	public Boolean SpawnGhost(GhostCharacter ghost) {
		return spawnBlockObject(ghost);
	}

	public Boolean SpawnFruit(Fruit fruit) {
		return spawnBlockObject(fruit);
	}
}