package uni.evocomp.a1;

import static junit.framework.Assert.assertEquals;

import java.util.Random;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import uni.evocomp.util.IntegerPair;

public class OrderCrossoverTest {
  OrderCrossover crossover;

  @Before
  public void setUp() {
    crossover = new OrderCrossover();
  }

  @Test
  public void testCrossover1() {
    Individual a = new Individual(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
    Individual b = new Individual(Arrays.asList(9, 3, 7, 8, 2, 6, 5, 1, 4));
    Individual child = crossover.recombine(a, b, new IntegerPair(3, 7));
    assertEquals(Arrays.asList(3, 8, 2, 4, 5, 6, 7, 1, 9), child.getGenotype());
  }

  @Test
  public void testCrossover2() {
    Individual a = new Individual(Arrays.asList(1, 2, 3, 6, 5, 4));
    Individual b = new Individual(Arrays.asList(2, 5, 6, 1, 4, 3));
    Individual child = crossover.recombine(a, b, new IntegerPair(0, 3));
    assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6), child.getGenotype());
  }

  @Test
  public void testFuzzed() {
    Random r = new Random();
    for (int i = 3; i < 16; i += 1) {
      Individual a = new Individual(i);
      Individual b = new Individual(i);
      crossover.recombine(a, b);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEntireSlice() {
    Individual a = new Individual(Arrays.asList(1, 2));
    Individual b = new Individual(Arrays.asList(2, 1));
    Individual child = crossover.recombine(a, b, new IntegerPair(0, 2));
    assertEquals(Arrays.asList(1, 2), child.getGenotype());
  }

  @Test
  public void testSliceSmall() {
    Individual a = new Individual(Arrays.asList(1, 2));
    Individual b = new Individual(Arrays.asList(2, 1));
    Individual child = crossover.recombine(a, b, new IntegerPair(0, 1));
    assertEquals(Arrays.asList(1, 2), child.getGenotype());
  }

  @Test
  public void testPastArrayEdge() {
    Individual a = new Individual(Arrays.asList(1, 2, 3, 4));
    Individual b = new Individual(Arrays.asList(1, 2, 4, 3));
    Individual child = crossover.recombine(a, b, new IntegerPair(3, 1));
    assertEquals(Arrays.asList(1, 2, 3, 4), child.getGenotype());
  }
}
