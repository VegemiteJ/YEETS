package uni.evocomp.a1;

public class LocalSearchBenchmark {

  public static void main(String[] args) {
    new LocalSearchBenchmark();
  }

  public static final String[] testNames = {
    "eil51",
    "eil76",
    "eil101",
    "kroA100",
    "kroC100",
    "kroD100",
    "lin105",
    "pcb442",
    "pr2392",
    "usa13509"
  };

  public static final Mutate[] mutationFunctions = {new Jump(), new Swap(), new Invert()};
  public static final String[] mutationNames = {"Jump", "Exchange", "2-Opt"};
  public static final int repeats = 30;

  public static final String testSuffix = ".tsp";
  public static final String tourSuffix = ".opt.tour";

  LocalSearchBenchmark() {
    // Assume we create a local search function with parameters
    // new LocalSearch(problem, mutator)

    // and there is a search that returns it's best guess

    for (String testString : testNames) {
      System.out.println(testString);
      for (int mi = 0; mi < mutationFunctions.length; mi++) {
        System.out.println(mutationNames[mi]);
        Mutate mutationFunction = mutationFunctions[mi];

        LocalSearch ls = new RandomizedLocalSearch(testString, mutationFunction);

        double cost = 0;
        double minCost = Double.MAX_VALUE;
        double maxCost = 0;
        double averageCost = 0;

        for (int i = 0; i < repeats; i++) {
          cost = ls.solve();
          if (cost < minCost) minCost = cost;
          if (cost > maxCost) maxCost = cost;
          averageCost += cost;
        }
        averageCost /= repeats;

        System.out.println("Min: " + minCost);
        System.out.println("Max: " + maxCost);
        System.out.println("Ave: " + averageCost);
      }
    }
  }
}
