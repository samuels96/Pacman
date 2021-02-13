package tddMan.Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import tddMan.Character.GhostType;
import tddMan.Movement.Direction;

public class BlockGraphicsResources {
    public static final BlockGraphicsResources instance = new BlockGraphicsResources();
	public static BufferedImage img_blackBlock;
	public static BufferedImage img_blueBlock;
	public static BufferedImage img_pacDefault;
	public static Hashtable<Direction, ArrayList<BufferedImage>> imgDict_pac;
    public static Hashtable<GhostType, Hashtable<Direction, ArrayList<BufferedImage>>> imgDict_ghost; 
	public static ArrayList<BufferedImage> imgArr_ghostFlashing;
	public static BufferedImage img_fruit;
    public static BufferedImage img_cherryFruit;
    public static BufferedImage img_superPower;
    public static BufferedImage img_notImplemented;

    private static void ghostImageLoad(GhostType ghostType) throws IOException {
                String ghostName = ghostType.toString().toLowerCase();
                String rootFolder = "graphicResources/ghosts/" + ghostName + "/";

                imgDict_ghost.put(ghostType, new Hashtable<Direction, ArrayList<BufferedImage>>());

                for(Direction dir : Direction.values()){
                    if(dir == Direction.NONE)
                        imgDict_ghost.get(ghostType).put(dir, new ArrayList<BufferedImage>(Arrays.asList(
                            ImageIO.read(new File(rootFolder + ghostName + ".png")),
                            ImageIO.read(new File(rootFolder + ghostName + ".png"))
                        )));
                    else
                        imgDict_ghost.get(ghostType).put(dir, new ArrayList<BufferedImage>(Arrays.asList(
                            ImageIO.read(new File(rootFolder + ghostName + "1_" + dir.toString().toLowerCase() + ".png")),
                            ImageIO.read(new File(rootFolder + ghostName + "2_" + dir.toString().toLowerCase() + ".png"))
                        )));
                }
            }

    public BlockGraphicsResources() {
        imgDict_pac = new Hashtable<Direction, ArrayList<BufferedImage>>();
        imgDict_ghost = new Hashtable<GhostType, Hashtable<Direction, ArrayList<BufferedImage>>>();
        imgArr_ghostFlashing = new ArrayList<BufferedImage>();
        try {
            img_blackBlock = ImageIO.read(new File("graphicResources/blackBlock.png"));
            img_blueBlock = ImageIO.read(new File("graphicResources/blueBlock.png"));
            img_pacDefault = ImageIO.read(new File("graphicResources/pac/pac1.png"));

            for(GhostType ghost : GhostType.values()){
                ghostImageLoad(ghost);
            }

            for(Direction dir : Direction.values()){
                if(dir == Direction.NONE)
                    imgDict_pac.put(dir, new ArrayList<BufferedImage>(Arrays.asList(
                        img_pacDefault,
                        img_pacDefault,
                        img_pacDefault
                    )));
                else
                    imgDict_pac.put(dir, new ArrayList<BufferedImage>(Arrays.asList(
                        img_pacDefault,
                        ImageIO.read(new File("graphicResources/pac/pac2_" + dir.toString().toLowerCase() + ".png")),
                        ImageIO.read(new File("graphicResources/pac/pac3_" + dir.toString().toLowerCase() + ".png"))
                    )));
            }

            imgArr_ghostFlashing.add(ImageIO.read(new File("graphicResources/ghosts/flash1.png")));
            imgArr_ghostFlashing.add(ImageIO.read(new File("graphicResources/ghosts/flash2.png")));

            img_fruit = ImageIO.read(new File("graphicResources/fruit.png"));
            img_cherryFruit = ImageIO.read(new File("graphicResources/cherryFruit.png"));
            img_superPower = ImageIO.read(new File("graphicResources/superPower.png"));
            img_notImplemented = ImageIO.read(new File("graphicResources/ni.png"));
        } catch (IOException e) {
        }
    }
}