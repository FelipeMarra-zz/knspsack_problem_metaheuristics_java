package grasp;

import java.util.ArrayList;
import knapsack.Instance;
import knapsack.KnapsackObject;
import knapsack.Solution;
import path_relinking.PathRelinking;
import path_relinking.PathRelinking.Direction;
import utils.Console;
import utils.Numbers;
import vnd.VND;

public class Grasp {
	// Controllers
	final Instance instance = Instance.getInstance();

	// With Path Relinking
	public enum WithPR {
		NO(-1), INTENSIFICATION(1), POST_OPTIMIZATION(2), BOTH(3);

		public int pathReOption;

		WithPR(int value) {
			pathReOption = value;
		}
	}

	private int MAX_ELITE;

	private ArrayList<Solution> eliteSet = new ArrayList<Solution>();

	// Choose an elite solution to go into path relinking with the localOptimum
	private Solution chooseEliteS(Solution localOptimum) {
		if (eliteSet.size() == 0) {
			return localOptimum;
		}
		return Solution.biggerSymmetricDifference(localOptimum, eliteSet);
	}

	// Methods
	/*
	 * @param [Solution] s: The solution in which the algorithm will be applied
	 * 
	 * @param [WithPathRelinking] pathRelinking: If and how [PathRelinking] will be
	 * applied
	 * 
	 * @return the best [Solution] found
	 */
	public Solution run(Solution s, WithPR pathRelinking) {
		if (instance.instanceIsNull()) {
			return null;
		}
		Console.log("Running Grasp: ");
		// path relinking options
		boolean willRunPathRelinking = !pathRelinking.equals(WithPR.NO);

		boolean willRunIntensification = pathRelinking.equals(WithPR.BOTH)
				|| pathRelinking.equals(WithPR.INTENSIFICATION);

		boolean willRunPostOptimization = pathRelinking.equals(WithPR.BOTH)
				|| pathRelinking.equals(WithPR.POST_OPTIMIZATION);

		// Best solution
		Solution bestS = new Solution();
		bestS.setFo(-Double.MAX_VALUE);

		// Auxiliary solution
		Solution sl = new Solution();

		// Till stopping criteria (max of iterations)
		for (int i = 0; i < instance.iter_max; i++) {
			Console.log("\n #######Iteration " + (i + 1) + "#########");
			// Build partially greedy solution
			sl = greedyRandomizedConstruction();
			Console.log("Built Solution: " + instance.calculateFo(sl));

			// Apply local search to the built solution
			Solution vndS = new VND().run(sl);
			sl = vndS;
			Console.log("VND Solution: " + instance.calculateFo(sl));

			// If we will have path relinking
			if (willRunPathRelinking) {
				// Wait a minimun size for elite set
				if (willRunIntensification && eliteSet.size() >= 5) {
					Solution prS = new PathRelinking().run(sl, chooseEliteS(sl), Direction.BACKWORD);
					sl = prS;
					Console.log("PR Intensification Solution: " + prS.getFo());
				}

				new PathRelinking().updateAPoolOfSolutions(sl, eliteSet, MAX_ELITE);
			}

			// Update best solution
			// Console.log("IS BETTER " + (instance.calculateFo(sl) > bestS.getFo()) + " " +
			// instance.calculateFo(sl) + " > " +bestS.getFo());
			if (instance.calculateFo(sl) > bestS.getFo()) {
				// Change s to the best solution
				bestS = sl;
			}
		}

		if (willRunPostOptimization && eliteSet.size() > 1) {
			Solution prS = new PathRelinking().runOnEliteSet(eliteSet, Direction.BACKWORD, MAX_ELITE);
			bestS = prS;
			Console.log("\n PR Post Solution: " + prS.getFo());
		}

		Console.log("FINAL BEST Solution: " + instance.calculateFo(bestS));
		return bestS;
	}

	private Solution greedyRandomizedConstruction() {
		double peso = 0;
		int j;
		int restrictSize;
		double referenceValue;
		boolean settedMaxElite = false;

		Solution s = new Solution(instance.getN());

		// Create list of ordered objects in descending order
		ArrayList<KnapsackObject> sortedObjs = instance.getSortedObjects(true);

		// Build a solution element by element, checking if each object fits in the
		// residual capacity of the backpack
		while (sortedObjs.size() > 0 && peso < instance.getB()) {
			restrictSize = 0;

			// Defines the size of the restricted list

			KnapsackObject bestObj = sortedObjs.get(0);
			KnapsackObject worstObj = sortedObjs.get(sortedObjs.size() - 1);

			referenceValue = bestObj.getProfit() - instance.alfa * (bestObj.getProfit() - worstObj.getProfit());
			// Console.log("Reference value " + value);

			for (int i = 0; i < sortedObjs.size(); i++) {
				KnapsackObject obj = sortedObjs.get(i);

				if (obj.getProfit() >= referenceValue)
					restrictSize++;
				else
					break;
			}

			// set elite set max size to the first restrict Size
			if (!settedMaxElite) {
				MAX_ELITE = restrictSize / 2;
				settedMaxElite = true;
			}

			// Console.log("RESTRIC SIZE " + restrictSize + " SIZE OF LIST " +
			// sortedObjs.size());
			// Sort random position from residual list
			j = (int) (Numbers.random() * restrictSize);

			KnapsackObject randObj = sortedObjs.get(j);

			// If object is not yet in the backpack and fits in it, add object to the
			// backpack
			if (s.getIndex(randObj.getId()) != 1 && peso + randObj.getWeight() <= instance.getB()) {
				s.setIndex(randObj.getId(), 1);
				peso += randObj.getWeight();
			}

			// Remove object from list as it has already been tested
			sortedObjs.remove(j);
		}
		return s;
	}
}
