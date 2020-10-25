package grasp;

import java.util.ArrayList;
import java.util.Random;

import VND.VND;
import utils.Console;
import utils.Instance;
import utils.KnapsackObject;
import utils.Solution;

public class Grasp {
	// Controllers
	Instance instance = Instance.getInstance();

	// Methods
	public Solution run(Solution s) {
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

			// Build partially greedy solution
			sl = buildGraspSolution();
			Console.log("solucao construida: " + instance.calculateFo(sl));

			//Apply local search to the built solution
			Solution vndS = new VND().run(sl);
			sl.setSolution(vndS);
			Console.log("solucao refinada: " + instance.calculateFo(sl));

			//Update best solution
			if (instance.calculateFo(sl) > bestS.getFo()) {

				//Change s to the best solution
				bestS.setSolution(sl);

				//update fo
				bestS.setFo(instance.calculateFo(sl));
			}
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
			int rand = new Random().nextInt(max);
			j = rand/max * restrictSize;
			//Console.log("RAND INDEX " + j);
			
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
