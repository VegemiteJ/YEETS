package uni.evocomp.a1;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import uni.evocomp.util.Pair;

/**
 * 
 * Pick two alleles at random and then invert the substring between them.
 * 
 * @author Namdrib
 *
 */
public class Invert implements Mutate {

  /**
   * 
   * @param i Individual on which to perform a mutation operation
   * @param n first index to insert
   * @param m last index to insert
   * @throws IndexOutOfBoundsException
   * @throws NullPointerException
   */
  private void invert(Individual i, int n, int m)
      throws IndexOutOfBoundsException, NullPointerException {
    int first = Math.min(n, m);
    int second = Math.max(n, m);

    for (int j = first; j < second; j++) {
      Collections.swap(i.getGenotype(), first, second);
      first++;
      second--;
    }
  }

  @Override
  public void run(Individual i, List<Pair<Integer, Integer>> pairs)
      throws IndexOutOfBoundsException, NullPointerException {
    for (Iterator<Pair<Integer, Integer>> it = pairs.iterator(); it.hasNext();) {
      Pair<Integer, Integer> p = it.next();
      invert(i, (int) p.first, (int) p.second);
    }
  }

}
