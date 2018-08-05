package uni.evocomp.a1;

import static junit.framework.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import uni.evocomp.util.IntegerPair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author Namdrib
 *
 */
public class SwapTest {

  private TSPProblem p;
  private EvaluateEuclid eval2D;
  private List<Integer> original;
  private Mutate m;

  @Before
  public void setUp() throws Exception {
    m = new Swap();

    List<List<Double>> weights = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      weights.add(new ArrayList<>());
      for (int j = 0; j < 10; j++) {
        weights.get(i).add((double) 3*i + j);
      }
    }
    //weights in matrix form
    //0,1,2
    //3,4,5
    //6,7,8
    p = new TSPProblem("", "", "", "", weights);
    original = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    eval2D = new EvaluateEuclid();
  }

  @Test
  public void testSwapEmptyList() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(p, i, new ArrayList<>());
    assertEquals(original, i.getGenotype());
  }

  @Test
  public void testSwapValid() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(1, 4))));
    assertEquals(i.getGenotype(), Arrays.asList(1, 5, 3, 4, 2));
  }

  @Test
  public void testSwapSameIndex() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(2, 2))));
    assertEquals(i.getGenotype(), original);
  }

  @Test
  public void testSwapBadOrder() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(4, 1))));
    assertEquals(i.getGenotype(), Arrays.asList(1, 5, 3, 4, 2));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testSwapOutOfBoundsLow() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(-1, 4))));
    assertEquals(i.getGenotype(), original);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testSwapOutOfBoundsHigh() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(1, 10))));
    assertEquals(i.getGenotype(), original);
  }

  @Test
  public void testSwapMultipleValid() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(1, 10))));
    assertEquals(i.getGenotype(), original);
  }
}
