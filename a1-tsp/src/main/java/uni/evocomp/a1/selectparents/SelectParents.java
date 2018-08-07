package uni.evocomp.a1.selectparents;

import java.util.List;
import uni.evocomp.a1.Individual;
import uni.evocomp.a1.Population;
import uni.evocomp.util.Pair;

/** @author joshuafloh */
public interface SelectParents {
  public List<Pair<Individual,Individual>> selectParents(Population population);
}


