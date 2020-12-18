package tddMan.Movement;

import java.util.ArrayList;
import java.util.jar.Attributes.Name;

import tddMan.Course;
import tddMan.Game;
import tddMan.Block.Block;
import tddMan.Block.Block.BlockType;
import tddMan.Block.BlockInteractionStatus.Status;
import tddMan.Block.BlockFactory;
import tddMan.Block.BlockInteractionStatus;
import tddMan.Block.BlockInteractiveInterface;
import tddMan.Block.TeleportBlockObj;
import tddMan.Fruit.*;
import tddMan.Character.GameCharacter;
import tddMan.Character.GhostCharacter;
import tddMan.Character.PlayerCharacter;

public class Movement {
	
	private static void teleport(GameCharacter character, TeleportBlockObj teleportObj) {
		character.SetXPos(teleportObj.GetTeleportTarget().GetXPos());
		character.SetYPos(teleportObj.GetTeleportTarget().GetYPos());
	}

	public static BlockInteractionStatus.Status turnHelper(Course course, GameCharacter character, Integer oldXPos, Integer oldYPos, Integer attemptXPos, Integer attemptYPos) {
		character.SetXPos(attemptXPos);
		character.SetYPos(attemptYPos);

		BlockInteractiveInterface attemptBlockObj = null;
		attemptBlockObj = course.GetBlockFromCourse(attemptXPos, attemptYPos).BlockObject;

		BlockInteractionStatus.Status status = course.PlaceObjectOnCourseBlock(character);

		if(character.getClass().equals(PlayerCharacter.class))
		{
			PlayerCharacter playerCharacter = ((PlayerCharacter)character);
			playerCharacter.DecreaseSuperPowerDuration();

			if
			(status != BlockInteractionStatus.Status.ILLEGAL_INTERACTION &&
			 status != BlockInteractionStatus.Status.SOLID_COLLISION &&
			 status != BlockInteractionStatus.Status.SAME_OBJECT_COLLISION)
			{
				BlockInteractiveInterface overlayedObj = character.GetAndResetOverlayedBlockObject();
				if(overlayedObj!= null) {
					course.PlaceObjectOnCourseBlock(overlayedObj);
				}
				else {
					BlockInteractiveInterface freeBlock = BlockFactory.CreateFreeBlockObjAtPos(oldXPos, oldYPos);
					course.PlaceObjectOnCourseBlock(freeBlock);
				}

				switch(status){
					case SUPERPOWER_COLLISION:
						playerCharacter.ActivateSuperPower();
						break;
					case FRUIT_COLLISION:
						if(attemptBlockObj.getClass().equals(CherryFruit.class)){
							CherryFruit.Spawnable = true;
						}
						playerCharacter.AddPoints(((Fruit)attemptBlockObj).GetPoints());
						break;
				    case TELEPORT:
						character.SetOverlayedBlockObject((TeleportBlockObj)attemptBlockObj);
						teleport(character, (TeleportBlockObj)attemptBlockObj);
						break;
					case PLAYER_GHOST_COLLISION:
						GhostCharacter ghost = (GhostCharacter)attemptBlockObj;

						if(playerCharacter.HasSuperPower()){
							ghost.ActivateRespawn();
							Status st = Status.PLAYER_GHOST_COLLISION;

							character.SetXPos(oldXPos);
							character.SetYPos(oldYPos);
							course.PlaceObjectOnCourseBlock(character);

							while(st == Status.PLAYER_GHOST_COLLISION){
								st = turnHelper(course, character, oldXPos, oldYPos, attemptXPos, attemptYPos);
								System.out.println(st);
							}
							return Status.PLAYER_EAT_GHOST;
						}
						playerCharacter.DecreaseLives();
						return Status.GHOST_EAT_PLAYER;
				}
			}
			else {
				character.SetXPos(oldXPos);
				character.SetYPos(oldYPos);
			}
		}
		else if(character.getClass().equals(GhostCharacter.class))
		{
			GhostCharacter ghostCharacter = (GhostCharacter)character;

			ghostCharacter.DecreaseRespawnDuration();

			if
			(status != BlockInteractionStatus.Status.ILLEGAL_INTERACTION &&
			 status != BlockInteractionStatus.Status.SOLID_COLLISION)
			{
				BlockInteractiveInterface overlayedObj = character.GetAndResetOverlayedBlockObject();
				if(overlayedObj!= null) {
					course.ForcePlaceObjectOnCourseBlock(overlayedObj);
				}

				if(attemptBlockObj.getClass().equals(PlayerCharacter.class) == false)
					character.SetOverlayedBlockObject(attemptBlockObj);

				for(GhostCharacter gh : ghostCharacter.GetOverlayGhostsExcludingThisIncludingOverlaying())
				{
					course.PlaceObjectOnCourseBlock(gh);
				}

				ghostCharacter.RemoveFromOverlayGhost();
				ghostCharacter.ResetOverlayGhosts();

				switch(status){
					case SAME_OBJECT_COLLISION:
						((GhostCharacter)attemptBlockObj).AddOverlayGhost(ghostCharacter);
						break;

				    case TELEPORT:
						if(status == BlockInteractionStatus.Status.TELEPORT) {
							teleport(character, (TeleportBlockObj)attemptBlockObj);
						}
						break;

				    case PLAYER_GHOST_COLLISION:
						if(status == BlockInteractionStatus.Status.PLAYER_GHOST_COLLISION){

							if(attemptBlockObj.getClass().equals(PlayerCharacter.class)){
								PlayerCharacter playerCharacter = (PlayerCharacter)attemptBlockObj;
								if(playerCharacter.HasSuperPower()){
									ghostCharacter.ActivateRespawn();
									Status st = Status.PLAYER_GHOST_COLLISION;
									while(st == Status.PLAYER_GHOST_COLLISION){
										st = turnHelper(course, character, oldXPos, oldYPos, attemptXPos, attemptYPos);
									}
									return Status.PLAYER_EAT_GHOST;
								}
								BlockInteractiveInterface freeBlock = BlockFactory.CreateFreeBlockObjAtPos(attemptXPos, attemptYPos);
								course.ForcePlaceObjectOnCourseBlock(freeBlock);

								playerCharacter.DecreaseLives();
							}
							return Status.GHOST_EAT_PLAYER;
						}
						break;
					}
			}
			else 
			{
				character.SetXPos(oldXPos);
				character.SetYPos(oldYPos);
			}
		}

		return status;
	}

	public static BlockInteractionStatus.Status NoTurn(Course course, GameCharacter character) {
		return course.PlaceObjectOnCourseBlock(character);
	}
	
	public static BlockInteractionStatus.Status TurnUp(Course course, GameCharacter character) {
		Integer curXPos = character.GetXPos();
		Integer curYPos = character.GetYPos();
		
		return turnHelper(course, character, curXPos, curYPos, curXPos, curYPos-1);
	}
	public static BlockInteractionStatus.Status TurnDown(Course course, GameCharacter character) {
		Integer curXPos = character.GetXPos();
		Integer curYPos = character.GetYPos();
		
		return turnHelper(course, character, curXPos, curYPos, curXPos, curYPos+1);
	}
	public static BlockInteractionStatus.Status TurnLeft(Course course, GameCharacter character) {
		Integer curXPos = character.GetXPos();
		Integer curYPos = character.GetYPos();

		return turnHelper(course, character, curXPos, curYPos, curXPos-1, curYPos);
	}
	public static BlockInteractionStatus.Status TurnRight(Course course, GameCharacter character) {
		Integer curXPos = character.GetXPos();
		Integer curYPos = character.GetYPos();

		return turnHelper(course, character, curXPos, curYPos, curXPos+1, curYPos);
	}

	public static ArrayList<Direction> PossibleDirectionsForCharacterToMakeMove(Course course, GameCharacter character){
		Integer xPos = character.GetXPos();
		Integer yPos = character.GetYPos();
		
		Block[][] courseBlocks = course.GetCourse();
		
		ArrayList<Direction> possibleDirections = new ArrayList<Direction>();
		
		try {
			if(courseBlocks[yPos][xPos-1].Type == BlockType.FREE)
				possibleDirections.add(Direction.LEFT);
			}
		catch(Exception e) {}

		try {
			if(courseBlocks[yPos+1][xPos].Type == BlockType.FREE)
				possibleDirections.add(Direction.DOWN);
			}
		catch(Exception e) {}

		try {
			if(courseBlocks[yPos][xPos+1].Type == BlockType.FREE)
				possibleDirections.add(Direction.RIGHT);
			}
		catch(Exception e) {}

		try {
			if(courseBlocks[yPos-1][xPos].Type == BlockType.FREE)
				possibleDirections.add(Direction.UP);
			}
		catch(Exception e) {}

		return possibleDirections;
	}
	
	public static Boolean CanCharacterMakeStepInDirection(Course course, GameCharacter character, String direction){
		Integer xPos = character.GetXPos();
		Integer yPos = character.GetYPos();

		Block[][] courseBlocks = course.GetCourse();
		
		if(direction == "LEFT")
			return courseBlocks[yPos][xPos-1].Type == BlockType.FREE;
		else if(direction == "DOWN")
			return courseBlocks[yPos+1][xPos].Type == BlockType.FREE;
		else if(direction == "RIGHT")
			return courseBlocks[yPos][xPos+1].Type == BlockType.FREE;
		else if(direction == "UP")
			return courseBlocks[yPos-1][xPos].Type == BlockType.FREE;

		return false;
	}

}
