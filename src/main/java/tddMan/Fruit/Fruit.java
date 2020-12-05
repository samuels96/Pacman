package tddMan.Fruit;

import java.awt.image.BufferedImage;
import tddMan.Block.BlockInteractiveInterface;

public class Fruit implements BlockInteractiveInterface {
	private BufferedImage img;
	public static enum FruitType { Cherry, Apple };

	private Integer xPos, yPos;

	private Integer points;
	
	Fruit(Integer xPos, Integer yPos, Integer points){
		this.xPos = xPos;
		this.yPos = yPos;
		this.points = points;
	}

	public BufferedImage GetImg(){
		return img;
	}

	public void SetImg(BufferedImage img){
		this.img = img;
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
