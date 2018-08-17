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
  public void setUp() throws Exception
  {
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
    i0 = new Individual(Arrays.asList(1, 2, 3, 4, 5)); // 40
    i1 = new Individual(Arrays.asList(1, 3, 2, 5, 4)); // 50
    i2 = new Individual(Arrays.asList(5, 1, 2, 3, 4)); // 44
    i3 = new Individual(Arrays.asList(1, 3, 2, 4, 5)); // 54
    i4 = new Individual(Arrays.asList(5, 2, 1, 4, 3)); // 52
    i5 = new Individual(Arrays.asList(1, 5, 2, 4, 3)); // 48
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

  @Test
  public void test1() {
    fp = new FitnessProportional();
    rand.setDoubles(Arrays.asList(0.0, 0.5, 0.9, 0.8));
    Population result = fp.selectSurvivors(population, problem, rand);
    assertEquals(3, result.getSize());
  }

}
