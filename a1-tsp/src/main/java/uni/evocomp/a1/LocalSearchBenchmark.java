package uni.evocomp.a1;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import uni.evocomp.a1.evaluate.Evaluate;
import uni.evocomp.a1.evaluate.EvaluateEuclid;
import uni.evocomp.a1.mutate.Invert;
import uni.evocomp.a1.mutate.Jump;
import uni.evocomp.a1.mutate.Mutate;
import uni.evocomp.a1.mutate.Swap;
import uni.evocomp.util.Pair;

public class LocalSearchBenchmark {

  public static final String[] testNames = {
    "tests/eil51",
    "tests/eil76",
    "tests/eil101",
    "tests/kroA100",
    "tests/kroC100",
    "tests/kroD100",
    "tests/lin105",
    "tests/pcb442",
    "tests/pr2392",
    "tests/usa13509"
  };

  public static final Mutate[] mutationFunctions = {new Invert(), new Jump(), new Swap()};
  public static final String[] mutationNames = {"Jump", "Exchange", "2-Opt"};
  public static final int repeats = 30;
  public static final String testSuffix = ".tsp";
  public static final String tourSuffix = ".opt.tour";

  LocalSearchBenchmark() {
    // Assume we create a local search function with parameters
    // new LocalSearch(problem, mutator)

    // and there is a search that returns it's best guess

    Evaluate evaluator = new EvaluateEuclid();

    TSPIO io = new TSPIO();
    ArrayList<Pair<TSPProblem, Individual>> benchmarks = new ArrayList<>();
    for (String testString : testNames) {
      TSPProblem problem = null;
      try (FileReader fr1 = new FileReader(testString + testSuffix);
        FileReader fr2 = new FileReader(testString + tourSuffix)) {
        problem = io.read(fr1);
        Individual solution = io.readSolution(fr2);
        solution.setCost(evaluator.evaluate(problem, solution));
        benchmarks.add(new Pair<>(problem, solution));
      } catch (IOException e) {
//        e.printStackTrace();
        benchmarks.add(new Pair<>(problem, null));
      }
    }
    benching:
    for (Pair<TSPProblem, Individual> benchmark : benchmarks) {
      TSPProblem problemDef = benchmark.first;
      if (problemDef == null){
        continue;
      }
      if (benchmark.second != null) {
        System.out.println(problemDef.getName() + " (Best: " + benchmark.second.getCost() + ")");
      }

      for (int mi = 0; mi < mutationFunctions.length; mi++) {
        System.out.println("  " + mutationNames[mi]);
        Mutate mutationFunction = mutationFunctions[mi];

        LocalSearch ls = new RandomizedLocalSearch(problemDef, evaluator, mutationFunction);

        double cost = 0;
        double minCost = Double.MAX_VALUE;
        double maxCost = 0;
        double averageCost = 0;

        for (int i = 0; i < repeats; i++) {
          //          System.out.println("Running a new test");
          //          System.out.println("===================================");
          Individual result = ls.solve();
          if (result.getCost() < minCost) {
            minCost = result.getCost();
          }
          if (result.getCost() > maxCost) {
            maxCost = result.getCost();
          }
          averageCost += result.getCost();
          //          System.out.println("===================================");
          try {
            Individual.serialise(result);
          } catch (IOException e) {
            e.printStackTrace();
          }
          break benching;
        }
        averageCost /= repeats;

        System.out.println("    Min: " + minCost);
        System.out.println("    Max: " + maxCost);
        System.out.println("    Ave: " + averageCost);
      }
    }
  }

  public static void main(String[] args) {
    new LocalSearchBenchmark();
  }
}
