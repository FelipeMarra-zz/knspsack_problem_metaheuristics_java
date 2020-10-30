package vnd;

import knapsack.Instance;
import knapsack.Solution;

public class VND {
	// Controllers
	final Instance instance = Instance.getInstance();

	public Solution run(Solution s) {
		int k;
		double fo_s;

		k = 1;

		while (k <= 2) { // while 1
			fo_s = instance.calculateFo(s);

			switch (k) {
				case 1:
					s = bestNeighborN1(s);
					break;
				case 2:
					s = bestNeighborN2(s);
					break;
				default:
					break;
			}
			if (instance.calculateFo(s) > fo_s) {
				k = 1;
			} else
				k++;
		}
		return s;
	}

	// apply local search for the best enhancer strategy with a neighborhood of 1
	// bit
	private Solution bestNeighborN1(Solution s) {
		double fo_original;
		double fo_max;
		double fo_neighbor;
		int best_bit = -1;

		fo_original = instance.calculateFo(s);

		fo_max = -Double.MAX_VALUE;

		for (int j = 0; j < instance.getN(); j++) {
			// create neighbor
			s.changeBit(j);

			fo_neighbor = instance.calculateFo(s);

			// stores best neighbor
			if (fo_neighbor > fo_max) {
				best_bit = j;
				fo_max = fo_neighbor;
			}

			// back to initial solution
			s.changeBit(j);
		}

		// if you found a better neighbor
		if (fo_max > fo_original) {
			s.changeBit(best_bit);
			// Console.log("Vizinho melhor em N1! FO = " + fo_max);
		}
		return s;
	}

	// apply local search for the best enhancer strategy with a neighborhood of 2
	// bits
	private Solution bestNeighborN2(Solution s) {
		double fo_max, fo_neighbor;
		int best_bit_1 = -1, best_bit_2 = -1;

		double fo_original = s.getFo();

		fo_max = -Double.MAX_VALUE;

		// for each pair of positions in the vector
		for (int i = 0; i < instance.getN(); i++) {

			// change first bit
			s.changeBit(i);

			for (int j = i + 1; j < instance.getN(); j++) {

				// change second bit
				s.changeBit(j);

				// fo_neighbor
				fo_neighbor = instance.calculateFo(s);

				// stores best neighbor
				if (fo_neighbor > fo_max) {
					best_bit_1 = i;
					best_bit_2 = j;
					fo_max = fo_neighbor;
				}

				// second bit back
				s.changeBit(j);
			}
			// back to initial solution
			s.changeBit(i);
		}

		// if you found a better neighbor
		if (fo_max > fo_original) {
			s.changeBit(best_bit_1);
			s.changeBit(best_bit_2);
			// Console.log("Vizinho melhor em N2! FO = " + fo_max);
		}
		return s;
	}
}
