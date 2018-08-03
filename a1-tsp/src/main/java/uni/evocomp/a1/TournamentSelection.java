package uni.evocomp.a1;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.Set;
import uni.evocomp.util.Util;

public class TournamentSelection implements SelectSurvivors {
  // TODO: Decide whether to store these here or pass them directly to the function

  private Integer tournamentSize;
  private Double p;

  @Override
  public Population selectSurvivors(Population population, Random rand) {
    // choose k random individuals from population
    // Use a HashSet to generate k unique integers
    Set<Integer> s = new HashSet<>();
    while (s.size() < tournamentSize) {
      s.add(rand.nextInt(population.size));
    }
    // Iterate over HashSet and add individuals to the tournament list
    List<Individual> tournamentList = new ArrayList<>();
    List<Individual> survivorList = new ArrayList<>();
    Iterator<Integer> itr = s.iterator();
    List<Individual> individualList = new ArrayList<>(population.getPopulation());
    while (itr.hasNext()) {
      tournamentList.add(individualList.get(itr.next()));
    }
    // Sort the list based on fitness
    // https://stackoverflow.com/questions/49122512/sorting-an-arraylist-based-on-the-result-of-a-method
    tournamentList.sort((o1, o2) -> Util.calculateFitness(o1) - Util.calculateFitness((o2)));

    // TODO: choose the best individual with probability p
    for (Individual individual : tournamentList) {
      
    }

    // choose the second best with probability p*(1-p)

    // 3rd: p*(1-p)^2)

    // etc

    Population survivorPopulation = new Population(new HashSet<>(survivorList));
    return survivorPopulation;
  }

}

