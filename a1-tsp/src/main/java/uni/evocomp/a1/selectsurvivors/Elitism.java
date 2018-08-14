package uni.evocomp.a1.selectsurvivors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import uni.evocomp.a1.Individual;
import uni.evocomp.a1.Population;
import uni.evocomp.a1.TSPProblem;

/**
 * Have some portion of the population that is guaranteed a place in the thing, selected by highest
 * fitness straight up.
 * 
 * From the lecture slides (01-Intro:47)
 * <pre>
 * - Most EAs use fixed population size so need a way of going from (parents + offspring) to next generation
 * - Often deterministic
 *   – Fitness based : e.g., rank parents + offspring and take best
 *   – Age based: make as many offspring as parents and delete all parents
 * - Sometimes do combination (elitism)
 * </pre>
 * 
 * @author Namdrib
 *
 */
public class Elitism implements SelectSurvivors {

  double survivalRate; // the proportion fo the input population to steal solely based on fitness

  public Elitism() {
    survivalRate = 0.25;
  }

  public Elitism(double survivalRate) {
    this.survivalRate = survivalRate;
  }

  /**
   * Don't need to use <code>rand</code>
   * Assume the first half of the population is the parents, next half is children.
   * Therefore only want to steal some percentage of the first half
   * As a backing algorithm, call tournament select on the remaining Individuals
   */
  @Override
  public Population selectSurvivors(Population population, TSPProblem problem, Random rand) {
    List<Individual> sortedPopulation = new ArrayList<>();
    
    // Really hope here that the population is ordered by insertion order
    int i = 0;
    for (Individual individual : population.getPopulation()) {
      if (i > population.getSize()) {
        break;
      }
      sortedPopulation.add(individual);
    }
    i++;
    Collections.sort(sortedPopulation);

    sortedPopulation.forEach(i -> System.out.println("Cost is " + i.getCost(problem)));
    
    // Add the first survivalRate portion of
    List<Individual> out = new ArrayList<>();
    for (int i=0; i < sortedPopulation.size()/2 * survivalRate; i++) {
      out.add(sortedPopulation.get(i));
    }
    return new Population(population);
  }

}
