package uni.evocomp.a1.mutate;

import java.util.Collections;
import uni.evocomp.a1.Individual;
import uni.evocomp.a1.TSPProblem;
import uni.evocomp.util.IntegerPair;

/**
 * Pick two alleles at random and then invert the substring between them.
 *
 * @author Namdrib
 */
public class Invert extends MutateImpl {

  public Invert() {
    ;
  }

  @Override
  public void run(TSPProblem problem, Individual individual, IntegerPair pair) {
    invert(problem, individual, pair.first, pair.second);
  }

  /**
   * Pick two alleles at random and then invert the substring between them.
   *
   * @param problem <code>TSPProblem</code> used to update costs
   * @param individual <code>Individual</code> on which to perform a mutation operation
   * @param n first index to insert
   * @param m last index to insert
   */
  private void invert(TSPProblem problem, Individual individual, int n, int m) {
    double cost = individual.getCost(problem);
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

  private double calculateDifferentialCost(
      TSPProblem problem, Individual individual, int n, int m) {

    int size = problem.getSize();
    double differentialCost = 0.0;

    int a = Math.min(n, m);
    m = Math.max(n, m);
    n = a;

    int idxI = individual.getGenotype().get(n) - 1;
    int idxJ = individual.getGenotype().get(m) - 1;

    // (i - 1, i)
    int idxINegOne = individual.getGenotype().get((n - 1 + size) % size) - 1;
    differentialCost += problem.getWeights().get(idxINegOne, idxI);

    // (j, j + 1)
    int idxJPosOne = individual.getGenotype().get((m + 1 + size) % size) - 1;
    differentialCost += problem.getWeights().get(idxJ, idxJPosOne);

    return differentialCost;
  }
}
