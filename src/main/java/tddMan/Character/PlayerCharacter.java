package tddMan.Character;

import java.awt.image.BufferedImage;

import tddMan.Block.BlockGraphicsResources;
import tddMan.Block.BlockGraphicsStrategy;
import tddMan.Movement.Direction;

public class PlayerCharacter extends GameCharacter {
	private Integer points;
	private Integer lives;

	private Integer spawnXPos;
	private Integer spawnYPos;

	private Boolean hasSuperPower;
	private Integer superPowerDuration;

	private Boolean liveLostReset;

	PlayerCharacter(Integer xPos, Integer yPos){
		super(xPos, yPos);

		spawnXPos = xPos;
		spawnYPos = yPos;
		points = 0;
		lives = 3;
		
		hasSuperPower = false;
		superPowerDuration = 0;

		liveLostReset = false;

		Graphics = new BlockGraphicsStrategy(){
			protected int animationFrame;

			@Override
			public BufferedImage DetermineAndReturnImg() {
				if(animationFrame >= 2)
					animationFrame = 0;

				return BlockGraphicsResources.imgDict_pac.get(currDir).get(animationFrame++);
			}

			@Override
			public void SetImg(Object imgSource){
				try{
				}
				catch(Exception e){}
			}
		};
	}

	public Boolean ResetIfLiveLost(){
		if(liveLostReset){
			this.xPos = spawnXPos;
			this.yPos = spawnYPos;

			hasSuperPower = false;
			superPowerDuration = 0;
			liveLostReset = false;
			currDir = Direction.NONE;

			return true;
		}
		return false;
	}
	
	public Boolean HasSuperPower() {
		return hasSuperPower;
	}
	
	public void ActivateSuperPower() {
		hasSuperPower = true;
		superPowerDuration = 50;
	}
	
	public void DecreaseSuperPowerDuration() {
		if(hasSuperPower) {
			if(superPowerDuration <= 0) {
				superPowerDuration = 0;
				hasSuperPower = false;
			}
			else
			{
				superPowerDuration -= 1;
			}
		}
	}

	public void DecreaseLives(){
		lives--;
		liveLostReset = true;
	}

	public Boolean HasLivesLeft(){
		return lives > 0;
	}

	
	public Integer GetPoints() {
		return points;
	}

	public void AddPoints(Integer points) {
		this.points += points;
	}
}
