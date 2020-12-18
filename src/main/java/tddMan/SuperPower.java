package tddMan;

import java.awt.image.BufferedImage;

import tddMan.Block.BlockGraphicsResources;
import tddMan.Block.BlockGraphicsStrategy;
import tddMan.Block.BlockInteractiveInterface;

public class SuperPower implements BlockInteractiveInterface {
	private BlockGraphicsStrategy Graphics;
	private Integer xPos, yPos;

	public SuperPower(Integer xPos, Integer yPos){
		this.xPos = xPos;
		this.yPos = yPos;

		Graphics = new BlockGraphicsStrategy(){
			protected BufferedImage img;

			@Override
			public BufferedImage DetermineAndReturnImg() {
				return BlockGraphicsResources.img_superPower;
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

}
