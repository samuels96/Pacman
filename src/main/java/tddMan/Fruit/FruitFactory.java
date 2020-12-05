package tddMan.Fruit;

public class FruitFactory {
	public static AppleFruit CreateApple(Integer xPos, Integer yPos) {
		return new AppleFruit(xPos, yPos);
	}

	public static CherryFruit CreateCherry(Integer xPos, Integer yPos) {
		CherryFruit instance = CherryFruit.GetInstance();
		instance.SetXPos(xPos);
		instance.SetYPos(yPos);
		CherryFruit.Spawnable = false;

		return instance;
	}
}
