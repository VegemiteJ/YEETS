package uni.evocomp.a1;

import uni.evocomp.a1.evaluate.Evaluate;
import uni.evocomp.a1.logging.BenchmarkStatsTracker;
import uni.evocomp.a1.mutate.Mutate;

public abstract class LocalSearch {

  protected Individual currentBestIndividual;
  protected TSPProblem problem;
  protected Mutate mutator;
  protected long totalIterations;

  LocalSearch(TSPProblem problem, Mutate mutationFunction) {
    // Load Problem
    this.problem = problem;

    // Mutate function
    this.mutator = mutationFunction;
  }

  public Long getTotalIterations() {
    return this.totalIterations;
  }

  abstract Individual solve(BenchmarkStatsTracker bst);
}
