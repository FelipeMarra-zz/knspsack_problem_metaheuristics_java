package simulated_annealing;

import knapsack.Instance;
import knapsack.Solution;
import utils.Numbers;

public class SimulatedAnnealing {
	// Controllers
	final Instance instance = Instance.getInstance();

	public Solution run(Solution s, double initialTemp, double finalTemp, int thermalEqui) {
		double currentTemp = initialTemp;
		int currentTempIterations;
		// int tempChanges = 0;
		double currentOF = instance.calculateFo(s);
		Solution bestS = new Solution(s);

		// While the system is not frozen
		while (currentTemp > finalTemp) {
			currentTempIterations = 0;
			// While the thermal equilibrium is not reached
			while (currentTempIterations < thermalEqui) {
				currentTempIterations++;

				// Chose a random neighbor
				int pos1 = (int) Numbers.random() * instance.getN();
				int pos2 = (int) Numbers.random() * instance.getN();
				while (pos1 == pos2)
					pos2 = (int) Numbers.random() * instance.getN();
				s.changeBit(pos1);
				s.changeBit(pos2);
				double neighborOF = instance.calculateFo(s);

				// Delta energy
				double deltaE = currentOF - neighborOF;

				if (deltaE > 0) {
					currentOF = neighborOF;

					if (currentOF > instance.calculateFo(bestS)) {
						bestS = new Solution(s);
					}
					// if it gets worse, accept the neighbor with a certain probability
				} else {
					double p = Numbers.random();

					if (p < Math.exp(deltaE / currentTemp)) {
						currentOF = neighborOF;
					}
					// If the neighbor was not accepted
					else {
						s.changeBit(pos1);
						s.changeBit(pos2);
					}
				}

			}
			// decreases the temperature
			currentTemp *= instance.alfa;
			// tempChanges++;
		}

		return bestS;
	}

}
