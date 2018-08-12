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

  /*
  edge case if consecutive indices are swapped
  either i-j or j-i is doubled
   */
  private double calculateDifferentialCost(
      TSPProblem problem, Individual individual, int i, int j) {
    List<Integer> g = individual.getGenotype();
    Matrix weights = problem.getWeights();

    if (i == j) {
      return 0;
    }

    double differentialCost = 0.0;

    //Prepare modulo indices
    int n = problem.getSize();
    int i_plus_1 = (i + 1 + n) % n;
    int i_minus_1 = (i - 1 + n) % n;
    int j_plus_1 = (j + 1 + n) % n;
    int j_minus_1 = (j - 1 + n) % n;

    // (i-1,i)
    differentialCost += weights.get(g.get(i_minus_1) - 1, g.get(i) - 1);
    // (i,i+1)
    differentialCost += weights.get(g.get(i) - 1, g.get(i_plus_1) - 1);

    // (j-1,j) -- don't repeat if == (i,i+1)
    if (j_minus_1 != i) {
      differentialCost += weights.get(g.get(j_minus_1) - 1, g.get(j) - 1);
    }
    // (j,j+1) -- don't repeat if == (i-1,i)
    if (j_plus_1 != i) {
      differentialCost += weights.get(g.get(j) - 1, g.get(j_plus_1) - 1);
    }
    return differentialCost;
  }
}
