package tddMan.Fruit;

public class CherryFruit extends Fruit {
	private final static CherryFruit instance = new CherryFruit(0, 0);
	public static Boolean Spawnable = true; 

	CherryFruit(Integer xPos, Integer yPos){
		super(xPos, yPos, 10);
	}

	public static CherryFruit GetInstance(){
		return instance;
	}
}
