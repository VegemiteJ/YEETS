package uni.evocomp.a1.selectsurvivors;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import uni.evocomp.a1.Individual;
import uni.evocomp.a1.Population;
import uni.evocomp.a1.TSPProblem;
import uni.evocomp.a1.evaluate.Evaluate;
import uni.evocomp.a1.evaluate.EvaluateEuclid;

/**
 * TournamentSelection class, one of the implementations of the SelectSurvivors interface Contains
 * configurable fields:
 * 
 * tournamentSize: the number of individuals per tournament
 * <p>
 * survivalProportion: a double representing the proportion of the tournament that should survive
 * <p>
 * p the probability that the fittest individual survives in a given tournament run
 * <p>
 * (p * (1-P) ^ i = the probability that the ith fittest individual survives)
 * <p>
 * 
 * @author joshuafloh
 *
 */
public class TournamentSelection implements SelectSurvivors {
  private Evaluate evaluate;
  private int tournamentSize;
  private double survivalProportion;
  private double p;

  /**
   * Default constructor, this generally shouldn't be called ever
   */
  public TournamentSelection() {
    this.evaluate = new EvaluateEuclid();
    
//    tournamentSize = 0.5;
    survivalProportion = 0.5;
    p = 0.9;
  }

  /**
   * Initialise the TournamentSelection object by assigning its configurable fields
   * 
   * @param tournamentSize the proportion of individuals per run of a tournament
   * @param survivalProportion the proportion of survivors per tournament
   * @param p (p*(1-p)^i) = the probability that the ith fittest survives
   */
  TournamentSelection(int tournamentSize, double survivalProportion, double p) {
    this();
    this.tournamentSize = tournamentSize; // should I just change this to a prop now?
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

  /**
   * Runs a single run of a tournament, adds survivors to the survivors set
   * 
   * @param population the whole population
   * @param problem the TSP map
   * @param rand a random number generator
   * @param survivors set containing all the current survivors
   */
  private void runTournament(Population population, TSPProblem problem, Random rand,
      Set<Individual> survivors) {
    Set<Integer> s = new LinkedHashSet<>();
    List<Individual> tournamentList = new ArrayList<>();

    // choose k random individuals from population
    if (tournamentSize < population.getSize()) {
      while (s.size() < tournamentSize) {
        int index = rand.nextInt(population.getSize());
        s.add(index);
      }
      // Iterate over HashSet and add individuals to the tournament list
      List<Individual> individualList = new ArrayList<>(population.getPopulation());
      Iterator<Integer> itr = s.iterator();
      while (itr.hasNext()) {
        int index = itr.next();
        Individual next = individualList.get(index);
        tournamentList.add(next);
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
      if (rand.nextDouble() < (p * Math.pow((1 - p), i))) {
        survivors.add(individual);
        if (survivors.size() >= (int) (survivalProportion * population.getSize())) {
          break;
        }
      }
      i++;
    }
  }
}

