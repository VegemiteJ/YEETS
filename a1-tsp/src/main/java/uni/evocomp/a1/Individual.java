package uni.evocomp.a1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Used to store a single trial/solution of the TSP problem.
<<<<<<< HEAD
 *
 * <p>As such, it should contain a Set of numbers, where each number is a city, representing a
 * permutation of all the cities (from 1-n)
 *
=======
 * <p>
 * As such, it should contain a Set of numbers, where each number is a city, representing a
 * permutation of all the cities (from 1-n)
 *
>>>>>>> 50_TSPIO_read_scientific_notation
 * @author Namdrib
 */
public class Individual {
  List<Integer> genotype; // the tour, elements should be 1-n
  private Double cost;

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
  }

  /** @param n initialise to have a tour of n cities */
  public Individual(int n) {
    initialise(n);
  }

  public Individual(List<Integer> genotype) {
    this.genotype = genotype;
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

  public void setCost(Double cost) { this.cost = cost; }
  public Double getCost() { return this.cost; }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (Iterator<Integer> it = genotype.iterator(); it.hasNext(); ) {
      sb.append(String.valueOf(it.next()));
      sb.append("\n");
    }
    sb.append("-1"); // Complete the tour
    return sb.toString();
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

    IllegalStateException exc =
        new IllegalStateException(
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
}
