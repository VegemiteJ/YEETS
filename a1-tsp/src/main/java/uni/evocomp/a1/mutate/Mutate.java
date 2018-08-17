package uni.evocomp.a1.mutate;

import java.util.Random;
import uni.evocomp.a1.Individual;
import uni.evocomp.a1.TSPProblem;
import uni.evocomp.util.IntegerPair;

/**
 * An interface to handle representing mutation operations. The run method should be overriden in
 * implementations to call a private function.
 *
 * <p>
 * All implementations should be stateless w.r.t the mutator.
 *
 * <p>
 * Mutators recalculate differential costs relevant to their operation and update accordingly on
 * Individual.
 *
 * @author Namdrib
 */
public interface Mutate {

  /**
   * Perform a list of mutation operations on an Individual
   *
   * @param problem <code>TSPProblem</code> used to update costs
   * @param individual <code>Individual</code> on which to perform a mutation operation
   * @param pair <code>Pair</code> of <code>IntegerPairs</code> whose <code>first</code> and
   *        <code>second</code> dictate which indices to use when mutating
   */
  void run(TSPProblem problem, Individual individual, IntegerPair pair);

  /**
   * Perform a mutate with random indices with probability mutateProbability.
   *
   * @param mutateProbability a value in the range <code>[0.0, 1.0]</code> with 0.0 representing no
   *        mutation and 1.0 for guaranteed mutation
   * @param problem <code>TSPProblem</code> used to update costs
   * @param individual <code>Individual</code> on which to perform a mutation operation
   * @param rand <code>Random</code> object to select indices between which the mutation occurs
   */
  void mutateWithProbability(double mutateProbability, TSPProblem problem, Individual individual,
      Random rand);
}
