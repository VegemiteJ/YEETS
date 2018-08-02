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

  /**
   * @param individual Individual on which to perform a mutation operation
   * @param n first index to insert
   * @param m last index to insert
   * @throws IndexOutOfBoundsException
   * @throws NullPointerException
   */
  private void invert(Individual individual, int n, int m)
      throws IndexOutOfBoundsException, NullPointerException {
    int first = Math.min(n, m);
    int second = Math.max(n, m);

    for (int i = first; i < second; i++) {
      Collections.swap(individual.getGenotype(), first, second);
      first++;
      second--;
    }
  }

  @Override
  public void run(Individual individual, List<IntegerPair> pairs)
      throws IndexOutOfBoundsException, NullPointerException {
    for (Iterator<IntegerPair> it = pairs.iterator(); it.hasNext();) {
      Pair p = it.next();
      invert(individual, (int) p.first, (int) p.second);
    }
  }
}
