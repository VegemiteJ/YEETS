package uni.evocomp.a1;

import static junit.framework.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import uni.evocomp.util.IntegerPair;

/**
 * 
 * @author Namdrib
 *
 */
public class SwapTest {

  private List<Integer> original;
  private Mutate m;

  @Before
  public void setUp() throws Exception {
    original = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    m = new Swap();
  }

  @Test
  public void testSwapEmptyList() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(i, new ArrayList<>());
    assertEquals(original, i.getGenotype());
  }

  @Test
  public void testSwapValid() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(i, new ArrayList<>(Arrays.asList(new IntegerPair(1, 4))));
    assertEquals(Arrays.asList(1, 5, 3, 4, 2), i.getGenotype());
  }

  @Test
  public void testSwapSameIndex() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(i, new ArrayList<>(Arrays.asList(new IntegerPair(2, 2))));
    assertEquals(original, i.getGenotype());
  }

  @Test
  public void testSwapBadOrder() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(i, new ArrayList<>(Arrays.asList(new IntegerPair(4, 1))));
    assertEquals(Arrays.asList(1, 5, 3, 4, 2), i.getGenotype());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testSwapOutOfBoundsLow() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(i, new ArrayList<>(Arrays.asList(new IntegerPair(-1, 4))));
    assertEquals(original, i.getGenotype());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testSwapOutOfBoundsHigh() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(i, new ArrayList<>(Arrays.asList(new IntegerPair(1, 10))));
    assertEquals(original, i.getGenotype());
  }

  @Test
  public void testSwapMultipleValid() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(i, new ArrayList<>(Arrays.asList(new IntegerPair(1, 4), new IntegerPair(4, 1))));
    assertEquals(original, i.getGenotype());
  }
}
