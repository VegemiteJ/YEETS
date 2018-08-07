package uni.evocomp.a1.recombine;

import uni.evocomp.a1.Individual;
import uni.evocomp.util.Pair;

public class RecombineDummy implements Recombine {

  public RecombineDummy() {
    ;
  }

  public Pair<Individual, Individual> recombine(Individual firstParent, Individual secondParent) {
    Pair<Individual, Individual> o = new Pair<>(new Individual(firstParent), new Individual(secondParent));
    return o;
  }
}