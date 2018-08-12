package uni.evocomp.a1.mutate;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import uni.evocomp.a1.Individual;
import uni.evocomp.a1.TSPProblem;
import uni.evocomp.a1.evaluate.EvaluateEuclid;
import uni.evocomp.a1.mutate.Insert;
import uni.evocomp.a1.mutate.Mutate;
import uni.evocomp.util.IntegerPair;

/**
 * @author Namdrib
 */
public class InsertTest {

  private TSPProblem p;
  private EvaluateEuclid eval2D;
  private List<Integer> original;
  private Mutate m;
  private Double initialCost;

  @Before
  public void setUp() throws Exception {
    m = new Insert();

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
    p = new TSPProblem("", "", "", "", weights);
    original = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    eval2D = new EvaluateEuclid();
    initialCost = eval2D.evaluate(p, new Individual(original, 0.0));
  }

  @Test
  public void testInsertEmptyList() {
    Individual i = new Individual(new ArrayList<>(original), initialCost);
    m.run(p, i, new ArrayList<>());
    assertEquals(original, i.getGenotype());
  }

  @Test
  public void testInsertValid() {
    Individual i = new Individual(new ArrayList<>(original), initialCost);
    m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(1, 4))));
    assertEquals(Arrays.asList(1, 2, 5, 3, 4), i.getGenotype());
  }

  @Test
  public void testInsertSameIndex() {
    Individual i = new Individual(new ArrayList<>(original), initialCost);
    m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(2, 2))));
    assertEquals(original, i.getGenotype());
  }

  @Test
  public void testInsertBadOrder() {
    Individual i = new Individual(new ArrayList<>(original), initialCost);
    m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(4, 1))));
    assertEquals(Arrays.asList(1, 2, 5, 3, 4), i.getGenotype());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testInsertOutOfBoundsLow() {
    Individual i = new Individual(new ArrayList<>(original), initialCost);
    m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(-1, 4))));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testInsertOutOfBoundsHigh() {
    Individual i = new Individual(new ArrayList<>(original), initialCost);
    m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(1, 10))));
  }

  @Test
  public void testInsertMultipleValid() {
    Individual i = new Individual(new ArrayList<>(original), initialCost);
    m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(0, 3), new IntegerPair(3, 4))));
    assertEquals(Arrays.asList(1, 4, 2, 3, 5), i.getGenotype());
  }

  @Test
  public void testInsertMultipleInverse() {
    Individual i = new Individual(new ArrayList<>(original), initialCost);
    m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(0, 3), new IntegerPair(3, 0))));
    assertEquals(Arrays.asList(1, 3, 4, 2, 5), i.getGenotype());
  }

  @Test(expected = NullPointerException.class)
  public void testInsertNullIndividual() {
    Individual i = null;
    m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(0, 4))));
  }

  @Test(expected = NullPointerException.class)
  public void testInsertNullPairs() {
    Individual i = new Individual(new ArrayList<>(original), initialCost);
    m.run(p, i, null);
  }

  @Test
  public void testValidFuzz() {
    int size = p.getSize();
    for (int i = 0; i < size; i++) {
      for (int j = 0; j < size; j++) {
        Individual individual = new Individual(original, p);
        IntegerPair pair = new IntegerPair(i, j);

        m.run(p, individual, Arrays.asList(pair));

        double cost = eval2D.evaluate(p, individual);
        assertEquals(individual.getCost(), cost, 0);
      }
    }
  }
}
