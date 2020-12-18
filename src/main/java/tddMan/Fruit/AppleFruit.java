package tddMan.Fruit;

import java.awt.image.BufferedImage;

import tddMan.Block.BlockGraphicsResources;
import tddMan.Block.BlockGraphicsStrategy;

public class AppleFruit extends Fruit {

	public AppleFruit(Integer xPos, Integer yPos){
		super(xPos, yPos, 1);

		Graphics = new BlockGraphicsStrategy(){
			@Override
			public BufferedImage DetermineAndReturnImg() {
				return BlockGraphicsResources.img_fruit;
			}

			@Override
			public void SetImg(Object imgSource){
				try{
				}
				catch(Exception e){}
			}
		};
	}
}
