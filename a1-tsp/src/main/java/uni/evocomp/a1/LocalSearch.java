package uni.evocomp.a1;

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

    // Mutate function
    this.mutator = mutationFunction;
  }
}
