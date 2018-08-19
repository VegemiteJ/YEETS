package uni.evocomp.a1;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import uni.evocomp.a1.InverOver;
import uni.evocomp.a1.logging.BenchmarkStatsTracker;

/**
 * Class for InverOverBenchmark
 * 
 * Provides metrics for the InverOver algorithm implemented in InverOver.java
 * 
 * @author Nehal
 *
 */

public class InverOverBenchmark {

  public static void benchmark(String testName, String propertiesFileName, int populationSize,
      int repeats) {

    TSPIO io = new TSPIO();
    TSPProblem problem = null;
    Individual optimalSolution = null;

    // Read problem and corresponding solution if available
    try (BufferedReader br1 = new BufferedReader(new FileReader(testName + Global.testSuffix));
        BufferedReader br2 = new BufferedReader(new FileReader(testName + Global.tourSuffix))) {
      problem = io.read(br1);
      optimalSolution = io.readSolution(br2);
    } catch (IOException e) {
      e.printStackTrace();
      optimalSolution = null;
    }

    // Record metrics
    BenchmarkStatsTracker bst =
        new BenchmarkStatsTracker(problem.getName() + "_" + propertiesFileName, problem);

    InverOver inverOver = new InverOver(bst);
    System.out
        .println("Running benchmark: " + problem.getName() + "(" + problem.getComment() + ")");

    bst.setSolutionTour(optimalSolution);
    long startTime = System.nanoTime();
    for (int i = 0; i < repeats; i++) {
      System.out.println("Repeat i " + i);
      bst.startSingleRun();
      // Probability is 0.02 based on paper
      Individual run = inverOver.run(problem, 50, 100, 0.02);
      bst.endSingleRun(inverOver.getNumGenerations());
    }
    if (optimalSolution != null) {
      System.out.println("Known Best Solution: " + optimalSolution.getCost(problem));
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
    String testfile = (args.length > 0 ? args[0] : "tests/eil51");
    System.out.println("Test file is " + testfile);

    String configName = (args.length < 1 ? "config.properties" : args[0]);
    benchmark(testfile, configName, 20, 3);
  }
}

