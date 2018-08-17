package uni.evocomp.a1.selectsurvivors;

import static org.junit.Assert.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import uni.evocomp.a1.Individual;
import uni.evocomp.a1.Population;
import uni.evocomp.a1.TSPIO;
import uni.evocomp.a1.TSPProblem;
import uni.evocomp.util.RandomStub;

public class FitnessProportionalTest {

  private RandomStub rand;
  private FitnessProportional fp;
  private TSPProblem problem;
  private Population population;
  Individual i0;
  Individual i1;
  Individual i2;
  Individual i3;
  Individual i4;
  Individual i5;

  @Before
  public void setUp() throws Exception {
    rand = new RandomStub();
    TSPIO io = new TSPIO();
    try (Reader r = new FileReader("tests/custom/test1.tsp")) {
      problem = io.read(r);
    } catch (IOException e) {
      e.printStackTrace();
    }
    // initialise the population
    population = new Population();
    // construct individuals using integer lists
    i0 = new Individual(Arrays.asList(1, 2, 3, 4, 5), problem); // 54
    i1 = new Individual(Arrays.asList(1, 2, 5, 4, 3), problem); // 60
    i2 = new Individual(Arrays.asList(5, 2, 1, 4, 3), problem); // 66
    i3 = new Individual(Arrays.asList(1, 3, 2, 4, 5), problem); // 68
    i4 = new Individual(Arrays.asList(1, 3, 2, 5, 4), problem); // 72
    i5 = new Individual(Arrays.asList(1, 4, 2, 5, 3), problem); // 80
    // debugging purposes
    // System.out.println("i0: " + i0.hashCode());
    // System.out.println("i1: " + i1.hashCode());
    // System.out.println("i2: " + i2.hashCode());
    // System.out.println("i3: " + i3.hashCode());
    // System.out.println("i4: " + i4.hashCode());
    // System.out.println("i5: " + i5.hashCode());
    population.add(i0);
    population.add(i1);
    population.add(i2);
    population.add(i3);
    population.add(i4);
    population.add(i5);
  }

  /**
   * Buckets:
   * 
   * <pre>
   * 0.313953488372093 
   * 0.5581395348837209 
   * 0.7325581395348837 
   * 0.8837209302325582 
   * 0.9883720930232558
   * 1.0
   * </pre>
   */
  
  // Basic test
  @Test
  public void test1() {
    fp = new FitnessProportional();
    rand.setDoubles(Arrays.asList(0.0, 0.32, 0.56));
    Population result = fp.selectSurvivors(population, problem, rand);
    assertEquals(3, result.getSize());
    assertTrue(result.getPopulation().containsAll(Arrays.asList(i0,i1,i2)));
  }
  
  // Test for duplicates
  @Test
  public void test2() {
    fp = new FitnessProportional();
    rand.setDoubles(Arrays.asList(0.0, 0.1, 0.32, 0.56));
    Population result = fp.selectSurvivors(population, problem, rand);
    assertEquals(3, result.getSize());
    assertTrue(result.getPopulation().containsAll(Arrays.asList(i0,i1,i2)));
  }
  
  @Test
  public void test3() {
    fp = new FitnessProportional();
    rand.setDoubles(Arrays.asList(1.0, 0.9, 0.1));
    Population result = fp.selectSurvivors(population, problem, rand);
    assertEquals(3, result.getSize());
    assertTrue(result.getPopulation().containsAll(Arrays.asList(i5,i4,i0)));
  }
  
  @Test
  public void test4() {
    fp = new FitnessProportional();
    rand.setDoubles(Arrays.asList(0.7, 0.8, 0.99));
    Population result = fp.selectSurvivors(population, problem, rand);
    assertEquals(3, result.getSize());
    assertTrue(result.getPopulation().containsAll(Arrays.asList(i2,i3,i5)));
  }
  
  @Test
  public void test5() {
    fp = new FitnessProportional();
    rand.setDoubles(Arrays.asList(0.0, 0.8, 1.1));
    Population result = fp.selectSurvivors(population, problem, rand);
    assertEquals(3, result.getSize());
    assertTrue(result.getPopulation().containsAll(Arrays.asList(i0,i3,i5)));
  }

}
