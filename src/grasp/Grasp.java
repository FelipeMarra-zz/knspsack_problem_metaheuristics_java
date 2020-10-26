package grasp;

import java.util.ArrayList;
import java.util.Random;

import knapsack.Instance;
import knapsack.KnapsackObject;
import knapsack.Solution;
import path_relinking.PathRelinking;
import path_relinking.PathRelinking.Direction;
import utils.Console;
import vnd.VND;

public class Grasp {
	// Controllers
	final Instance instance = Instance.getInstance();
	
	//With Path Relinking
	public enum WithPR{
		NO(-1), INTENSIFICATION(1), POST_OPTIMIZATION(2);
		
		public int pathReOption;
		
		WithPR(int value){
			pathReOption = value;
		}
	}

	private ArrayList<Solution> eliteSet = new ArrayList<Solution>();
	
	// Methods
	/*
	 * @param [Solution] s: The solution in which the algorithm will be applied
	 * @param [WithPathRelinking] pathRelinking: If and how [PathRelinking] will be applied
	 * 
	 * @return the best [Solution] found
	 */
	public Solution run(Solution s, WithPR pathRelinking) {
		if (instance.instanceIsNull()) {
			return null;
		}
		Console.log("Running Grasp: ");

		//Best solution
		Solution bestS = new Solution();
		bestS.setFo(-Double.MAX_VALUE);

		// Auxiliary solution
		Solution sl = new Solution();
		

		// Till stopping criteria (max of iterations)
		for (int i = 0; i < instance.iter_max; i++) {
			Console.log("#######Iteration " + i);
			// Build partially greedy solution
			sl = buildGraspSolution();
			Console.log("built solution: " + instance.calculateFo(sl));

			//Apply local search to the built solution
			Solution vndS = new VND().run(sl);
			sl = vndS;
			Console.log("refined solution: " + instance.calculateFo(sl));

			//Update best solution
			if (instance.calculateFo(sl) > bestS.getFo()) {
				
				//If we will have path relinking
				if(!pathRelinking.equals(WithPR.NO)) {

					eliteSet.add(sl);
					instance.sortSolutions(eliteSet);
					Console.log("SORTED ELIT SET " + eliteSet);

					if(pathRelinking.equals(WithPR.INTENSIFICATION)) {
						Solution prS = new PathRelinking().run(sl, eliteSet.get(0), Direction.BACKWORD);
						sl = prS;
					}
				}
				
				//Change s to the best solution
				bestS = sl;

				//update fo
				bestS.setFo(instance.calculateFo(sl));
			}
		}
		if(pathRelinking.equals(WithPR.POST_OPTIMIZATION)) {
			Solution prS = new PathRelinking().runOnEliteSet(eliteSet, Direction.BACKWORD);
			bestS = prS;
		}
		return bestS;
	}

	private Solution buildGraspSolution() {
		double peso = 0;
		int j;
		int restrictSize;
		double value;

		Solution s = new Solution(instance.getN());
		
		//Create list of ordered objects
		ArrayList<KnapsackObject> sortedObjs = instance.getSortedObjects();

		//imprime_lista(objetosOrd);

		//Build a solution element by element, checking if each object fits in the residual capacity of the backpack 
		while(sortedObjs.size() > 0 && peso < instance.getB()) {
			restrictSize = 0;

			//Defines the size of the restricted list

			KnapsackObject bestObj = sortedObjs.get(0);
			KnapsackObject worstObj = sortedObjs.get(sortedObjs.size() - 1);
			
			value = bestObj.getProfit() - instance.alfa * (bestObj.getProfit() - worstObj.getProfit());
			//Console.log("Reference value " + value);

			for (int i = 0; i < sortedObjs.size(); i++){
				KnapsackObject obj = sortedObjs.get(i);
				if (obj.getProfit() >= value)
					restrictSize++;
				else
					break;
			}

			//Console.log("RESTRIC SIZE " + restrictSize + " SIZE OF LIST " + sortedObjs.size());
			//Sort random position from residual list
			int max = instance.getN();
			double rand = new Random().nextInt(max);
			double p = rand/max;
			j = (int) (p * restrictSize);
			
			KnapsackObject randObj = sortedObjs.get(j);

			//If object is not yet in the backpack and fits in it, add object to the backpack
			if (s.getIndex(randObj.getId()) != 1 && peso + randObj.getWeight() <= instance.getB()){
				s.setIndex(randObj.getId(), 1);
				peso += randObj.getWeight();
			}

			//Remove object from list as it has already been tested
			sortedObjs.remove(j);
		}
		return s;
	}
}
