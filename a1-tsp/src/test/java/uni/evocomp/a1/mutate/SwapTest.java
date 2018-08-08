package uni.evocomp.a1.mutate;

import static junit.framework.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import uni.evocomp.a1.Individual;
import uni.evocomp.a1.TSPProblem;
import uni.evocomp.a1.evaluate.EvaluateEuclid;
import uni.evocomp.a1.mutate.Mutate;
import uni.evocomp.a1.mutate.Swap;
import uni.evocomp.util.IntegerPair;

/** @author Namdrib */
public class SwapTest {

  private TSPProblem p;
  private EvaluateEuclid eval2D;
  private List<Integer> original;
  private Mutate m;
  private Double initialCost;

  @Before
  public void setUp() throws Exception {
    m = new Swap();

    List<List<Double>> weights = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      weights.add(new ArrayList<>());
      for (int j = 0; j < 5; j++) {
        weights.get(i).add((double) 3 * i + j);
      }
    }
    // weights in matrix form
    // 0,1,2
    // 3,4,5
    // 6,7,8
    p = new TSPProblem(weights);
    original = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    eval2D = new EvaluateEuclid();
    initialCost = eval2D.evaluate(p, new Individual(original, 0.0));
  }

  @Test
  public void testSwapEmptyList() {
    Individual i = new Individual(new ArrayList<>(original), initialCost);
    m.run(p, i, new ArrayList<>());
    assertEquals(original, i.getGenotype());
  }

  @Test
  public void testSwapValid() {
    Individual i = new Individual(new ArrayList<>(original), initialCost);
    m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(1, 4))));
    assertEquals(i.getGenotype(), Arrays.asList(1, 5, 3, 4, 2));
  }

  @Test
  public void testSwapSameIndex() {
    Individual i = new Individual(new ArrayList<>(original), initialCost);
    m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(2, 2))));
    assertEquals(i.getGenotype(), original);
  }

  @Test
  public void testSwapBadOrder() {
    Individual i = new Individual(new ArrayList<>(original), initialCost);
    m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(4, 1))));
    assertEquals(i.getGenotype(), Arrays.asList(1, 5, 3, 4, 2));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testSwapOutOfBoundsLow() {
    Individual i = new Individual(new ArrayList<>(original), initialCost);
    m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(-1, 4))));
    assertEquals(i.getGenotype(), original);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testSwapOutOfBoundsHigh() {
    Individual i = new Individual(new ArrayList<>(original), initialCost);
    m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(1, 10))));
    assertEquals(i.getGenotype(), original);
  }

  @Test
  public void testSwapMultipleValid() {
    Individual i = new Individual(new ArrayList<>(original), initialCost);
    m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(1, 4), new IntegerPair(4, 1))));
    assertEquals(i.getGenotype(), original);
  }
}
