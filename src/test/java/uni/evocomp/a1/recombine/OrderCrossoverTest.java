package uni.evocomp.a1.recombine;

import static junit.framework.Assert.assertEquals;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import uni.evocomp.util.IntegerPair;
import uni.evocomp.a1.Individual;

public class OrderCrossoverTest {

  private OrderCrossover crossover;

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

  @Test(expected = Test.None.class)
  public void testFuzzed() throws IllegalStateException {
    for (int n = 3; n < 300; n += 1) {
      IntegerPair p = crossover.getRandomSlice(n);
      Individual a = new Individual(n);
      Individual b = new Individual(n);
      Individual child = crossover.recombine(a, b, p);
      try {
        child.assertIsValidTour();
      } catch (IllegalStateException e) {
        System.out.println("n: " + n);
        System.out.println("Pair: " + p);
        System.out.println("a: " + a.getGenotype().toString());
        System.out.println("b: " + b.getGenotype().toString());
        System.out.println("child: " + child.getGenotype().toString());
        throw e;
      }
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
