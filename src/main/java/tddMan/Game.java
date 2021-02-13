package tddMan;

import java.awt.EventQueue;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.w3c.dom.events.Event;

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
import tddMan.Course.Course;
import tddMan.Movement.Direction;
import tddMan.Movement.Movement;
import tddMan.SuperPower.SuperPower;

public class Game {
	public final static Game instance = new Game();

	private Course course;
	private PlayerCharacter player;
	Integer highscore;
	private GameGraphics graphics;

	public static Direction playerDirection = Direction.NONE;

	public volatile static Boolean stop = false;
	public volatile static Boolean pause = false;

	private Timer graphicsRepaintTimer = new Timer();
	private Timer playerMoveTimer = new Timer();
	private Timer ghostMoveTimer = new Timer();
	private Timer cherryFruitTimer = new Timer();


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

	public static void Reset() {
		playerDirection = Direction.NONE;
		stop = false;
		pause = false;
	}

	public void LaunchGraphics() {
		gameLoopThread.start();

		EventQueue.invokeLater(new Runnable() {

			public void run() {

				graphics.setVisible(true);
			}
		});
	}

	private class gameLoop implements Runnable {

		Game game;

		NormalGhostBehavior normalGhostBehavior = new NormalGhostBehavior();
		FleeingGhostBehavior fleeingGhostBehavior = new FleeingGhostBehavior();
		RespawnGhostBehavior respawnGhostBehavior = new RespawnGhostBehavior();

		GhostCharacter Blinky = CharacterFactory.CreateGhost(GhostType.Blinky, 11, 10);
		GhostCharacter Inky = CharacterFactory.CreateGhost(GhostType.Inky, 12, 10);
		GhostCharacter Clyde = CharacterFactory.CreateGhost(GhostType.Clyde, 14, 10);
		GhostCharacter Pinky = CharacterFactory.CreateGhost(GhostType.Pinky, 15, 10);

		public gameLoop(Game game) {
			this.game = game;
		}

		private void ghostMove(GhostCharacter ghost){
			if (player.HasLivesLeft() && !stop) {
				if (pause != true) {
					if (ghost.IsRespawning()) {
						ghost.SetBehaviorStrategy(respawnGhostBehavior);
					} else if (player.HasSuperPower()) {
						ghost.SetBehaviorStrategy(fleeingGhostBehavior);
					} else {
						ghost.SetBehaviorStrategy(normalGhostBehavior);
					}

					ghost.SetNextDirection(game);

					switch (ghost.GetCurrentDirection()) {
						case LEFT:
							Movement.TurnLeft(course, ghost);
							break;
						case RIGHT:
							Movement.TurnRight(course, ghost);
							break;
						case UP:
							Movement.TurnUp(course, ghost);
							break;
						case DOWN:
							Movement.TurnDown(course, ghost);
							break;
						case NONE:
							Movement.NoTurn(course, ghost);
							break;
					}
				}
			}
		}

		public void run() {
			player.SetCurrentDirection(Direction.NONE);

			GhostCharacter[] ghosts = { Blinky, Inky, Clyde, Pinky };

			TimerTask graphicsRepaintTimerTask = new TimerTask() {
				public void run() {
						graphics.repaint();
				}
			};

			TimerTask playerMoveTimerTask = new TimerTask(){
				public void run() {
					if (player.HasLivesLeft() && !stop) {
							if (player.ResetIfLiveLost() == true) {
								for (GhostCharacter ghost : ghosts) {
									course.PlaceObjectOnCourseBlock(ghost.GetOverlayedBlockObject());
									ghost.InstantRespawn();
									Movement.NoTurn(course, ghost);
								}
								player.SetCurrentDirection(Direction.NONE);
								Movement.NoTurn(course, player);
								stop = true;
								try {
									Thread.sleep(2000);
									stop = false;
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

							if (pause != true) {
								player.SetCurrentDirection(playerDirection);

								switch (playerDirection) {
									case LEFT:
										Movement.TurnLeft(course, player);
										break;
									case RIGHT:
										Movement.TurnRight(course, player);
										break;
									case UP:
										Movement.TurnUp(course, player);
										break;
									case DOWN:
										Movement.TurnDown(course, player);
										break;
									default:
										Movement.NoTurn(course, player);
										break;
							}
						}
					}
				}
			};


			TimerTask ghostMoveTimerTask = new TimerTask(){
				public void run() {
					for (GhostCharacter gh : ghosts) {
						ghostMove(gh);
					}
				}
			};

			TimerTask cherryFruitTimerTask = new TimerTask(){
				public void run() {
					if (CherryFruit.Spawnable) {
						Integer xPos = ((int) (Math.random() * 100) % 27);
						Integer yPos = ((int) (Math.random() * 100) % 22);

						while ((course.GetBlockFromCourse(xPos, yPos).BlockObject.getClass() != SuperPower.class)
								&& (course.IsInSpawnArea(xPos, yPos) == false) && (course.PlaceObjectOnCourseBlock(
										FruitFactory.CreateCherry(xPos, yPos)) != Status.NO_COLLISION)) {
							xPos = (Integer) ((int) (Math.random() * 100) % 27);
							yPos = (Integer) ((int) (Math.random() * 100) % 22);
						}
					}

				}
			};

			graphicsRepaintTimer.schedule(graphicsRepaintTimerTask, 0, 60);
			playerMoveTimer.schedule(playerMoveTimerTask, 0, 100);
			ghostMoveTimer.schedule(ghostMoveTimerTask, 0, 120);
			cherryFruitTimer.schedule(cherryFruitTimerTask, 0, 3000);
		}
	}

	public void ResetInstance() {
		graphics.dispose();
		graphicsRepaintTimer.cancel(); graphicsRepaintTimer = new Timer();
		playerMoveTimer.cancel(); playerMoveTimer = new Timer();
		ghostMoveTimer.cancel(); ghostMoveTimer = new Timer();
		cherryFruitTimer.cancel(); cherryFruitTimer = new Timer();
		player.SetCurrentDirection(Direction.NONE);
		Stop();
		InitInstance();
		try {
			Start();
		} catch (Exception e) {}
		LaunchGraphics();
	}

	public void InitInstance(){
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