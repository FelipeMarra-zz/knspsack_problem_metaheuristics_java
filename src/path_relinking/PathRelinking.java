package path_relinking;

import java.util.ArrayList;

import knapsack.Instance;
import knapsack.Solution;

public class PathRelinking {
	// Controllers
	final Instance instance = Instance.getInstance();

	public enum Direction {
		FORWARD(1), BACKWORD(-1);

		public int directionValue;

		Direction(int valor) {
			directionValue = valor;
		}
	}

	public Solution run(Solution startS, Solution guideS, Direction direction) {
		Solution pathS = new Solution();

		return pathS;
	}
	
	public Solution runOnEliteSet(ArrayList<Solution> eliteSet, Direction direction) {
		Solution pathS = new Solution();

		return pathS;
	}
}
