package uni.evocomp.a1;

import uni.evocomp.util.IntegerPair;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;

public class LocalSearch {

  public static void main(String[] args) {
    new LocalSearch().solve();
  }

  protected TSPProblem problem;
  protected Individual currentBestIndividual;
  protected double currentBestCost;
  protected Evaluate evaluate;
  protected Mutate N;

  protected boolean updateCost(double cost, Individual s) {
    if (cost < currentBestCost) {
      currentBestCost = cost;
      currentBestIndividual = s;
      System.out.println("");
      System.out.println("New Best: " + cost);
      return true;
    } else {
      System.out.print(".");
      return false;
    }
  }

  public double solve() {
    int totalIterations = 0;
    boolean madeChange = true;
    while (madeChange) {
      madeChange = false;
      for (int idxA=0; idxA<problem.getSize()-1; idxA++) {
        for (int idxB=0; idxB<problem.getSize()-1; idxB++) {
          Individual s = new Individual(currentBestIndividual);
          N.run(s, new ArrayList<>(Arrays.asList(new IntegerPair(idxA, idxB))));
          double cost = evaluate.evaluate(problem, s);
          if (updateCost(cost, s)) {
            madeChange = true;
          }
          totalIterations++;
        }
      }
    }
    System.out.println("\nTotal iterations was "+totalIterations );
    return currentBestCost;
  }

  LocalSearch() {
    // Load Problem
    TSPIO io = new TSPIO();
    try (Reader r = new FileReader("tests/eil76.tsp")) {
      this.problem = io.read(r);
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Evaluate function
    this.evaluate = new EvaluateEuclid();

    // Mutate function
    this.N = new Jump();

    // Initial solution
    this.currentBestIndividual = new Individual(problem.getSize());
    this.currentBestCost = evaluate.evaluate(problem, currentBestIndividual);
  }
}
