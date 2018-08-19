package uni.evocomp.a1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import uni.evocomp.a1.logging.BenchmarkStatsTracker;
import uni.evocomp.a1.mutate.Mutate;
import uni.evocomp.a1.recombine.Recombine;
import uni.evocomp.a1.selectparents.SelectParents;
import uni.evocomp.a1.selectsurvivors.SelectSurvivors;
import uni.evocomp.util.Pair;

/**
 * Class for EA
 *
 * <p>Able to return individual and generations
 *
 * @author Namdrib
 */
public class EA {
  BenchmarkStatsTracker bst;
  private long printItr;
  private long maxTime; // nanoseconds
  private long generation = 0;

  EA(BenchmarkStatsTracker bst) {
    this(bst, 900000000000L, 2000L);
  }

  EA(BenchmarkStatsTracker bst, long maxTimeout) {
    this(bst, maxTimeout, 2000L);
  }

  EA(BenchmarkStatsTracker bst, long maxTimeout, long printIteration) {
    this.bst = bst;
    this.maxTime = maxTimeout * 1000000000;
    this.printItr = printIteration;
  }

  /**
   * A typical evolutionary algorithm. Pass in different implementations of each argument to result
   * in a new algorithm. Return the best <code>Individual</code> after termination
   *
   * <pre>
   * INITIALISE population with random candidate solutions;
   * EVALUATE each candidate;
   * REPEAT UNTIL (TERMINATION CONDITION is satisfied) DO
   *   1. SELECT parents;
   *   2. RECOMBINE pairs of parents;
   *   3. MUTATE resulting offspring;
   *   4. EVALUATE new candidates;
   *   5. SELECT individuals for next generation;.
   * OD
   * </pre>
   *
   * @param problem an object representing the problem
   * @param selectParents a class to define how to pair parents together
   * @param recombine a class to define how to recombine parents to produce offspring
   * @param mutate a class to define how to mutate the resulting offspring
   * @param mutateProbability probability to mutate a given individual
   * @param selectSurvivors a class to define how to select <code>Individuals</code> for next round
   * @param populationSize the size of the population
   * @return the <code>Individual</code> with the best fitness
   */
  public Individual solve(
      TSPProblem problem,
      SelectParents selectParents,
      Recombine recombine,
      Mutate mutate,
      double mutateProbability,
      SelectSurvivors selectSurvivors,
      int populationSize,
      int totalGenerations) {

    // Initialise population with random candidate solutions and
    // Evaluate each candidate
    Population population = new Population(problem, populationSize);

    // The return value
    Individual bestIndividual = Collections.min(population.getPopulation());

    generation = 1;
    // Add initial best
    bst.newBestIndividualForSingleRun(bestIndividual, generation);

    long start = System.nanoTime();
    while (generation <= totalGenerations && System.nanoTime() - start < maxTime) {
      if (generation % printItr == 0) {
        System.out.println("At iteration " + generation + " of " + totalGenerations);
      }
      // 1. select parents from the population
      List<Pair<Individual, Individual>> parents = selectParents.selectParents(population);

      // 2. recombine pairs of parents
      List<Individual> offspring = parents.parallelStream().flatMap(pi -> {
        Stream.Builder<Individual> offspringStream = Stream.builder();
        Pair<Individual, Individual> twins = recombine.recombineDouble(pi.first, pi.second);
        offspringStream.add(twins.first);
        offspringStream.add(twins.second);
        return offspringStream.build();
      }).collect(Collectors.toList());

      // 3. mutate resulting offspring and
      // 4. evaluate new candidates, then add these to the population
      offspring
          .parallelStream()
          .forEach(
              individual -> {
                mutate.mutateWithProbability(mutateProbability, problem, individual, ThreadLocalRandom.current());
                individual.getCost(problem);
              });
      for (Individual i : offspring) population.add(i);

      // 5. select individuals for next generation
      population =
          selectSurvivors.selectSurvivors(population, problem, ThreadLocalRandom.current());
      // Update best individual so far
      Individual popBest = Collections.min(population.getPopulation());
      if (popBest.compareTo(bestIndividual) < 0) {
        bestIndividual = popBest;
        bst.newBestIndividualForSingleRun(bestIndividual, generation);
      }
      bst.bestIndividualForThisGeneration(bestIndividual, (int) generation);
      generation++;
    }
    bst.writeEAGensToFile();
    return bestIndividual;
  }

  public long getGeneration() {
    return generation;
  }
}
