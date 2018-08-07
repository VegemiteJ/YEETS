package uni.evocomp.a1.mutate;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import uni.evocomp.a1.Individual;
import uni.evocomp.a1.TSPProblem;
import uni.evocomp.util.IntegerPair;
import uni.evocomp.util.Matrix;

/**
 * Perform a swap on position n and m of i's genotype
 *
 * @author Namdrib
 */
public class Swap implements Mutate {

  /**
   * Perform a swap on position n and m of i's genotype
   *
   * @param individual Individual on which to perform a mutation operation
   * @param n first index to swap
   * @param m last index to swap
   */
  private void swap(TSPProblem problem, Individual individual, int n, int m)
      throws IndexOutOfBoundsException, NullPointerException {
    double cost = individual.getCost();
    cost -= calculateDifferentialCost(problem, individual, n, m);

    Collections.swap(individual.getGenotype(), n, m);

    cost += calculateDifferentialCost(problem, individual, n, m);
    individual.setCost(cost);
  }

  @Override
  public void run(TSPProblem problem, Individual individual, List<IntegerPair> pairs) {
    for (Iterator<IntegerPair> it = pairs.iterator(); it.hasNext(); ) {
      IntegerPair p = it.next();
      swap(problem, individual, p.first, p.second);
    }
  }

  private double calculateDifferentialCost(
      TSPProblem problem, Individual individual, int i, int j) {
    List<Integer> g = individual.getGenotype();
    Matrix weights = problem.getWeights();

    if (i == j) {
      return 0;
    }

    double differentialCost = 0.0;

    // (i-1,i)
    if (i - 1 >= 0) {
      differentialCost += weights.get(g.get(i - 1) - 1, g.get(i) - 1);
    }
    // (i,i+1)
    if (i + 1 < problem.getSize()) {
      differentialCost += weights.get(g.get(i) - 1, g.get(i + 1) - 1);
    }
    // (j-1,j)
    if (j - 1 >= 0) {
      differentialCost += weights.get(g.get(j - 1) - 1, g.get(j) - 1);
    }
    // (j,j+1)
    if (j + 1 < problem.getSize()) {
      differentialCost += weights.get(g.get(j) - 1, g.get(j + 1) - 1);
    }
    return differentialCost;
  }
}
