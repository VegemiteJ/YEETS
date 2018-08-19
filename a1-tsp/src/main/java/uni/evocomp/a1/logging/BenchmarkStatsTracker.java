package uni.evocomp.a1.logging;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import uni.evocomp.a1.Individual;
import uni.evocomp.a1.TSPProblem;
import uni.evocomp.util.Pair;

/**
 * Intended for tracking stats throughout a (approx 30) benchmark run. But it can handle as many
 * runs as you want Also provides useful options for serializing this for plotGui or examination
 * later Has toString functions for printing resulting stats + to csv for plotting
 *
 * <p>Usage: Before starting a benchmark run of 30 averages create this class.
 *
 * <p>1) Call BENCHMARK.setSolutionTour(Individual solution) where solution is the optimal solution
 * from file
 *
 * <p>At the start of each benchmark loop: 2) Call BENCHMARK.startSingleRun()
 *
 * <p>Upon finding a new best individual for the run: 3) Call
 * BENCHMARK.newBestIndividualForSingleRun(Individual bestSoFar)
 *
 * <p>Completion of run: 4) Call BENCHMARK.endSingleRun() 5) Call BENCHMARK.get**() to retrieve
 * relevant info you want
 */
public class BenchmarkStatsTracker implements Serializable {

  protected static final long serialVersionUID = 393238973421366553L;
  static final String serialSuffix = ".ser";

  private String comment;
  private TSPProblem problem;
  private Individual bestTourFound;
  private Individual providedBestTour;

  private long averageTimePerRun;
  private double standardDeviation;
  private double averageCost;
  private double minCost;
  private double maxCost;

  private int minRunIdx;
  private int maxRunIdx;

  private List<Individual> bestTourPerRun;

  // Stores Best Individual Tour + number of iterations since last improvement
  //   Number of iterations is fuzzy for evoalg
  private List<Pair<Individual, Long>> bestToursFromMinRun;
  transient private List<Pair<Individual, Long>> bestToursFromMaxRun;
  transient private List<Pair<Individual, Long>> currentRunTours;

  // Time Elapsed, Number of Iterations
  private List<Pair<Long, Long>> timePerRun;

  private long currStartTime;
  private long currEndTime;

  // EA specific stats - Represents the benchmark best (not best for this generation) individual at
  // this generation number
  private Map<Integer, Individual> bestIndividualPerGeneration;

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

  /** * Initializes all stats for a repeated run (of 30 for local search). */
  private void initializeFullRun() {
    this.bestTourFound = null;
    this.providedBestTour = null;
    this.averageTimePerRun = 0L;
    this.averageCost = 0.0;
    this.standardDeviation = 0.0;
    this.minCost = Double.MAX_VALUE;
    this.maxCost = Double.MIN_VALUE;
    this.bestTourPerRun = new ArrayList<>();
    this.bestToursFromMaxRun = null;
    this.bestToursFromMinRun = null;
    this.currentRunTours = null;
    this.bestIndividualPerGeneration = new HashMap<>();
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

  public List<Pair<Double, Long>> getIterationsSinceImprovementFromMinRun() {
    return this.bestToursFromMinRun
        .stream()
        .map(i -> new Pair<>(i.first.getCost(problem), i.second))
        .collect(Collectors.toList());
  }

  public List<Pair<Double, Long>> getIterationsSinceImprovementFromMaxRun() {
    return this.bestToursFromMaxRun
        .stream()
        .map(i -> new Pair<>(i.first.getCost(problem), i.second))
        .collect(Collectors.toList());
  }

  public Individual getProvidedBestTour() {
    if (this.providedBestTour == null) {
      Individual i = new Individual(Arrays.asList(1), 0.0);
      return i;
    }
    return this.providedBestTour;
  }

  public String getComment() {
    return this.comment;
  }

  public TSPProblem getProblem() {
    return this.problem;
  }

  public Double getStandardDeviation() {
    return this.standardDeviation;
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

  public List<Pair<Long, Long>> getTimePerRun() {
    return this.timePerRun;
  }

  public Integer getMinRunIdx() {
    return this.minRunIdx;
  }

  public Integer getMaxRunIdx() {
    return this.maxRunIdx;
  }

  public Individual getBestIndividualPerGeneration(int generation) throws Exception {
    return bestIndividualPerGeneration.get(generation);
  }

  /** Resets currentRunTours to empty List */
  public void startSingleRun() {
    this.currentRunTours = new ArrayList<>();
    this.currStartTime = System.nanoTime();
    this.bestIndividualPerGeneration = new HashMap<>();
  }

  /**
   * After we find a new best individual, call this. It updates the currentRunTours with a new best
   * individual
   *
   * @param newBest
   * @param iterationNumber
   */
  public void newBestIndividualForSingleRun(Individual newBest, Long iterationNumber) {
    this.currentRunTours.add(new Pair<>(new Individual(newBest), iterationNumber));
  }

  /**
   * After every generation of the EA, call this to store the best individual of this generation.
   *
   * @param genIndividual
   * @param generationNumber
   */
  public void bestIndividualForThisGeneration(Individual genIndividual, int generationNumber) {
    this.bestIndividualPerGeneration.put(generationNumber, genIndividual);
  }

  /**
   * Sets bestTourPerRun with the best individual (currentRunTours.back())
   *
   * <p>Updates Average, Min and Max costs Checks the cost of the best individual found in this run
   * (currentRunTours.back()) If greater than best found overall (this.bestTourFound) overwrite best
   * found overall sets bestToursFromMinRun = currentRunTours (as this is the current minimum cost
   * run)
   */
  public void endSingleRun(Long iterations) {
    // Update timing
    this.currEndTime = System.nanoTime();
    long timeElapsed = this.currEndTime - this.currStartTime;
    this.timePerRun.add(new Pair<>(timeElapsed, iterations));

    // Don't overflow... Divide first.
    this.averageTimePerRun =
        this.timePerRun.stream().mapToLong(i -> i.first / timePerRun.size()).sum();

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

    this.standardDeviation =
        Math.sqrt(
            this.bestTourPerRun
                    .stream()
                    .mapToDouble(
                        individual ->
                            Math.pow(individual.getCost(this.problem) - this.averageCost, 2.0))
                    .sum()
                / this.bestTourPerRun.size());

    // Update Max cost
    if (bestCostForRun > maxCost) {
      maxCost = bestCostForRun;
      this.bestToursFromMaxRun = this.currentRunTours;
      this.maxRunIdx = this.currentRunTours.size() - 1;
    }
    // Update Min cost
    if (bestCostForRun < minCost) {
      minCost = bestCostForRun;
      this.bestTourFound = bestForRun;
      this.bestToursFromMinRun = this.currentRunTours;
      this.minRunIdx = this.currentRunTours.size() - 1;
    }
  }

  private String getCsvFormat() {
    return getComment()
        + ","
        + getProvidedBestTour().getCost(getProblem())
        + ","
        + getMinCost()
        + ","
        + getMaxCost()
        + ","
        + getAvgCost()
        + ","
        + getStandardDeviation()
        + ","
        + (double) getAvgTimeTaken() / 1000000000.0
        + "\n";
  }

  private String getEACsvFormat() {
    try {
      return getComment()
          + ","
          + (getProvidedBestTour() == null ? -1.0 : getProvidedBestTour().getCost(problem))
          + ","
          + (getBestIndividualPerGeneration(2000) == null
              ? -1.0
              : getBestIndividualPerGeneration(2000).getCost(problem))
          + ","
          + (getBestIndividualPerGeneration(5000) == null
              ? -1.0
              : getBestIndividualPerGeneration(5000).getCost(problem))
          + ","
          + (getBestIndividualPerGeneration(10000) == null
              ? -1.0
              : getBestIndividualPerGeneration(10000).getCost(problem))
          + ","
          + (getBestIndividualPerGeneration(20000) == null
              ? -1.0
              : getBestIndividualPerGeneration(20000).getCost(problem))
          + "\n";
    } catch (Exception e) {
      e.printStackTrace();
      return getComment()
          + ","
          + (getProvidedBestTour() == null ? -1.0 : getProvidedBestTour().getCost(problem))
          + ",FAIL"
          + ",FAIL"
          + ",FAIL"
          + ",FAIL\n";
    }
  }

  private String getEACsvFormatAlt() {
    try {
      String init = getComment()
          + ","
          + (getProvidedBestTour() == null ? -1.0 : getProvidedBestTour().getCost(problem));
          for (int i = 10; i <= 20000; i+=50) {
            Individual indiv = getBestIndividualPerGeneration(i);
            if (indiv == null) {
              init += ",-1";
            } else {
              init += "," + indiv.getCost(problem);
            }
          }
          init += "\n";
          return init;
    } catch (Exception e) {
      e.printStackTrace();
      return getComment()
          + ","
          + (getProvidedBestTour() == null ? -1.0 : getProvidedBestTour().getCost(problem))
          + ",FAIL"
          + ",FAIL"
          + ",FAIL"
          + ",FAIL\n";
    }
  }

  public void writeEAGensToFile() {
    writeEAGensToFile(this.getComment());
  }

  public void writeEAGensToFile(String fileName) {
    fileName = fileName + ".gen";
    System.out.println("Writing Stats to: " + fileName);
    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true));
      bw.write(getEACsvFormat());
      bw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void writeToFile() {
    writeToFile(this.getComment());
  }

  public void writeToFile(String fileName) {
    fileName = fileName + ".csv";
    System.out.println("Writing Stats to: " + fileName);
    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
      bw.write(getCsvFormat());
      bw.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public String getSerialFileName() {
    return this.getComment() + serialSuffix;
  }
}
