package uni.evocomp.a1;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import uni.evocomp.util.Util;

public class TournamentSelection implements SelectSurvivors {
  // TODO: Decide whether to store these here or pass them directly to the function

  private Integer tournamentSize;
  private Double p;

  @Override
  public Population selectSurvivors(Population population, TSPProblem problem, Random rand) {
    // choose k random individuals from population
    // Use a HashSet to generate k unique integers
    Set<Integer> s = new HashSet<>();
    while (s.size() < tournamentSize) {
      s.add(rand.nextInt(population.size));
    }
    // Iterate over HashSet and add individuals to the tournament list
    List<Individual> tournamentList = new ArrayList<>();
    Set<Individual> survivorSet = new HashSet<>();
    Iterator<Integer> itr = s.iterator();
    List<Individual> individualList = new ArrayList<>(population.getPopulation());
    while (itr.hasNext()) {
      tournamentList.add(individualList.get(itr.next()));
    }
    // Sort the list based on fitness
    // https://stackoverflow.com/questions/49122512/sorting-an-arraylist-based-on-the-result-of-a-method
    tournamentList.sort((o1, o2) -> Util.calculateFitness(o1, problem) - Util.calculateFitness((o2, problem)));

    // Choose the best individual with probability p, 2nd with p(1-p), 3rd with p(1-p)^2 etc.
    int i = 0;
    for (Individual individual : tournamentList) {
      if (rand.nextFloat() < (p*Math.pow((1-p), i))) {
        survivorSet.add(individual);
      }
      i++;
    }
    // Construct the surviving population from the individual set
    Population survivorPopulation = new Population(survivorSet);
    return survivorPopulation;
  }

}

