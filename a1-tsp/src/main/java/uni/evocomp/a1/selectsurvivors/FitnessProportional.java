package uni.evocomp.a1.selectsurvivors;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import uni.evocomp.a1.Individual;
import uni.evocomp.a1.Population;
import uni.evocomp.a1.TSPProblem;

public class FitnessProportional implements SelectSurvivors {
  private Double maxCost;
  private List<Double> buckets;
  private List<Individual> individuals;

  public FitnessProportional() {
    ;
  }

  @Override
  public Population selectSurvivors(Population population, TSPProblem problem, Random rand) {
    // TODO Auto-generated method stub
    for (Individual individual : population.getPopulation()) {
      double c = individual.getCost(problem);
      if (c > maxCost) {
        maxCost = c;
      }
    }
    buckets = new ArrayList<>();
    individuals = new ArrayList<>(population.getPopulation());
    double totalCost = individuals.stream().mapToDouble(i -> i.getCost(problem)).sum();
    double cumProb = 0.0;
    
    // the range for individuals[i] is
    // buckets[i-1] and buckets[i]
    
    // i.e. if buckets[i-1] < x < buckets[i], then individuals[i]
    // is chosen to survive
    for (Individual individual : individuals) {
      cumProb += (double)((double)individual.getCost(problem) / (double)totalCost); // im paranoid ok
      buckets.add(cumProb);
    }
    
    // run the roulette
    Double r = rand.nextDouble();

    return null;
  }

  /**
   * Helper function: Converts from cost into "fitness" Should only be called when maxCost has been
   * assigned
   * 
   * @param individual
   * @return
   */
  private double calcFitness(Individual individual, TSPProblem problem) {
    return maxCost - individual.getCost(problem) + 1;
  }


}
