package uni.evocomp.a1;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
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
    int generation = 1;
    while (generation < 50) {
      // 1. select parents from the population
      List<Pair<Individual, Individual>> parents = selectParents.selectParents(population);

      // 2. recombine pairs of parents
      List<Individual> offspring;
      offspring = parents.parallelStream()
          // Can't use {} notation in flatMap to produce intermediate variables
          // Unnecessarily calls recombine twice, which already calls recombine twice
          .flatMap(pi ->
          // Pair<Individual, Individual> offspring = recombine.recombine(pi.first, pi.second);
          Arrays.asList(recombine.recombine(pi.first, pi.second),
              recombine.recombine(pi.second, pi.first)).stream())
          .collect(Collectors.toList());

      // Step 2 but normal for loop. Can't just automagically parallelise but doesn't need to call
      // recombine twice
      // for (Iterator<Pair<Individual, Individual>> it = parents.iterator(); it.hasNext();) {
      // Pair<Individual, Individual> p = it.next();
      // Pair<Individual, Individual> offspringPair = recombine.recombine(p.first, p.second);
      // offspring.add(offspringPair.first);
      // offspring.add(offspringPair.second);
      // }

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
      }
      generation++;
    }
    return bestIndividual;
  }

  /**
   * Runs an EA benchmark on <code>problem</code>
   * 
   * @param testName the name of the test to run, without extension
   * @param propertiesFileName name of properties file to read customisation from
   * @param populationSize size of the population
   * @param repeats how many times to repeat the benchmark
   */
  public static void benchmark(String testName, String propertiesFileName, int populationSize,
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

      evaluate = (Evaluate) Util.classFromName(
          prop.getProperty("Evaluate", Global.a1Prefix + ".evaluate.EvaluateEuclid"));
      selectParents = (SelectParents) Util.classFromName(
          prop.getProperty("SelectParents", Global.a1Prefix + ".selectparents.UniformRandom"));
      recombine = (Recombine) Util.classFromName(
          prop.getProperty("Recombine", Global.a1Prefix + ".recombine.OrderCrossover"));
      mutate = (Mutate) Util
          .classFromName(prop.getProperty("Mutate", Global.a1Prefix + ".mutate.Invert"));
      selectSurvivors = (SelectSurvivors) Util.classFromName(prop.getProperty("SelectSurvivors",
          Global.a1Prefix + ".selectsurvivors.TournamentSelection"));
    } catch (Exception ex) {
      ex.printStackTrace();
      return;
    }

    TSPIO io = new TSPIO();
    TSPProblem problem = null;
    Individual optimalSolution = null;

    // Read problem and corresponding solution if available
    try (BufferedReader br1 = new BufferedReader(new FileReader(testName + Global.testSuffix));
        BufferedReader br2 = new BufferedReader(new FileReader(testName + Global.tourSuffix))) {
      problem = io.read(br1);
      optimalSolution = io.readSolution(br2);
      System.out.println("Known Best Solution: " + optimalSolution.getCost(problem));
      // System.out.println("Known Best Solution: " + optimalSolution.getGenotype());
      // problem.getWeights().print();
    } catch (IOException e) {
      e.printStackTrace();
      optimalSolution = null;
    }

    // Record metrics
    BenchmarkStatsTracker bst =
        new BenchmarkStatsTracker(problem.getName() + "_" + propertiesFileName, problem);

    EA ea = new EA(bst);
    System.out
        .println("Running benchmark: " + problem.getName() + "(" + problem.getComment() + ")");

    bst.setSolutionTour(optimalSolution);
    long startTime = System.nanoTime();
    for (int i = 0; i < repeats; i++) {
      bst.startSingleRun();
      Individual result = ea.solve(problem, evaluate, selectParents, recombine, mutate,
          selectSurvivors, populationSize);
      bst.endSingleRun(ea.getGeneration());
    }

    System.out.println("Avg: " + bst.getAvgCost());
    System.out.println("Min: " + bst.getMinCost());
    System.out.println("Max: " + bst.getMaxCost());
    System.out
        .println("Avg search Elapsed Time (s): " + (double) bst.getAvgTimeTaken() / 1000000000.0);
    System.out.println("Benchmark Total Elapsed Time (s): "
        + (double) (System.nanoTime() - startTime) / 1000000000.0);
    System.out.println("Save file: " + bst.getSerialFileName());

    try {
      BenchmarkStatsTracker.serialise(bst);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    String testfile = (args.length > 0 ? args[0] : "tests/eil101");
    System.out.println("Test file is " + testfile);

    String configName = (args.length < 1 ? "config.properties" : args[0]);
    benchmark(testfile, configName, 20, 3);
  }
}
