package tddMan.Block;

import java.util.HashMap;

import tddMan.SuperPower.SuperPower;
import tddMan.Character.GameCharacter;
import tddMan.Character.GhostCharacter;
import tddMan.Character.PlayerCharacter;
import tddMan.Fruit.Fruit;

interface Action {
    public BlockInteractionStatus.Status Execute(Class attemptBlockObjClass, BlockInteractiveInterface attemptBlockObj, BlockInteractiveInterface newBlockObj);
}

public class FreeBlockBehavior implements BlockBehaviorStrategy {

	private static HashMap<String, Action> actionMap = new HashMap<String, Action>();
	public FreeBlockBehavior(){
		actionMap.put(GhostCharacter.class.getName(), new GhostAction());
		actionMap.put(PlayerCharacter.class.getName(), new PlayerAction());
	}

	class GhostAction implements Action{
		public BlockInteractionStatus.Status Execute(Class attemptBlockObjClass, BlockInteractiveInterface attemptBlockObj, BlockInteractiveInterface newBlockObj){
				if(isCollisionWithFruit(attemptBlockObjClass))
					return BlockInteractionStatus.Status.FRUIT_COLLISION;
				else if(isCollisionWithPlayer(attemptBlockObjClass))
					return onPlayerGhostCollision((PlayerCharacter)attemptBlockObj, (GhostCharacter)newBlockObj);
				else if(isFreeMove(attemptBlockObjClass))
					return BlockInteractionStatus.Status.NO_COLLISION;
				else if(isCollisionWithSolidBlock(attemptBlockObjClass)) 
					return BlockInteractionStatus.Status.SOLID_COLLISION;
				else if(isCollisionWithSuperPower(attemptBlockObjClass)) 
					return BlockInteractionStatus.Status.SUPERPOWER_COLLISION;
				else
					return BlockInteractionStatus.Status.ILLEGAL_INTERACTION;
		}
	}

	class PlayerAction implements Action{
		public BlockInteractionStatus.Status Execute(Class attemptBlockObjClass, BlockInteractiveInterface attemptBlockObj, BlockInteractiveInterface newBlockObj){
			if(isCollisionWithFruit(attemptBlockObjClass)) 
				return BlockInteractionStatus.Status.FRUIT_COLLISION;
			else if(isCollisionWithGhost(attemptBlockObjClass))
				return onPlayerGhostCollision((PlayerCharacter)newBlockObj, (GhostCharacter)attemptBlockObj);
			else if(isFreeMove(attemptBlockObjClass))
				return BlockInteractionStatus.Status.NO_COLLISION;
			else if(isCollisionWithSolidBlock(attemptBlockObjClass)) 
				return BlockInteractionStatus.Status.SOLID_COLLISION;
			else if(isCollisionWithSuperPower(attemptBlockObjClass)) 
				return BlockInteractionStatus.Status.SUPERPOWER_COLLISION;
			else
				return BlockInteractionStatus.Status.ILLEGAL_INTERACTION;
		}
	}

	private void replacePos(BlockInteractiveInterface attemptObj, BlockInteractiveInterface newObj) {
				newObj.SetXPos(attemptObj.GetXPos());
				newObj.SetYPos(attemptObj.GetYPos());
	}

	private Boolean isCollisionWithFruit(Class attemptObjClass) {
		return attemptObjClass.getSuperclass().equals(Fruit.class);
	}
	private Boolean isCollisionWithGhost(Class attemptObjClass) {
		return attemptObjClass.equals(GhostCharacter.class);
	}
	private Boolean isCollisionWithPlayer(Class attemptObjClass) {
		return attemptObjClass.equals(PlayerCharacter.class);
	}
	private Boolean isFreeMove(Class attemptObjClass) {
		return attemptObjClass.equals(FreeBlockObj.class);
	}
	private Boolean isCollisionWithSolidBlock(Class attemptObjClass) {
		return attemptObjClass.equals(SolidBlockObj.class);
	}
	private Boolean isCollisionWithSuperPower(Class attemptObjClass) {
		return attemptObjClass.equals(SuperPower.class);
	}
	private BlockInteractionStatus.Status onPlayerGhostCollision(PlayerCharacter player, GhostCharacter ghost) {
		return BlockInteractionStatus.Status.PLAYER_GHOST_COLLISION;
	}
	private BlockInteractionStatus.Status onGhostWithGhostCollision(GhostCharacter ghost1, GhostCharacter ghost2){
		if(ghost1.GetOverlayedBlockObject() != null){
			if(ghost1.GetOverlayedBlockObject().getClass().equals(PlayerCharacter.class)){
				return onPlayerGhostCollision((PlayerCharacter)ghost1.GetOverlayedBlockObject(), ghost1);
			}
		}
		else if(ghost2.GetOverlayedBlockObject() != null){
			if(ghost2.GetOverlayedBlockObject().getClass().equals(PlayerCharacter.class)){
				return onPlayerGhostCollision((PlayerCharacter)ghost2.GetOverlayedBlockObject(), ghost2);
			}
		}
		return BlockInteractionStatus.Status.SAME_OBJECT_COLLISION;
	}

	public BlockInteractionStatus.Status OnCollision(BlockInteractiveInterface attemptBlockObj, BlockInteractiveInterface newBlockObj) {
		Class attemptBlockObjClass = attemptBlockObj.getClass();
		Class newBlockObjClass = newBlockObj.getClass();

		if(newBlockObjClass == attemptBlockObjClass){
			if(newBlockObjClass == GhostCharacter.class){
				return onGhostWithGhostCollision((GhostCharacter)attemptBlockObj, (GhostCharacter)newBlockObj);
			}
			return BlockInteractionStatus.Status.SAME_OBJECT_COLLISION;
		}
		else if(newBlockObjClass.equals(FreeBlockObj.class))
			return BlockInteractionStatus.Status.NO_COLLISION;
		else if(newBlockObjClass.getSuperclass().equals(Fruit.class)) 
				return BlockInteractionStatus.Status.NO_COLLISION;
		else if(newBlockObjClass.equals(SuperPower.class) && 
			   ((attemptBlockObjClass.getSuperclass() != GameCharacter.class) || 
			   (attemptBlockObjClass.equals(TeleportBlockObj.class)))){
				return BlockInteractionStatus.Status.NO_COLLISION;
		}
		else if(newBlockObjClass.equals(TeleportBlockObj.class))
				return BlockInteractionStatus.Status.TELEPORT;
		else if(attemptBlockObjClass.equals(TeleportBlockObj.class))
				return BlockInteractionStatus.Status.TELEPORT;

		return actionMap.get(newBlockObjClass.getName()).Execute(attemptBlockObjClass, attemptBlockObj, newBlockObj);
	}
}

