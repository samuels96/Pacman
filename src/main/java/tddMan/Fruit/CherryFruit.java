package tddMan.Fruit;

import java.awt.image.BufferedImage;

import tddMan.Block.BlockGraphicsResources;
import tddMan.Block.BlockGraphicsStrategy;

public class CherryFruit extends Fruit {
	private final static CherryFruit instance = new CherryFruit(0, 0);
	public static Boolean Spawnable = true; 

	private CherryFruit(Integer xPos, Integer yPos){
		super(xPos, yPos, 10);

		Graphics = new BlockGraphicsStrategy(){
			@Override
			public BufferedImage DetermineAndReturnImg() {
				return BlockGraphicsResources.img_cherryFruit;
			}

			@Override
			public void SetImg(Object imgSource){
				try{
				}
				catch(Exception e){}
			}
		};
	}

	public static CherryFruit GetInstance(){
		return instance;
	}
}
