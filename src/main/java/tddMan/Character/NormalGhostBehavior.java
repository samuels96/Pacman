package tddMan.Character;

import java.util.ArrayList;
import java.util.Random;

import tddMan.Game;
import tddMan.Movement.Direction;
import tddMan.Movement.Movement;

public class NormalGhostBehavior implements GhostBehaviorStrategy {
	
	private Direction getCounterDirection(Direction dir) {
		switch(dir) {
			case LEFT:
				return Direction.RIGHT;
			case DOWN:
				return Direction.UP;
			case RIGHT:
				return Direction.LEFT;
			case UP:
				return Direction.DOWN;
			default:
				return Direction.NONE;
		}
	}

	public Direction DetermineNextDirection(Game game, GhostCharacter character){
		
		Direction currDir = character.GetCurrentDirection();
		Direction counterDirection = getCounterDirection(currDir);
		Direction nextDir = Direction.NONE;
		
		Integer xPos = character.GetXPos();
		Integer yPos = character.GetYPos();

		Integer PlayerXPos = game.GetPlayer().GetXPos();
		Integer PlayerYPos = game.GetPlayer().GetYPos();
		
		ArrayList<Direction> possibleDirections = Movement.PossibleDirectionsForCharacterToMakeMove(game.GetCourse(), character);

		//Prevent from going back to spawn
		if(yPos == 8 && (xPos >= 11 && xPos <= 15))
				possibleDirections.remove(Direction.DOWN);

		if(character.isOutOfSpawnArea().equals(false)) {
			if(yPos <= 8 || yPos >= 12) {
				character.WentOutOfSpawnArea();
			}
			else {
				if(yPos == 9 || yPos == 10) {
					if(xPos == 11)
						return Direction.RIGHT;
					else if(xPos == 15)
						return Direction.LEFT;
					else
						return Direction.UP;
				}
			}
		}
		
		possibleDirections.remove(counterDirection);
		
		if(Math.abs(PlayerXPos - xPos) >= Math.abs(PlayerYPos - yPos)) {
			if(PlayerXPos >= xPos) 
				nextDir = Direction.RIGHT;
			else if(PlayerXPos <= xPos)
				nextDir = Direction.LEFT;
		}
		else{
			if(PlayerYPos >= yPos) 
				nextDir = Direction.DOWN;
			else if(PlayerYPos <= yPos)
				nextDir = Direction.UP;
		}
		
		if(possibleDirections.contains(nextDir) != true) {
			if(PlayerXPos >= xPos) 
				nextDir = Direction.RIGHT;
			else if(PlayerXPos <= xPos)
				nextDir = Direction.LEFT;
		}

		if(possibleDirections.contains(nextDir) != true) {

			if(possibleDirections.size() >= 1)
				nextDir = possibleDirections.get(new Random().nextInt(possibleDirections.size()));
			else
				nextDir = counterDirection;
		}
		
	return nextDir;
	}

}
