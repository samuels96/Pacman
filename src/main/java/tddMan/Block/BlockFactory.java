package tddMan.Block;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import tddMan.SuperPower;
import tddMan.Block.Block.BlockType;
import tddMan.Fruit.FruitFactory;

public class BlockFactory {
	private static HashMap<Character, BufferedImage> solidBlockImgMap = new HashMap<Character, BufferedImage>();

	//╔╗ ╚╝ ═ ╦╩ ╞ ╡ ╨╥ ■ ║ ╩╦ ┬┴
	public static void loadSolidBlockImg() {
		try {

			solidBlockImgMap.put(Character.valueOf('╙'), ImageIO.read(new File("graphicResources/block/a1.png")));
			solidBlockImgMap.put(Character.valueOf('╓'), ImageIO.read(new File("graphicResources/block/a2.png")));
			solidBlockImgMap.put(Character.valueOf('╖'), ImageIO.read(new File("graphicResources/block/a3.png")));
			solidBlockImgMap.put(Character.valueOf('╜'), ImageIO.read(new File("graphicResources/block/a4.png")));
			solidBlockImgMap.put(Character.valueOf('┬'), ImageIO.read(new File("graphicResources/block/b1.png")));
			solidBlockImgMap.put(Character.valueOf('┴'), ImageIO.read(new File("graphicResources/block/b2.png")));
			solidBlockImgMap.put(Character.valueOf('←'), ImageIO.read(new File("graphicResources/block/b3.png")));
			solidBlockImgMap.put(Character.valueOf('→'), ImageIO.read(new File("graphicResources/block/b4.png")));
			solidBlockImgMap.put(Character.valueOf('╞'), ImageIO.read(new File("graphicResources/block/c1.png")));
			solidBlockImgMap.put(Character.valueOf('╥'), ImageIO.read(new File("graphicResources/block/c2.png")));
			solidBlockImgMap.put(Character.valueOf('╡'), ImageIO.read(new File("graphicResources/block/c3.png")));
			solidBlockImgMap.put(Character.valueOf('╨'), ImageIO.read(new File("graphicResources/block/c4.png")));
			solidBlockImgMap.put(Character.valueOf('╦'), ImageIO.read(new File("graphicResources/block/e1.png")));
			solidBlockImgMap.put(Character.valueOf('╩'), ImageIO.read(new File("graphicResources/block/e2.png")));
			solidBlockImgMap.put(Character.valueOf('║'), ImageIO.read(new File("graphicResources/block/d1.png")));
			solidBlockImgMap.put(Character.valueOf('═'), ImageIO.read(new File("graphicResources/block/d2.png")));
			solidBlockImgMap.put(Character.valueOf('■'), ImageIO.read(new File("graphicResources/block/f.png")));
			solidBlockImgMap.put(Character.valueOf('x'), ImageIO.read(new File("graphicResources/block/f.png")));
			solidBlockImgMap.put(Character.valueOf('<'), ImageIO.read(new File("graphicResources/block/f.png")));
			solidBlockImgMap.put(Character.valueOf('>'), ImageIO.read(new File("graphicResources/block/f.png")));
			solidBlockImgMap.put(Character.valueOf('╔'), ImageIO.read(new File("graphicResources/block/g1.png")));
			solidBlockImgMap.put(Character.valueOf('╗'), ImageIO.read(new File("graphicResources/block/g2.png")));
			solidBlockImgMap.put(Character.valueOf('╝'), ImageIO.read(new File("graphicResources/block/g3.png")));
			solidBlockImgMap.put(Character.valueOf('╚'), ImageIO.read(new File("graphicResources/block/g4.png")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static FreeBlockObj CreateFreeBlockObjAtPos(Integer xPos, Integer yPos){
				return new FreeBlockObj(xPos, yPos);
	}

	public static SolidBlockObj CreateSolidBlockObjAtPos(Integer xPos, Integer yPos){
				return new SolidBlockObj(xPos, yPos);
	}

	public static Block CreateBlockAtPos(BlockType blockType, Integer xPos, Integer yPos){
		switch(blockType) {
			case FREE :
				return new FreeBlock(xPos, yPos);
			case SOLID :
				return new SolidBlock(xPos, yPos);
			default:
				return null;
		}
	}

	public static Block CreateBlockFromCharAtPos(char ch, Integer xPos, Integer  yPos){
		Block block;
		switch(ch) {
			case ' ' :
				return new FreeBlock(xPos, yPos);
			case '.' :
				block = new FreeBlock(xPos, yPos);
				block.ReplaceObject(FruitFactory.CreateApple(xPos, yPos));
				return block;
			case 'S' :
				block = new FreeBlock(xPos, yPos);
				block.ReplaceObject(new SuperPower(xPos, yPos));
				return block;
			case '<': case '>':
				block = new FreeBlock(xPos, yPos);
				block.ReplaceObject(new TeleportBlockObj(xPos, yPos));
				return block;
			default:
				block = new SolidBlock(xPos, yPos);
				block.BlockObject.GraphicsSetImg(solidBlockImgMap.get(Character.valueOf(ch)));
				return block;
		}
	}
}
