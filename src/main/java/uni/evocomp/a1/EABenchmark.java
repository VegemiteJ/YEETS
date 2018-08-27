package uni.evocomp.a1;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import uni.evocomp.a1.logging.BenchmarkStatsTracker;
import uni.evocomp.a1.mutate.Mutate;
import uni.evocomp.a1.recombine.Recombine;
import uni.evocomp.a1.selectparents.SelectParents;
import uni.evocomp.a1.selectsurvivors.SelectSurvivors;
import uni.evocomp.util.Util;

public class EABenchmark {

  /**
   * Runs a single averaged test case
   *
   * @param problem
   * @param totalGenerations
   * @param timeoutLimit Limit in seconds
   * @param repeats
   * @param optimalSolution
   * @param selectParents
   * @param recombine
   * @param mutate
   * @param mutateProbability
   * @param selectSurvivors
   * @param populationSize
   */
  private static void averagedSingleTestCaseRun(
      TSPProblem problem,
      int totalGenerations,
      long timeoutLimit,
      int repeats,
      Individual optimalSolution,
      SelectParents selectParents,
      Recombine recombine,
      Mutate mutate,
      double mutateProbability,
      SelectSurvivors selectSurvivors,
      int populationSize) {

    // Create a reasonable name for the stats files
    String name =
        problem.getName()
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

    System.out.println(
        "Running benchmark: " + problem.getName() + "(" + problem.getComment() + ")");
    System.out.println("\tConfig: " + name);

    // Record metrics
    BenchmarkStatsTracker bst = new BenchmarkStatsTracker(name, problem);
    bst.setSolutionTour(optimalSolution);
    EA ea = new EA(bst, timeoutLimit);

    long startTime = System.nanoTime();
    for (int i = 0; i < repeats; i++) {
      System.out.println("Repeat: " + i + " of " + repeats);
      bst.startSingleRun();
      ea.solve(
          problem,
          selectParents,
          recombine,
          mutate,
          mutateProbability,
          selectSurvivors,
          populationSize,
          totalGenerations);
      bst.endSingleRun(ea.getGeneration());
    }
    if (optimalSolution != null) {
      System.out.println("Known Best Solution: " + optimalSolution.getCost(problem));
    }
    System.out.println("Avg: " + bst.getAvgCost());
    System.out.println("StdDev: " + bst.getStandardDeviation());
    System.out.println("Min: " + bst.getMinCost());
    System.out.println("Max: " + bst.getMaxCost());
    System.out.println(
        "Avg search Elapsed Time (s): " + (double) bst.getAvgTimeTaken() / 1000000000.0);
    System.out.println(
        "Benchmark Total Elapsed Time (s): "
            + (double) (System.nanoTime() - startTime) / 1000000000.0);
    System.out.println("Save file: " + bst.getSerialFileName());

    // Write stats to file
    try {
      bst.writeToFile();
      BenchmarkStatsTracker.serialise(bst);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Runs an EA benchmark on <code>problem</code>
   *
   * @param propertiesFileName name of properties file to read customisation from
   */
  public static void benchmark(String propertiesFileName) throws IOException {
    // Read a .properties file to figure out which implementations to use and instantiate
    // one of each using SelectParents, Recombine, Mutate and SelectSurvivors
    Properties prop = new Properties();

    List<String> testCases;
    SelectParents selectParents;
    Recombine recombine;
    Mutate mutate;
    double mutateProbability;
    SelectSurvivors selectSurvivors;
    int populationSize;
    int totalGenerations;
    long timeoutLimit;
    int repeats;

    // Create the objects from the properties file
    // If a query isn't found (e.g. Mutate doesn't have an entry, fallback on second arg)
    // WARNING : EVERY implementation must have a constructor (even if it's blank)
    // This doesn't have to be a default constructor, it could have arguments
    try (InputStream input = new FileInputStream(propertiesFileName)) {
      prop.load(input);

      String testPrefix = prop.getProperty("TestDirPrefix", "tests/");
      testCases =
          Arrays.asList(
                  prop.getProperty(
                          "TestFiles",
                          "st70,eil51,eil76,eil101,kroA100,kroC100,kroD100,lin105,pcb442,pr2392,usa13509")
                      .split("\\s*,\\s*"))
              .stream()
              .map(testCase -> testPrefix + testCase)
              .collect(Collectors.toList());
      selectParents =
          Util.classFromName(
              prop.getProperty("SelectParents", Global.a1Prefix + ".selectparents.UniformRandom"));
      recombine =
          Util.classFromName(
              prop.getProperty("Recombine", Global.a1Prefix + ".recombine.OrderCrossover"));
      mutate = Util.classFromName(prop.getProperty("Mutate", Global.a1Prefix + ".mutate.Invert"));
      mutateProbability = Double.valueOf(prop.getProperty("MutateProbability", "0.1"));
      selectSurvivors =
          Util.classFromName(
              prop.getProperty(
                  "SelectSurvivors", Global.a1Prefix + ".selectsurvivors.TournamentSelection"));
      populationSize = Integer.valueOf(prop.getProperty("PopulationSize", "-1"));
      totalGenerations = Integer.valueOf(prop.getProperty("TotalGenerations", "20000"));
      timeoutLimit = Long.valueOf(prop.getProperty("TimeoutLimit", "900"));
      repeats = Integer.valueOf(prop.getProperty("NumberOfRuns", "30"));
    } catch (Exception ex) {
      ex.printStackTrace();
      return;
    }

    // Run for every defined test case
    for (String testName : testCases) {
      TSPIO io = new TSPIO();
      TSPProblem problem;
      Individual optimalSolution = null;

      // Read problem and corresponding solution if available
      @SuppressWarnings("resource")
      BufferedReader br1 = new BufferedReader(new FileReader(testName + Global.testSuffix));
      BufferedReader br2 = null;
      problem = io.read(br1);
      try {
        br2 = new BufferedReader(new FileReader(testName + Global.tourSuffix));
        optimalSolution = io.readSolution(br2);
      } catch (IOException e) {
        System.out.println("Unable to find optimal solution tour for: " + testName);
      } finally {
        if (br2 != null) {
          br2.close();
        }
      }

      averagedSingleTestCaseRun(
          problem,
          totalGenerations,
          timeoutLimit,
          repeats,
          optimalSolution,
          selectParents,
          recombine,
          mutate,
          mutateProbability,
          selectSurvivors,
          populationSize);
    }
  }

  public static void main(String[] args) throws IOException {
    String configName = (args.length < 1 ? "config.properties" : args[0]);
    benchmark(configName);
  }
}
