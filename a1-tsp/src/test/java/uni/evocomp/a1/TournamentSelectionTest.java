package uni.evocomp.a1;

import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Random;
import org.junit.Test;
import junit.framework.TestCase;

public class TournamentSelectionTest extends TestCase {

  private RandomStub rand;
  private TournamentSelection tournament;
  private TSPProblem problem;
  private Population population;
  Individual i1;
  Individual i2;
  Individual i3;
  Individual i4;
  Individual i5;
  Individual i6;

  protected void setUp() throws Exception {
    super.setUp();
    // set up the random stub
    rand = new RandomStub();
    TSPIO io = new TSPIO();
    try (Reader r = new FileReader("src/test/java/uni/evocomp/a1/testmaps/test1.tsp")) {
      problem = io.read(r);
    } catch (IOException e) {
      e.printStackTrace();
    }
    // initialise the population
    population = new Population();
    // construct individuals using integer lists
    i1 = new Individual(Arrays.asList(1, 2, 3, 4, 5)); // 40
    i2 = new Individual(Arrays.asList(1, 3, 2, 5, 4)); // 50
    i3 = new Individual(Arrays.asList(5, 1, 2, 3, 4)); // 44
    i4 = new Individual(Arrays.asList(1, 3, 2, 4, 5)); // 54
    i5 = new Individual(Arrays.asList(5, 2, 1, 4, 3)); // 52
    i6 = new Individual(Arrays.asList(1, 5, 2, 4, 3)); // 48
    // debugging purposes
    // System.out.println("i1: " + i1.hashCode());
    // System.out.println("i2: " + i2.hashCode());
    // System.out.println("i3: " + i3.hashCode());
    // System.out.println("i4: " + i4.hashCode());
    // System.out.println("i5: " + i5.hashCode());
    // System.out.println("i6: " + i6.hashCode());
    population.add(i1);
    population.add(i2);
    population.add(i3);
    population.add(i4);
    population.add(i5);
    population.add(i6);
  }

  /**
   * test1 map: cities: 0 0 0 10 0 20 10 20 10 10
   */


  @Test
  public void test1() {
    tournament = new TournamentSelection(6, 0.5, 0.99);
    // seed the randomstub
    rand.setInt(Arrays.asList(1));
    rand.setDoubles(Arrays.asList(0.0));
    Population result = tournament.selectSurvivors(population, problem, rand);
    // population should be halved
    assertEquals(3, result.getSize());
    // check the survivor to see they're who we expect
    // we expect individuals 1, 4 and 6 to survive because they have the lowest cost
    assertTrue(result.getPopulation().contains(i1));
    assertTrue(result.getPopulation().contains(i3));
    assertTrue(result.getPopulation().contains(i6));
  }
  
  @Test
  public void test2() {
    tournament = new TournamentSelection(6, 0.5, 0.99);
    // seed the randomstub
    rand.setInt(Arrays.asList(1));
    rand.setDoubles(Arrays.asList(0.0, 1.0));
    Population result = tournament.selectSurvivors(population, problem, rand);
    // population should be halved
    assertEquals(3, result.getSize());
    // check the survivor to see they're who we expect
    // we expect individuals 1, 6 and 5 as we skip every second individual due to RNG
    assertTrue(result.getPopulation().contains(i1));
    assertTrue(result.getPopulation().contains(i6));
    assertTrue(result.getPopulation().contains(i5));
  }
  
  @Test
  public void test3() {
    tournament = new TournamentSelection(6, 0.5, 0.99);
    // seed the randomstub
    rand.setInt(Arrays.asList(1));
    rand.setDoubles(Arrays.asList(0.0, 0.0, 1.0));
    Population result = tournament.selectSurvivors(population, problem, rand);
    // population should be halved
    assertEquals(3, result.getSize());
    // check the survivor to see they're who we expect
    // we expect individuals 1, 3 and 2 due to RNG
    assertTrue(result.getPopulation().contains(i1));
    assertTrue(result.getPopulation().contains(i3));
    assertTrue(result.getPopulation().contains(i2));
  }

}
