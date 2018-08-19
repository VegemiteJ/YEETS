package uni.evocomp.a1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import uni.evocomp.a1.logging.BenchmarkStatsTracker;
import uni.evocomp.a1.mutate.Mutate;
import uni.evocomp.a1.mutate.Invert;
import uni.evocomp.a1.Population;
import uni.evocomp.a1.TSPProblem;
import uni.evocomp.util.IntegerPair;

/**
 * Class for InverOver
 * 
 * Implements Inver Over Algorithm as described by
 * http://www.cs.adelaide.edu.au/~zbyszek/Papers/p44.pdf
 * 
 * @author Nehal
 *
 */

public class InverOver {

  BenchmarkStatsTracker bst;
  long numGenerations;

  final long printItr = 1000;

  InverOver(BenchmarkStatsTracker bst) {
    this.bst = bst;
  }

  /**
   * Run InverOver on a problem to produce the best Individual
   * 
   * @param problem
   * @param populationSize
   * @param maxGenerations how many generations to run
   * @param probability of random inversion
   * @return the best <code>Individual</code> after running for <code>maxGenerations</code>
   *         generations
   */
  public Individual run(TSPProblem problem, int populationSize, int maxGenerations,
      double probability) {
    // Randomly initialise population and store best Individual so far
    Population population = new Population(problem, populationSize);
    numGenerations = 1;
    Individual bestIndividual = Collections.min(population.getPopulation());
    bst.newBestIndividualForSingleRun(bestIndividual, numGenerations);
    List<Individual> populationList = new ArrayList<>();
    populationList.addAll(population.getPopulation());
    Mutate m = new Invert();

    int numInversions = 0;
    while (numGenerations <= maxGenerations) {
      for (Individual individual : population.getPopulation()) {
        // Select random city c from sDash
        Individual sDash = new Individual(individual);
        int len = sDash.getGenotype().size();
        int index = ThreadLocalRandom.current().nextInt(0, len);
        int c = sDash.getGenotype().get(index);

        while (true) {
          int cDash;
          // Select a city cDash from the remaining cities in sDash
          if (ThreadLocalRandom.current().nextDouble(1) <= probability) {
            int indexNext;
            do {
              indexNext = ThreadLocalRandom.current().nextInt(0, len);
            } while (index == indexNext);
            cDash = sDash.getGenotype().get(indexNext);
            // cDash = the city "next to" c in a randomly selected Individual
          } else {
            int iDashIndex = ThreadLocalRandom.current().nextInt(0, populationSize);
            Individual iDash = populationList.get(iDashIndex);

            cDash = iDash.getGenotype().get((iDash.getGenotype().indexOf(c) + 1) % len);
          }

          // If cDash is the same city as the next or previous of <code>city</code>
          int cityIndexLast = (sDash.getGenotype().indexOf(c) - 1 + len) % len;
          int cityIndexNext = (sDash.getGenotype().indexOf(c) + 1) % len;
          int cityLast = (sDash.getGenotype().get(cityIndexLast));
          int cityNext = (sDash.getGenotype().get(cityIndexNext));
          if ((cDash == cityLast) || (cDash == cityNext)) {
            break;
          }

          // Mutate -> Invert the section from next to cDash in sDash
          m.run(problem, sDash, new IntegerPair(cityIndexNext, sDash.getGenotype().indexOf(cDash)));
          numInversions++;
          c = cDash;
        }

        if (sDash.getCost(problem) <= individual.getCost(problem)) {
          individual = sDash;
        }
      }
      numGenerations++;
      if (Collections.min(population.getPopulation()).compareTo(bestIndividual) < 0) {
        bestIndividual = Collections.min(population.getPopulation());
      }
      bst.bestIndividualForThisGeneration(bestIndividual, (int) numGenerations);
    }
    System.out.println("Inv: " + numInversions);
    return bestIndividual;
  }

  /**
   * Gets the current number of generations
   * 
   * @return number of generations is returned
   */
  public long getNumGenerations() {
    return numGenerations;
  }
}
