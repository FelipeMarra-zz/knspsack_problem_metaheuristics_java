package utils;

import java.util.Scanner;

public class Console {
	public static void log(Object obj) {
		System.out.println(obj);
	}

	private static Scanner scan = new Scanner(System.in);
	
	public static String readString() {
		String s = scan.nextLine();
		return s;
	}

	public static int readInt() {
		String s = readString();
		int i = Integer.parseInt(s);
		return i;
	}
	
	public static double readDouble() {
		String s = readString();
		double i = Double.parseDouble(s);
		return i;
	}
}
