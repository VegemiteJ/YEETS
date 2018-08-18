package uni.evocomp.a1.recombine;

import java.util.Collections;
import java.util.Random;
import uni.evocomp.util.Pair;
import uni.evocomp.util.IntegerPair;
import uni.evocomp.a1.Individual;

/**
 * Perform the order crossover recombination: take an ordered part of the first parent,
 * then fill in the remaining elements with the
 */
public class OrderCrossover implements Recombine {

  public OrderCrossover() {
    ;
  }

  @Override
  public Pair<Individual, Individual> recombineDouble(Individual firstParent, Individual secondParent) {
    IntegerPair slice = getRandomSlice(firstParent.getGenotype().size());
    return new Pair<>(
        recombine(firstParent, secondParent, slice),
        recombine(secondParent, firstParent, slice)
    );
  }

  @Override
  public Individual recombine(Individual firstParent, Individual secondParent) {
    IntegerPair slice = getRandomSlice(firstParent.getGenotype().size());
    return recombine(firstParent, secondParent, slice);
  }

  /**
   * @param firstParent The first individual to crossover
   * @param secondParent The second individual to crossover
   * @param slice The range [start, end) of the order to select
   * @return The resulting child
   */
  Individual recombine(Individual firstParent, Individual secondParent, IntegerPair slice)
  throws IllegalArgumentException {
    final int n = firstParent.getGenotype().size();
    slice.first %= n;
    slice.second %= n;

    // Validate inputs.
    if (slice.first < 0 || slice.second < 0 || slice.first > n || slice.second > n
        || slice.first.equals(slice.second) || secondParent.getGenotype().size() != n) {
      throw new IllegalArgumentException("");
    }

    Individual child = new Individual(n);
    Collections.fill(child.getGenotype(), 0);

    // Copy slice from first into child.
    for (int i = slice.first; i != slice.second; i = Math.floorMod(i + 1, n)) {
      child.getGenotype().set(i, firstParent.getGenotype().get(i));
    }

    // Copy remaining elements into child.
    // Starting from slice.second and filling to the right, wrapping around,
    // and copying in the order of the second parent.

    // next is the position to fill next in the child.
    int next = Math.floorMod(slice.second, n);
    // pos is the position iterating through the second array.
    int pos = next;
    for (int i = 0; i < n; i++) {
      // If the current element is already in the
      Integer current = secondParent.getGenotype().get(pos);
      pos = Math.floorMod(pos + 1, n);
      if (elementInPopRange(current, child, slice)) {
        continue;
      }

      assert !child.getGenotype().contains(current): "Child contains element-to-add " + current;
      child.getGenotype().set(next, current);
      next = (next + 1) % n;
    }

    return child;
  }

  private boolean elementInPopRange(Integer element, Individual population, IntegerPair range) {
    final int n = population.getGenotype().size();
    for (int i = range.first; i != range.second; i = Math.floorMod(i + 1, n)) {
      if (population.getGenotype().get(i).equals(element)) {
        return true;
      }
    }
    return false;
  }

  public IntegerPair getRandomSlice(Integer n) {
    Random r = new Random();
    int start = r.nextInt(n);
    int end = r.nextInt(n - 2);
    if (end >= start) {
      end += 2;
    }
    return new IntegerPair(start, end);
  }
}
