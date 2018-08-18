package uni.evocomp.a1;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import uni.evocomp.a1.logging.BenchmarkStatsTracker;
import uni.evocomp.a1.mutate.Mutate;
import uni.evocomp.a1.recombine.Recombine;
import uni.evocomp.a1.selectparents.SelectParents;
import uni.evocomp.a1.selectsurvivors.SelectSurvivors;
import uni.evocomp.util.Util;

public class Main {
  public static final String[] testNames = {
    "eil51",
    "eil76",
    "eil101",
    "kroA100"};
//    "kroC100",
//    "kroD100",
//    "lin105",
//    "pcb442",
//    "pr2392",
//    "st70",
//    "usa13509"
//  };
  public static final String testSuffix = ".tsp";
  public static final String tourSuffix = ".opt.tour";
  public static final String a1Prefix = "uni.evocomp.a1";

  static final int totalGenerations = 20000;

  /**
   * Runs an EA benchmark on <code>problem</code>
   *
   * @param testName the name of the test to run, without extension
   * @param propertiesFileName name of properties file to read customisation from
   * @param repeats how many times to repeat the benchmark
   */
  public static void benchmark(
      String testName, String propertiesFileName, int repeats) {
    // Read a .properties file to figure out which implementations to use and instantiate
    // one of each using Evaluate, SelectParents, Recombine, Mutate and SelectSurvivors
    Properties prop = new Properties();

    SelectParents selectParents = null;
    Recombine recombine = null;
    Mutate mutate = null;
    SelectSurvivors selectSurvivors = null;
    int populationSize = -1;

    // Create the objects from the properties file
    // If a query isn't found (e.g. Evaluate doesn't have an entry, fallback on second arg)
    // WARNING : EVERY implementation must have a constructor (even if it's blank)
    // This doesn't have to be a default constructor, it could have arguments
    try (InputStream input = new FileInputStream(propertiesFileName)) {
      prop.load(input);

      selectParents =
          (SelectParents)
              Util.classFromName(
                  prop.getProperty(
                      "SelectParents", Global.a1Prefix + ".selectparents.UniformRandom"));
      recombine =
          (Recombine)
              Util.classFromName(
                  prop.getProperty("Recombine", Global.a1Prefix + ".recombine.OrderCrossover"));
      mutate =
          (Mutate)
              Util.classFromName(prop.getProperty("Mutate", Global.a1Prefix + ".mutate.Invert"));
      selectSurvivors =
          (SelectSurvivors)
              Util.classFromName(
                  prop.getProperty(
                      "SelectSurvivors", Global.a1Prefix + ".selectsurvivors.TournamentSelection"));
      populationSize = Integer.valueOf(prop.getProperty("PopulationSize", "-1"));
    } catch (Exception ex) {
      ex.printStackTrace();
      return;
    }

    TSPIO io = new TSPIO();
    TSPProblem problem = null;
    Individual optimalSolution = null;

    // Read problem and corresponding solution if available
    try {
      BufferedReader br1 = new BufferedReader(new FileReader(testName + Global.testSuffix));
      problem = io.read(br1);
      BufferedReader br2 = new BufferedReader(new FileReader(testName + Global.tourSuffix));
      optimalSolution = io.readSolution(br2);
    } catch (IOException e) {
      e.printStackTrace();
      optimalSolution = null;
    }


    String name = problem.getName()
        + "_"
        + selectParents.getClass().getSimpleName()
        + "_"
        + recombine.getClass().getSimpleName()
        + "_"
        + mutate.getClass().getSimpleName()
        + "_"
        + selectSurvivors.getClass().getSimpleName()
        + "_"
        + populationSize
        + ".bst";

    // Record metrics
    BenchmarkStatsTracker bst =
        new BenchmarkStatsTracker(name, problem);

    EA ea = new EA(bst);
    ea.printItr = 10000;
    bst.setSolutionTour(optimalSolution);
    System.out.println(
        "Running benchmark: " + problem.getName() + "(" + problem.getComment() + ")");
    System.out.println("\tConfig: " + name);
    long startTime = System.nanoTime();
    for (int i = 0; i < repeats; i++) {
      System.out.println("Repeat: " + i + " of " + repeats);
      bst.startSingleRun();
      Individual result =
          ea.solve(
              problem,
              selectParents,
              recombine,
              mutate,
              selectSurvivors,
              populationSize,
              totalGenerations);
      bst.endSingleRun(ea.getGeneration());
    }
    if (optimalSolution != null) {
      System.out.println("Known Best Solution: " + optimalSolution.getCost(problem));
    }
    System.out.println("Avg: " + bst.getAvgCost());
    System.out.println("Min: " + bst.getMinCost());
    System.out.println("Max: " + bst.getMaxCost());
    System.out.println(
        "Avg search Elapsed Time (s): " + (double) bst.getAvgTimeTaken() / 1000000000.0);
    System.out.println(
        "Benchmark Total Elapsed Time (s): "
            + (double) (System.nanoTime() - startTime) / 1000000000.0);
    System.out.println("Save file: " + bst.getSerialFileName());

    try {
      BenchmarkStatsTracker.serialise(bst);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
//    String testfile = (args.length > 0 ? args[0] : "tests/eil101");
//    System.out.println("Test file is " + testfile);
    String configName = (args.length < 1 ? "config.properties" : args[0]);

    for (String testfile : testNames) {
      testfile = "tests/" + testfile;
      System.out.println("Running testfile: " + testfile);
      benchmark(testfile, configName, 5);
    }
  }
}
