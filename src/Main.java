import grasp.Grasp;
import utils.Console;
import utils.Instance;
import utils.Solution;

public class Main {
	public static void main(String[] args) {
		//App controller
		Instance instance = Instance.getInstance();
	
		//Welcome
		Console.log("Welcome");
		Console.log("Before any algorithm you need to provide an instance");
		instance.read();
		instance.setIterMax();
		instance.setAlfa();
	
		//Main menu
		int choice = 0;
		do {
		Console.log("0 - close");
		Console.log("1 - new instance");
		Console.log("2 - grasp");
		Console.log("Make a choice: ");
		choice = Console.readInt();
		switch(choice) {
		case 0:
			System.exit(0);
			break;
		case 1:
			instance.read();
			break;
		case 2:
			Solution graspS = new Grasp().run(instance.getS());
			instance.setS(graspS);
			break;
		}
		}while(choice != 0);
	}
	
}
