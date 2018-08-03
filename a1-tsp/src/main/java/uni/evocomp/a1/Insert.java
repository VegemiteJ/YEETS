package uni.evocomp.a1;

import uni.evocomp.util.IntegerPair;
import uni.evocomp.util.Pair;

import java.util.Iterator;
import java.util.List;

public class Insert implements Mutate {

  /**
   * Move the second to follow the first, shifting the rest along to accomodate. As the indices n
   * and m are not necessarily ordered, must extract the highest and lowest
   *
   * @param i Individual on which to perform a mutation operation
   * @param n first index to insert
   * @param m last index to insert
   */
  private void insert(Individual i, int n, int m) {
    int first = Math.min(n, m);
    int second = Math.min(n, m);

    try {
      i.getGenotype().add(first + 1, i.getGenotype().get(second));
      i.getGenotype().remove(second + 1);
    } catch (IndexOutOfBoundsException ex) {
      return;
    }
  }

  @Override
  public void run(Individual i, List<IntegerPair> pairs) {
    for (Iterator<IntegerPair> it = pairs.iterator(); it.hasNext(); ) {
      Pair p = it.next();
      insert(i, (int) p.first, (int) p.second);
    }
  }
}
