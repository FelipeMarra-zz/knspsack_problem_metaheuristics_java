package utils;

import java.util.Random;

public class Numbers {
	
	//Random percentage (double between 0 & 1)
	public static double random() {
		double rand = new Random().nextInt(100);
		double p = rand/100.00;
		return p;
	}
}
