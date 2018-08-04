package uni.evocomp.a1;

import uni.evocomp.util.IntegerPair;
import uni.evocomp.util.Pair;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Perform a swap on position n and m of i's genotype
 *
 * @author Namdrib
 */
public class Swap implements Mutate {

  /**
   * @param individual Individual on which to perform a mutation operation
   * @param n first index to swap
   * @param m last index to swap
   * @throws IndexOutOfBoundsException
   * @throws NullPointerException
   */
  private void swap(Individual individual, int n, int m)
      throws IndexOutOfBoundsException, NullPointerException {
    Collections.swap(individual.getGenotype(), n, m);
  }

  @Override
  public void run(Individual individual, List<IntegerPair> pairs)
      throws IndexOutOfBoundsException, NullPointerException {
    for (Iterator<IntegerPair> it = pairs.iterator(); it.hasNext(); ) {
      Pair p = it.next();
      swap(individual, (int) p.first, (int) p.second);
    }
  }
}
