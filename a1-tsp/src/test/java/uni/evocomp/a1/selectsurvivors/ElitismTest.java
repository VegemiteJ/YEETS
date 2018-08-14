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
    i0 = new Individual(Arrays.asList(1, 2, 3, 4, 5), 99.0);
    i1 = new Individual(Arrays.asList(1, 2, 3, 4, 5), 90.0);
    i2 = new Individual(Arrays.asList(1, 2, 3, 4, 5), 9.00);
    i3 = new Individual(Arrays.asList(1, 2, 3, 4, 5), 1.00);
    i4 = new Individual(Arrays.asList(1, 2, 3, 4, 5), 20.0);
    i5 = new Individual(Arrays.asList(1, 2, 3, 4, 5), 10.0);
    i6 = new Individual(Arrays.asList(1, 2, 3, 4, 5), 5.01);
    i7 = new Individual(Arrays.asList(1, 2, 3, 4, 5), 5.20);
  }

  // Expect children to be the same as the population
  @Test
  public void testAllElite() {
    selector = new Elitism(1.0);
    Population newPop = selector.selectSurvivors(population, problem, ThreadLocalRandom.current());
    assertEquals(population.getPopulation(), newPop.getPopulation());
  }

  // Expect children to be the same as the population
  @Test
  public void testMoreThanAllElite() {
    selector = new Elitism(2.0);
    Population newPop = selector.selectSurvivors(population, problem, ThreadLocalRandom.current());
    assertEquals(population.getPopulation(), newPop.getPopulation());
  }

  @Test
  public void testOneElite() {
    Population newPop = selector.selectSurvivors(population, problem, ThreadLocalRandom.current());
    assertEquals(newPop.getPopulation().)
  }
}
