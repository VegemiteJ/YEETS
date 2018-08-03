package uni.evocomp.a1;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

public class TournamentSelection implements SelectSurvivors {
  // TODO: Decide whether to store these here or pass them directly to the function
  private Evaluate evaluate;
  private Integer tournamentSize; // TODO: This could be a proportion instead of a fixed size
  private Double survivalProportion;
  private Double p;

  TournamentSelection() {
    this.evaluate = new EvaluateEuclid();
  }

  TournamentSelection(Integer tournamentSize, Double survivalProportion, Double p) {
    this();
    this.tournamentSize = tournamentSize;
    this.survivalProportion = survivalProportion;
    this.p = p;
  }

  @Override
  public Population selectSurvivors(Population population, TSPProblem problem, Random rand) {
    Set<Individual> survivorSet = new HashSet<>();

    // Keep running tournaments until the limit has been reached
    while (survivorSet.size() < (int) (survivalProportion * population.size)) {
      runTournament(population, problem, rand, survivorSet);
    }
    // Construct the surviving population from the individual set
    Population survivorPopulation = new Population(survivorSet);
    return survivorPopulation;
  }

  private void runTournament(Population population, TSPProblem problem, Random rand,
      Set<Individual> survivors) {
    Set<Integer> s = new HashSet<>();
    List<Individual> tournamentList = new ArrayList<>();

    // choose k random individuals from population
    if (tournamentSize > population.size) {
      while (s.size() < tournamentSize) {
        s.add(rand.nextInt(population.size));
      }
      
      // Iterate over HashSet and add individuals to the tournament list
      List<Individual> individualList = new ArrayList<>(population.getPopulation());
      Iterator<Integer> itr = s.iterator();
      while (itr.hasNext()) {
        tournamentList.add(individualList.get(itr.next()));
      }
    }
    // if k < pop.size, use the whole population
    else {
      tournamentList = new ArrayList<>(population.getPopulation());
    }

    // Sort the list based on fitness
    // https://stackoverflow.com/questions/49122512/sorting-an-arraylist-based-on-the-result-of-a-method
    tournamentList
        .sort((o1, o2) -> (int) (evaluate.evaluate(problem, o1) - evaluate.evaluate(problem, o2)));

    // Choose the best individual with probability p, 2nd with p(1-p), 3rd with p(1-p)^2 etc.
    int i = 0;
    for (Individual individual : tournamentList) {
      if (rand.nextFloat() < (p * Math.pow((1 - p), i))) {
        survivors.add(individual);
        if (survivors.size() < (int) (survivalProportion * population.size)) {
          break;
        }
      }
      i++;
    }
  }
}

