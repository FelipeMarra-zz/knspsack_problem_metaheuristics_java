
import grasp.Grasp;
import grasp.Grasp.WithPR;
import knapsack.Instance;
import knapsack.Solution;
import test.GraspVsGraspPr.GraspVsGraspPr;
import utils.Console;
import vnd.VND;

public class Main {
	public static void main(String[] args) {
		// App controller
		final Instance instance = Instance.getInstance();

		// Welcome
		Console.log("Welcome");

		// Main menu
		int choice = 0;
		do {
			Console.log("0 - close");
			Console.log("1 - new instance");
			Console.log("2 - new number of iterations");
			Console.log("3 - new alfa");
			Console.log("4 - Run Grasp");
			Console.log("5 - Run VND");
			Console.log("6 - Print S_Star");
			Console.log("7 - Run Tests");
			Console.log("Make a choice: ");
			choice = Console.readInt();
			switch (choice) {
				case 0:
					System.exit(0);
					break;
				case 1:
					instance.read(null);
					break;
				case 2:
					instance.setIterMax();
					break;
				case 3:
					instance.setAlfa();
					break;
				case 4:
					Solution graspS = new Grasp().run(instance.getS(), WithPR.BOTH);
					instance.setS(graspS);
					break;
				case 5:
					Solution vndS = new VND().run(instance.getS());
					instance.setS(vndS);
					break;
				case 6:
					Console.log(instance.s_star.getSolution());
					Console.log("FO: " + instance.s_star.getFo());
					break;
				case 7:
					testsMenu();
					break;
			}
		} while (choice != 0);
	}

	private static void testsMenu(){
		int choice = 0;
		do{
			Console.log("0 - close");
			Console.log("1 - Grasp Vs Grasp-Pr");
			choice = Console.readInt();
			switch (choice) {
				case 0:
					return;
				case 1:
				GraspVsGraspPr.run();
				break;
			}
		} while (choice != 0);
	}

}
