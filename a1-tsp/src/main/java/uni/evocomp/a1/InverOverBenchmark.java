package uni.evocomp.a1;

import uni.evocomp.a1.InverOver;

public class InverOverBenchmark {
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

    EA ea = new EA(bst);
    System.out
        .println("Running benchmark: " + problem.getName() + "(" + problem.getComment() + ")");

    bst.setSolutionTour(optimalSolution);
    long startTime = System.nanoTime();
    for (int i = 0; i < repeats; i++) {
      bst.startSingleRun();
      Individual result = ea.solve(problem, evaluate, selectParents, recombine, mutate,
          selectSurvivors, populationSize, totalGenerations);
      bst.endSingleRun(ea.getGeneration());
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

