package uni.evocomp.a1;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import uni.evocomp.a1.logging.BenchmarkStatsTracker;
import uni.evocomp.a1.mutate.Mutate;
import uni.evocomp.util.IntegerPair;

public class RandomizedLocalSearch extends LocalSearch {

  private final boolean USE_RANDOM = true;
  private final long maxTime = 300000000000L; // 5 minutes

  RandomizedLocalSearch(TSPProblem problem, Mutate mutationFunction) {
    super(problem, mutationFunction);
  }

  private void modifyAccessOrdering(List<Integer> idxAccessOrdering) {
    if (USE_RANDOM) {
      Collections.shuffle(idxAccessOrdering);
    }
  }

  @Override
  public Individual solve(BenchmarkStatsTracker bst) {
    // Initial solution
    this.currentBestIndividual = new Individual(problem);

    List<Integer> outerIdx =
        IntStream.range(0, this.problem.getSize() - 1).boxed().collect(Collectors.toList());
    List<Integer> innerIdx =
        IntStream.range(0, this.problem.getSize() - 1).boxed().collect(Collectors.toList());

    totalIterations = 0L;
    boolean madeChange = true;
    long start = System.nanoTime();
    loops:
    while (madeChange) {
      // Shuffle indexes
      modifyAccessOrdering(outerIdx);
      modifyAccessOrdering(innerIdx);
      madeChange = false;
      for (Integer anOuterIdx : outerIdx) {
        if (System.nanoTime() - start > maxTime) {
          break loops;
        }
        for (Integer anInnerIdx : innerIdx) {
          Individual s = new Individual(currentBestIndividual);
          IntegerPair indexPair = new IntegerPair(anOuterIdx, anInnerIdx);
          mutator.run(problem, s, indexPair);
          double cost = s.getCost(problem);
          if (cost < currentBestIndividual.getCost(problem)) {
            currentBestIndividual = s;
            madeChange = true;
            bst.newBestIndividualForSingleRun(s, totalIterations);
          }
          totalIterations++;
        }
      }
    }

    currentBestIndividual.assertIsValidTour();
    currentBestIndividual.assertIsValidCost(problem);
    return currentBestIndividual;
  }
}
