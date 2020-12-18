package tddMan.Character;

import java.util.ArrayList;
import java.util.Iterator;

import tddMan.Game;
import java.awt.image.BufferedImage;

import tddMan.Block.BlockGraphicsResources;
import tddMan.Block.BlockGraphicsStrategy;
import tddMan.Movement.Direction;

public class GhostCharacter extends GameCharacter {
	private GhostType type;
	private Integer spawnXPos, spawnYPos;
	private GhostBehaviorStrategy ghostBehavior;
	private Boolean outOfSpawnArea;
	private Boolean isRespawning;
	private Integer respawnDuration;
	private ArrayList<GhostCharacter> overlayedGhosts = new ArrayList<GhostCharacter>();
	public GhostCharacter OverlayingGhost;


	GhostCharacter(GhostType type, Integer xPos, Integer yPos){
		super(xPos, yPos);

		this.type = type;
		spawnXPos = xPos;
		spawnYPos = yPos;

		ghostBehavior = new NormalGhostBehavior();
		currDir = Direction.NONE;
		outOfSpawnArea = false;
		isRespawning = false;
		OverlayingGhost = null;

		Graphics = new BlockGraphicsStrategy(){
			private Integer animationFrame = 0;

			@Override
			public BufferedImage DetermineAndReturnImg() {
				if(animationFrame >= 1)
					animationFrame = 0;

				if(ghostBehavior.getClass() == FleeingGhostBehavior.class && outOfSpawnArea){
					return BlockGraphicsResources.imgArr_ghostFlashing.get(animationFrame++);
				}
				else
					return BlockGraphicsResources.imgDict_ghost.get(GetGhostType()).get(currDir).get(animationFrame++);
			}

			@Override
			public void SetImg(Object imgSource){
				try{
				}
				catch(Exception e){}
			}
		};
	}

	public Iterator<GhostCharacter> GetOverlayedGhostIterator(){
		return new GhostIterator();
	}

	private class GhostIterator implements Iterator<GhostCharacter> {
		int index = 0;

		@Override
		public boolean hasNext() {
			if(index < GetOverlayGhostsStack().size()) 
				return true;
			return false;
		}

		@Override
		public GhostCharacter next() {
			if(this.hasNext())
				return GetOverlayGhostsStack().get(index++);
			return null;
		}


	}

	private GhostCharacter getHeadOverlayGhost(){
		if(OverlayingGhost == null){
			return this;
		}
		else
			return OverlayingGhost.getHeadOverlayGhost();
	}

	private void setOverlayGhost(GhostCharacter ghost){
		if(ghost != this){
			OverlayingGhost = ghost;
		}
	}

	public GhostCharacter GetHeadOverlayGhost(){
		if(OverlayingGhost == null){
			return this;
		}
		else
			return OverlayingGhost.getHeadOverlayGhost();
	}

	private void addOverlayGhost(GhostCharacter ghost){
		if(overlayedGhosts.contains(ghost) == false)
		{
			overlayedGhosts.add(ghost);
			ghost.setOverlayGhost(this);
		}
	}

	public void AddOverlayGhost(GhostCharacter ghost) {
		getHeadOverlayGhost().addOverlayGhost(ghost);
	}

	private void removeFromOverlayGhost(GhostCharacter ghost){
		if(overlayedGhosts.remove(ghost) == true)
		{
			ghost.OverlayingGhost = null;
		}
	}

	public void RemoveFromOverlayGhost(){
		if(OverlayingGhost != null)
			getHeadOverlayGhost().removeFromOverlayGhost(this);
	}

	private ArrayList<GhostCharacter> getOverlayGhosts(){
		return overlayedGhosts;
	}

	public ArrayList<GhostCharacter> GetOverlayGhosts(){
		return getOverlayGhosts();
	}

	public ArrayList<GhostCharacter> GetOverlayGhostsStack(){
		ArrayList<GhostCharacter> ghostArr = new ArrayList<GhostCharacter>();
		if(this.getHeadOverlayGhost() != null){
			ghostArr.addAll(this.getHeadOverlayGhost().getOverlayGhosts());
			ghostArr.add(this.getHeadOverlayGhost());
		}
		else
		{
			ghostArr.addAll(getOverlayGhosts());
			ghostArr.add(this);
		}
		return ghostArr;
	}

	public ArrayList<GhostCharacter> GetOverlayGhostsExcludingThisIncludingOverlaying(){
		ArrayList<GhostCharacter> ghostArr = new ArrayList<GhostCharacter>();
		ghostArr.addAll(getOverlayGhosts());
		ghostArr.add(this.GetHeadOverlayGhost());
		ghostArr.remove(this);
		return ghostArr;
	}

	public void ResetOverlayGhosts(){
		if(overlayedGhosts.size() >= 1){
			GhostCharacter newOverlayGh = overlayedGhosts.remove(0);
			newOverlayGh.OverlayingGhost = null;
			for(GhostCharacter gh : overlayedGhosts){
				newOverlayGh.addOverlayGhost(gh);
			}
		}
		else{
			for(GhostCharacter gh : overlayedGhosts){
				gh.OverlayingGhost = null;
			}
		}
		overlayedGhosts.clear();
	}

	public void DecreaseRespawnDuration() {
		if(isRespawning){
			if(respawnDuration <= 0) {
				respawnDuration = 0;
				Respawn();
			}
			else
			{
				respawnDuration -= 1;
			}
		}
	}

	public Boolean IsRespawning(){
		return isRespawning;
	}

	public void InstantRespawn(){
		currDir = Direction.NONE;

		this.xPos = spawnXPos;
		this.yPos = spawnYPos;

		outOfSpawnArea = false;
		isRespawning = false;
	}

	public void ActivateRespawn(){
		this.xPos = spawnXPos;
		this.yPos = spawnYPos;

		currDir = Direction.NONE;

		outOfSpawnArea = false;
		isRespawning = true;
		respawnDuration = 25;
	}

	public GhostType GetGhostType(){
		return type;
	}
	
	public GhostBehaviorStrategy GetBehaviorStrategy() {
		return this.ghostBehavior;
	}
	public void SetBehaviorStrategy(GhostBehaviorStrategy behavior) {
		if(behavior != ghostBehavior){
			ghostBehavior = behavior;
		}
	}
	
	public void Respawn() {
		isRespawning = false;
	}
	
	public Boolean isOutOfSpawnArea() {
		return outOfSpawnArea.equals(true);
	}
	
	public void WentOutOfSpawnArea() {
		outOfSpawnArea = true;
	}
	
	public void SetNextDirection(Game game){
		currDir = ghostBehavior.DetermineNextDirection(game, this);
	}


}

