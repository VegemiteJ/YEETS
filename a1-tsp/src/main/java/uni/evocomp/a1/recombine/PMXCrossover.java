package uni.evocomp.a1.recombine;

import uni.evocomp.a1.Individual;
import uni.evocomp.util.IntegerPair;
import uni.evocomp.util.Pair;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class PMXCrossover implements Recombine {

  @Override
  public Pair<Individual, Individual> recombineDouble(Individual firstParent,
      Individual secondParent) {
    IntegerPair slice = getRandomSlice(firstParent.getGenotype().size());
    return new Pair<>(recombine(firstParent, secondParent, slice),
        recombine(secondParent, firstParent, slice));
  }

  /**
   * This should never be called ever, just to satisfy interface
   */
  @Deprecated
  @Override
  public Individual recombine(Individual firstParent, Individual secondParent) {
    IntegerPair slice = getRandomSlice(firstParent.getGenotype().size());
    return recombine(firstParent, secondParent, slice);
  }

  Individual recombine(Individual firstParent, Individual secondParent, IntegerPair slice) {
    int n = firstParent.getGenotype().size();
    List<Integer> child_g = new ArrayList<>(Collections.nCopies(n, -1));

    Map<Integer, Boolean> valueCopied = new HashMap<>();

    // Copy segment from 1 into child
    for (int i = slice.first; i != slice.second % n; i = (i + 1) % n) {
      child_g.set(i, firstParent.getGenotype().get(i));
      valueCopied.put(firstParent.getGenotype().get(i), true);
    }

    // Look in segment in parent 2, bounce with position in parent 1, to find location
    for (int i = slice.first; i != slice.second % n; i = (i + 1) % n) {
      int i_city = secondParent.getGenotype().get(i);
      if (valueCopied.containsKey(i_city)) {
        // Value has already been copied
        continue;
      }
      //
      int j_city = firstParent.getGenotype().get(i);
      // Find location of j in parent 2
      int j = secondParent.getGenotype().indexOf(j_city); // TODO: SLOW
      while (child_g.get(j) != -1) {
        // Bounce
        j_city = firstParent.getGenotype().get(j);
        j = secondParent.getGenotype().indexOf(j_city);
      }
      // Copy element in i to found idx
      child_g.set(j, i_city);
      valueCopied.put(i_city, true);
    }

    // fill in blanks
    for (int i = 0; i < n; i++) {
      if (child_g.get(i) == -1) {
        child_g.set(i, secondParent.getGenotype().get(i));
      }
    }
    Individual child = new Individual(child_g);
    return child;
  }


  public IntegerPair getRandomSlice(Integer n) {
    if (n == 0) {
      return new IntegerPair(0, 0);
    }
    int r1 = ThreadLocalRandom.current().nextInt(0, n);
    int r2 = ThreadLocalRandom.current().nextInt(0, n);
    while (r1 == r2) {
      r2 = ThreadLocalRandom.current().nextInt(0, n);
    }
    return new IntegerPair(r1, r2);
  }
}
