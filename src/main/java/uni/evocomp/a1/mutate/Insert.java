package uni.evocomp.a1.mutate;

import uni.evocomp.a1.Individual;
import uni.evocomp.a1.TSPProblem;
import uni.evocomp.util.Bounds;
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

  @Override
  public void run(TSPProblem problem, Individual individual, IntegerPair pair) {
    insert(individual, pair.first, pair.second);
  }

  /**
   * Move the second to follow the first, shifting the rest along to accommodate. As the indices n
   * and m are not necessarily ordered, must extract the highest and lowest
   *
   * @param individual <code>Individual</code> on which to perform a mutation operation
   * @param n first index to insert
   * @param m last index to insert
   */
  private void insert(Individual individual, int n, int m)
      throws IndexOutOfBoundsException, NullPointerException {
    // Out of bounds
    if (!Bounds.inBounds(individual.getGenotype(), n)
        || !Bounds.inBounds(individual.getGenotype(), m)) {
      throw new IndexOutOfBoundsException();
    }
    int first = Math.min(n, m);
    int second = Math.max(n, m);

    individual.getGenotype().add(first + 1, individual.getGenotype().get(second));
    individual.getGenotype().remove(second + 1);
  }
}
