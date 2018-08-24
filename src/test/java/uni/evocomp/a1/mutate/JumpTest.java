package uni.evocomp.a1.mutate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import uni.evocomp.a1.Individual;
import uni.evocomp.a1.TSPProblem;
import uni.evocomp.a1.mutate.Jump;
import uni.evocomp.a1.mutate.Mutate;
import uni.evocomp.util.IntegerPair;

public class JumpTest extends TestCase {

  private TSPProblem problem;
  private List<Integer> original;
  private Mutate m;

  @Before
  public void setUp() throws Exception {
    m = new Jump();

    List<List<Double>> weights = new ArrayList<>();
    for (int i = 0; i < 8; i++) {
      weights.add(new ArrayList<>());
      for (int j = 0; j < 8; j++) {
        weights.get(i).add((double) 3 * i + j);
      }
    }
    problem = new TSPProblem(weights);
    original = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));
  }

  @Test
  public void testJumpValid1() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(problem, i, new IntegerPair(0, 4));
    // Expect {2,3,4,1,5,6,7,8}
    assertEquals(Arrays.asList(2, 3, 4, 5, 1, 6, 7, 8), i.getGenotype());
  }

  @Test
  public void testJumpValid2() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(problem, i, new IntegerPair(1, 0));
    assertEquals(Arrays.asList(2, 1, 3, 4, 5, 6, 7, 8), i.getGenotype());
  }

  @Test
  public void testJumpValid3() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(problem, i, new IntegerPair(4, 2));
    assertEquals(Arrays.asList(1, 2, 5, 3, 4, 6, 7, 8), i.getGenotype());
  }

  @Test
  public void testJumpValid4() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(problem, i, new IntegerPair(7, 6));
    assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 8, 7), i.getGenotype());
  }

  @Test
  public void testJumpValid5() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(problem, i, new IntegerPair(3, 6));
    assertEquals(Arrays.asList(1, 2, 3, 5, 6, 7, 4, 8), i.getGenotype());
  }

  @Test
  public void testJumpRotate1() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(problem, i, new IntegerPair(7, 0));
    assertEquals(Arrays.asList(8, 1, 2, 3, 4, 5, 6, 7), i.getGenotype());
  }

  public void testJumpRotate2() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(problem, i, new IntegerPair(0, 7));
    assertEquals(Arrays.asList(2, 3, 4, 5, 6, 7, 8, 1), i.getGenotype());
  }

  @Test
  public void testJumpSameIndex() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(problem, i, new IntegerPair(3, 3));
    // Expect {1,2,3,4,5,6,7,8}
    assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8), i.getGenotype());
  }

  @Test
  public void testManyJumps() {
    Individual i = new Individual(new ArrayList<>(original));
    List<IntegerPair> pairs = new ArrayList<>(
        Arrays.asList(
            new IntegerPair(7, 0),
            new IntegerPair(7, 1),
            new IntegerPair(7, 2),
            new IntegerPair(7, 3),
            new IntegerPair(7, 4),
            new IntegerPair(7, 5),
            new IntegerPair(7, 6)));

    for (IntegerPair pair : pairs) {
      m.run(problem, i, pair);
    }

    // Expect {1,2,3,4,5,6,7,8}
    assertEquals(Arrays.asList(8, 7, 6, 5, 4, 3, 2, 1), i.getGenotype());
  }

  @Test
  public void testJumpOutOfBoundsLow() {
    Individual i = new Individual(new ArrayList<>(original));
    try {
      m.run(problem, i, new IntegerPair(-1, 4));
    } catch (IndexOutOfBoundsException exc) {
      return;
    }
    fail();
  }

  @Test
  public void testJumpOutOfBoundsHigh() {
    Individual i = new Individual(new ArrayList<>(original));
    try {
      m.run(problem, i, new IntegerPair(1, 10));
    } catch (IndexOutOfBoundsException exc) {
      return;
    }
    fail();
  }

  @Test(expected = Test.None.class)
  public void testValidFuzz() {
    int size = problem.getSize();
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        Individual individual = new Individual(original, problem);
        IntegerPair pair = new IntegerPair(i, j);

        m.run(problem, individual, pair);
      }
    }
  }
}
