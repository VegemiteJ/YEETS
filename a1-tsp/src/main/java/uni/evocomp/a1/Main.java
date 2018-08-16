package uni.evocomp.a1;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import uni.evocomp.a1.evaluate.Evaluate;
import uni.evocomp.a1.mutate.Mutate;
import uni.evocomp.a1.recombine.Recombine;
import uni.evocomp.a1.selectparents.SelectParents;
import uni.evocomp.a1.selectsurvivors.SelectSurvivors;
import uni.evocomp.util.IntegerPair;
import uni.evocomp.util.Pair;
import uni.evocomp.util.Util;

public class Main {
  public static final String[] testNames = {"eil51", "eil76", "eil101", "kroA100", "kroC100",
      "kroD100", "lin105", "pcb442", "pr2392", "st70", "usa13509"};
  public static final String testSuffix = ".tsp";
  public static final String tourSuffix = ".opt.tour";
  public static final String a1Prefix = "uni.evocomp.a1";

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
  public static Individual evolutionaryAlgorithm(TSPProblem problem, Evaluate evaluate,
      SelectParents selectParents, Recombine recombine, Mutate mutate,
      SelectSurvivors selectSurvivors, int populationSize) {

    // Initialise population with random candidate solutions and
    // Evaluate each candidate
    Population population = new Population(problem, populationSize);

    // The return value
    Individual bestIndividual = Collections.min(population.getPopulation());

    // TODO : define better terminal condition
    int numRuns = 0;
    while (numRuns < 50) {
      // 1. select parents from the population
      List<Pair<Individual, Individual>> parents = selectParents.selectParents(population);

      // 2. recombine pairs of parents, add resulting offspring to existing population
      List<Individual> thing = parents.parallelStream()
          // Can't use {} notation in flatMap to produce intermediate variables
          // Unnecessarily calls recombine twice, which already calls recombine twice
          .flatMap(pi ->
          // Pair<Individual, Individual> offspring = recombine.recombine(pi.first, pi.second);
          Arrays.asList(recombine.recombine(pi.first, pi.second).first,
              recombine.recombine(pi.first, pi.second).second).stream())
          .collect(Collectors.toList());
      for (Individual i : thing) {
        population.add(i);
      }

      // Step 2 but normal for loop. Can't just automagically parallelise but doesn't need to call
      // recombine twice
      // for (Iterator<Pair<Individual, Individual>> it = parents.iterator(); it.hasNext();) {
      // Pair<Individual, Individual> p = it.next();
      // Pair<Individual, Individual> offspring = recombine.recombine(p.first, p.second);
      // population.add(offspring.first);
      // population.add(offspring.second);
      // }

      // 3. mutate resulting offspring and
      // 4. evaluate new candidates
      population.getPopulation().parallelStream().forEach(individual -> {
        // Pick a random range to mutate
        IntegerPair ip = new IntegerPair(ThreadLocalRandom.current().nextInt(0, problem.getSize()),
            ThreadLocalRandom.current().nextInt(0, problem.getSize()));
        // System.out.print("Mutation from " + individual.getCost() + " -> ");
        mutate.run(problem, individual, ip);
        // System.out.print(individual.getCost() + " -> ");
        individual.getCost(problem);
        // System.out.println(individual.getCost());
      });

      // 5. select individuals for next generation
      population =
          selectSurvivors.selectSurvivors(population, problem, ThreadLocalRandom.current());
      // Update best individual so far
      Individual popBest = Collections.min(population.getPopulation());
      // System.out.println("Best cost : " + popBest.getCost(problem));
      if (popBest.compareTo(bestIndividual) < 0) {
        bestIndividual = popBest;
      }
      numRuns++;
    }
    return bestIndividual;
  }

  /**
   * Runs an EA benchmark on <code>problem</code>
   * 
   * @param problem the problem to run EA on
   * @param propertiesFileName name of properties file to read customisation from
   * @param populationSize size of the population
   * @param repeats how many times to repeat the benchmark
   */
  public static void benchmark(TSPProblem problem, String propertiesFileName, int populationSize,
      int repeats) {
    // Read a .properties file to figure out which implementations to use and instantiate
    // one of each using Evaluate, SelectParents, Recombine, Mutate and SelectSurvivors
    Properties prop = new Properties();

    Evaluate evaluate = null;
    SelectParents selectParents = null;
    Recombine recombine = null;
    Mutate mutate = null;
    SelectSurvivors selectSurvivors = null;

    // Create the objects from the properties file
    // If a query isn't found (e.g. Evaluate doesn't have an entry, fallback on second arg)
    // WARNING : EVERY implementation must have a constructor (even if it's blank)
    // This doesn't have to be a default constructor, it could have arguments
    try (InputStream input = new FileInputStream(propertiesFileName)) {
      prop.load(input);

      evaluate = (Evaluate) Util
          .classFromName(prop.getProperty("Evaluate", a1Prefix + ".evaluate.EvaluateEuclid"));
      selectParents = (SelectParents) Util.classFromName(
          prop.getProperty("SelectParents", a1Prefix + ".selectparents.UniformRandom"));
      recombine = (Recombine) Util
          .classFromName(prop.getProperty("Recombine", a1Prefix + ".recombine.OrderCrossover"));
      mutate = (Mutate) Util.classFromName(prop.getProperty("Mutate", a1Prefix + ".mutate.Invert"));
      selectSurvivors = (SelectSurvivors) Util.classFromName(
          prop.getProperty("SelectSurvivors", a1Prefix + ".selectsurvivors.TournamentSelection"));
    } catch (Exception ex) {
      ex.printStackTrace();
      return;
    }

    // TODO : record metrics
    double minCost = Double.MAX_VALUE;
    double maxCost = Double.MIN_VALUE;
    double averageCost = 0;

    for (int i = 0; i < repeats; i++) {
      Individual result = evolutionaryAlgorithm(problem, evaluate, selectParents, recombine, mutate,
          selectSurvivors, populationSize);
      // System.out.println("Best individual in iteration " + i + ": " + result);
      if (result.getCost(problem) < minCost) {
        System.out.println("New min cost: " + minCost + " to " + result.getCost(problem));
        minCost = result.getCost(problem);
      }
      if (result.getCost(problem) > maxCost) {
        System.out.println("New max cost: " + minCost + " to " + result.getCost(problem));
        maxCost = result.getCost(problem);
      }
      averageCost += result.getCost(problem);
    }

    averageCost /= repeats;

    System.out.println("    Min: " + minCost);
    System.out.println("    Max: " + maxCost);
    System.out.println("    Ave: " + averageCost);
  }

  public static void main(String[] args) {
    TSPProblem problem = new TSPProblem();
    TSPIO io = new TSPIO();
    String testfile = (args.length > 0 ? args[0] : "tests/eil51.tsp");
    System.out.println("Test file is " + testfile);
    try (Reader r = new FileReader(testfile)) {
      problem = io.read(r);
    } catch (IOException e) {
      e.printStackTrace();
    }

    String configName = (args.length < 1 ? "config.properties" : args[0]);
    benchmark(problem, configName, 20, 3);
  }
}
