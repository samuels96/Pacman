package tddMan.Block;
import java.awt.image.BufferedImage;

public class SolidBlockObj implements BlockInteractiveInterface {
	private BufferedImage img;
	private Integer xPos, yPos;

	SolidBlockObj(Integer xPos, Integer yPos){
		this.xPos = xPos;
		this.yPos = yPos;
	}

	public BufferedImage GetImg(){
		return img;
	}

	public void SetImg(BufferedImage img){
		this.img = img;
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
