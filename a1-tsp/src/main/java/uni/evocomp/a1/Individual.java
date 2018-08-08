package uni.evocomp.a1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import uni.evocomp.util.Matrix;

/**
 * Used to store a single trial/solution of the TSP problem.
 * <p>
 * As such, it should contain a Set of numbers, where each number is a city, representing a
 * permutation of all the cities (from 1-n)
 * 
 * @author Namdrib
 */
public class Individual implements Serializable {

  private static final long serialVersionUID = -8931448347288126553L;
  static final String serialLocation = "individual.ser";

  private List<Integer> genotype; // the tour, elements should be 1-n
  private double cost;

  public Individual() {
    genotype = new ArrayList<>();
    initialise(0);
  }

  /**
   * Copy constructor for Individual
   * 
   * @param src source Individual object to copy construct
   */
  public Individual(Individual src) {
    this.genotype = new ArrayList<>(src.getGenotype());
    this.setCost(src.cost);
  }

  /**
   * 
   * @param n initialise to have a tour of n cities
   *
   *        /** @param n initialise to have a tour of n cities
   */
  public Individual(int n) {
    initialise(n);
  }

  /**
   * @param n initialise to have a tour of n cities
   * @param problem problem to evaluate initial cost against
   */
  public Individual(int n, TSPProblem problem) {
    initialise(n);
    setCost(evaluateCost(problem));
  }

  /**
   * @param genotype initial tour for the Individual
   * @param initialCost Initial cost of tour
   */
  public Individual(List<Integer> genotype, Double initialCost) {
    this.genotype = genotype;
    setCost(initialCost);
  }

  /**
   * @param genotype initial tour for the Individual
   */
  public Individual(List<Integer> genotype) {
    this.genotype = genotype;
  }

  /**
   * @param genotype initial tour for the Individual
   * @param problem problem to evaluate initial cost against
   */
  public Individual(List<Integer> genotype, TSPProblem problem) {
    this.genotype = genotype;
    setCost(evaluateCost(problem));
  }

  /**
   * Initialise as per Exercise 3, "constructs .. a solution [uniformly at random] in linear time"
   * 
   * @param n the size of the genotype
   */
  public void initialise(int n) {
    genotype = new ArrayList<>();
    for (int i = 1; i <= n; i++) {
      genotype.add(i);
    }
    Collections.shuffle(genotype);
  }

  public List<Integer> getGenotype() {
    return genotype;
  }

  public Double getCost() {
    return this.cost;
  }

  public void setCost(Double cost) {
    this.cost = cost;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Iterator<Integer> it = genotype.iterator(); it.hasNext();) {
      sb.append(String.valueOf(it.next()));
      sb.append("\n");
    }
    sb.append("-1"); // Complete the tour
    return sb.toString();
  }

  /**
   * Default serialise function for object - write to <code>serialLocation</code>
   * 
   * Acts as a wrapper function for <code>serialise(Individual, outFileName)</code>
   * 
   * @param individual Individual object to serialise
   * @throws IOException
   */
  public static final void serialise(Individual individual) throws IOException {
    serialise(individual, serialLocation);
  }

  /**
   * Serialise <code>individual</code> into a file whose name is <code>outFileName</code>
   * 
   * @param individual <code>Individual</code> object to serialise
   * @param filename name of the file to write
   * @throws IOException
   */
  public static final void serialise(Individual individual, String filename) throws IOException {
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
      out.writeObject(individual);
    }
    System.out.print("Serialised data is saved in \"" + serialLocation + "\"");
  }

  /**
   * Default deserialise function for object - read from <code>serialLocation</code>
   * 
   * @return an Individual object read from a serialised Individual
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public static final Individual deserialise() throws IOException, ClassNotFoundException {
    return deserialise(serialLocation);
  }

  /**
   * Deserialise an Individual object from a file whose name is <code>filename</code>
   * 
   * @param filename the name of the file to read
   * @return an Individual object read from a serialised Individual
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public static final Individual deserialise(String filename)
      throws IOException, ClassNotFoundException {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
      return (Individual) in.readObject();
    }
  }

  private String getTourAsDebugString(List<Integer> tour) {
    StringBuilder sb = new StringBuilder();
    sb.append("Tour: ");
    for (int i : tour) {
      sb.append(i);
      sb.append(" ");
    }
    sb.append("\n");
    return sb.toString();
  }

  public void assertIsValidTour() throws IllegalStateException {
    Set<Integer> tour =
        IntStream.rangeClosed(1, getGenotype().size()).boxed().collect(Collectors.toSet());

    // Assert only distinct elements
    IllegalStateException exc = new IllegalStateException(
        "Illegal tour state. Printing tour...\n" + getTourAsDebugString(getGenotype()));

    for (Integer i : getGenotype()) {
      if (tour.contains(i)) {
        tour.remove(i);
      } else {
        throw exc;
      }
    }
    if (tour.size() != 0) {
      throw exc;
    }
  }

  public void assertIsValidCost(TSPProblem problem) throws IllegalStateException {
    IllegalStateException exc = new IllegalStateException(
        "Differential Cost not equal to actual cost...\n" + getTourAsDebugString(getGenotype()));
    if (cost != evaluateCost(problem)) {
      throw exc;
    }
  }

  /**
   * Finds the cost of the tour the individual is holding. Note: does not update Individual.cost
   *
   * @param problem Problem to evaluate cost against
   * @return Cost of the tour
   */
  public double evaluateCost(TSPProblem problem) {
    double newCost = 0;
    Matrix weights = problem.getWeights();
    for (int i = 0; i < genotype.size() - 1; i++) {
      // TODO: Loading tour file has the last element as -1, should change in TSPIO
      if (genotype.get(i + 1) == -1) {
        break;
      }
      newCost += weights.get(genotype.get(i) - 1, genotype.get(i + 1) - 1);
    }
    return newCost;
  }
}
