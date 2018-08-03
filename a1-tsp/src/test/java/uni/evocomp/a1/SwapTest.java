package uni.evocomp.a1;

import junit.framework.TestCase;
import org.junit.Test;
import uni.evocomp.util.IntegerPair;
import uni.evocomp.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SwapTest extends TestCase {

  private List<Integer> original;
  private Mutate m;

  protected void setUp() throws Exception {
    super.setUp();
    original = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5));
    m = new Swap();
  }

  @Test
  public void testSwapValid() {
    Individual i = new Individual(new ArrayList<Integer>(original));
    m.run(i, new ArrayList<>(Arrays.asList(new IntegerPair(1, 4))));
    assertEquals(i.getGenotype(), Arrays.asList(1, 5, 3, 4, 2));
  }

  @Test
  public void testSwapSameIndex() {
    Individual i = new Individual(new ArrayList<Integer>(original));
    m.run(i, new ArrayList<>(Arrays.asList(new IntegerPair(2, 2))));
    assertEquals(i.getGenotype(), original);
  }

  @Test
  public void testSwapBadOrder() {
    Individual i = new Individual(new ArrayList<Integer>(original));
    m.run(i, new ArrayList<>(Arrays.asList(new IntegerPair(4, 1))));
    assertEquals(i.getGenotype(), Arrays.asList(1, 5, 3, 4, 2));
  }

  @Test
  public void testSwapOutOfBoundsLow() {
    Individual i = new Individual(new ArrayList<Integer>(original));
    m.run(i, new ArrayList<>(Arrays.asList(new IntegerPair(-1, 4))));
    assertEquals(i.getGenotype(), original);
  }

  @Test
  public void testSwapOutOfBoundsHigh() {
    Individual i = new Individual(new ArrayList<Integer>(original));
    m.run(i, new ArrayList<>(Arrays.asList(new IntegerPair(1, 10))));
    assertEquals(i.getGenotype(), original);
  }

  @Test
  public void testSwapMultipleValid() {
    Individual i = new Individual(new ArrayList<Integer>(original));
    m.run(i, new ArrayList<>(Arrays.asList(new IntegerPair(1, 10))));
    assertEquals(i.getGenotype(), original);
  }
}
