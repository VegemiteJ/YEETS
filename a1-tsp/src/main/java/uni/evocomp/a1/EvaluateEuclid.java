package uni.evocomp.a1;

import java.util.List;

public class EvaluateEuclid implements Evaluate {
  @Override
  public double evaluate(TSPProblem problem, Individual individual) {
    double cost = 0;
    List<List<Double>> weights = problem.getWeights();
    List<Integer> genotype = individual.getGenotype();
    for (int i = 0; i < genotype.size() - 1; i++) {
      // TODO: Loading tour file has the last element as -1, should change in TSPIO
      if (genotype.get(i + 1) == -1)
        break;
      cost += weights.get(genotype.get(i) - 1).get(genotype.get(i + 1) - 1);
    }
    return cost;
  }
}
