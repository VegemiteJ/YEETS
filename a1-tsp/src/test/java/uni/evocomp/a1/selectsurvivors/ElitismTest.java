package uni.evocomp.a1.selectsurvivors;

import static org.junit.Assert.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import org.junit.Before;
import org.junit.Test;
import uni.evocomp.a1.Individual;
import uni.evocomp.a1.Population;
import uni.evocomp.a1.TSPIO;
import uni.evocomp.a1.TSPProblem;

public class ElitismTest {

  private SelectSurvivors selector;
  private TSPProblem problem;
  private Population population;

  // Basically always guarantee the lowest cost of the first half (i0 to i3) is the first of the
  // resultant Individuals. Then the rest are put in in any order
  private Individual i0, i1, i2, i3, i4, i5, i6, i7;

  @Before
  public void setUp() throws Exception {
    TSPIO io = new TSPIO();
    try (Reader r = new FileReader("tests/custom/test1.tsp")) {
      problem = io.read(r);
    } catch (IOException e) {
      e.printStackTrace();
    }
    selector = new Elitism();

    // TODO : Update routes
    // maybe make a helper script to generate every single permutation of cost?
    // therefore don't have to manually select stuff
    i0 = new Individual(Arrays.asList(1, 2, 3, 4, 5), problem); // 54
    i1 = new Individual(Arrays.asList(1, 2, 3, 5, 4), problem); // 66
    i2 = new Individual(Arrays.asList(1, 2, 4, 3, 5), problem); // 62
    i3 = new Individual(Arrays.asList(1, 2, 4, 5, 3), problem); // 68
    i4 = new Individual(Arrays.asList(1, 2, 5, 4, 3), problem); // 60
    i5 = new Individual(Arrays.asList(1, 3, 2, 5, 4), problem); // 70
    i6 = new Individual(Arrays.asList(1, 3, 5, 2, 4), problem); // 80
    i7 = new Individual(Arrays.asList(2, 3, 1, 4, 5), problem); // 72

    population = new Population();
    population.add(i0);
    population.add(i1);
    population.add(i2);
    population.add(i3);
    population.add(i4);
    population.add(i5);
    population.add(i6);
    population.add(i7);
  }

  // Expect children to be the same as the population
  @Test
  public void testAllElite() {
    selector = new Elitism(1.0);
    Population newPop = selector.selectSurvivors(population, problem, ThreadLocalRandom.current());
    assertTrue(population.getPopulation().containsAll(newPop.getPopulation()));
  }

  // Expect children to be the same as the population
  @Test
  public void testMoreThanAllElite() {
    selector = new Elitism(2.0);
    Population newPop = selector.selectSurvivors(population, problem, ThreadLocalRandom.current());
    assertTrue(population.getPopulation().containsAll(newPop.getPopulation()));
  }

  // The one with the cheapest cost (out of 8) must be elite (25%)
  // since only half of the population were parents (now out of 4)
  @Test
  public void testOneElite() {
    Population newPop = selector.selectSurvivors(population, problem, ThreadLocalRandom.current());
    assertTrue(newPop.getPopulation().contains(i0));
  }

  // The one with the cheapest cost (out of 8) must be elite (25%)
  // since only half of the population were parents (now out of 4)
  @Test
  public void testTwoElite() {
    selector = new Elitism(0.5);
    Population newPop = selector.selectSurvivors(population, problem, ThreadLocalRandom.current());
    assertTrue(newPop.getPopulation().contains(i0));
    assertTrue(newPop.getPopulation().contains(i2));
  }
}
