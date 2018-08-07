package uni.evocomp.a1.selectparents;

import java.util.List;
import uni.evocomp.a1.Individual;
import uni.evocomp.a1.Population;
import uni.evocomp.util.Pair;

/**
 * Parent selection interface. Takes in a population and returns a list of pairs, where the pairs
 * correspond to paired up individuals acting as parents
 * 
 * @author joshuafloh
 *
 */
public interface SelectParents {
  public List<Pair<Individual, Individual>> selectParents(Population population);
}


