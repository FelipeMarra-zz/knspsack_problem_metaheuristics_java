package utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Files {
    public static void printFiles(File[] files) {
        for (File file : files) {
            Console.log(file.getName());
        }
    }

    public static void createFile(String name) {
        try {
            File file = new File(name);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred crenting file " + name);
            e.printStackTrace();
        }
    }

    public static void writeLine(String file, String line) {
        try {
            FileWriter myWriter = new FileWriter(file, true);
            myWriter.write(line + " \n");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred writing on file " + file);
            e.printStackTrace();
        }
    }

}
