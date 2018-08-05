package uni.evocomp.a1;

import java.util.List;
import uni.evocomp.util.Bounds;
import uni.evocomp.util.IntegerPair;
import uni.evocomp.util.Pair;


public class Jump implements Mutate {

  /**
   * Perform a list of mutation operations on an Individual
   *
   * @param individual Individual on which to perform a mutation operation
   * @param pairs List of Points whose x and y dictate which indices to use when mutating
   */
  @Override
  public void run(TSPProblem problem, Individual individual, List<IntegerPair> pairs) {
    for (Pair p : pairs) {
      jumpSingle(problem, individual, p);
    }
  }

  private void printArr(List<Integer> arr) {
    for (Integer i : arr) {
      System.out.print(String.format("%d ", i));
    }
    System.out.println();
  }

  private void jumpSingle(TSPProblem problem, Individual i, Pair<Integer, Integer> pair) {
    double cost = i.getCost();
    cost -= calculateDifferentialCost(problem, i, pair.first, pair.second);

    List<Integer> data = i.getGenotype();
    if (!Bounds.inBounds(data, pair.first) || !Bounds.inBounds(data, pair.second)) {
      throw new IndexOutOfBoundsException(
          String.format("Jump called with invalid index %d, %d\n", pair.first, pair.second));
    }
    if (pair.first <= pair.second) {
      Integer value = data.get(pair.first);
      for (int i1 = pair.first; i1 < pair.second; i1++) {
        data.set(i1, data.get(i1 + 1));
      }
      data.set(pair.second, value);
    } else {
      Integer value = data.get(pair.first);
      for (int i1 = pair.first; i1 > pair.second; i1--) {
        data.set(i1, data.get(i1 - 1));
      }
      data.set(pair.second, value);
    }

    cost += calculateDifferentialCost(problem, i, pair.first, pair.second);
    i.setCost(cost);
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
    return differentialCost;
  }

}
