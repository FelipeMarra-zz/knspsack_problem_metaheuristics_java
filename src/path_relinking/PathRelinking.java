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

	public Solution run(Solution s1, Solution s2, Direction direction) {
		//Set initial and target solutions
		Solution initialS = new Solution();
		Solution targetS = new Solution();
		if(direction.equals(Direction.FORWARD)) {
			initialS = s1;
			targetS = s2;
		}else {
			initialS = s2;
			targetS = s1;
		}
		
		//Initialize answer
		Solution bestS = new Solution(initialS);
		
		//Symmetric difference: moves to reach target from initial solution
		ArrayList<Integer> symmetricDifference = new ArrayList<Integer>();
		for(int i = 0; i < instance.getN(); i++) {
			if(initialS.getIndex(i) == targetS.getIndex(i))
				continue;
			else
				symmetricDifference.add(i);
		}

		//While we have moves left
		while(symmetricDifference.size() != 0) {
			//Calculate best move
			int bestMove = -1;
			int bestMoveIndex = -1;
			double bestMoveFo = -Double.MAX_VALUE;
		
			for(int i = 0; i < symmetricDifference.size(); i++) {
				int currentMove = symmetricDifference.get(i);
				double currentFo = instance.calculateFo(bestS);
				//make a move
				bestS.changeBit(currentMove);
				//if the move didn't improve the solution undo it
				if(currentFo > bestMoveFo) {
					bestMove = currentMove;
					bestMoveFo = currentFo;
					bestMoveIndex = i;
				}
				bestS.changeBit(currentMove);
			}
			//Remove best move from symmetricDifference and apply it on the bestS
			symmetricDifference.remove(bestMoveIndex);
			bestS.changeBit(bestMove);
		}

		return bestS;
	}
	
	public Solution runOnEliteSet(ArrayList<Solution> eliteSet, Direction direction) {
		//init best solution as the best on the eliteSet
		Solution bestS = new Solution(eliteSet.get(0));
		
		//run path relinking for each solution in eliteSet, set bestS to the best answer
		Solution prS = new Solution();
		for(int i = 1; i < eliteSet.size(); i++) {
			prS = run(bestS, eliteSet.get(i), direction);
			
			if(prS.getFo() > instance.calculateFo(bestS)) {
				bestS = prS;
			}
		}
		return bestS;
	}
}
