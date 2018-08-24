package uni.evocomp.a1;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import uni.evocomp.a1.logging.BenchmarkStatsTracker;
import uni.evocomp.a1.mutate.Invert;
import uni.evocomp.a1.mutate.Jump;
import uni.evocomp.a1.mutate.Mutate;
import uni.evocomp.a1.mutate.Swap;
import uni.evocomp.util.Pair;

public class LocalSearchBenchmark {

  public static final Mutate[] mutationFunctions = {new Jump(), new Swap(), new Invert()};
  public static final String[] mutationNames = {"Jump", "Exchange", "2-Opt"};
  public static final int repeats = 30;

  LocalSearchBenchmark() throws IOException {
    System.out.println("Version 2");
    TSPIO io = new TSPIO();
    ArrayList<Pair<TSPProblem, Individual>> benchmarks = new ArrayList<>();
    for (String testString : Global.testNames) {
      testString = "tests/" + testString;
      Individual solution = null;

      // If throws - don't continue
      FileReader fr = new FileReader(testString + Global.testSuffix);
      TSPProblem problem = io.read(fr);
      // Allowed to throw
      try (FileReader fr2 = new FileReader(testString + Global.tourSuffix)) {
        solution = io.readSolution(fr2);
        solution.setCost(solution.getCost(problem));
      } catch (FileNotFoundException e) {
        System.out.println("Tour file not found for: " + testString);
      } catch (IOException e) {
        e.printStackTrace();
        System.exit(10);
      } finally {
        fr.close();
      }
      benchmarks.add(new Pair<>(problem, solution));
    }
    for (Pair<TSPProblem, Individual> benchmark : benchmarks) {
      TSPProblem problemDef = benchmark.first;
      if (problemDef == null) {
        continue;
      }

      System.out.println(
          "Running Benchmark: " + problemDef.getName() + "(" + problemDef.getComment() + ")");
      if (benchmark.second != null) {
        System.out.println(
            problemDef.getName() + " (Best: " + benchmark.second.getCost(problemDef) + ")");
      }
      for (int mi = 0; mi < mutationFunctions.length; mi++) {
        System.out.println("  " + mutationNames[mi]);
        Mutate mutationFunction = mutationFunctions[mi];

        LocalSearch ls = new RandomizedLocalSearch(problemDef, mutationFunction);
        BenchmarkStatsTracker bst =
            new BenchmarkStatsTracker(problemDef.getName() + "_" + mutationNames[mi], problemDef);
        bst.setSolutionTour(benchmark.second);
        long startTime = System.nanoTime();
        for (int i = 0; i < repeats; i++) {
          bst.startSingleRun();
          ls.solve(bst);
          bst.endSingleRun(ls.getTotalIterations());
        }
        System.out.println("Avg cost: " + bst.getAvgCost());
        System.out.println("StdDev cost: " + bst.getStandardDeviation());
        System.out.println("Min cost: " + bst.getMinCost());
        System.out.println("Max cost: " + bst.getMaxCost());
        System.out.println(
            "Avg search Elapsed Time (s): " + (double) bst.getAvgTimeTaken() / 1000000000.0);
        System.out.println("Benchmark Total Elapsed Time (s): "
            + (double) (System.nanoTime() - startTime) / 1000000000.0);
        System.out.println("Save file: " + bst.getSerialFileName());
        try {
          bst.writeToFile();
          BenchmarkStatsTracker.serialise(bst);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public static void main(String[] args) throws IOException {
    new LocalSearchBenchmark();
  }
}
