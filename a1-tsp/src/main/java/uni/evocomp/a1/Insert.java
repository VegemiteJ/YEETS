package uni.evocomp.a1;

import java.util.Iterator;
import java.util.List;
import uni.evocomp.util.IntegerPair;
import uni.evocomp.util.Pair;

/**
 * Move the second to follow the first, shifting the rest along to accommodate. As the indices n and
 * m are not necessarily ordered, must extract the highest and lowest
 * 
 * @author Namdrib
 *
 */
public class Insert implements Mutate {

  /**
   * @param individual Individual on which to perform a mutation operation
   * @param n first index to insert
   * @param m last index to insert
   * @throws IndexOutOfBoundsException
   * @throws NullPointerException
   */
  private void insert(Individual individual, int n, int m)
      throws IndexOutOfBoundsException, NullPointerException {
    // Out of bounds
    if (n < 0 || m < 0 || n >= individual.getGenotype().size()
        || m >= individual.getGenotype().size()) {
      throw new IndexOutOfBoundsException();
    }
    int first = Math.min(n, m);
    int second = Math.max(n, m);

    individual.getGenotype().add(first + 1, individual.getGenotype().get(second));
    individual.getGenotype().remove(second + 1);
  }

  @Override
  public void run(Individual i, List<IntegerPair> pairs)
      throws IndexOutOfBoundsException, NullPointerException {
    for (Iterator<IntegerPair> it = pairs.iterator(); it.hasNext();) {
      Pair p = it.next();
      insert(i, (int) p.first, (int) p.second);
    }
  }
}