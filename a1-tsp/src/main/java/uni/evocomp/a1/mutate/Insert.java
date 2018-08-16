package uni.evocomp.a1.mutate;

import java.util.Iterator;
import java.util.List;
import uni.evocomp.a1.Individual;
import uni.evocomp.a1.TSPProblem;
import uni.evocomp.util.IntegerPair;

/**
 * Move the second to follow the first, shifting the rest along to accommodate. As the indices n and
 * m are not necessarily ordered, must extract the highest and lowest
 *
 * @author Namdrib
 */
public class Insert extends MutateImpl {

  public Insert() {
    ;
  }

  /**
   * Move the second to follow the first, shifting the rest along to accommodate. As the indices n
   * and m are not necessarily ordered, must extract the highest and lowest
   *
   * @param individual Individual on which to perform a mutation operation
   * @param n first index to insert
   * @param m last index to insert
   */
  private void insert(Individual individual, int n, int m)
      throws IndexOutOfBoundsException, NullPointerException {
    // Out of bounds
    if (n < 0
        || m < 0
        || n >= individual.getGenotype().size()
        || m >= individual.getGenotype().size()) {
      throw new IndexOutOfBoundsException();
    }
    int first = Math.min(n, m);
    int second = Math.max(n, m);

    individual.getGenotype().add(first + 1, individual.getGenotype().get(second));
    individual.getGenotype().remove(second + 1);
  }

  @Override
  public void run(TSPProblem problem, Individual individual, IntegerPair pair) {
    insert(individual, pair.first, pair.second);
  }
}
