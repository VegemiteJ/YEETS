package uni.evocomp.a1;

import java.util.List;
import uni.evocomp.util.IntegerPair;

/**
 * An interface to handle representing mutation operations. The run method should be overriden in
 * implementations to call a private function.
 *
 * <p>All implementations should be stateless w.r.t the mutator.
 * <p> Mutators recalculate differential costs relevant to their operation and update accordingly on
 * Individual.
 *
 * @author Namdrib
 */
public interface Mutate {

  /**
   * Perform a list of mutation operations on an Individual
   *
   * @param individual Individual on which to perform a mutation operation
   * @param pairs List of Points whose x and y dictate which indices to use when mutating
   */
  public void run(TSPProblem problem, Individual individual, List<IntegerPair> pairs);
}
