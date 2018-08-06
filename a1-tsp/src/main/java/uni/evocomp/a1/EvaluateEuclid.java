package uni.evocomp.a1;

import java.util.List;
import uni.evocomp.util.Matrix;

public class EvaluateEuclid implements Evaluate {

  @Override
  public double evaluate(TSPProblem problem, Individual individual) {
    double cost = 0;
    Matrix weights = problem.getWeights();
    List<Integer> genotype = individual.getGenotype();
    for (int i = 0; i < genotype.size() - 1; i++) {
      // TODO: Loading tour file has the last element as -1, should change in TSPIO
      if (genotype.get(i + 1) == -1) {
        break;
      }
      cost += weights.get(genotype.get(i) - 1, genotype.get(i + 1) - 1);
    }
    return cost;
  }
}
