package uni.evocomp.a1.selectsurvivors;

import java.util.Random;
import uni.evocomp.a1.Population;
import uni.evocomp.a1.TSPProblem;

/**
 * SelectSurvivors interface
 * <p>
 * Implemented by TournamentSelection, FitnessProportional and Elitism
 * <p>
 * Takes in a population, a problem and a random number generator
 * <p>
 * Note that some implementations may require configuration of implementation specific fields e.g.
 * TournamentSelect requires choosing a tournament size, p value and survival proportion
 *
 * @author joshuafloh
 */
interface SelectSurvivors {
  /**
   * 
   * @param population the initial population
   * @param problem a TSP map
   * @param rand a random number generator (preferably a repeatable random)
   * @return a Population object containing only the survivors
   */
  public Population selectSurvivors(Population population, TSPProblem problem, Random rand);
}
