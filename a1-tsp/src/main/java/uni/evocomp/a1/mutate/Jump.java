package uni.evocomp.a1.mutate;

import java.util.List;
import uni.evocomp.a1.Individual;
import uni.evocomp.a1.TSPProblem;
import uni.evocomp.util.Bounds;
import uni.evocomp.util.IntegerPair;
import uni.evocomp.util.Matrix;

public class Jump implements Mutate {

  /**
   * Perform a list of mutation operations on an Individual
   *
   * @param individual Individual on which to perform a mutation operation
   * @param pairs List of Points whose x and y dictate which indices to use when mutating
   */
  @Override
  public void run(TSPProblem problem, Individual individual, List<IntegerPair> pairs) {
    for (IntegerPair p : pairs) {
      jumpSingle(problem, individual, p);
    }
  }

  private void printArr(List<Integer> arr) {
    for (Integer i : arr) {
      System.out.print(String.format("%d ", i));
    }
    System.out.println();
  }

  private void jumpSingle(TSPProblem problem, Individual i, IntegerPair pair) {
    double cost = i.getCost();

    List<Integer> data = i.getGenotype();
    if (!Bounds.inBounds(data, pair.first) || !Bounds.inBounds(data, pair.second)) {
      throw new IndexOutOfBoundsException(
          String.format("Jump called with invalid index %d, %d\n", pair.first, pair.second));
    }
    if (pair.first <= pair.second) {
      cost -= calculateDifferentialCostForwards(problem, i, pair.first, pair.second, true);
      Integer value = data.get(pair.first);
      for (int i1 = pair.first; i1 < pair.second; i1++) {
        data.set(i1, data.get(i1 + 1));
      }
      data.set(pair.second, value);
      cost += calculateDifferentialCostForwards(problem, i, pair.first, pair.second, false);
    } else {
      cost -= calculateDifferentialCostBackwards(problem, i, pair.first, pair.second, true);
      Integer value = data.get(pair.first);
      for (int i1 = pair.first; i1 > pair.second; i1--) {
        data.set(i1, data.get(i1 - 1));
      }
      data.set(pair.second, value);
      cost += calculateDifferentialCostBackwards(problem, i, pair.first, pair.second, false);
    }

    i.setCost(cost);
  }

  /*Differential Cost summary:
  Forwards
    Remove(Before):(i-1,i),(i,i+1),(j,j+1)
    Add(After)    :(i-1,i),(j-1,j),(j,j+1)
   Backwards
    Remove(Before):(i-1,i),(j-1,j),(j,j+1)
    Add(After)    :(i-1,i),(i,i+1),(j,j+1)

  Only middle column is different, could refactor even further
  */

  private double calculateDifferentialCostForwards(
      TSPProblem problem, Individual individual, int i, int j, boolean beforeJump) {
    List<Integer> g = individual.getGenotype();
    Matrix weights = problem.getWeights();

    //no-op
    if (i == j) {
      return 0;
    }

    // Always make i<j
    if (i > j) {
      int t = i;
      i = j;
      j = t;
    }

    int n = problem.getSize();
    int i_plus_1 = (i + 1 + n) % n;
    int i_minus_1 = (i - 1 + n) % n;
    int j_plus_1 = (j + 1 + n) % n;
    int j_minus_1 = (j - 1 + n) % n;

    double differentialCost = 0.0;

    if(j_plus_1 == i) {
      return 0;
    }

    // (i-1,i)
    differentialCost += weights.get(g.get(i_minus_1) - 1, g.get(i) - 1);

    // (i,i+1)
    if (beforeJump) {
      differentialCost += weights.get(g.get(i) - 1, g.get(i_plus_1) - 1);
    }

    // (j-1,j)
    if (!beforeJump) {
      differentialCost += weights.get(g.get(j_minus_1) - 1, g.get(j) - 1);
    }
    // (j,j+1)
    differentialCost += weights.get(g.get(j) - 1, g.get(j_plus_1) - 1);

    return differentialCost;
  }

  private double calculateDifferentialCostBackwards(
      TSPProblem problem, Individual individual, int i, int j, boolean beforeJump) {
    List<Integer> g = individual.getGenotype();
    Matrix weights = problem.getWeights();

    if (i == j) {
      return 0;
    }

    // Always make i<j
    if (i > j) {
      int t = i;
      i = j;
      j = t;
    }

    int n = problem.getSize();
    int i_plus_1 = (i + 1 + n) % n;
    int i_minus_1 = (i - 1 + n) % n;
    int j_plus_1 = (j + 1 + n) % n;
    int j_minus_1 = (j - 1 + n) % n;

    if(j_plus_1 == i) {
      return 0;
    }

    double differentialCost = 0.0;

    // (i-1,i)
    differentialCost += weights.get(g.get(i_minus_1) - 1, g.get(i) - 1);

    // (i,i+1)
    if (!beforeJump) {
      differentialCost += weights.get(g.get(i) - 1, g.get(i_plus_1) - 1);
    }

    // (j-1,j)
    if (beforeJump) {
      differentialCost += weights.get(g.get(j_minus_1) - 1, g.get(j) - 1);
    }
    // (j,j+1)
    differentialCost += weights.get(g.get(j) - 1, g.get(j_plus_1) - 1);

    return differentialCost;
  }
}
