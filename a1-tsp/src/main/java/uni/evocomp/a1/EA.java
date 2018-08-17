package uni.evocomp.a1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import uni.evocomp.a1.evaluate.Evaluate;
import uni.evocomp.a1.logging.BenchmarkStatsTracker;
import uni.evocomp.a1.mutate.Mutate;
import uni.evocomp.a1.recombine.Recombine;
import uni.evocomp.a1.selectparents.SelectParents;
import uni.evocomp.a1.selectsurvivors.SelectSurvivors;
import uni.evocomp.util.IntegerPair;
import uni.evocomp.util.Pair;

/**
 * Class for EA
 * 
 * Able to return individual and generations
 * 
 * @author Namdrib
 *
 */
public class EA {
  BenchmarkStatsTracker bst;
  long generation;
  

  EA(BenchmarkStatsTracker bst) {
    this.bst = bst;
  }
  /**
  *
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
  * @param evaluate a class to define how to evaluate the fitness of an <code>Individual</code>
  * @param selectParents a class to define how to pair parents together
  * @param recombine a class to define how to recombine parents to produce offspring
  * @param mutate a class to define how to mutate the resulting offspring
  * @param selectSurvivors a class to define how to select <code>Individuals</code> for next round
  * @param populationSize the size of the population
  * @return the <code>Individual</code> with the best fitness
  */
  public Individual solve(TSPProblem problem, Evaluate evaluate, SelectParents selectParents,
      Recombine recombine, Mutate mutate, SelectSurvivors selectSurvivors, int populationSize) {

    // Initialise population with random candidate solutions and
    // Evaluate each candidate
    Population population = new Population(problem, populationSize);

    // The return value
    Individual bestIndividual = Collections.min(population.getPopulation());

    // TODO : define better terminal condition
    generation = 1;
    while (generation < 1000) {
      // 1. select parents from the population
      List<Pair<Individual, Individual>> parents = selectParents.selectParents(population);

      // 2. recombine pairs of parents
      List<Individual> offspring = new ArrayList<>();
//      offspring = parents.stream()
//          // Can't use {} notation in flatMap to produce intermediate variables
//          // Unnecessarily calls recombine twice, which already calls recombine twice
//          .flatMap(pi ->
//          // Pair<Individual, Individual> offspring = recombine.recombine(pi.first, pi.second);
//          Arrays.asList(recombine.recombine(pi.first, pi.second).first,
//              recombine.recombine(pi.first, pi.second).second).stream())
//          .collect(Collectors.toList());

      // Step 2 but normal for loop. Can't just automagically parallelise but doesn't need to call
      // recombine twice
      for (Iterator<Pair<Individual, Individual>> it = parents.iterator(); it.hasNext();) {
        Pair<Individual, Individual> p = it.next();
        Pair<Individual, Individual> offspringPair = recombine.recombine(p.first, p.second);
        offspring.add(offspringPair.first);
        offspring.add(offspringPair.second);
      }

      // 3. mutate resulting offspring and
      // 4. evaluate new candidates, then add these to the population
      offspring.parallelStream().forEach(individual -> {
        // Pick a random range to mutate
        IntegerPair ip = new IntegerPair(ThreadLocalRandom.current().nextInt(0, problem.getSize()),
            ThreadLocalRandom.current().nextInt(0, problem.getSize()));
        // System.out.print("Mutation from " + individual.getCost() + " -> ");
        mutate.run(problem, individual, ip);
        // System.out.print(individual.getCost() + " -> ");
        individual.getCost(problem);
        // System.out.println(individual.getCost());
      });
      for (Individual i : offspring) {
        population.add(i);
      }

      // 5. select individuals for next generation
      population =
          selectSurvivors.selectSurvivors(population, problem, ThreadLocalRandom.current());
      // Update best individual so far
      Individual popBest = Collections.min(population.getPopulation());
      // System.out.println("Best cost : " + popBest.getCost(problem));
      if (popBest.compareTo(bestIndividual) < 0) {
        bestIndividual = popBest;
        bst.newBestIndividualForSingleRun(bestIndividual, generation);
      }
      generation++;
    }
    return bestIndividual;
  }
  
  public long getGeneration() {
    return generation;
  }
}
