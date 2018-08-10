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
    Population population = new Population(populationSize, problem.getSize());
    System.out.println("Old pop size: " + population.getSize());
    population.getPopulation().parallelStream().forEach(i -> i.setCost(i.evaluateCost(problem)));

    // The return value
    Individual bestIndividual = null;

    // TODO : define terminal condition
    boolean RUN_ONCE = true;
    while (RUN_ONCE) {
      // 1. select parents from the population
      List<Pair<Individual, Individual>> parents = selectParents.selectParents(population);

      System.out.println("old pop, parents sizes: " + population.getSize() + ", " + parents.size());

      // 2. recombine pairs of parents, add resulting offspring to existing population
      for (Iterator<Pair<Individual, Individual>> it = parents.iterator(); it.hasNext();) {
        Pair<Individual, Individual> p = it.next();
        Pair<Individual, Individual> offspring = recombine.recombine(p.first, p.second);
        population.add(offspring.first);
        population.add(offspring.second);
      }

      System.out.println("New population size: " + population.getSize());

      // 3. mutate resulting offspring and
      // 4. evaluate new candidates
      population.getPopulation().parallelStream().forEach(individual -> {
        // Pick a random range to mutate
        IntegerPair ip = new IntegerPair(ThreadLocalRandom.current().nextInt(0, problem.getSize()),
            ThreadLocalRandom.current().nextInt(0, problem.getSize()));
        mutate.run(problem, individual, Arrays.asList(ip));
        System.out.println("Done. Old cost was " + individual.getCost());
        individual.setCost(Math.min(individual.getCost(), individual.evaluateCost(problem)));
        System.out.println("Done. New cost is " + individual.getCost());
      });

      if (problem == null) {
        System.out.println("pop is null");
      }
      // 5. select individuals for next generation
      System.out.println("selecting!");
      population =
          selectSurvivors.selectSurvivors(population, problem, ThreadLocalRandom.current());
      System.out.println("selected!");
      // Update best individual so far
      // Optional<Individual> popBest = population.getPopulation().stream().min((i1, i2) ->
      // Double.compare(i1.getCost(), i2.getCost()));
      Individual popBest = Collections.min(population.getPopulation());
      System.out.println("A : " + popBest.getCost());
      if (popBest.compareTo(bestIndividual) < 0) {
        System.out.println("Happen");
        bestIndividual = popBest;
      }
      RUN_ONCE = false;;
    }
    return bestIndividual;
  }

  // TODO : Benchmark function
  public static void benchmark(TSPProblem problem, String propertiesFileName, int populationSize,
      int timesToRun) {
    // TODO : Read a .properties file to figure out which implementations to use
    // and instantiate one of each using Evaluate, SelectParents, Recombine, Mutate and
    // SelectSurvivors
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
    for (int i = 0; i < timesToRun; i++) {
      Individual bestIndividual = evolutionaryAlgorithm(problem, evaluate, selectParents, recombine,
          mutate, selectSurvivors, populationSize);
      System.out.println("Best individual in iteration " + i + ": " + bestIndividual);
    }
  }

  public static void main(String[] args) {
    TSPProblem problem = new TSPProblem();
    TSPIO io = new TSPIO();
    try (Reader r = new FileReader("tests/eil51.tsp")) {
      problem = io.read(r);
    } catch (IOException e) {
      e.printStackTrace();
    }

    String configName = (args.length < 1 ? "config.properties" : args[0]);
    benchmark(problem, configName, 20, 3);
  }
}
