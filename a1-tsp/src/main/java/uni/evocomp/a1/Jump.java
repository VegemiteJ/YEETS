package uni.evocomp.a1;

import uni.evocomp.util.Bounds;
import uni.evocomp.util.IntegerPair;
import uni.evocomp.util.Pair;

import java.util.List;

public class Jump implements Mutate {
  /**
   * Perform a list of mutation operations on an Individual
   *
   * @param i Individual on which to perform a mutation operation
   * @param pairs List of Points whose x and y dictate which indices to use when mutating
   */
  @Override
  public void run(Individual i, List<IntegerPair> pairs) {
    for (Pair p : pairs) {
      jumpSingle(i, p);
    }
  }

  private void printArr(List<Integer> arr) {
    for (Integer i : arr) {
      System.out.print(String.format("%d ",i));
    }
    System.out.println();
  }

  private void jumpSingle(Individual i, Pair<Integer, Integer> pair) {
    List<Integer> data = i.getGenotype();
    if (!Bounds.inBounds(data, pair.first) || !Bounds.inBounds(data, pair.second)) {
      throw new IndexOutOfBoundsException(
          String.format("Jump called with invalid index %d, %d\n", pair.first, pair.second));
    }
    Integer value = data.get(pair.first);
    for (int i1 = pair.first; i1 < pair.second; i1++) {
      data.set(i1, data.get(i1 + 1));
    }
    data.set(pair.second, value);
  }
}
