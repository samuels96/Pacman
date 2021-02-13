package tddMan.Character;

import java.util.ArrayList;
import java.util.Random;

import tddMan.Game;
import tddMan.Course.Course;
import tddMan.Movement.Direction;
import tddMan.Movement.Movement;

public class FleeingGhostBehavior implements GhostBehaviorStrategy {
	
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

		if(character.isOutOfSpawnArea().equals(false)) {
			if(yPos <= 8 || yPos >= 11) {
				character.WentOutOfSpawnArea();
			}
			else {
				return Direction.UP;
			}
		}
		else {
			if(yPos == 8) {
				if(xPos >= 12 && xPos <= 15) {
					possibleDirections.remove(Direction.DOWN);
				}
			}
		}
		
		possibleDirections.remove(counterDirection);
		
		if(Math.abs(PlayerXPos - xPos) >= Math.abs(PlayerYPos - yPos)) {
			if(PlayerXPos >= xPos) {
				nextDir = Direction.LEFT;
			}
			else if(PlayerXPos <= xPos){
				nextDir = Direction.RIGHT;
			}
		}
		if(possibleDirections.contains(nextDir) != true)
		{
			if(PlayerYPos >= yPos) {
				nextDir = Direction.UP;
			}
			else if(PlayerYPos <= yPos){
				nextDir = Direction.DOWN;
			}
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
