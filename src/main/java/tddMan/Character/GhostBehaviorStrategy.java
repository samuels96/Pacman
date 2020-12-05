package tddMan.Character;

import tddMan.Game;
import tddMan.Movement.Direction;

public interface GhostBehaviorStrategy {
	public Direction DetermineNextDirection(Game game, GhostCharacter ghost);
}
