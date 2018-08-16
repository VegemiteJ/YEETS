package uni.evocomp.a1.mutate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import uni.evocomp.a1.Individual;
import uni.evocomp.a1.TSPProblem;
import uni.evocomp.util.IntegerPair;

/**
 * An interface to handle representing mutation operations. The run method should be overriden in
 * implementations to call a private function.
 *
 * <p>All implementations should be stateless w.r.t the mutator.
 *
 * <p>Mutators recalculate differential costs relevant to their operation and update accordingly on
 * Individual.
 *
 * @author Namdrib
 */
public abstract class MutateImpl implements Mutate {

  /**
   * Perform a list of mutation operations on an Individual
   *
   * @param individual Individual on which to perform a mutation operation
   * @param pair List of Points whose x and y dictate which indices to use when mutating
   */
  @Override
  public abstract void run(TSPProblem problem, Individual individual, IntegerPair pair);

  /**
   * Perform a random mutate with probability mutateProbability. mutateProbability must be in range
   * [0.0, 1.0] with a value of 1.0 representing a guaranteed mutation (random indicies)
   */
  @Override
  public void mutateWithProbability(double mutateProbability, TSPProblem problem,
      Individual individual, Random rand) {}
}
