package uni.evocomp.a1.evaluate;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uni.evocomp.a1.Individual;
import uni.evocomp.a1.TSPIO;
import uni.evocomp.a1.TSPProblem;
import uni.evocomp.a1.evaluate.Evaluate;
import uni.evocomp.a1.evaluate.EvaluateEuclid;

public class EvaluateEuclidTest {

  Evaluate e;
  TSPProblem testProblem;

  @Before
  public void setUp() throws Exception {
    List<List<Double>> weights = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      weights.add(new ArrayList<>());
      for (int j = 0; j < 3; j++) {
        weights.get(i).add((double) 3 * i + j);
      }
    }
    // weights in matrix form
    // 0,1,2
    // 3,4,5
    // 6,7,8
    testProblem = new TSPProblem("", "", "", "", weights);

    e = new EvaluateEuclid();
  }

  @Test
  public void testSimpleTour1() {
    // 1,2,3 -> 1+5+6 = 12
    List<Integer> tour = new ArrayList<>(Arrays.asList(1, 2, 3));
    Individual i = new Individual(tour, 0.0);
    double cost = e.evaluate(testProblem, i);
    Assert.assertEquals(12.0, cost, 0);
  }

  @Test
  public void testSimpleTour2() {
    // 1,2,3,2,1 -> 1+5+7+3+0 = 16
    List<Integer> tour = new ArrayList<>(Arrays.asList(1, 2, 3, 2, 1));
    Individual i = new Individual(tour, 0.0);
    double cost = e.evaluate(testProblem, i);
    Assert.assertEquals(16.0, cost, 0);
  }

  @Test
  public void testSimpleTour3() {
    // 3,1,2,1,2,3 -> 6+1+3+1+5+8=24
    List<Integer> tour = new ArrayList<>(Arrays.asList(3, 1, 2, 1, 2, 3));
    Individual i = new Individual(tour, 0.0);
    double cost = e.evaluate(testProblem, i);
    Assert.assertEquals(24.0, cost, 0);
  }

  @Test
  public void testWrapCost() {
    // same loop, just starting at different position
    List<Integer> tour = new ArrayList<>(Arrays.asList(1, 2, 3));
    Individual i = new Individual(tour);
    double cost1 = e.evaluate(testProblem, i);

    List<Integer> tour2 = new ArrayList<>(Arrays.asList(2, 3, 1));
    Individual j = new Individual(tour2);
    double cost2 = e.evaluate(testProblem, i);

    Assert.assertEquals(cost1, cost2, 0);
  }

  // TODO: Failure tests
}
