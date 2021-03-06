package uni.evocomp.a1;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A population in an evolutionary algorithm represents a set of solutions, which is a set of
 * individuals
 * 
 * @author Namdrib
 *
 */
public class Population {
  private Set<Individual> population;

  public Population() {
    population = new LinkedHashSet<>();
  }

  /**
   * Copy constructor for Population
   * 
   * @param src source Population object to copy construct
   */
  public Population(Population src) {
    this.population = new LinkedHashSet<>(src.getPopulation());
  }

  /**
   * Initialise the Population with <code>size</code> randomly generated Individuals
   * 
   * @param popSize how many Individuals in the Population
   * @param indSize the size of each Individual
   */
  public Population(TSPProblem problem, int popSize) {
    this();

    for (int i = 0; i < popSize; i++) {
      population.add(new Individual(problem));
    }
  }

  /**
   * Initialise a population using a Set of Individuals
   * 
   * @param individualSet
   * @author joshuafloh
   */
  public Population(Set<Individual> individualSet) {
    this();
    this.population = individualSet;
  }

  /**
   * Adds a single Individual to the Population
   * 
   * @param i Individual to add
   */
  public void add(Individual i) {
    population.add(i);
  }

  public int getSize() {
    return population.size();
  }

  public Set<Individual> getPopulation() {
    return population;
  }
}
