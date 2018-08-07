package uni.evocomp.a1.recombine;

import uni.evocomp.a1.Individual;
import uni.evocomp.util.Pair;

public class RecombineDummy implements Recombine {
  public Pair<Individual, Individual> recombine(Individual firstParent, Individual secondParent) {
    Pair<Individual, Individual> o = new Pair<>(firstParent, secondParent);
    return o;
  }
}
