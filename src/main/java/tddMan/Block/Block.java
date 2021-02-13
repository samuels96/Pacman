package tddMan.Block;
import java.awt.image.BufferedImage;

import tddMan.Character.PlayerCharacter;

public class Block {
	public static enum BlockType { FREE, SOLID};
	public BlockBehaviorStrategy BlockBehavior = null;
	public BlockInteractiveInterface BlockObject = null;
	
	public BlockType Type;

	public void ReplaceObject(BlockInteractiveInterface bObj) {
			BlockObject = bObj;
	}

	public BlockInteractionStatus.Status PlaceObject(BlockInteractiveInterface bObj) {

		BlockInteractionStatus.Status status = BlockBehavior.OnCollision(BlockObject, bObj);

		if(status != BlockInteractionStatus.Status.ILLEGAL_INTERACTION &&
		   status != BlockInteractionStatus.Status.SOLID_COLLISION &&
		   status != BlockInteractionStatus.Status.GHOST_EAT_PLAYER &&
		   status != BlockInteractionStatus.Status.PLAYER_EAT_GHOST 
		   )
		{
			BlockObject = bObj;
		}

		return status;
		
	}
}

class FreeBlock extends Block {
	FreeBlock(Integer xPos, Integer yPos){
		Type = BlockType.FREE;

		BlockBehavior = new FreeBlockBehavior();
		BlockObject = new FreeBlockObj(xPos, yPos);
	}
}

class SolidBlock extends Block {
	public BufferedImage Img;

	SolidBlock(Integer xPos, Integer yPos){
		Type = BlockType.SOLID;

		BlockBehavior = new SolidBlockBehavior();
		BlockObject = new SolidBlockObj(xPos, yPos); 
	}
}