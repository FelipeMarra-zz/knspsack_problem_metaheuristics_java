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

	public Solution run(Solution toImprove, Solution s2, Direction direction) {
		//Set initial and target solutions
		Solution initialS = new Solution();
		Solution targetS = new Solution();
		if(direction.equals(Direction.FORWARD)) {
			initialS = toImprove;
			targetS = s2;
		}else {
			initialS = s2;
			targetS = toImprove;
		}
		
		//Initialize best solution with the initial one
		Solution bestS = new Solution(initialS);
		
		//Calculate symmetric difference
		ArrayList<Integer> symmetricDifference = initialS.symmetricDifference(targetS);

		//While we have moves left
		while(symmetricDifference.size() != 0) {

			int bestMove = -1;
			int bestMoveIndex = -1;
			double bestMoveFo = -Double.MAX_VALUE;

			//Calculate best move
			for(int i = 0; i < symmetricDifference.size(); i++) {

				int currentMove = symmetricDifference.get(i);
				double currentFo = instance.calculateFo(bestS);
	
				//make a move
				bestS.changeBit(currentMove);

				//if the move improves the solution
				if(currentFo > bestMoveFo) {
					bestMove = currentMove;
					bestMoveFo = currentFo;
					bestMoveIndex = i;
				}
				
				//undo current move
				bestS.changeBit(currentMove);
			}

			//Remove best move from symmetricDifference and apply it on the bestS
			symmetricDifference.remove(bestMoveIndex);
			if(bestMove > -1) {
				bestS.changeBit(bestMove);
			}
		}
		
		//if the best solution we found is better than the one we want to improve
		if(instance.calculateFo(bestS) > instance.calculateFo(toImprove)) {
			toImprove = bestS;
		}

		return toImprove;
	}
	
	//TODO
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
