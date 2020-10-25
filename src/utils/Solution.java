package utils;

import java.util.ArrayList;

public class Solution {
	public Solution(){};
	
	public Solution(int solutionSize){
		initSolution(solutionSize);
	}
	
	private ArrayList<Integer> s = new ArrayList<Integer>();
	private double fo;
	
	//Methods
	private void initSolution(int size) {
		for(int i = 0; i < size; i++)
			s.add(0);
	}

	public Integer getIndex(int index) {
		return s.get(index);
	}
	
	private ArrayList<Integer> getS() {
		return s;
	}

	public void setIndex(int index, int value) {
		s.set(index, value);
	}
	
	public void setSolution(Solution newS) {
		s = newS.getS();
	}
	
	public void add(int i) {
		if(i == 1 || i == 0) {
			s.add(i);
		}
	}
	
	public void changeBit(int index) {
		int currentValue = s.get(index);
		int newValue = currentValue == 0 ? 1 : 0;
		s.set(index, newValue);
	}

	public double getFo() {
		return fo;
	}
	
	public void setFo(Double newFo) {
		fo = newFo;
	}
	
}
