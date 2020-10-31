package knapsack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import utils.Console;

public class Instance {
	// ###Singleton:
	private static Instance instance;

	public static synchronized Instance getInstance() {
		if (instance == null)
			instance = new Instance();

		return instance;
	}

	private int n;// number of objects
	private double b;// knapsack capacity
	public Solution s = new Solution();// current solution
	public Solution s_star = new Solution();// best solution
	private ArrayList<Double> w = new ArrayList<Double>();// objects weight
	private ArrayList<Double> p = new ArrayList<Double>();// objects profit
	public int iter_max;
	public double alfa;

	public boolean instanceIsNull() {
		if (n == 0) {
			Console.log("ERROR 404 - INSTANCE NOT FOUND");
			return true;
		}
		return false;
	}

	public void read(String path) {
		if (path == null) {
			Console.log("Type the path: ");
			path = Console.readString();
		}

		if (iter_max == 0) {
			instance.setIterMax();
		}

		if (alfa == 0) {
			instance.setAlfa();
		}

		//Reset data
		s = new Solution();
		s_star = new Solution();
		w = new ArrayList<Double>();
		p = new ArrayList<Double>();

		try {
			FileReader file = new FileReader(path);
			BufferedReader readFile = new BufferedReader(file);

			int line = 0;
			int c = 0;

			ArrayList<Character> current = new ArrayList<Character>();

			while (c != -1) {
				c = readFile.read();
				// Input 1, Space = 32
				if (c == 32) {
					if (line == 0) {
						n = arrayCharToInt(current);
					} else if (line > 0 && line < n + 1) {
						p.add(arrayCharToDouble(current));
					} else {
						s.add(arrayCharToInt(current));
					}
					current.clear();
					// Input 2, \n = 13
				} else if (c == 13) {
					if (line == 0) {
						b = arrayCharToInt(current);
					} else if (line > 0 && line < n + 1) {
						w.add(arrayCharToDouble(current));
					} else {
						s.add(arrayCharToInt(current));
					}

					// Reset current, increment line counter
					current.clear();
					line++;
					// Line Feed = 10
				} else if (c != 10) {
					current.add((char) c);
				}
			}

			Console.log("n: " + n);
			Console.log("b: " + b);
			// Console.log("s: " + s.getSolution());
			// Console.log("w: " + w);
			// Console.log("p: " + p);

			// close file and reset best fo
			file.close();
			resetFo_star();

		} catch (IOException e) {
			System.err.printf("Something went wrong: %s.\n", e.getMessage());
		}
	}

	// Methods to convert the char array we reading

	private String arrayCharToString(ArrayList<Character> array) {
		char[] ch = new char[20];
		int count = 0;
		for (Character c : array) {
			ch[count] = c;
			count++;
		}
		String str = new String(ch);
		return str.trim();
	}

	private int arrayCharToInt(ArrayList<Character> array) {
		String s = arrayCharToString(array);
		return Integer.parseInt(s);
	}

	private double arrayCharToDouble(ArrayList<Character> array) {
		String s = arrayCharToString(array);
		return Double.parseDouble(s);
	}

	// Other Methods

	public void resetFo_star() {
		instance.s_star.setFo(-Double.MAX_VALUE);
	}

	public ArrayList<KnapsackObject> getSortedObjects(boolean descending) {
		// get objects list
		ArrayList<KnapsackObject> objects = new ArrayList<KnapsackObject>();
		for (int i = 0; i < n; i++) {
			double wheight = w.get(i);
			double profity = p.get(i);
			int id = i;
			KnapsackObject obj = new KnapsackObject(wheight, profity, id);
			objects.add(obj);
		}

		// Sorted objects
		if (descending) {
			Collections.sort(objects, Comparator.comparingDouble(KnapsackObject::getProfit).reversed());

		} else {
			Collections.sort(objects, Comparator.comparingDouble(KnapsackObject::getProfit));
		}

		return objects;
	}

	public Double calculateFo(Solution solution) {
		double foValue;
		double utility = 0, weight = 0, penality = 0;

		for (int i = 0; i < n; i++) {
			if (solution.getIndex(i).equals(1)) {
				utility += p.get(i);
				weight += w.get(i);
			}
			penality += w.get(i);
		}

		foValue = utility - penality * Double.max(0, weight - b);

		solution.setFo(foValue);

		return foValue;
	}

	// Getters & setters

	public void setIterMax() {
		Console.log("The maximum of iterations: ");
		iter_max = Console.readInt();
	}

	public void setAlfa() {
		Console.log("Value of alfa: ");
		alfa = Console.readDouble();
	}

	public Solution getS() {
		return s;
	}

	public void setS(Solution s) {
		this.s = s;

		if (s.getFo() > s_star.getFo()) {
			s_star = s;
			calculateFo(s_star);
		}
	}

	public void sortSolutions(ArrayList<Solution> solutionSet) {
		solutionSet.sort(new Comparator<Solution>() {
			@Override
			public int compare(Solution s1, Solution s2) {
				return Double.compare(calculateFo(s1), calculateFo(s2));
			}
		});
	}

	public int getN() {
		return n;
	}

	public double getB() {
		return b;
	}

	public ArrayList<Double> getW() {
		return w;
	}

	public ArrayList<Double> getP() {
		return p;
	}

}
