package tddMan.Block;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import tddMan.Graphics.GameGraphics;;

public class TeleportBlockObj implements BlockInteractiveInterface {
	private BlockGraphicsStrategy Graphics;
	private TeleportBlockObj teleportTarget;
	private Integer xPos, yPos;
		
	TeleportBlockObj(Integer xPos, Integer yPos){
		this.xPos = xPos;
		this.yPos = yPos;

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
	
	public void SetTeleportTarget(TeleportBlockObj target) {
		teleportTarget = target;
	}

	public TeleportBlockObj GetTeleportTarget() {
		return teleportTarget;
	}

}
