package uni.evocomp.a1;

import java.util.List;
import uni.evocomp.util.Bounds;
import uni.evocomp.util.IntegerPair;


public class Jump implements Mutate {
  /**
   * Perform a list of mutation operations on an Individual
   *
   * @param i Individual on which to perform a mutation operation
   * @param pairs List of Points whose x and y dictate which indices to use when mutating
   */
  @Override
  public void run(Individual i, List<IntegerPair> pairs) {
    for (IntegerPair p : pairs) {
      jumpSingle(i, p);
    }
  }

  private void jumpSingle(Individual i, IntegerPair pair) {
    List<Integer> data = i.getGenotype();
    if (!Bounds.inBounds(data, pair.first) || !Bounds.inBounds(data, pair.second)) {
      throw new IndexOutOfBoundsException(
          String.format("Jump called with invalid index %d, %d\n", pair.first, pair.second));
    }
    int value = data.get(pair.first);
    if (pair.first <= pair.second) {
      for (int i1 = pair.first; i1 < pair.second; i1++) {
        data.set(i1, data.get(i1 + 1));
      }
      data.set(pair.second, value);
    } else {
      for (int i1 = pair.first; i1 > pair.second; i1--) {
        data.set(i1, data.get(i1 - 1));
      }
      data.set(pair.second, value);
    }
  }
}
