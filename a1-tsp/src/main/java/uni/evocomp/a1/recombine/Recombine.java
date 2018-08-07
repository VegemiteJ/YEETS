package uni.evocomp.a1;

import uni.evocomp.util.Pair;

public interface Recombine {
  Pair<Individual, Individual> recombine(Individual firstParent, Individual secondParent);
}
