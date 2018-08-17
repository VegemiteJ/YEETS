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
