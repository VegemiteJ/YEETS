package uni.evocomp.a1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import junit.framework.TestCase;
import uni.evocomp.util.Pair;

public class SwapTest extends TestCase {

  private List<Integer> original;
  private Mutate m;

  protected void setUp() throws Exception {
    super.setUp();
    original = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    m = new Swap();
  }

  @Test
  public void testSwapValid() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(i, new ArrayList<>(Arrays.asList(new Pair<>(1, 4))));
    assertEquals(i.getGenotype(), Arrays.asList(1, 5, 3, 4, 2));
  }

  @Test
  public void testSwapSameIndex() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(i, new ArrayList<>(Arrays.asList(new Pair<>(2, 2))));
    assertEquals(i.getGenotype(), original);
  }

  @Test
  public void testSwapBadOrder() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(i, new ArrayList<>(Arrays.asList(new Pair<>(4, 1))));
    assertEquals(i.getGenotype(), Arrays.asList(1, 5, 3, 4, 2));
  }

  @Test
  public void testSwapOutOfBoundsLow() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(i, new ArrayList<>(Arrays.asList(new Pair<>(-1, 4))));
    assertEquals(i.getGenotype(), original);
  }

  @Test
  public void testSwapOutOfBoundsHigh() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(i, new ArrayList<>(Arrays.asList(new Pair<>(1, 10))));
    assertEquals(i.getGenotype(), original);
  }

  @Test
  public void testSwapMultipleValid() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(i, new ArrayList<>(Arrays.asList(new Pair<>(0, 3), new Pair<>(3, 4))));
    assertEquals(i.getGenotype(), Arrays.asList(4, 2, 3, 5, 1));
  }

  @Test
  public void testSwapMultipleInverse() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(i, new ArrayList<>(Arrays.asList(new Pair<>(0, 3), new Pair<>(3, 0))));
    assertEquals(i.getGenotype(), original);
  }
}
