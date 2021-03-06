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
public class Individual implements Comparable<Individual>, Serializable {

  protected static final long serialVersionUID = -8931448347288126553L;
  static final String serialLocation = "individual.ser";

  protected List<Integer> genotype; // the tour, elements should be 1-n
  protected double cost;
  protected boolean dirty;

  public Individual() {
    genotype = new ArrayList<>();
    initialise(0);
    this.dirty = false;
  }

  /**
   * Copy constructor for Individual
   *
   * @param src source Individual object to copy construct
   */
  public Individual(Individual src) {
    // e x p l i c i t
    this.genotype = new ArrayList<>(src.getGenotype().size());
    for (Integer i : src.getGenotype()) {
      this.genotype.add(new Integer(i));
    }
    this.setCost(src.cost);
    // Copy src dirty bit is expected behaviour per chat on #87
    this.dirty = src.dirty;
  }

  /**
   * @param n the number of cities in the <code>Individual</code>'s tour
   */
  public Individual(int n) {
    initialise(n);
    this.dirty = true;
  }

  /**
   * Initialise the Individual with random solution for <code>problem</code>, and set initial cost
   * 
   * @param problem problem to evaluate initial cost against
   */
  public Individual(TSPProblem problem) {
    initialise(problem.getSize());
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
    this.dirty = true;
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

  public void setGenotype(List<Integer> g) {
    genotype = g;
  }

  /**
   * return the cost of this Individual with respect to a TSPProblem. Updates the cost if the dirty
   * bit is set
   * 
   * @param problem TSPProblem against which to derive cost
   * @return the Individual's cost with respect to the TSPProblem
   */
  public Double getCost(TSPProblem problem) {
    if (this.dirty) {
      setCost(evaluateCost(problem));
    }
    return this.cost;
  }

  public void setCost(Double cost) {
    this.cost = cost;
    this.dirty = false;
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
    System.out.println("Serialised data is saved in \"" + filename + "\"");
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
        System.err.println("Tour doesn't contain expected city: " + i);
        throw exc;
      }
    }
    if (tour.size() != 0) {
      System.err.println("Tour contained duplicate or error city: ");
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
   * Used for assertEquals() in JUnit 4
   * 
   * @param o Object to test against
   * @return <code>true</code> if o is an <code>Individual</code> and the genotypes are equal,
   *         <code>false</code> otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Individual)) {
      return false;
    }
    return this.getGenotype().equals(((Individual) o).getGenotype());
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
      newCost += weights.get(genotype.get(i) - 1, genotype.get(i + 1) - 1);
    }
    // Complete loop
    newCost += weights.get(genotype.get(genotype.size() - 1) - 1, genotype.get(0) - 1);

    return newCost;
  }

  /**
   * Compare Individuals by cost. Assumes both individuals in question (this and i) have the most
   * up-to-date cost, otherwise may give incorrect results
   */
  @Override
  public int compareTo(Individual i) {
    if (i == null) {
      return -1;
    }

    if (this.getCost(null) < i.getCost(null)) {
      return -1;
    } else if (this.getCost(null) > i.getCost(null)) {
      return 1;
    } else {
      return 0;
    }
  }
}
