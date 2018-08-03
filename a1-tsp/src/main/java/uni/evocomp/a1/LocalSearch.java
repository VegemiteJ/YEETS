package uni.evocomp.a1;

import uni.evocomp.util.IntegerPair;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class LocalSearch {

    public static void main(String[] args) {
        new LocalSearch();
    }

    LocalSearch() {
        //Load Problem
        TSPProblem problem = new TSPProblem();
        TSPIO io = new TSPIO();
        try (Reader r = new FileReader("tests/eil76.tsp")) {
            problem = io.read(r);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Evaluate function
        Evaluate evaluate = new EvaluateEuclid();

        //Initial solution
        Individual currentBestIndividual = new Individual(problem.getSize());
        double currentBestCost = evaluate.evaluate(problem, currentBestIndividual);

        //Mutate function
        Mutate N = new Jump();
        Random rand = new Random();

        for (int i=0; i<10000; i++) {
            //Rand jump
            Individual s = new Individual(currentBestIndividual);
            int first = rand.nextInt(problem.getSize()-1);
            int second = rand.nextInt(problem.getSize()-1);
            N.run(s, new ArrayList<>(Arrays.asList(new IntegerPair(first, second))));
            double cost = evaluate.evaluate(problem, s);

            if(cost < currentBestCost) {
                currentBestCost = cost;
                currentBestIndividual = s;
                System.out.println("");
                System.out.println("New Best: " + cost);
            } else {
                System.out.print(".");
            }
        }
    }
}
