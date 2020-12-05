package tddMan;

public class Application {
	
	public static void main(String args[])  
	{ 

		Game TDDMan = new Game();

		try {
			TDDMan.Start();
			TDDMan.LaunchGraphics();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
