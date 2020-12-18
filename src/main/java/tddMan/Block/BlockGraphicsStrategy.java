package tddMan.Block;
import java.awt.image.BufferedImage;

public interface BlockGraphicsStrategy {
    public BufferedImage DetermineAndReturnImg();
    public void SetImg(Object imgSource);
}
