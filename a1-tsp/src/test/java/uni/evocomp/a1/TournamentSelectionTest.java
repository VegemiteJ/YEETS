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

  protected void setUp() throws Exception {
    super.setUp();
    // set up the random stub
    rand = new RandomStub();
    TSPIO io = new TSPIO();
    try (Reader r = new FileReader("testmaps/test1.tsp")) {
      problem = io.read(r);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * test1 map: cities: 0 0 0 10 0 20 10 20 10 10
   */


  @Test
  public void test1() {
    tournament = new TournamentSelection(2, 0.5, 0.8);
    Population population = new Population();
    // construct individuals using integer lists
    Individual i1 = new Individual(Arrays.asList(1, 2, 3, 4, 5)); // 40
    Individual i2 = new Individual(Arrays.asList(1, 3, 2, 5, 4)); // 50
    Individual i3 = new Individual(Arrays.asList(5, 1, 2, 3, 4)); // 44
    Individual i4 = new Individual(Arrays.asList(1, 3, 2, 4, 5)); // 54
    Individual i5 = new Individual(Arrays.asList(5, 2, 1, 4, 3)); // 52
    Individual i6 = new Individual(Arrays.asList(1, 5, 2, 4, 3)); // 48
    population.population.add(i1);
    population.population.add(i2);
    population.population.add(i3);
    population.population.add(i4);
    population.population.add(i5);
    population.population.add(i6);
    // seed the randomstub TODO: use proper seeds
    rand.setInt(Arrays.asList(1));
    rand.setDoubles(Arrays.asList(1.0));
    Population result = tournament.selectSurvivors(population, problem, rand);
    // population should be halved
    assertEquals(result.size, 3);
    // TODO: check the survivor to see they're who we expect
  }

}
