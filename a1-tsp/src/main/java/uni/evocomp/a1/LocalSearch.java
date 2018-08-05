package uni.evocomp.a1;

import java.io.FileReader;
import java.io.IOException;

public abstract class LocalSearch {

  final EvaluateEuclid evaluate;
  protected Individual currentBestIndividual;
  protected TSPProblem problem;
  protected Mutate mutator;

  abstract Double solve();

  LocalSearch(String testCaseFile, Mutate mutationFunction) {
    // Load Problem
    this.problem = new TSPProblem();

    TSPIO io = new TSPIO();
    try {
      this.problem = io.read(new FileReader(testCaseFile));
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Evaluate function
    this.evaluate = new EvaluateEuclid();

    // Initial solution
    this.currentBestIndividual = new Individual(problem.getSize());

    // Mutate function
    this.mutator = mutationFunction;
  }
}
