package uni.evocomp.a1;

import uni.evocomp.util.IntegerPair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RandomizedLocalSearch extends LocalSearch {

  RandomizedLocalSearch() {
    super();
  }

  public void solve() {
    // (Jack): Lol Java... so verbose
    List<Integer> outerIdx = IntStream.range(0,this.problem.getSize()-1).boxed().collect(Collectors.toList());
    List<Integer> innerIdx = IntStream.range(0,this.problem.getSize()-1).boxed().collect(Collectors.toList());

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
          N.run(s, new ArrayList<>(Arrays.asList(indexPair)));
          double cost = evaluate.evaluate(problem, s);
          if (updateCost(cost, s)) {
            madeChange = true;
          }
          totalIterations++;
        }
      }
    }
    System.out.println("\nTotal iterations was "+totalIterations );
    System.out.println("Known Best is: " + knownBestCost);
  }
}
