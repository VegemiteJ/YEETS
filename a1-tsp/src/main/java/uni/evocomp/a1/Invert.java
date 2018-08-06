package uni.evocomp.a1;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import uni.evocomp.util.IntegerPair;

/**
 * Pick two alleles at random and then invert the substring between them.
 *
 * @author Namdrib
 */
public class Invert implements Mutate {

  private double calculateDifferentialCost(TSPProblem problem, Individual individual, int n,
      int m) {
    int a = Math.min(n, m);
    m = Math.max(n, m);
    n = a;
    double differentialCost = 0.0;
    int idxI = individual.getGenotype().get(n) - 1;
    int idxJ = individual.getGenotype().get(m) - 1;
    // (i-1,i)
    if (n - 1 >= 0) {
      int idxINegOne = individual.getGenotype().get(n - 1) - 1;
      differentialCost += problem.getWeights().get(idxINegOne, idxI);
    }
    // (j,j+1)
    if (m + 1 < problem.getWeights().size()) {
      int idxJPosOne = individual.getGenotype().get(m + 1) - 1;
      differentialCost += problem.getWeights().get(idxJ, idxJPosOne);
    }
    return differentialCost;
  }

  /**
   * Pick two alleles at random and then invert the substring between them.
   *
   * @param individual Individual on which to perform a mutation operation
   * @param n first index to insert
   * @param m last index to insert
   */
  private void invert(TSPProblem problem, Individual individual, int n, int m) {
    double cost = individual.getCost();
    cost -= calculateDifferentialCost(problem, individual, n, m);
    int first = Math.min(n, m);
    int second = Math.max(n, m);
    for (int j = first; j < second; j++) {
      Collections.swap(individual.getGenotype(), first, second);
      first++;
      second--;
    }
    cost += calculateDifferentialCost(problem, individual, n, m);
    individual.setCost(cost);
  }

  @Override
  public void run(TSPProblem problem, Individual individual, List<IntegerPair> pairs) {
    for (Iterator<IntegerPair> it = pairs.iterator(); it.hasNext(); ) {
      IntegerPair p = it.next();
      invert(problem, individual, p.first, p.second);
    }
  }
}
