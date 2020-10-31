package test.GraspVsGraspPr;

import knapsack.Instance;
import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;

import grasp.Grasp;
import grasp.Grasp.WithPR;
import utils.Console;
import utils.Files;

public class GraspVsGraspPr {
    static final String SPACES_15 = "               "; 
    // App controller
    final static Instance instance = Instance.getInstance();

    public static void run() {
        String fileName = "test.txt"; 
        Files.createFile(fileName);

        File[] files = new File("./instances").listFiles();

        Console.log("Loaded files: ");
        Files.printFiles(files);

        for (File file : files) {
            Console.log("Loading instance " + file.getName());

            Files.writeLine(fileName, "Testing instance " + file.getName());
            Files.writeLine(fileName, SPACES_15 + "Grasp" + SPACES_15 + "Grasp-PR");

            instance.read(file.getAbsolutePath());

            if(instance.getN() > 500){
                continue;
            }
            instance.iter_max = 1;

            // Classic Grasp
            ArrayList<Long> classicData = testGrasp(WithPR.NO, 1);

            // Grasp PR
            ArrayList<Long> prData = testGrasp(WithPR.BOTH, 1);
            
            Files.writeLine(fileName, "Avarage Fo " + classicData.get(0) + "  " + prData.get(0));
            Files.writeLine(fileName, "Avarage Time " + classicData.get(0) + "  " + prData.get(0));
        }
    }

    private static  ArrayList<Long> testGrasp(WithPR withPR, int iter) {
        ArrayList<Long> resultsAvarage = new ArrayList<Long>();

        ArrayList<Long> ofs = new ArrayList<Long>();
        ArrayList<Long> times = new ArrayList<Long>();

        for (int i = 0; i < iter; i++) {
            Timestamp init = new Timestamp(System.currentTimeMillis());

            ofs.add((long) Grasp.run(instance.s, withPR).getFo());

            Timestamp end = new Timestamp(System.currentTimeMillis());
            // Add timestamp delta
            times.add(end.getTime() - init.getTime());
        }

        resultsAvarage.add(calculateAvarage(ofs));
        resultsAvarage.add(calculateAvarage(times));

        return resultsAvarage;
    }

    private static Long calculateAvarage(ArrayList<Long> array){
        long sum = 0;

        for(Long l : array){
            sum += l;
        }
        return sum/array.size();
    }
}
