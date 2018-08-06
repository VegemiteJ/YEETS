package uni.evocomp.a1;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
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

    while (survivorSet.size() < (int) (survivalProportion * population.getSize())) {
      runTournament(population, problem, rand, survivorSet);
    }

    // Construct the surviving population from the individual set
    Population survivorPopulation = new Population(survivorSet);
    return survivorPopulation;
  }

  private void runTournament(Population population, TSPProblem problem, Random rand,
      Set<Individual> survivors) {
    Set<Integer> s = new LinkedHashSet<>();
    List<Individual> tournamentList = new ArrayList<>();

    // choose k random individuals from population
    if (tournamentSize < population.getSize()) {
      while (s.size() < tournamentSize) {
        int index = rand.nextInt(population.getSize());
        s.add(index);
        // System.out.println("added to tournament: " + index);
      }
      // Iterate over HashSet and add individuals to the tournament list
      List<Individual> individualList = new ArrayList<>(population.getPopulation());
      Iterator<Integer> itr = s.iterator();
      while (itr.hasNext()) {
        int index = itr.next();
        // System.out.println("Index added to set (after extraction from set)" + index);
        Individual next = individualList.get(index);// TODO: this is fucked
        tournamentList.add(next);
        // System.out.println("added to tournament: " + next.hashCode());
      }
    }
    // if k < pop.size, use the whole population
    else {
      // System.out.println("k > pop size");
      tournamentList = new ArrayList<>(population.getPopulation());
    }

    // Sort the list based on fitness
    // https://stackoverflow.com/questions/49122512/sorting-an-arraylist-based-on-the-result-of-a-method
    tournamentList
        .sort((o1, o2) -> (int) (evaluate.evaluate(problem, o1) - evaluate.evaluate(problem, o2)));

    // Choose the best individual with probability p, 2nd with p(1-p), 3rd with p(1-p)^2 etc.
    int i = 0;
    // System.out.println("tournament size: " + tournamentList.size());
    for (Individual individual : tournamentList) {
      if (rand.nextDouble() < (p * Math.pow((1 - p), i))) {
        survivors.add(individual);
        // System.out.println("added to survivors: " + individual.hashCode());
        if (survivors.size() >= (int) (survivalProportion * population.getSize())) {
          break;
        }
      }
      i++;
    }
  }
}

