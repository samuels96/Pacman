package tddMan.Course;

import tddMan.Block.Block;
import tddMan.Block.BlockFactory;
import tddMan.Block.BlockInteractionStatus;
import tddMan.Block.BlockInteractiveInterface;
import tddMan.Block.TeleportBlockObj;

public class Course {
	//╔╗ ╚╝ ═ ╦╩ ╞ ╡ ╨╥ ■ ║ ╩╦ ┬┴
	private final static String courseLayout = String.join("\n"
			,"xxxxxxxxxxxxxxxxxxxxxxxxxxx" //0
			,"x............║............x" //1
			,"xS╓┬╖.╓┬┬┬┬╖.║.╓┬┬┬┬╖.╓┬╖Sx" //2
			,"x.╙┴╜.╙┴┴┴┴╜.╨.╙┴┴┴┴╜.╙┴╜.x" //3
			,"x.........................x" //4
			,"x.╞═╡.╥.╞════╦════╡.╥.╞═╡.x" //5
			,"x.....╨......║......╨.....x" //6
			,"x┬┬┬╖.╔════╡ ╨ ╞════╗.╓┬┬┬x" //7
			,"x■■■→.║             ║.←■■■x" //8
			,"┴┴┴┴╜.╨ ╔══╡   ╞══╗ ╨.╙┴┴┴┴" //9
			,"<    .  ║■■     ■■║  .    >" //10
			,"┬┬┬┬╖.╥ ╚═════════╝ ╥.╓┬┬┬┬" //11
			,"x■■■→.║             ║.←■■■x" //12
			,"x┴┴┴╜.╨ ╞════╦════╡ ╨.╙┴┴┴x" //13
			,"x............║............x" //14
			,"x.╞═╗.╞════╡.╨.╞════╡.╔═╡.x" //15
			,"xS..║........ ........║..Sx" //16
			,"x═╡.╨.╥.╞════╦════╡.╥.╨.╞═x" //17
			,"x.....║......║......║.....x" //18
			,"x.╞═══╩════╡.╨.╞════╩═══╡.x" //19
			,"x.........................x" //20
			,"xxxxxxxxxxxxxxxxxxxxxxxxxxx" //21
			);
	//0-9     0123456789
	//10-19             0123456789
	//20-27          		      0123456


	private final static char courseRows = 22;
	private final static char courseCols = 27;

	private static Block[][] course = new Block[courseRows][courseCols];

	public void InitCourse() {
		String courseLayoutWithoutNl = courseLayout.replace("\n", "");
		for(int row = 0; row < courseRows; row++) {
			int offset = courseCols*row;
			for(int col = 0; col < courseCols; col++) {
				course[row][col] = BlockFactory.CreateBlockFromCharAtPos( 
										courseLayoutWithoutNl.charAt(col+offset), col, row);
			}
		}
		
		TeleportBlockObj leftSideTeleport = (TeleportBlockObj)course[10][0].BlockObject;
		TeleportBlockObj rightSideTeleport = (TeleportBlockObj)course[10][26].BlockObject;
		
		leftSideTeleport.SetTeleportTarget(rightSideTeleport);
		rightSideTeleport.SetTeleportTarget(leftSideTeleport);
	}
	
	public Course(){
		InitCourse();
	}

	public Boolean IsInSpawnArea(Integer xPos, Integer yPos){
		if((yPos == 9 || yPos == 10) && (xPos >= 8 && xPos <= 18))
			return true;
		return false;
	}

	public Block GetBlockFromCourse(Integer xPos, Integer yPos) {
		return course[yPos][xPos];
	}

	public void ForcePlaceObjectOnCourseBlock(BlockInteractiveInterface bObj) {
		course[bObj.GetYPos()][bObj.GetXPos()].ReplaceObject(bObj);
	}

	public BlockInteractionStatus.Status PlaceObjectOnCourseBlock(BlockInteractiveInterface bObj) {
		try {
			return course[bObj.GetYPos()][bObj.GetXPos()].PlaceObject(bObj);
		}
		catch (Exception e){
			return BlockInteractionStatus.Status.ILLEGAL_INTERACTION;
		}
	}
	
	public Block[][] GetCourse(){
		return course;
	}

	public Block[] GetFlatCourse(){
		Block[] retList = new Block[courseRows*courseCols];
		int i = 0;
		for(int row = 0; row < courseRows; row++) {
			for(int col = 0; col < courseCols; col++) {
				retList[i++] = course[row][col];
			}
		}
		return retList;
	}
}
