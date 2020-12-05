package tddMan.Character;

import java.awt.image.BufferedImage;
import tddMan.Game;
import tddMan.Block.BlockInteractiveInterface;

public class GameCharacter implements BlockInteractiveInterface {
	private BufferedImage img;

	protected Integer xPos;
	protected Integer yPos;
	protected BlockInteractiveInterface overlayedBlockObject;

	GameCharacter(Integer xPos, Integer yPos){
		this.xPos = xPos;
		this.yPos = yPos;
	}	

	public BufferedImage GetImg(){
		return img;
	}

	public void SetImg(BufferedImage img){
		this.img = img;
	}

	public void SetOverlayedBlockObject(BlockInteractiveInterface bObj) {
		if(bObj.getClass() != this.getClass()) {
			overlayedBlockObject = bObj;
		}
		else {
			overlayedBlockObject = (((GhostCharacter)bObj).GetOverlayedBlockObject());
		}
	}
	
	public BlockInteractiveInterface GetAndResetOverlayedBlockObject() {
		BlockInteractiveInterface obj = overlayedBlockObject;
		overlayedBlockObject = null;
		return obj;
	}
	public BlockInteractiveInterface GetOverlayedBlockObject() {
		return overlayedBlockObject;
	}
	
	public Integer GetXPos() {
		return xPos;
	}

	public Integer GetYPos() {
		return yPos;
	}

	public void SetXPos(Integer newPos) {
		xPos = newPos;
	}

	public void SetYPos(Integer newPos) {
		yPos = newPos;
	}
}

