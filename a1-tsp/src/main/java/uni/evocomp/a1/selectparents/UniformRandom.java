package uni.evocomp.a1.selectparents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import uni.evocomp.a1.Individual;
import uni.evocomp.a1.Population;
import uni.evocomp.util.Pair;

/**
 * Matches parents by using the entire population and randomly pairing up individuals
 * 
 * @author joshuafloh
 *
 */
public class UniformRandom implements SelectParents {
  /**
   * Take a population, scramble the individuals within the population and match parents in new
   * order
   */
  @Override
  public List<Pair<Individual, Individual>> selectParents(Population population) {
    List<Pair<Individual, Individual>> parentPairs = new ArrayList<>();
    ArrayList<Individual> individuals = new ArrayList<>(population.getPopulation());
    Collections.shuffle(individuals);
    for (int i = 0; i < individuals.size() / 2; i++) {
      Pair<Individual, Individual> pair =
          new Pair<Individual, Individual>(individuals.get(i * 2), individuals.get(i * 2 + 1));
      parentPairs.add(pair);
    }
    return parentPairs;
  }

}
