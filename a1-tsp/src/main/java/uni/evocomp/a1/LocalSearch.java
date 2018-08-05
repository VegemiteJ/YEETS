package uni.evocomp.a1;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import uni.evocomp.util.IntegerPair;

public class LocalSearch {

  protected TSPProblem problem;
  Individual currentBestIndividual;
  double knownBestCost;
  Evaluate evaluate;
  Mutate N;
  private double currentBestCost;
  LocalSearch() {
    // Load Problem
    this.problem = new TSPProblem();
    Individual knownBestIndividual = new Individual();

    TSPIO io = new TSPIO();
    try {
      this.problem = io.read(new FileReader("tests/eil76.tsp"));
      knownBestIndividual = io.readSolution(new FileReader("tests/eil76.opt.tour"));
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Evaluate function
    this.evaluate = new EvaluateEuclid();

    // Initial solution
    this.currentBestIndividual = new Individual(problem.getSize());
    this.currentBestCost = evaluate.evaluate(problem, currentBestIndividual);
    this.knownBestCost = evaluate.evaluate(problem, knownBestIndividual);

    // Mutate function
    this.N = new Jump();
  }

  public static void main(String[] args) {
    new RandomizedLocalSearch().solve();
  }

  boolean updateCost(double cost, Individual s) {
    if (cost < currentBestCost) {
      currentBestCost = cost;
      currentBestIndividual = s;
      System.out.println();
      System.out.println("New Best: " + cost);
      return true;
    } else {
      System.out.print(".");
      return false;
    }
  }

  public void solve() {
    int totalIterations = 0;
    boolean madeChange = true;
    while (madeChange) {
      madeChange = false;
      for (int idxA = 0; idxA < problem.getSize() - 1; idxA++) {
        for (int idxB = 0; idxB < problem.getSize() - 1; idxB++) {
          Individual s = new Individual(currentBestIndividual);
          N.run(s, new ArrayList<>(Collections.singletonList(new IntegerPair(idxA, idxB))));
          double cost = evaluate.evaluate(problem, s);
          if (updateCost(cost, s)) {
            madeChange = true;
          }
          totalIterations++;
        }
      }
    }
    System.out.println("\nTotal iterations was " + totalIterations);
    System.out.println("Known Best is: " + knownBestCost);
  }
}
