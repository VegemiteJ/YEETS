package uni.evocomp.a1.recombine;

import uni.evocomp.a1.Individual;
import uni.evocomp.util.Pair;

public interface Recombine {
  public Pair<Individual, Individual> recombine(Individual firstParent, Individual secondParent);
}
