package tddMan.Block;

public class BlockInteractionStatus {
	public static enum Status {
			TELEPORT,
			SUPERPOWER_COLLISION,
			PLAYER_GHOST_COLLISION,
			PLAYER_EAT_GHOST,
			GHOST_EAT_PLAYER,
			FRUIT_COLLISION,
			NO_COLLISION,
			SOLID_COLLISION,
			ILLEGAL_INTERACTION,
			SAME_OBJECT_COLLISION
	};
}