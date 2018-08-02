package uni.evocomp.a1;

import java.util.List;
import uni.evocomp.util.Pair;

/**
 * An interface to handle representing mutation operations. The run method should be overriden in
 * implementations to call a private function.
 * 
 * All implementations should be stateless.
 * 
 * @author Namdrib
 *
 */
public interface Mutate {
  /**
   * Perform a list of mutation operations on an Individual
   * 
   * @param individual Individual on which to perform a mutation operation
   * @param pairs List of Pair whose <code>first</code> and <code>second</code> dictate which
   *        indices to use when mutating
   * @throws IndexOutOfBoundsException
   * @throws NullPointerException
   */
  public void run(Individual individual, List<Pair<Integer, Integer>> pairs)
      throws IndexOutOfBoundsException, NullPointerException;
}
