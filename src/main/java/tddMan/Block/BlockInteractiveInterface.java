package tddMan.Block;

import java.awt.image.BufferedImage;

public interface BlockInteractiveInterface {
	public Integer GetXPos();
	public Integer GetYPos();
	public void SetXPos(Integer newPos);
	public void SetYPos(Integer newPos);
	public BufferedImage GraphicsGetImg();
	public void GraphicsSetImg(Object imgSource);
}
