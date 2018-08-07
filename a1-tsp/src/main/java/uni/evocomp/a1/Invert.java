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
   * Pick two alleles at random and then invert the substring between them.
   *
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
    for (int j = first; j < second; j++) {
      Collections.swap(individual.getGenotype(), first, second);
      first++;
      second--;
    }
  }

  @Override
  public void run(Individual i, List<IntegerPair> pairs) {
    for (Iterator<IntegerPair> it = pairs.iterator(); it.hasNext();) {
      IntegerPair p = it.next();
      invert(i, p.first, p.second);
    }
  }
}
