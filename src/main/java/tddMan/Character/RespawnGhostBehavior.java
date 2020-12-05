package tddMan.Character;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import tddMan.Game;
import tddMan.Movement.Direction;
import tddMan.Movement.Movement;

public class RespawnGhostBehavior implements GhostBehaviorStrategy {
	public Direction DetermineNextDirection(Game game, GhostCharacter character){
        Direction nextDir = Direction.NONE;

		ArrayList<Direction> possibleDirections = Movement.PossibleDirectionsForCharacterToMakeMove(game.GetCourse(), character);
        possibleDirections.remove(Direction.DOWN);
        possibleDirections.remove(Direction.UP);
        
        if(possibleDirections.size() >= 1)
            nextDir = possibleDirections.get(new Random().nextInt(possibleDirections.size()));

        return nextDir;
	}
}

