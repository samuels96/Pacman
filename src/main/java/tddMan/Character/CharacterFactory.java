package tddMan.Character;

public class CharacterFactory {
	public static PlayerCharacter CreatePlayer(Integer xPos, Integer yPos) {
		return new PlayerCharacter(xPos, yPos);
	}

	public static GhostCharacter CreateGhost(GhostType type, Integer xPos, Integer yPos) {
		return new GhostCharacter(type, xPos, yPos);
	}

}