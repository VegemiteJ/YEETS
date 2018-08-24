package uni.evocomp.a1.selectparents;

import static org.junit.Assert.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import uni.evocomp.a1.Global;
import uni.evocomp.a1.Individual;
import uni.evocomp.a1.Population;
import uni.evocomp.a1.TSPIO;
import uni.evocomp.a1.TSPProblem;
import uni.evocomp.util.Pair;

public class UniformRandomTest {
  private Population population;
  private TSPProblem problem;
  Individual i0;
  Individual i1;
  Individual i2;
  Individual i3;
  Individual i4;
  Individual i5;

  @Before
  public void setUp() throws Exception {
    TSPIO io = new TSPIO();
    try (Reader r = new FileReader(Global.testPath + "/custom/test1.tsp")) {
      problem = io.read(r);
    } catch (IOException e) {
      e.printStackTrace();
    }

    population = new Population();
    // construct individuals using integer lists
    i0 = new Individual(Arrays.asList(1, 2, 3, 4, 5), problem); // 40
    i1 = new Individual(Arrays.asList(1, 3, 2, 5, 4), problem); // 50
    i2 = new Individual(Arrays.asList(5, 1, 2, 3, 4), problem); // 44
    i3 = new Individual(Arrays.asList(1, 3, 2, 4, 5), problem); // 54
    i4 = new Individual(Arrays.asList(5, 2, 1, 4, 3), problem); // 52
    i5 = new Individual(Arrays.asList(1, 5, 2, 4, 3), problem); // 48

    population.add(i0);
    population.add(i1);
    population.add(i2);
    population.add(i3);
    population.add(i4);
    population.add(i5);
  }

  @Test
  public void test() {
    UniformRandom uni = new UniformRandom();
    List<Individual> individualList = new ArrayList<>(population.getPopulation());
    List<Pair<Individual, Individual>> parentsList = uni.setParents(individualList);
    assertEquals(individualList.get(0), parentsList.get(0).first);
    assertEquals(individualList.get(1), parentsList.get(0).second);
    assertEquals(individualList.get(2), parentsList.get(1).first);
    assertEquals(individualList.get(3), parentsList.get(1).second);
    assertEquals(individualList.get(4), parentsList.get(2).first);
    assertEquals(individualList.get(5), parentsList.get(2).second);
  }

}
