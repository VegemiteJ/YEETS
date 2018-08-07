package uni.evocomp.a1.recombine;

import uni.evocomp.util.Pair;
import uni.evocomp.a1.Individual;

public interface Recombine {
  Pair<Individual, Individual> recombine(Individual firstParent, Individual secondParent);
}
