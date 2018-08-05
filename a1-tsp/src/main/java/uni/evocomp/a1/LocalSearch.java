package uni.evocomp.a1;

import uni.evocomp.util.IntegerPair;
import uni.evocomp.util.Logger;

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
        Individual knownBestIndividual = new Individual();
        TSPIO io = new TSPIO();
        try {
            problem = io.read(new FileReader("tests/eil76.tsp"));
            knownBestIndividual = io.readSolution(new FileReader("tests/eil76.opt.tour"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Evaluate function
        Evaluate evaluate = new EvaluateEuclid();

        //Initial solution
        Individual currentBestIndividual = new Individual(problem.getSize());
        double currentBestCost = evaluate.evaluate(problem, currentBestIndividual);

        double knownBestCost = evaluate.evaluate(problem, knownBestIndividual);

        //Mutate function
        Mutate N = new Jump();
        Random rand = new Random();

        //Logger
        Logger log = new Logger<Float>();

        for (int i=0; i<1000000; i++) {
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
                System.out.println("New Best: " + cost + " / " + knownBestCost);
            } else {
                System.out.print(".");
            }
            log.addPair(cost, currentBestCost);
        }
        log.saveCSV("LocalSearchResult.csv");
    }
}
