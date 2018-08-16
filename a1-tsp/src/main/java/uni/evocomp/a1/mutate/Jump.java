package uni.evocomp.a1.mutate;

import java.util.List;
import uni.evocomp.a1.Individual;
import uni.evocomp.a1.TSPProblem;
import uni.evocomp.util.Bounds;
import uni.evocomp.util.IntegerPair;
import uni.evocomp.util.Matrix;

public class Jump extends MutateImpl {

  public Jump() {
    ;
  }

  @Override
  public void run(TSPProblem problem, Individual individual, IntegerPair pair) {
    jumpSingle(problem, individual, pair);
  }

  private void printArr(List<Integer> arr) {
    for (Integer i : arr) {
      System.out.print(String.format("%d ", i));
    }
    System.out.println();
  }

  private void jumpSingle(TSPProblem problem, Individual i, IntegerPair pair) {
    double cost = i.getCost(problem);

    List<Integer> data = i.getGenotype();
    if (!Bounds.inBounds(data, pair.first) || !Bounds.inBounds(data, pair.second)) {
      throw new IndexOutOfBoundsException(
          String.format("Jump called with invalid index %d, %d\n", pair.first, pair.second));
    }
    if (pair.first <= pair.second) {
      cost -= calculateDifferentialCost(problem, i, pair.first, pair.second, true, true);
      Integer value = data.get(pair.first);
      for (int i1 = pair.first; i1 < pair.second; i1++) {
        data.set(i1, data.get(i1 + 1));
      }
      data.set(pair.second, value);
      cost += calculateDifferentialCost(problem, i, pair.first, pair.second, false, true);
    } else {
      cost -= calculateDifferentialCost(problem, i, pair.first, pair.second, true, false);
      Integer value = data.get(pair.first);
      for (int i1 = pair.first; i1 > pair.second; i1--) {
        data.set(i1, data.get(i1 - 1));
      }
      data.set(pair.second, value);
      cost += calculateDifferentialCost(problem, i, pair.first, pair.second, false, false);
    }

    i.setCost(cost);
  }

  /*
   * Notes about edges changed in jump operation:
   *   Forwards
   *     Remove(Before):(i-1,i),(i,i+1),(j,j+1)
   *     Add(After)    :(i-1,i),(j-1,j),(j,j+1)
   *   Backwards
   *     Remove(Before):(i-1,i),(j-1,j),(j,j+1)
   *     Add(After)    :(i-1,i),(i,i+1),(j,j+1)
   *   Only middle column changes, handled with boolean logic
   *
   *   However doesn't cancel if no-op (i==j)
   *   or if path just rotates (a-b-c -> b-c-a)
   *   needs to be handled as special case
   */

  /**
   * Find difference in cost due to the Jump mutation, call before the mutation to get cost of edges
   * removed. Call after mutation to find cost of new edges added.
   *
   * @param problem TSPProblem that individual is making a tour for
   * @param individual Individual that is undergoing a Jump mutation
   * @param i mutation index
   * @param j mutation index
   * @param beforeJump Set True if being called before mutation operation, False if being called
   * after mutation
   * @param forwards Set true if mutation is a forward jump (node at i moves forwards to j), false
   * if backwards jump (node at i moves backwards to j
   * @return Cost of edges that were removed (when beforeJump==true) or cost of edges added (when
   * beforeJump==false)
   */
  private double calculateDifferentialCost(
      TSPProblem problem, Individual individual, int i, int j, boolean beforeJump,
      boolean forwards) {
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

    //Prepare modulo indices
    int n = problem.getSize();
    int i_plus_1 = (i + 1 + n) % n;
    int i_minus_1 = (i - 1 + n) % n;
    int j_plus_1 = (j + 1 + n) % n;
    int j_minus_1 = (j - 1 + n) % n;

    double differentialCost = 0.0;

    //Stop if jump is just a rotate (no change in cost)
    if (j_plus_1 == i) {
      return 0;
    }

    // (i-1,i)
    differentialCost += weights.get(g.get(i_minus_1) - 1, g.get(i) - 1);

    //if (forwards && before) or (backwards && after)
    if (forwards == beforeJump) {
      // (i,i+1)
      differentialCost += weights.get(g.get(i) - 1, g.get(i_plus_1) - 1);
    } else {
      // (j-1,j)
      differentialCost += weights.get(g.get(j_minus_1) - 1, g.get(j) - 1);
    }

    // (j,j+1)
    differentialCost += weights.get(g.get(j) - 1, g.get(j_plus_1) - 1);

    return differentialCost;
  }
}
