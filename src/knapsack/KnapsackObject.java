package knapsack;

public class KnapsackObject {
	public KnapsackObject(double weight, double profit, int id) {
		this.weight = weight;
		this.profit = profit;
		this.id = id;
	}

	double weight;
	double utility;
	double profit;
	private int id;

	public double getWeight() {
		return weight;
	}

	public double getUtility() {
		return utility;
	}

	public double getProfit() {
		return profit;
	}

	public int getId() {
		return id;
	}
}
