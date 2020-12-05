package tddMan.Block;
import java.awt.image.BufferedImage;

public interface BlockInteractiveInterface {
	public BufferedImage GetImg();
	public void SetImg(BufferedImage img);
	public Integer GetXPos();
	public Integer GetYPos();
	public void SetXPos(Integer newPos);
	public void SetYPos(Integer newPos);
}
