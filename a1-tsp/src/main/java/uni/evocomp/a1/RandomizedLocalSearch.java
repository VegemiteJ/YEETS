package uni.evocomp.a1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import uni.evocomp.util.IntegerPair;

public class RandomizedLocalSearch extends LocalSearch {

  RandomizedLocalSearch(TSPProblem problem, Evaluate evaluate, Mutate mutationFunction) {
    super(problem, evaluate, mutationFunction);
  }

  @Override
  public Double solve() {
    double currentBestCost = evaluate.evaluate(problem, currentBestIndividual);

    // (Jack): Lol Java... so verbose
    List<Integer> outerIdx =
        IntStream.range(0, this.problem.getSize() - 1).boxed().collect(Collectors.toList());
    List<Integer> innerIdx =
        IntStream.range(0, this.problem.getSize() - 1).boxed().collect(Collectors.toList());

    int totalIterations = 0;
    boolean madeChange = true;
    while (madeChange) {
      // Shuffle indexes
      Collections.shuffle(outerIdx);
      Collections.shuffle(innerIdx);
      madeChange = false;
      for (Integer anOuterIdx : outerIdx) {
        for (Integer anInnerIdx : innerIdx) {
          Individual s = new Individual(currentBestIndividual);
          IntegerPair indexPair = new IntegerPair(anOuterIdx, anInnerIdx);
          mutator.run(s, new ArrayList<>(Arrays.asList(indexPair)));
          s.assertIsValidTour();
          double cost = evaluate.evaluate(problem, s);
          if (cost < currentBestCost) {
            currentBestCost = cost;
            currentBestIndividual = s;
             madeChange = true;
          }
          totalIterations++;
        }
      }
    }
    return currentBestCost;
  }
}
