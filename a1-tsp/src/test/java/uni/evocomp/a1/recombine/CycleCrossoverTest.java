package uni.evocomp.a1.recombine;

import static org.junit.Assert.*;

import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import uni.evocomp.a1.Individual;
import uni.evocomp.a1.TSPProblem;
import uni.evocomp.util.Pair;
import uni.evocomp.util.Util;

public class CycleCrossoverTest {
  private Recombine crossover;
  private TSPProblem problem;

  @Before
  public void setUp() {
    crossover = new CycleCrossover();
    problem = new TSPProblem(Util.createOnesMatrix(10));
  }

  @Test
  public void testExpectedLectureExample() {
    Individual a = new Individual(Arrays.asList(1,2,3,4,5,6,7,8,9), problem);
    Individual b = new Individual(Arrays.asList(9,3,7,8,2,6,5,1,4), problem);

    Individual expA = new Individual(Arrays.asList(1,3,7,4,2,6,5,8,9), problem);
    Individual expB = new Individual(Arrays.asList(9,2,3,8,5,6,7,1,4), problem);

    Pair<Individual, Individual> result = crossover.recombineDouble(a, b);
    assertEquals(expA, result.first);
    assertEquals(expB, result.second);

    Pair<Individual, Individual> result1 = crossover.recombineDouble(b, a);
    assertEquals(expB, result1.first);
    assertEquals(expA, result1.second);
  }

  @Test
  public void testOrderedRecombine() {
    Individual b = new Individual(Arrays.asList(1,2,3,4), problem);
    Individual a = new Individual(Arrays.asList(1,2,3,4), problem);

    Individual expB = new Individual(Arrays.asList(1,2,3,4), problem);
    Individual expA = new Individual(Arrays.asList(1,2,3,4), problem);

    Pair<Individual, Individual> result = crossover.recombineDouble(a, b);
    assertEquals(expA, result.first);
    assertEquals(expB, result.second);
  }


  @Test
  public void testReverseOrderedRecombine() {
    Individual a = new Individual(Arrays.asList(1,2,3,4), problem);
    Individual b = new Individual(Arrays.asList(4,3,2,1), problem);

    Individual expA = new Individual(Arrays.asList(1,3,2,4), problem);
    Individual expB = new Individual(Arrays.asList(4,2,3,1), problem);

    Pair<Individual, Individual> result = crossover.recombineDouble(a, b);
    assertEquals(expA, result.first);
    assertEquals(expB, result.second);

    Pair<Individual, Individual> result1 = crossover.recombineDouble(b, a);
    assertEquals(expB, result1.first);
    assertEquals(expA, result1.second);
  }

  @Test
  public void testInternetExample() {
    Individual a = new Individual(Arrays.asList(9,5,8,4,7,3,6,2,10,1), problem);
    Individual b = new Individual(Arrays.asList(1,2,3,4,5,6,7,8,9,10), problem);

    Individual expA = new Individual(Arrays.asList(9,2,3,4,5,6,7,8,10,1), problem);
    Individual expB = new Individual(Arrays.asList(1,5,8,4,7,3,6,2,9,10), problem);

    Pair<Individual, Individual> result = crossover.recombineDouble(a, b);
    assertEquals(expA, result.first);
    assertEquals(expB, result.second);

    Pair<Individual, Individual> result1 = crossover.recombineDouble(b, a);
    assertEquals(expB, result1.first);
    assertEquals(expA, result1.second);
  }

}
