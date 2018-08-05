package uni.evocomp.a1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import uni.evocomp.util.IntegerPair;

public class JumpTest extends TestCase {

  private TSPProblem p;
  private EvaluateEuclid eval2D;
  private List<Integer> original;
  private Mutate m;
  private Double initialCost;

  @Before
  public void setUp() throws Exception {
    m = new Jump();

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
    initialCost = eval2D.evaluate(p, new Individual(original, 0.0));
  }

  @Test
  public void testJumpValid1() {
    Individual i = new Individual(new ArrayList<>(original), initialCost);
    m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(0, 4))));
    // Expect {2,3,4,1,5,6,7,8}
    assertEquals(Arrays.asList(2, 3, 4, 5, 1, 6, 7, 8), i.getGenotype());
  }

  @Test
  public void testJumpValid2() {
    Individual i = new Individual(new ArrayList<>(original), initialCost);
    m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(1, 0))));
    assertEquals(Arrays.asList(2, 1, 3, 4, 5, 6, 7, 8), i.getGenotype());
  }

  @Test
  public void testJumpValid3() {
    Individual i = new Individual(new ArrayList<>(original), initialCost);
    m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(4, 2))));
    assertEquals(Arrays.asList(1, 2, 5, 3, 4, 6, 7, 8), i.getGenotype());
  }

  @Test
  public void testJumpSameIndex() {
    Individual i = new Individual(new ArrayList<>(original), initialCost);
    m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(3, 3))));
    // Expect {1,2,3,4,5,6,7,8}
    assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), i.getGenotype());
  }

  @Test
  public void testManyJumps() {
    Individual i = new Individual(new ArrayList<>(original), initialCost);
    m.run(p,
        i, new ArrayList<>(
            Arrays.asList(
                new IntegerPair(7, 0),
                new IntegerPair(7, 1),
                new IntegerPair(7, 2),
                new IntegerPair(7, 3),
                new IntegerPair(7, 4),
                new IntegerPair(7, 5),
                new IntegerPair(7, 6))));
    // Expect {1,2,3,4,5,6,7,8}
    assertEquals(Arrays.asList(8, 7, 6, 5, 4, 3, 2, 1), i.getGenotype());
  }

  @Test
  public void testJumpOutOfBoundsLow() {
    Individual i = new Individual(new ArrayList<>(original), initialCost);
    try {
      m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(-1, 4))));
    } catch (IndexOutOfBoundsException exc) {
      return;
    }
    fail();
  }

  @Test
  public void testJumpOutOfBoundsHigh() {
    Individual i = new Individual(new ArrayList<>(original), initialCost);
    try {
      m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(1, 10))));
    } catch (IndexOutOfBoundsException exc) {
      return;
    }
    fail();
  }
}
