package uni.evocomp.a1;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import uni.evocomp.util.IntegerPair;
import uni.evocomp.util.Pair;

/**
 * 
 * Perform a swap on position n and m of i's genotype
 * 
 * @author Namdrib
 *
 */
public class Swap implements Mutate {

  /**
   * Perform a swap on position n and m of i's genotype
   *
   * @param individual Individual on which to perform a mutation operation
   * @param n first index to swap
   * @param m last index to swap
   * @throws IndexOutOfBoundsException
   * @throws NullPointerException
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
      Pair p = it.next();
      swap(problem, individual, (int) p.first, (int) p.second);
    }
  }

  private double calculateDifferentialCost(TSPProblem problem, Individual individual, int n, int m) {
    double differentialCost = 0.0;
    int idxI = individual.getGenotype().get(n);
    int idxJ = individual.getGenotype().get(m);

    // (i-1,i)
    if (idxI - 1 >= 0) {
      differentialCost += problem.getWeights().get(idxI - 1).get(idxI);
    }
    // (i,i+1)
    if (idxI < problem.getSize()) {
      differentialCost += problem.getWeights().get(idxI).get(idxI + 1);
    }
    // (j,j+1)
    if (idxJ + 1 < problem.getSize()) {
      differentialCost += problem.getWeights().get(idxJ).get(idxJ + 1);
    }
    // (j-1,j)
    if (idxJ - 1 >= 0) {
      differentialCost += problem.getWeights().get(idxJ - 1).get(idxJ);
    }
    return differentialCost;
  }
}
