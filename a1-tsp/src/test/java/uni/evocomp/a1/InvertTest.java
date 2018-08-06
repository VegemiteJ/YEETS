package uni.evocomp.a1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import uni.evocomp.util.IntegerPair;

/**
 * 
 * @author Namdrib
 *
 */
public class InvertTest {

  private TSPProblem p;
  private EvaluateEuclid eval2D;
  private List<Integer> original;
  private Mutate m;
  private Double initialCost;

  @Before
  public void setUp() throws Exception {
    m = new Invert();

    List<List<Double>> weights = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      weights.add(new ArrayList<>());
      for (int j = 0; j < 5; j++) {
        weights.get(i).add((double) i + j);
        System.out.print(weights.get(i).get(j) + " ");
      }
      System.out.println();
    }
    //weights in matrix form
    //0,1,2,3,4
    //1,2,3,4,5
    //2,3,4,5,6
    //3,4,5,6,7
    //4,5,6,7,8
    p = new TSPProblem("", "", "", "", weights);
    original = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    eval2D = new EvaluateEuclid();
    initialCost = eval2D.evaluate(p, new Individual(original, 0.0));
  }

  @Test
  public void testInvertEmptyList() {
    Individual i = new Individual(new ArrayList<>(original), initialCost);
    m.run(p, i, new ArrayList<>());
    assertEquals(original, i.getGenotype());
    double cost = eval2D.evaluate(p, i);
    assertEquals(cost, i.getCost(), 0.00001);
  }

  @Test
  public void testInvertValid() {
    Individual i = new Individual(new ArrayList<>(original), initialCost);
    m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(1, 4))));
    assertEquals(Arrays.asList(1, 5, 4, 3, 2), i.getGenotype());
    double cost = eval2D.evaluate(p, i);
    assertEquals(cost, i.getCost(), 0.00001);
  }

  @Test
  public void testInvertAdjacent() {
    Individual i = new Individual(new ArrayList<>(original), initialCost);
    m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(0, 1))));
    assertEquals(Arrays.asList(2, 1, 3, 4, 5), i.getGenotype());
    double cost = eval2D.evaluate(p, i);
    assertEquals(cost, i.getCost(), 0.00001);
  }

  @Test
  public void testInvertSameIndex() {
    Individual i = new Individual(new ArrayList<>(original), initialCost);
    m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(2, 2))));
    assertEquals(original, i.getGenotype());
    double cost = eval2D.evaluate(p, i);
    assertEquals(cost, i.getCost(), 0.00001);
  }

  @Test
  public void testInvertBadOrder() {
    Individual i = new Individual(new ArrayList<>(original), initialCost);
    m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(4, 1))));
    assertEquals(Arrays.asList(1, 5, 4, 3, 2), i.getGenotype());
    double cost = eval2D.evaluate(p, i);
    assertEquals(cost, i.getCost(), 0.00001);
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testInvertOutOfBoundsLow() {
    Individual i = new Individual(new ArrayList<>(original), initialCost);
    m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(-1, 4))));
    assertEquals(original, i.getGenotype());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testInvertOutOfBoundsHigh() {
    Individual i = new Individual(new ArrayList<>(original), initialCost);
    m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(1, 10))));
    assertEquals(original, i.getGenotype());
  }

  @Test
  public void testInvertMultipleValid() {
    Individual i = new Individual(new ArrayList<>(original), initialCost);
    m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(0, 3), new IntegerPair(3, 4))));
    assertEquals(Arrays.asList(4, 3, 2, 5, 1), i.getGenotype());
    double cost = eval2D.evaluate(p, i);
    assertEquals(cost, i.getCost(), 0.00001);
  }

  @Test
  public void testInvertMultipleInverse() {
    Individual i = new Individual(new ArrayList<>(original), initialCost);
    m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(0, 3), new IntegerPair(3, 0))));
    assertEquals(original, i.getGenotype());
    double cost = eval2D.evaluate(p, i);
    assertEquals(cost, i.getCost(), 0.00001);
  }

  @Test(expected = NullPointerException.class)
  public void testInvertNullIndividual() {
    Individual i = null;
    m.run(p, i, new ArrayList<>(Arrays.asList(new IntegerPair(0, 4))));
  }

  @Test(expected = NullPointerException.class)
  public void testInvertNullPairs() {
    Individual i = new Individual(new ArrayList<>(original), initialCost);
    m.run(p, i, null);
  }
}
