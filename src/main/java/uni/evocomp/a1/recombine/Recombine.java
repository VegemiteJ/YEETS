package uni.evocomp.a1.recombine;

import uni.evocomp.util.Pair;
import uni.evocomp.a1.Individual;

public interface Recombine {
  public Individual recombine(Individual firstParent, Individual secondParent);
  public Pair<Individual, Individual> recombineDouble(Individual firstParent, Individual secondParent);
}
