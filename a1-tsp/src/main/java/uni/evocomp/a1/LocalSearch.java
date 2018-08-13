package uni.evocomp.a1;

import uni.evocomp.a1.evaluate.Evaluate;
import uni.evocomp.a1.logging.BenchmarkStatsTracker;
import uni.evocomp.a1.mutate.Mutate;

public abstract class LocalSearch {

  final Evaluate evaluate;
  protected Individual currentBestIndividual;
  protected TSPProblem problem;
  protected Mutate mutator;
  protected long totalIterations;

  LocalSearch(TSPProblem problem, Evaluate evaluate, Mutate mutationFunction) {
    // Load Problem
    this.problem = problem;

    // Evaluate function
    this.evaluate = evaluate;

    // Mutate function
    this.mutator = mutationFunction;
  }

  public Long getTotalIterations() {
    return this.totalIterations;
  }

  abstract Individual solve(BenchmarkStatsTracker bst);
}
