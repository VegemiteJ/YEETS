package uni.evocomp.a1;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import uni.evocomp.util.Pair;

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
    int second = Math.max(n, m);

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
  public void run(Individual i, List<Pair<Integer, Integer>> pairs) {
    for (Iterator<Pair<Integer, Integer>> it = pairs.iterator(); it.hasNext();) {
      Pair<Integer, Integer> p = it.next();
      invert(i, (int) p.first, (int) p.second);
    }
  }

}
