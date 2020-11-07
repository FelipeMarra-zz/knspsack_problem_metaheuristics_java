package tabu_search;

import java.util.ArrayList;

import knapsack.Instance;
import knapsack.Solution;

public class TabuSearch {
    // Controllers
    final Instance instance = Instance.getInstance();

    public Solution run(Solution s, double tabuListPercentage, int TSMax) {
        int tabuListSize = (int) Math.ceil(tabuListPercentage * instance.getN());
        int bestPosition = -1, bitValue = -1;
        double bestNeighborOF;

        int iterations = 0;
        int bestIteration = 0;

        Solution besSolution = new Solution(s);
        instance.calculateFo(besSolution);

        ArrayList<Integer> tabuList = new ArrayList<Integer>();

        while(iterations - bestIteration < TSMax){
            iterations++;

            bestNeighborOF = bestNeighbor(besSolution.getFo(), bestPosition, bitValue, s, tabuList);
            
            if(bestNeighborOF < besSolution.getFo()){
                tabuList.add(bestPosition);
            }

            if(tabuList.size() > tabuListSize){
                tabuList.remove(0);
            }

            s.setIndex(bestPosition, bitValue);

            if(instance.calculateFo(s) > besSolution.getFo()){
                bestIteration = iterations;
                besSolution = s;
            }
        }

        return besSolution;
    }

    private double bestNeighbor(Double bestOF, int bestPosition, int bitValue, Solution currentS, ArrayList<Integer> tabuList) {
        //double neighborFo;
        double bestNeighborOF = - Double.MAX_VALUE;
        boolean isOnTabuList;
        
        for (int i = 0; i < instance.getN(); i++) {
            currentS.changeBit(i);

            isOnTabuList = tabuList.contains(i);
            
            if(!isOnTabuList || instance.calculateFo(currentS) > bestOF){
                if(currentS.getFo() > bestNeighborOF){
                    bestNeighborOF = currentS.getFo();
                    bestPosition = i;
                    bitValue = currentS.getIndex(i);
                }
            }

            currentS.changeBit(i);
        }

        return bestNeighborOF;
    }
}
