package uni.evocomp.a1.recombine;

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

  @Override
  public Pair<Individual, Individual> recombine(Individual firstParent, Individual secondParent) {
    return null;
  }
}
