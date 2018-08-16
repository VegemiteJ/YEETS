package uni.evocomp.a1.mutate;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import junit.framework.TestCase;
import org.junit.Test;
import uni.evocomp.a1.Individual;
import uni.evocomp.a1.TSPProblem;
import uni.evocomp.util.RandomStub;
import uni.evocomp.util.Util;

public class MutateImplTest extends TestCase {
  private RandomStub rand;
  private TSPProblem problem;

  protected void setUp() throws Exception {
    super.setUp();
    // set up the random stub
    rand = new RandomStub();
    problem = new TSPProblem(Util.createOnesMatrix(10));
  }

  @Test
  public void testExpectedNoMutate() {
    Mutate m = new WhiteBoxMutateImpl();
    Individual i = new Individual(Arrays.asList(1, 2, 3, 4, 5, 6));
    rand.setDoubles(Arrays.asList(0.6));
    rand.setInt(Arrays.asList(0, 1));
    m.mutateWithProbability(0.5, problem, i, rand);

    assertEquals(1, i.getGenotype().get(0).intValue());
    assertEquals(2, i.getGenotype().get(1).intValue());
  }

  @Test
  public void testExpectedMutate() {
    Mutate m = new WhiteBoxMutateImpl();
    Individual i = new Individual(Arrays.asList(1, 2, 3, 4, 5, 6));
    rand.setDoubles(Arrays.asList(0.6));
    rand.setInt(Arrays.asList(0, 1));
    m.mutateWithProbability(0.7, problem, i, rand);

    assertEquals(-1, i.getGenotype().get(0).intValue());
    assertEquals(-2, i.getGenotype().get(1).intValue());
  }

  @Test
  public void testExpectedMutateWithHighProbability() {
    Mutate m = new WhiteBoxMutateImpl();
    Individual i = new Individual(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

    Random r = new Random();
    List<Double> probabilities = Arrays.asList(0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9);
    for (double mutateProb : probabilities) {
      int sum = 0;
      int totalSum = 100000;
      for (int itr = 0; itr < totalSum; itr++) {
        Individual j = new Individual(i);
        m.mutateWithProbability(mutateProb, problem, j, r);
        sum += j.getGenotype().stream().filter(k -> (k.intValue() < 0)).count();
      }
      // Mutating with probability 0.1 should mean sum/(2*totalSum) ~ 0.1
      double result = (double) sum / ((double) totalSum * 2.0);
      assertEquals(mutateProb, result, 0.01);
    }
  }

  @Test
  public void testCertainMutate() {
    Mutate m = new WhiteBoxMutateImpl();
    Individual i = new Individual(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

    Random r = new Random();
    int sum = 0;
    int totalSum = 100000;
    for (int itr = 0; itr < totalSum; itr++) {
      Individual j = new Individual(i);
      m.mutateWithProbability(1.0, problem, j, r);
      sum += j.getGenotype().stream().filter(k -> (k.intValue() < 0)).count();
    }
    assertEquals(sum, totalSum * 2);
  }

  @Test
  public void testCertainNoMutate() {
    Mutate m = new WhiteBoxMutateImpl();
    Individual i = new Individual(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

    Random r = new Random();
    int sum = 0;
    int totalSum = 100000;
    for (int itr = 0; itr < totalSum; itr++) {
      Individual j = new Individual(i);
      m.mutateWithProbability(0.0, problem, j, r);
      sum += j.getGenotype().stream().filter(k -> (k.intValue() < 0)).count();
    }
    assertEquals(sum, 0);
  }
}
