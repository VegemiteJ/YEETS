package uni.evocomp.a1;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import uni.evocomp.util.IntegerPair;
import uni.evocomp.util.Pair;

/**
 * 
 * Pick two alleles at random and then invert the substring between them.
 * 
 * @author Namdrib
 *
 */
public class Invert implements Mutate {

  private double calculateDifferentialCost(TSPProblem problem, Individual individual, int n, int m) {
    double differentialCost = 0.0;
    int idxI = individual.getGenotype().get(n);
    int idxJ = individual.getGenotype().get(m);
    // (i-1,i)
    if (idxI - 1 >= 0) {
      differentialCost += problem.getWeights().get(idxI - 1).get(idxI);
    }
    // (j,j+1)
    if (idxJ + 1 < problem.getWeights().get(idxJ).size()) {
      differentialCost += problem.getWeights().get(idxJ).get(idxJ + 1);
    }
    System.out.println("Cost: " + differentialCost);
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
    System.out.println("Subtracting Cost");
    cost -= calculateDifferentialCost(problem, individual, n, m);
    int first = Math.min(n, m);
    int second = Math.max(n, m);
    for (int j = first; j < second; j++) {
      Collections.swap(individual.getGenotype(), first, second);
      first++;
      second--;
    }
    System.out.println("Adding Cost");
    cost += calculateDifferentialCost(problem, individual, n, m);
    individual.setCost(cost);
  }

  @Override
  public void run(TSPProblem problem, Individual individual, List<IntegerPair> pairs) {
    for (Iterator<IntegerPair> it = pairs.iterator(); it.hasNext(); ) {
      Pair p = it.next();
      invert(problem, individual, (int) p.first, (int) p.second);
    }
  }
}
