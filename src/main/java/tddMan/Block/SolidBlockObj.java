package tddMan.Block;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SolidBlockObj implements BlockInteractiveInterface {
	private BlockGraphicsStrategy Graphics;
	private Integer xPos, yPos;

	SolidBlockObj(Integer xPos, Integer yPos){
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
					img = (BufferedImage)imgSource;
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
