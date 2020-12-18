package tddMan.Fruit;

import java.awt.image.BufferedImage;

import tddMan.Block.BlockGraphicsStrategy;
import tddMan.Block.BlockInteractiveInterface;

public class Fruit implements BlockInteractiveInterface {
	public static enum FruitType { Cherry, Apple };

	protected BlockGraphicsStrategy Graphics;

	private Integer xPos, yPos;

	private Integer points;
	
	Fruit(Integer xPos, Integer yPos, Integer points){
		this.xPos = xPos;
		this.yPos = yPos;
		this.points = points;

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

	public Integer GetPoints() {
		return points;
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
