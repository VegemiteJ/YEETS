package uni.evocomp.a1.selectsurvivors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import uni.evocomp.a1.Individual;
import uni.evocomp.a1.Population;
import uni.evocomp.a1.TSPProblem;

public class FitnessProportional implements SelectSurvivors {
  private Double maxCost;
  private List<Double> buckets;
  private List<Individual> individuals;
  private double survivalProportion;

  public FitnessProportional() {
    survivalProportion = 0.5;
    maxCost = 0.0;
  }

  @Override
  public Population selectSurvivors(Population population, TSPProblem problem, Random rand) {
    Set<Individual> survivorSet = new HashSet<>();

    for (Individual individual : population.getPopulation()) {
      double c = individual.getCost(problem);
      if (c > maxCost) {
        maxCost = c;
      }
    }
    buckets = new ArrayList<>();
    individuals = new ArrayList<>(population.getPopulation());
    double totalFit = individuals.stream().mapToDouble(i -> calcFitness(i, problem)).sum();
    double cumProb = 0.0;

    // the range for individuals[i] is
    // buckets[i-1] and buckets[i]

    // i.e. if buckets[i-1] < x < buckets[i], then individuals[i]
    // is chosen to survive
    for (Individual individual : individuals) {
      cumProb += (double) ((double) calcFitness(individual, problem) / (double) totalFit);
      // System.out.println(cumProb);
      buckets.add(cumProb);
    }

    // run the roulette until survivorSet is full
    while (survivorSet.size() < (int) (population.getSize() * survivalProportion)) {
      Double r = rand.nextDouble();
      int i;
      for (i = 0; i < buckets.size(); i++) {
        if (r < buckets.get(i))
          break;
      }
      if (i >= buckets.size()) {
        i--;
      }
      survivorSet.add(individuals.get(i));
    }
    return new Population(survivorSet);
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
