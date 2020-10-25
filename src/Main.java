
import grasp.Grasp;
import knapsack.Instance;
import knapsack.Solution;
import utils.Console;
import vnd.VND;

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
		Console.log("2 - new number of iterations");
		Console.log("3 - new alfa");
		Console.log("4 - Run Grasp");
		Console.log("5 - Run VND");
		Console.log("6 - Print S_Star");
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
			instance.setIterMax();
			break;
		case 3:
			instance.setAlfa();
			break;
		case 4:
			Solution graspS = new Grasp().run(instance.getS());
			instance.setS(graspS);
			break;
		case 5:
			Solution vndS = new VND().run(instance.getS());
			instance.setS(vndS);
			break;
		case 6:
			Console.log(instance.s_star);
			break;
		}
		}while(choice != 0);
	}
	
}
