package uni.evocomp.a1;

import java.io.FileReader;
import java.io.IOException;

public abstract class LocalSearch {

  final Evaluate evaluate;
  protected Individual currentBestIndividual;
  protected TSPProblem problem;
  protected Mutate mutator;

  abstract Double solve();

  LocalSearch(TSPProblem problem, Evaluate evaluate, Mutate mutationFunction) {
    // Load Problem
    this.problem = problem;

    // Evaluate function
    this.evaluate = evaluate;

    // Initial solution
    this.currentBestIndividual = new Individual(problem.getSize());

    // Mutate function
    this.mutator = mutationFunction;
  }
}
