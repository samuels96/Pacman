package tddMan.Character;

public class PlayerCharacter extends GameCharacter {
	private Integer points;

	private Boolean hasSuperPower;
	private Integer superPowerDuration;

	PlayerCharacter(Integer xPos, Integer yPos){
		super(xPos, yPos);
		points = 0;
		hasSuperPower = false;
		superPowerDuration = 0;
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

	public Integer GetPoints() {
		return points;
	}

	public void AddPoints(Integer points) {
		this.points += points;
	}
}
