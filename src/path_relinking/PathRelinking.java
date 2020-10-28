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
		
		//Initialize best and current solutions with the initial one
		Solution bestS = new Solution(initialS);
		Solution currentS = new Solution(initialS);
		
		//Calculate symmetric difference
		ArrayList<Integer> symmetricDifference = initialS.symmetricDifference(targetS);

		//While we have moves left
		while(symmetricDifference.size() != 0) {

			int bestMove = -1;
			int bestMoveIndex = -1;
			double bestMoveFo = -Double.MAX_VALUE;

			//Calculate best move
			for(int i = 0; i < symmetricDifference.size(); i++) {
				//Get and make a move
				int currentMove = symmetricDifference.get(i);
				currentS.changeBit(currentMove);

				//fo with the applied movement
				double currentFo = instance.calculateFo(currentS);
	
				

				//if the move improves the solution
				if(currentFo > bestMoveFo) {
					bestMove = currentMove;
					bestMoveFo = currentFo;
					bestMoveIndex = i;
				}
				
				//undo current move
				currentS.changeBit(currentMove);
			}

			if(bestMove > -1) {
				//Remove best move from symmetricDifference
				symmetricDifference.remove(bestMoveIndex);

				//Apply it on the currentS
				currentS.changeBit(bestMove);
				double foWithBestMove = instance.calculateFo(currentS);
				//If the movement don't improve undo it
				if(!(foWithBestMove > instance.calculateFo(currentS))) {
					currentS.changeBit(bestMove);
				}else {
					Console.log("BEST MOVE IMPROVED CURRENT");
				}
				
			}

			//If the current solution is better, copy it to the bestS
			if(currentS.getFo() > instance.calculateFo(bestS)) {
				bestS = new Solution(currentS);
				Console.log("CURRENT SOLUTION IMPROVED BEST ONE");
			}
		}

		return bestS;
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
