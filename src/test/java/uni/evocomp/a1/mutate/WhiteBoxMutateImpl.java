package uni.evocomp.a1.mutate;

import java.util.List;
import uni.evocomp.a1.Individual;
import uni.evocomp.a1.TSPProblem;
import uni.evocomp.util.IntegerPair;

public class WhiteBoxMutateImpl extends MutateImpl {

  /**
   * Given a pair of mutations, set city pair.first = -1 and pair.second = -2;
   *
   * @param individual Individual on which to perform a mutation operation
   * @param pair List of Points whose x and y dictate which indices to use when mutating
   */
  @Override
  public void run(TSPProblem problem, Individual individual, IntegerPair pair) {
    List<Integer> lists = individual.getGenotype();
    lists.set(pair.first, -1);
    lists.set(pair.second, -2);
  }
}
