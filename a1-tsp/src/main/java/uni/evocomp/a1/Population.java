package uni.evocomp.a1;

import java.util.HashSet;
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
    population = new HashSet<>();
  }

  /**
   * Copy constructor for Population
   * 
   * @param src source Population object to copy construct
   */
  public Population(Population src) {
    this.population = new HashSet<>(src.getPopulation());
  }

  /**
   * Initialise the Population with <code>size</code> randomly generated Individuals
   * 
   * @param popSize how many Individuals in the Population
   * @param indSize the size of each Individual
   */
  public Population(int popSize, int indSize) {
    this();

    for (int i = 0; i < getSize(); i++) {
      population.add(new Individual(indSize));
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
