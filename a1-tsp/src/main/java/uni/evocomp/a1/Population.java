package uni.evocomp.a1;

import java.util.HashSet;
import java.util.Set;

public class Population {
  Set<Individual> population;
  int size;

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
    this.size = popSize;

    for (int i = 0; i < this.size; i++) {
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
    this.size = individualSet.size();
  }

  public Set<Individual> getPopulation() {
    return population;
  }
}
