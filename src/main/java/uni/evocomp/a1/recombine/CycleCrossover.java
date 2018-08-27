package uni.evocomp.a1.recombine;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import uni.evocomp.a1.Individual;
import uni.evocomp.util.Pair;

/**
 * Performs CycleCrossover recombination in O(n).
 * (a) Start with the first allele of P1.
 * (b) Look at the allele at the same position in P2.
 * (c) Go to the position with the same allele in P1.
 * (d) Add this allele to the cycle.
 * (e) Repeat step b through d until you arrive at the first allele of P1.
 *
 */
public class CycleCrossover implements Recombine {
  
  public CycleCrossover() {
    ;
  }

  /**
   * @param terminatingValue
   * @param idx
   * @param parentA
   * @param parentB
   * @param childA Must be a List of size parentA.size()
   * @param childB Must be a List of size parentB.size()
   */
  private void runSingleCycle(
      int terminatingValue,
      int idx,
      List<Integer> parentA,
      List<Integer> parentB,
      int[] childA,
      int[] childB) {
    int aValue = terminatingValue;
    int bValue;
    do {
      childA[idx] = aValue;               // (a)
      bValue = parentB.get(idx);          // (b)
      childB[idx] = bValue;
      idx = parentA.indexOf(bValue);      // (c)
      aValue = parentA.get(idx);          // (d)
    } while (aValue != terminatingValue); // (e)
  }

  @Override
  public Individual recombine(Individual firstParent, Individual secondParent) {
    return recombineDouble(firstParent, secondParent).first;
  }

  @Override
  public Pair<Individual, Individual> recombineDouble(Individual firstParent, Individual secondParent) {
    List<Integer> parentA = firstParent.getGenotype();
    List<Integer> parentB = secondParent.getGenotype();
    int[] childA = new int[parentA.size()];
    int[] childB = new int[parentB.size()];

    // Initial value is index 0 with order A,B
    // Run a cycle generation with order A,B
    // Choose next index as first unseen index
    // Run a cycle generation with order B,A
    boolean abOrder = true;
    int idx = 0;
    int terminatingValue;
    while (idx < childA.length) {
      if (abOrder) {
        terminatingValue = parentA.get(idx);
        runSingleCycle(terminatingValue, idx, parentA, parentB, childA, childB);
      } else {
        terminatingValue = parentB.get(idx);
        runSingleCycle(terminatingValue, idx, parentB, parentA, childA, childB);
      }
      idx = findNext(childA, idx + 1);
      abOrder = !abOrder;
    }
    return new Pair<>(
        new Individual(Arrays.stream(childA).boxed().collect(Collectors.toList())),
        new Individual(Arrays.stream(childB).boxed().collect(Collectors.toList())));
  }

  /**
   * Returns index of next matching position containing 0
   * 
   * @param childA
   * @param i
   * @return
   */
  private int findNext(int[] childA, int i) {
    while (i < childA.length && childA[i] != 0) {
      i++;
    }
    return i;
  }
}
