package uni.evocomp.a1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import uni.evocomp.util.IntegerPair;

public class RandomizedLocalSearch extends LocalSearch {

  private final boolean USE_RANDOM = true;

  RandomizedLocalSearch(TSPProblem problem, Evaluate evaluate, Mutate mutationFunction) {
    super(problem, evaluate, mutationFunction);
  }

  private void modifyAccessOrdering(List<Integer> idxAccessOrdering) {
    if (USE_RANDOM) {
      Collections.shuffle(idxAccessOrdering);
    }
  }

  @Override
  public Double solve() {
    // Initial solution
    this.currentBestIndividual = new Individual(problem.getSize());

    // Initial cost
    double currentBestCost = evaluate.evaluate(problem, currentBestIndividual);
    currentBestIndividual.setCost(currentBestCost);

    // (Jack): Lol Java... so verbose
    List<Integer> outerIdx =
        IntStream.range(0, this.problem.getSize() - 1).boxed().collect(Collectors.toList());
    List<Integer> innerIdx =
        IntStream.range(0, this.problem.getSize() - 1).boxed().collect(Collectors.toList());

    LinkedList<Integer> iterationsSinceLastBest = new LinkedList<>();
    iterationsSinceLastBest.addLast(0);

    int totalIterations = 0;
    boolean madeChange = true;
    while (madeChange) {
      // Shuffle indexes
      modifyAccessOrdering(outerIdx);
      modifyAccessOrdering(innerIdx);
      madeChange = false;
      for (Integer anOuterIdx : outerIdx) {
        for (Integer anInnerIdx : innerIdx) {
          Individual s = new Individual(currentBestIndividual);
          IntegerPair indexPair = new IntegerPair(anOuterIdx, anInnerIdx);
          mutator.run(problem, s, new ArrayList<>(Arrays.asList(indexPair)));
          double cost = s.getCost();
          if (cost < currentBestCost) {
            currentBestCost = cost;
            currentBestIndividual = s;
            if (totalIterations % 100 == 0) {
              System.out.println(
                  "New Best: "
                      + currentBestCost
                      + " - iterations since last best: "
                      + (totalIterations
                      - iterationsSinceLastBest.getLast()));
            }
            madeChange = true;
            iterationsSinceLastBest.addLast(totalIterations);
          }
          totalIterations++;
        }
      }
    }
    currentBestIndividual.assertIsValidTour();
    return currentBestCost;
  }
}
