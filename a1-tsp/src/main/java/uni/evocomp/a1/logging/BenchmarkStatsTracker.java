package uni.evocomp.a1.logging;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import uni.evocomp.a1.Individual;
import uni.evocomp.a1.TSPProblem;
import uni.evocomp.util.Pair;

/**
 * Intended for tracking stats throughout a (approx 30) benchmark run. But it can handle as many
 * runs as you want Also provides useful options for serializing this for plotGui or examination
 * later Has toString functions for printing resulting stats + to csv for plotting
 *
 * <p>Usage: Before starting a benchmark run of 30 averages create this class. 1) Call
 * BENCHMARK.setSolutionTour(Individual solution) where solution is the optimal solution from file
 * At the start of each benchmark loop: 2) Call BENCHMARK.startSingleRun() Upon finding a new best
 * individual for the run 3) Call BENCHMARK.newBestIndividualForSingleRun(Individual bestSoFar)
 * Completion of run: 4) Call BENCHMARK.endSingleRun() 5) Call BENCHMARK.get**() to retrieve
 * relevant info you want
 */
public class BenchmarkStatsTracker implements Serializable {

  private String comment;
  private TSPProblem problem;
  private Individual bestTourFound;
  private Individual providedBestTour;

  private long averageTimePerRun;
  private double averageCost;
  private double minCost;
  private double maxCost;

  private int minRunIdx;
  private int maxRunIdx;

  private List<Individual> bestTourPerRun;

  // Stores Best Individual Tour + number of iterations since last improvement
  //   Number of iterations is fuzzy for evoalg
  private List<Pair<Individual, Integer>> bestToursFromMinRun;
  private List<Pair<Individual, Integer>> bestToursFromMaxRun;
  private List<Pair<Individual, Integer>> currentRunTours;
  private List<Pair<Long,Integer>> timePerRun;

  private long currStartTime;
  private long currEndTime;

  /**
   * Initialize a new benchmark.
   *
   * @param comment
   * @param problem
   */
  public BenchmarkStatsTracker(String comment, TSPProblem problem) {
    this.comment = new String(comment);
    this.problem = problem;
    initializeFullRun();
  }

  /** * Initializes all stats for a repeated run (of 30 for local search). */
  private void initializeFullRun() {
    this.bestTourFound = null;
    this.providedBestTour = null;
    this.averageTimePerRun = 0L;
    this.averageCost = 0.0;
    this.minCost = Double.MAX_VALUE;
    this.maxCost = Double.MIN_VALUE;
    this.bestTourPerRun = new ArrayList<>();
    this.bestToursFromMaxRun = null;
    this.bestToursFromMinRun = null;
    this.currentRunTours = null;
    this.timePerRun = new ArrayList<>();
  }

  public void setSolutionTour(Individual i) {
    this.providedBestTour = new Individual(i);
  }

  public Individual getBestTourFound() {
    return this.bestTourFound;
  }

  public List<Individual> getBestToursFromEveryRun() {
    return this.bestTourPerRun;
  }

  public List<Individual> getBestToursFromMinRun() {
    return this.bestToursFromMinRun.stream().map(i -> i.first).collect(Collectors.toList());
  }

  public List<Individual> getBestToursFromMaxRun() {
    return this.bestToursFromMaxRun.stream().map(i -> i.first).collect(Collectors.toList());
  }

  public List<Pair<Double, Integer>> getIterationsSinceImprovementFromMinRun() {
    return this.bestToursFromMinRun
        .stream()
        .map(i -> new Pair<>(i.first.getCost(problem), i.second))
        .collect(Collectors.toList());
  }

  public List<Pair<Double, Integer>> getIterationsSinceImprovementFromMaxRun() {
    return this.bestToursFromMaxRun
        .stream()
        .map(i -> new Pair<>(i.first.getCost(problem), i.second))
        .collect(Collectors.toList());
  }

  public Individual getProvidedBestTour() {
    return this.providedBestTour;
  }

  public String getComment() {
    return this.comment;
  }

  public TSPProblem getProblem() {
    return this.problem;
  }

  public Double getAvgCost() {
    return this.averageCost;
  }

  public Double getMinCost() {
    return this.minCost;
  }

  public Double getMaxCost() {
    return this.maxCost;
  }

  public Long getAvgTimeTaken() {
    return this.averageTimePerRun;
  }

  public List<Pair<Long, Integer>> getTimePerRun() {
    return this.timePerRun;
  }

  public Integer getMinRunIdx() {
    return this.minRunIdx;
  }

  public Integer getMaxRunIdx() {
    return this.maxRunIdx;
  }

  /** Resets currentRunTours to empty List */
  public void startSingleRun() {
    this.currentRunTours = new ArrayList<>();
    this.currStartTime = System.nanoTime();
  }

  /**
   * After we find a new best individual, call this. It updates the currentRunTours with a new best
   * individual
   *
   * @param newBest
   */
  public void newBestIndividualForSingleRun(Individual newBest, Integer iterationsSinceLast) {
    this.currentRunTours.add(new Pair<>(new Individual(newBest), iterationsSinceLast));
  }

  /**
   * Sets bestTourPerRun with the best individual (currentRunTours.back())
   *
   * <p>Updates Average, Min and Max costs Checks the cost of the best individual found in this run
   * (currentRunTours.back()) If greater than best found overall (this.bestTourFound) overwrite best
   * found overall sets bestToursFromMinRun = currentRunTours (as this is the current minimum cost
   * run)
   */
  public void endSingleRun(Integer iterations) {
    // Update timing
    this.currEndTime = System.nanoTime();
    long timeElapsed = this.currEndTime - this.currStartTime;
    this.timePerRun.add(new Pair<>(timeElapsed, iterations));

    // Don't overflow... Divide first.
    this.averageTimePerRun = this.timePerRun.stream().mapToLong(i -> i.first / timePerRun.size()).sum();

    // this.currentRunTours should always have size >= 1 (Initial Individual is always added)
    Individual bestForRun = this.currentRunTours.get(currentRunTours.size() - 1).first;
    this.bestTourPerRun.add(bestForRun);
    double bestCostForRun = bestForRun.getCost(this.problem);

    // Update Average cost
    // Could use running average but minor speed improvement - it doesn't even matter at all
    this.averageCost =
        this.bestTourPerRun
                .stream()
                .mapToDouble(individual -> individual.getCost(this.problem))
                .sum()
            / this.bestTourPerRun.size();

    // Update Max cost
    if (bestCostForRun > maxCost) {
      maxCost = bestCostForRun;
      this.bestToursFromMaxRun = this.currentRunTours;
      this.maxRunIdx = this.currentRunTours.size()-1;
    }
    // Update Min cost
    if (bestCostForRun < minCost) {
      minCost = bestCostForRun;
      this.bestTourFound = bestForRun;
      this.bestToursFromMinRun = this.currentRunTours;
      this.minRunIdx = this.currentRunTours.size()-1;
    }
  }

  public String getSerialFileName() {
    return this.getComment() + serialSuffix;
  }

  protected static final long serialVersionUID = 393238973421366553L;
  static final String serialSuffix = ".ser";

  public static final void serialise(BenchmarkStatsTracker BENCHMARK) throws IOException {
    serialise(BENCHMARK, BENCHMARK.getComment());
  }

  public static final void serialise(BenchmarkStatsTracker BENCHMARK, String filename)
      throws IOException {
    filename = filename + serialSuffix;
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
      out.writeObject(BENCHMARK);
    }
    System.out.println("Serialised data is saved in \"" + filename + "\"");
  }

  public static final BenchmarkStatsTracker deserialise(String filename)
      throws IOException, ClassNotFoundException {
    filename = filename + serialSuffix;
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
      return (BenchmarkStatsTracker) in.readObject();
    }
  }
}
