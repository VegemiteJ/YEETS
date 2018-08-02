package uni.evocomp.a1;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;

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
    m.run(i, new ArrayList<Point>(Arrays.asList(new Point(1, 4))));
    assertEquals(i.getGenotype(), Arrays.asList(1, 5, 3, 4, 2));
  }

  @Test
  public void testSwapSameIndex() {
    Individual i = new Individual(new ArrayList<Integer>(original));
    m.run(i, new ArrayList<Point>(Arrays.asList(new Point(2, 2))));
    assertEquals(i.getGenotype(), original);
  }

  @Test
  public void testSwapBadOrder() {
    Individual i = new Individual(new ArrayList<Integer>(original));
    m.run(i, new ArrayList<Point>(Arrays.asList(new Point(4, 1))));
    assertEquals(i.getGenotype(), Arrays.asList(1, 5, 3, 4, 2));
  }

  @Test
  public void testSwapOutOfBoundsLow() {
    Individual i = new Individual(new ArrayList<Integer>(original));
    m.run(i, new ArrayList<Point>(Arrays.asList(new Point(-1, 4))));
    assertEquals(i.getGenotype(), original);
  }

  @Test
  public void testSwapOutOfBoundsHigh() {
    Individual i = new Individual(new ArrayList<Integer>(original));
    m.run(i, new ArrayList<Point>(Arrays.asList(new Point(1, 10))));
    assertEquals(i.getGenotype(), original);
  }

  @Test
  public void testSwapMultipleValid() {
    Individual i = new Individual(new ArrayList<Integer>(original));
    m.run(i, new ArrayList<Point>(Arrays.asList(new Point(0, 3), new Point(3, 4))));
    assertEquals(i.getGenotype(), Arrays.asList(4, 2, 3, 5, 1));
  }

  @Test
  public void testSwapMultipleInverse() {
    Individual i = new Individual(new ArrayList<Integer>(original));
    m.run(i, new ArrayList<Point>(Arrays.asList(new Point(0, 3), new Point(3, 0))));
    assertEquals(i.getGenotype(), original);
  }
}