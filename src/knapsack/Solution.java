package knapsack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Solution {
	public Solution() {
	};

	public Solution(int solutionSize) {
		initSolution(solutionSize);
	}

	public Solution(Solution s) {
		copySolution(s);
	}

	private ArrayList<Integer> s = new ArrayList<Integer>();
	private double fo;

	// Methods

	// Calculate symmetric difference: moves to reach target from initial solution
	public ArrayList<Integer> symmetricDifference(Solution s2) {
		ArrayList<Integer> symmetricDifference = new ArrayList<Integer>();

		for (int i = 0; i < s.size(); i++) {
			if (s.get(i) == s2.getIndex(i))
				continue;
			else
				symmetricDifference.add(i);
		}

		return symmetricDifference;
	}

	// Get solution with bigger symmetric difference ir relation to the param in an
	// array of solutions
	public static Solution biggerSymmetricDifference(Solution param, ArrayList<Solution> solutions) {
		int index = -1;
		int biggerSimDifSize = -1;

		for (int i = 0; i < solutions.size(); i++) {
			Solution comparatingWith = solutions.get(i);

			int simDifSize = param.symmetricDifference(comparatingWith).size();

			if (simDifSize > biggerSimDifSize) {
				biggerSimDifSize = simDifSize;
				index = i;
			}

		}

		return solutions.get(index);
	}

	// Get, Set and Modify s

	public Integer getIndex(int index) {
		return s.get(index);
	}

	private ArrayList<Integer> getS() {
		return s;
	}

	public void setIndex(int index, int value) {
		s.set(index, value);
	}

	public ArrayList<Integer> getSolution() {
		return s;
	}

	public void add(int i) {
		if (i == 1 || i == 0) {
			s.add(i);
		}
	}

	public void changeBit(int index) {
		int currentValue = s.get(index);
		int newValue = currentValue == 0 ? 1 : 0;
		s.set(index, newValue);
	}

	private void initSolution(int size) {
		for (int i = 0; i < size; i++)
			s.add(0);
	}

	private void copySolution(Solution cpyS) {
		s = new ArrayList<Integer>(cpyS.getS());
		fo = cpyS.getFo();
	}

	public static void sortArrayOfSolutions(ArrayList<Solution> array, boolean descending) {
		if (descending) {
			Collections.sort(array, Comparator.comparingDouble(Solution::getFo).reversed());

		} else {
			Collections.sort(array, Comparator.comparingDouble(Solution::getFo));
		}
	}

	// Other Getters & Setters

	public double getFo() {
		return fo;
	}

	public void setFo(Double newFo) {
		fo = newFo;
	}

}
