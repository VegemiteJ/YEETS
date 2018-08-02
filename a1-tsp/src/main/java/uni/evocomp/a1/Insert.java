package uni.evocomp.a1;

import java.util.Iterator;
import java.util.List;
import uni.evocomp.util.Pair;

public class Insert implements Mutate {

  /**
   * Move the second to follow the first, shifting the rest along to accommodate. As the indices n
   * and m are not necessarily ordered, must extract the highest and lowest
   * 
   * 
   * @param i Individual on which to perform a mutation operation
   * @param n first index to insert
   * @param m last index to insert
   * @throws IndexOutOfBoundsException
   * @throws NullPointerException
   */
  private void insert(Individual i, int n, int m)
      throws IndexOutOfBoundsException, NullPointerException {
    if (i == null) {
      throw new NullPointerException();
    }

    int first = Math.min(n, m);
    int second = Math.max(n, m);

    i.getGenotype().add(first + 1, i.getGenotype().get(second));
    i.getGenotype().remove(second + 1);
  }

  @Override
  public void run(Individual i, List<Pair<Integer, Integer>> pairs)
      throws IndexOutOfBoundsException, NullPointerException {
    for (Iterator<Pair<Integer, Integer>> it = pairs.iterator(); it.hasNext();) {
      Pair<Integer, Integer> p = it.next();
      insert(i, (int) p.first, (int) p.second);
    }
  }

}
