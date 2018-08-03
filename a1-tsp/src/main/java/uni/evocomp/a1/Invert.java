package uni.evocomp.a1;

import uni.evocomp.util.IntegerPair;
import uni.evocomp.util.Pair;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Invert implements Mutate {

  /**
   * Pick two alleles at random and then invert the substring between them.
   *
   * @param i Individual on which to perform a mutation operation
   * @param n first index to insert
   * @param m last index to insert
   */
  private void invert(Individual i, int n, int m) {
    int first = Math.min(n, m);
    int second = Math.min(n, m);

    try {
      for (int j = first; j < second; j++) {
        Collections.swap(i.getGenotype(), first, second);
        first++;
        second--;
      }
    } catch (IndexOutOfBoundsException ex) {
      return;
    }
  }

  @Override
  public void run(Individual i, List<IntegerPair> pairs) {
    for (Iterator<IntegerPair> it = pairs.iterator(); it.hasNext(); ) {
      Pair p = it.next();
      invert(i, (int) p.second, (int) p.second);
    }
  }
}
