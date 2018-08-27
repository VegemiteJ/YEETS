package uni.evocomp.a1.mutate;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import uni.evocomp.a1.Individual;
import uni.evocomp.a1.TSPProblem;
import uni.evocomp.a1.mutate.Invert;
import uni.evocomp.a1.mutate.Mutate;
import uni.evocomp.util.IntegerPair;

/**
 * @author Namdrib
 */
public class InvertTest {

  private TSPProblem problem;
  private List<Integer> original;
  private Mutate m;

  @Before
  public void setUp() throws Exception {
    m = new Invert();

    List<List<Double>> weights = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      weights.add(new ArrayList<>());
      for (int j = 0; j < 5; j++) {
        weights.get(i).add((double) i + j);
      }
    }
    // weights in matrix form
    // 0,1,2,3,4
    // 1,2,3,4,5
    // 2,3,4,5,6
    // 3,4,5,6,7
    // 4,5,6,7,8
    problem = new TSPProblem(weights);
    original = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
  }

  @Test
  public void testInvertValid() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(problem, i, new IntegerPair(1, 4));
    assertEquals(Arrays.asList(1, 5, 4, 3, 2), i.getGenotype());
  }

  @Test
  public void testInvertAdjacent() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(problem, i, new IntegerPair(0, 1));
    assertEquals(Arrays.asList(2, 1, 3, 4, 5), i.getGenotype());
  }

  @Test
  public void testInvertSameIndex() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(problem, i, new IntegerPair(2, 2));
    assertEquals(original, i.getGenotype());
  }

  @Test
  public void testInvertBadOrder() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(problem, i, new IntegerPair(4, 1));
    assertEquals(Arrays.asList(1, 5, 4, 3, 2), i.getGenotype());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testInvertOutOfBoundsLow() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(problem, i, new IntegerPair(-1, 4));
    assertEquals(original, i.getGenotype());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testInvertOutOfBoundsHigh() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(problem, i, new IntegerPair(1, 10));
    assertEquals(original, i.getGenotype());
  }

  @Test(expected = NullPointerException.class)
  public void testInvertNullIndividual() {
    Individual i = null;
    m.run(problem, i, new IntegerPair(0, 4));
  }

  @Test(expected = NullPointerException.class)
  public void testInvertNullPairs() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(problem, i, null);
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
