package uni.evocomp.a1.recombine;

import static junit.framework.Assert.assertEquals;

import java.util.Random;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import uni.evocomp.a1.TSPProblem;
import uni.evocomp.util.IntegerPair;
import uni.evocomp.a1.Individual;

public class OrderCrossoverTest {

  private OrderCrossover crossover;
  private TSPProblem p;

  @Before
  public void setUp() {
    crossover = new OrderCrossover();
    p = null;
  }

  @Test
  public void testCrossover1() {
    Individual a = new Individual(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
    Individual b = new Individual(Arrays.asList(9, 3, 7, 8, 2, 6, 5, 1, 4));
    Individual child = crossover.recombine(a, b, new IntegerPair(3, 7), p);
    assertEquals(Arrays.asList(3, 8, 2, 4, 5, 6, 7, 1, 9), child.getGenotype());
  }

  @Test
  public void testCrossover2() {
    Individual a = new Individual(Arrays.asList(1, 2, 3, 6, 5, 4));
    Individual b = new Individual(Arrays.asList(2, 5, 6, 1, 4, 3));
    Individual child = crossover.recombine(a, b, new IntegerPair(0, 3), p);
    assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6), child.getGenotype());
  }

  @Test
  public void testFuzzed() {
    Random r = new Random();
    for (int i = 3; i < 16; i += 1) {
      Individual a = new Individual(i);
      Individual b = new Individual(i);
      crossover.recombine(a, b, p);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEntireSlice() {
    Individual a = new Individual(Arrays.asList(1, 2));
    Individual b = new Individual(Arrays.asList(2, 1));
    Individual child = crossover.recombine(a, b, new IntegerPair(0, 2), p);
    assertEquals(Arrays.asList(1, 2), child.getGenotype());
  }

  @Test
  public void testSliceSmall() {
    Individual a = new Individual(Arrays.asList(1, 2));
    Individual b = new Individual(Arrays.asList(2, 1));
    Individual child = crossover.recombine(a, b, new IntegerPair(0, 1), p);
    assertEquals(Arrays.asList(1, 2), child.getGenotype());
  }

  @Test
  public void testPastArrayEdge() {
    Individual a = new Individual(Arrays.asList(1, 2, 3, 4));
    Individual b = new Individual(Arrays.asList(1, 2, 4, 3));
    Individual child = crossover.recombine(a, b, new IntegerPair(3, 1), p);
    assertEquals(Arrays.asList(1, 2, 3, 4), child.getGenotype());
  }
}
