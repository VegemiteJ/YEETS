package uni.evocomp.a1.selectsurvivors;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import uni.evocomp.a1.Individual;
import uni.evocomp.a1.Population;
import uni.evocomp.a1.TSPProblem;

/**
 * Have some portion of the population that is guaranteed a place in the thing, selected by highest
 * fitness straight up.
 * 
 * After the appropriate number of parents are taken, randomly select the rest of the survivors
 * 
 * From the lecture slides (01-Intro:47)
 * 
 * <pre>
 * - Most EAs use fixed population size so need a way of going from (parents + offspring) to next generation
 * - Often deterministic
 *   Fitness based : e.g., rank parents + offspring and take best
 *   Age based: make as many offspring as parents and delete all parents
 * - Sometimes do combination (elitism)
 * </pre>
 * 
 * @author Namdrib
 *
 */
public class Elitism implements SelectSurvivors {

  double eliteProportion; // the proportion of the input population to steal solely based on fitness
  private double survivalProportion; // the proportion of the input population to take through

  public Elitism() {
    eliteProportion = 0.25;
    survivalProportion = 0.5;
  }

  /**
   * Constructor to set eliteProportion
   * 
   * @param eliteProportion portion of parents to preserve
   */
  Elitism(double eliteProportion) {
    this();
    this.eliteProportion = eliteProportion;
  }

  /**
   * Constructor to set eliteProportion and survivalProportion
   * 
   * @param eliteProportion portion of parents to preserve
   * @param survivalProportion portion of population to preserve
   */
  Elitism(double eliteProportion, double survivalProportion) {
    this(eliteProportion);
    this.survivalProportion = survivalProportion;
  }

  /**
   * Don't need to use <code>rand</code>
   * 
   * Assume the first half of the population is the parents, next half is children. Therefore only
   * want to steal some percentage of the first half
   * 
   * As a backing algorithm, call tournament select on the remaining Individuals
   */
  @Override
  public Population selectSurvivors(Population population, TSPProblem problem, Random rand) {
    List<Individual> parents = new ArrayList<>(); // separate the population into parents and
    Set<Individual> notElite = new LinkedHashSet<>(); // non/parents (in notElite)

    // Really hope here that the population is ordered by insertion order
    // Divide the population into the parents (first half) and children (second half)
    int count = 0;
    for (Individual individual : population.getPopulation()) {
      ((count++ < (population.getSize() / 2)) ? parents : notElite).add(individual);
    }
    Collections.sort(parents); // lowest cost first

    // Add the first survivalRate portion of the parents, everything else to notElite
    // At the end, elite is what gets returned
    Set<Individual> elite = new LinkedHashSet<>();
    count = 0;
    for (Individual parent : parents) {
      if (elite.size() >= (int) (survivalProportion * population.getSize())) {
        break;
      }
      ((count++ < (eliteProportion * parents.size())) ? elite : notElite).add(parent);
    }

    // At this point, notElite has everything except the fittest however many Individuals

    // Randomly select remaining population until appropriate size reached
    List<Individual> notEliteList = new ArrayList<>(notElite);
    Collections.shuffle(notEliteList);
    for (Individual individual : notEliteList) {
      if (elite.size() >= (int) (survivalProportion * population.getSize())) {
        break;
      }
      elite.add(individual);
    }

    return new Population(elite);
  }
}
