package uni.evocomp.a1;

import uni.evocomp.util.IntegerPair;

/**
 * Perform the order crossover recombination: take an ordered part of the first parent,
 * then fill in the remaining elements with the
 */
public class OrderCrossover implements Recombine {
  @Override
  public Individual recombine(Individual firstParent, Individual secondParent) {
    return null;
  }

  /**
   * @param firstParent   The first individual to crossover
   * @param secondParent  The second individual to crossover
   * @param slice         The range [start, end) of the order to select
   * @return The resulting child
   */
  public Individual recombine(Individual firstParent, Individual secondParent, IntegerPair slice)
  throws IllegalArgumentException {
    final int n = firstParent.getGenotype().size();

    // Validate inputs.
    if (slice.first < 0 || slice.second < 1 || slice.first >= slice.second
        || slice.first >= n || slice.second > n || secondParent.getGenotype().size() != n) {
      throw new IllegalArgumentException();
    }

    Individual child = new Individual(n);

    // Copy slice from first into child.
    for (int i = slice.first; i < slice.second; i += 1) {
      child.getGenotype().set(i, firstParent.getGenotype().get(i));
    }

    // Copy remaining elements into child.
    // Starting from slice.second and filling to the right, wrapping around,
    // and copying in the order of the second parent.

    // next is the position to fill next in the child.
    int next = slice.second % n;
    for (int i = next; i == slice.second - 1; i = (i + 1) % n) {
      // If the current element is already in the
      Integer current = secondParent.getGenotype().get(i);
      if (child.getGenotype().subList(slice.first, slice.second).contains(current)) {
        continue;
      }

      child.getGenotype().set(next, current);
      next = (next + 1) % n;
    }
    assert next == slice.first;

    child.assertIsValidTour();
    return child;
  }
}
