package tddMan.Character;

import java.awt.image.BufferedImage;
import tddMan.Block.BlockGraphicsStrategy;
import tddMan.Block.BlockInteractiveInterface;
import tddMan.Movement.Direction;

public class GameCharacter implements BlockInteractiveInterface {
	protected BlockGraphicsStrategy Graphics;

	protected Integer xPos;
	protected Integer yPos;

	protected Direction currDir;

	protected BlockInteractiveInterface overlayedBlockObject;

	GameCharacter(Integer xPos, Integer yPos){
		this.xPos = xPos;
		this.yPos = yPos;

		currDir = Direction.NONE;

		Graphics = new BlockGraphicsStrategy(){
			protected BufferedImage img;

			@Override
			public BufferedImage DetermineAndReturnImg() {
				return img;
			}

			@Override
			public void SetImg(Object imgSource){
				try{
					img = img.getClass().cast(imgSource);
				}
				catch(Exception e){}
			}
		};
	}

	public BufferedImage GraphicsGetImg(){
		return Graphics.DetermineAndReturnImg();
	}

	public void GraphicsSetImg(Object imgSource){
		Graphics.SetImg(imgSource);
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

	public void SetCurrentDirection(Direction dir){
		currDir = dir;
	}

	public Direction GetCurrentDirection(){
		return currDir;
	}
}

