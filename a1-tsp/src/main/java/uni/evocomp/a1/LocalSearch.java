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
    // Load Problem
    TSPProblem problem = new TSPProblem();
    TSPIO io = new TSPIO();
    try (Reader r = new FileReader("tests/eil76.tsp")) {
      problem = io.read(r);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Evaluate function
    Evaluate evaluate = new EvaluateEuclid();

    // Initial solution
    Individual currentBestIndividual = new Individual(problem.getSize());
    double currentBestCost = evaluate.evaluate(problem, currentBestIndividual);

    // Mutate function
    Mutate N = new Jump();
    Random rand = new Random();

    //            Rand jump
    //            int first = rand.nextInt(problem.getSize()-1);
    //            int second = rand.nextInt(problem.getSize()-1);

    int totalIterations = 0;

    boolean madeChange = false;
    while (!madeChange) {
      for (int idxA=0; idxA<problem.getSize()-1; idxA++) {
        for (int idxB=0; idxB<problem.getSize()-1; idxB++) {
          Individual s = new Individual(currentBestIndividual);
          N.run(s, new ArrayList<>(Arrays.asList(new IntegerPair(idxA, idxB))));
          double cost = evaluate.evaluate(problem, s);
          if (cost < currentBestCost) {
            currentBestCost = cost;
            currentBestIndividual = s;
            System.out.println("");
            System.out.println("New Best: " + cost);
            madeChange = true;
          } else {
            System.out.print(".");
          }
          totalIterations++;
        }
      }
    }
    System.out.println("\nTotal iterations was "+totalIterations );
  }
}
