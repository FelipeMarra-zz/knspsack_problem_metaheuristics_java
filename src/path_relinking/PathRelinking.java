package path_relinking;

import java.util.ArrayList;
import knapsack.Instance;
import knapsack.Solution;
import utils.Console;

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

	public Solution run(Solution s1, Solution s2, Direction direction) {
		// Set initial and target solutions
		Solution initialS = new Solution();
		Solution targetS = new Solution();
		if (direction.equals(Direction.FORWARD)) {
			initialS = s1;
			targetS = s2;
		} else {
			initialS = s2;
			targetS = s1;
		}

		// Initialize best and current solutions with the initial one
		Solution bestS = new Solution(initialS);
		Solution currentS = new Solution(initialS);

		// Calculate symmetric difference
		ArrayList<Integer> symmetricDifference = initialS.symmetricDifference(targetS);

		// While we have moves left
		while (symmetricDifference.size() != 0) {

			int bestMove = -1;
			int simDifMoveIndex = -1;
			double bestMoveFo = -Double.MAX_VALUE;

			// Calculate best move
			for (int i = 0; i < symmetricDifference.size(); i++) {
				// Get and make a move
				int currentMove = symmetricDifference.get(i);
				currentS.changeBit(currentMove);

				// fo with the applied movement
				double currentFo = instance.calculateFo(currentS);

				// if the move improves the solution
				if (currentFo > bestMoveFo) {
					bestMove = currentMove;
					bestMoveFo = currentFo;
					simDifMoveIndex = i;
				}

				// undo current move
				currentS.changeBit(currentMove);
			}

			// Remove best move from symmetricDifference
			symmetricDifference.remove(simDifMoveIndex);

			// Apply it on the currentS
			currentS.changeBit(bestMove);

			// If the current solution is better, copy it to the bestS
			if (instance.calculateFo(currentS) > instance.calculateFo(bestS)) {
				bestS = new Solution(currentS);
			}
		}

		return bestS;
	}

	public Solution runOnEliteSet(ArrayList<Solution> eliteSet, Direction direction, int maxSize) {
		// the initial current pool is the elite set
		ArrayList<Solution> currentPool = new ArrayList<Solution>(eliteSet);

		// init next pool with a solution worst than the better of the current to start
		// the loop
		ArrayList<Solution> nextPool = new ArrayList<Solution>();

		// While we generate better solutions
		while (nextPool.size() == 0) {
			// Combine all solutions in the current pool using path relinking
			for (int i = 0; i < currentPool.size(); i++) {
				Solution toRun = currentPool.get(i);

				for (int j = 0; j < currentPool.size(); j++) {
					if (i != j) {
						Solution toRunWith = currentPool.get(j);

						// Run path relinking with then
						Solution prS = run(toRun, toRunWith, Direction.BACKWORD);

						// update next pool
						updateAPoolOfSolutions(prS, nextPool, maxSize);
					}

				}
			}

			// if we got a new best solution
			// TODO index out of range
			Console.log("Next pool size " + nextPool.size());
			if(nextPool.isEmpty()){
				continue;
			}
			if (nextPool.get(0).getFo() > currentPool.get(0).getFo()) {
				// Current pool is now the next one
				currentPool = nextPool;

				// reset next pool
				nextPool.clear();
			}

		}

		return nextPool.get(0);
	}

	public void updateAPoolOfSolutions(Solution s, ArrayList<Solution> pool, int maxSize) {
		// If its not full just put s in there
		if (pool.size() <= maxSize) {

			if (isThereEqualSolution(s, pool)) {
				return;
			}

			pool.add(s);
		} else {
			int smallerSimDif = Integer.MAX_VALUE;
			int smallerSimDifIndex = Integer.MAX_VALUE;
			boolean willReplace = false;

			// Look for a eliteS with the smaller symmetric difference that is worst than s
			for (int i = 0; i < pool.size(); i++) {
				Solution eliteS = pool.get(i);

				if (instance.calculateFo(s) > instance.calculateFo(eliteS)) {
					int simDif = eliteS.symmetricDifference(s).size();

					if (simDif < smallerSimDif) {
						smallerSimDifIndex = i;
						willReplace = true;
					}

				}

			}

			if (isThereEqualSolution(s, pool)) {
				return;
			}

			if (willReplace)
				pool.set(smallerSimDifIndex, s);
		}

		// Sort elite set in descending order
		Solution.sortArrayOfSolutions(pool, true);

		// printPool(pool);
	}

	public boolean isThereEqualSolution(Solution s, ArrayList<Solution> pool) {
		for (Solution poolS : pool) {
			if (Double.compare(instance.calculateFo(s), instance.calculateFo(poolS)) == 0) {
				// Console.log("OF IS EQUAK " + s.getFo() + " != " + poolS.getFo());
				return true;
			}
		}
		// Console.log("THE IS NO EQUAL OF FOR " + s.getFo());
		return false;
	}

	public void printPool(ArrayList<Solution> pool) {
		Console.log("######## NEW POOL ###########");
		for (Solution s : pool) {
			Console.log(instance.calculateFo(s));
		}
	}
}
