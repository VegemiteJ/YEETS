package uni.evocomp.a1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import uni.evocomp.util.Matrix;
import uni.evocomp.util.Util;

public class IndividualTest {

  TSPProblem problem;

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
    problem = new TSPProblem(weights);

  }

  private boolean didThrowException(Individual i) {
    try {
      i.assertIsValidTour();
    } catch (Exception exc) {
      System.out.println("Caught exception as expected. Message Exception: ");
      System.out.println(exc.getMessage());
      return true;
    }
    System.out.println("No throw.");
    return false;
  }

  @Test(expected = Test.None.class)
  public void testValidTourWhenEmpty() {
    new Individual();
  }

  @Test(expected = Test.None.class)
  public void testValidTourWhenNormal() {
    new Individual(Arrays.asList(1, 5, 2, 3, 4));
  }

  @Test
  public void testInvalidTourBadIndex() {
    Individual i = new Individual(Arrays.asList(1, 5, 2, -1, 4));
    Boolean result = didThrowException(i);
    assertTrue(result);
  }

  @Test
  public void testInvalidTourMissingIndex() {
    Individual i = new Individual(Arrays.asList(1, 5, 2, 4));
    assertTrue(didThrowException(i));
  }

  @Test
  public void testEqualsOverrideHappy() {
    Individual i = new Individual(Arrays.asList(1, 2, 3, 4));
    Individual j = new Individual(Arrays.asList(1, 2, 3, 4));

    assertTrue(i.equals(j));
    assertTrue(j.equals(i));
    assertEquals(i, j);
    assertEquals(j, i);
  }

  @SuppressWarnings("unlikely-arg-type")
  @Test
  public void testEqualsOverrideSad() {
    Individual i = new Individual(Arrays.asList(1, 2, 3, 4));
    Individual j = new Individual(Arrays.asList(1, 2, 4, 3));

    assertFalse(i.equals(j));
    assertFalse(j.equals(i));
    assertFalse(i.equals("Hello"));
    assertFalse(i.equals(1));
  }

  @Test
  public void testComareTo() {
    Individual i = new Individual(Arrays.asList(1, 2, 3, 4), 0.0);
    Individual j = new Individual(Arrays.asList(1, 2, 3, 4), 1.0);

    assertTrue(i.compareTo(j) < 0);
    assertTrue(j.compareTo(i) > 0);

    j.setCost(0.0);
    assertEquals(0.0, i.compareTo(j), 0.0);
  }

  /**
   * compareTo only checks the Individual's cost, but asserEquals uses equals(Individual). Therefore
   * need to have different genotypes for the assertion to match the object
   */
  @Test
  public void testCompareToCollection() {
    Individual i0 = new Individual(Arrays.asList(1, 2, 3, 4), 2.0);
    Individual i1 = new Individual(Arrays.asList(1, 2, 4, 3), 1.0);
    Individual i2 = new Individual(Arrays.asList(1, 3, 2, 4), 3.0);
    Individual i3 = new Individual(Arrays.asList(1, 3, 4, 2), 1.4);
    List<Individual> list = new ArrayList<>(Arrays.asList(i0, i1, i2, i3));
    assertEquals(i1, Collections.min(list));
    assertEquals(i2, Collections.max(list));
    assertTrue(i1 == Collections.min(list)); // should be the same object
    assertTrue(i2 == Collections.max(list)); // should be the same object
  }

  @Test(expected = Test.None.class)
  public void testSerialiseDefault() throws IOException, ClassNotFoundException {
    // Create and serialise an object
    List<Integer> array = Arrays.asList(1, 2, 3, 4);
    Individual i = new Individual(array, 0.0);
    Individual.serialise(i);

    // Verify the file has been written
    assertTrue(new File(Individual.serialLocation).exists());

    // Read back and check same
    Individual recreate = Individual.deserialise();
    assertEquals(0.0, recreate.getCost(new TSPProblem()), 0);
    assertEquals(array, recreate.getGenotype());

    // Clean up the file created by serialise()
    new File(Individual.serialLocation).delete();
  }

  @Test(expected = Test.None.class)
  public void testSerialiseCustom() throws IOException, ClassNotFoundException {
    // Create and serialise an object
    String outName = "testSerialiseCustom.ser";
    Individual i = new Individual(Arrays.asList(1, 2, 3), problem);
    Individual.serialise(i, outName);

    // Verify the file has been written
    assertTrue(new File(outName).exists());

    // Read back and check same
    Individual recreate = Individual.deserialise(outName);
    assertEquals(12.0, recreate.getCost(problem), 0.0);

    // Clean up the file created by serialise()
    new File(outName).delete();
  }

  @Test
  public void testDirtyNotSetOnDefaultConstructor() {
    WhiteBoxIndividual i = new WhiteBoxIndividual();
    assertEquals(false, i.getDirty());
  }

  @Test
  public void testDirtySetOnConstructor1() {
    WhiteBoxIndividual i = new WhiteBoxIndividual(5);
    assertEquals(true, i.getDirty());
  }

  @Test
  public void testDirtyOnCopyConstructor() {
    WhiteBoxIndividual i = new WhiteBoxIndividual(5);
    assertEquals(true, new WhiteBoxIndividual(i).getDirty());

    WhiteBoxIndividual j = new WhiteBoxIndividual();
    assertEquals(false, new WhiteBoxIndividual(j).getDirty());
  }

  @Test
  public void testTSPProblemConstructorDirtyNotSet() {
    /*
    1. 1.
    1. 1.
     */
    Matrix DummyWeights = Util.createOnesMatrix(2);
    TSPProblem p = new TSPProblem(DummyWeights);

    WhiteBoxIndividual i = new WhiteBoxIndividual(p);
    assertEquals(false, i.getDirty());
  }

  @Test
  public void testGenotypeWithCostDirtyNotSet() {
    List<Integer> genotype = new ArrayList<>(Arrays.asList(1, 2, 3));
    WhiteBoxIndividual i = new WhiteBoxIndividual(genotype, 0.0);
    assertEquals(false, i.getDirty());
  }

  @Test
  public void testGenotypeNoCostDirtySet() {
    List<Integer> genotype = new ArrayList<>(Arrays.asList(1, 2, 3));
    WhiteBoxIndividual i = new WhiteBoxIndividual(genotype);
    assertEquals(true, i.getDirty());
  }

  @Test
  public void testGenotypeWithTSPProblemConstructorDirtyNotSet() {
    /*
     * 1. 1.
     * 1. 1.
     */
    Matrix DummyWeights = Util.createOnesMatrix(2);
    TSPProblem p = new TSPProblem(DummyWeights);

    WhiteBoxIndividual i = new WhiteBoxIndividual(new ArrayList<>(Arrays.asList(2, 1)), p);
    assertEquals(false, i.getDirty());
  }

  @Test
  public void testDirtySetOnDeferredCreationThanUnsetOnGetCost() {
    /*
     * 1. 1.
     * 1. 1.
     */
    Matrix DummyWeights = Util.createOnesMatrix(2);
    TSPProblem p = new TSPProblem(DummyWeights);

    WhiteBoxIndividual i = new WhiteBoxIndividual(2);
    assertEquals(true, i.getDirty());

    i.getCost(p);
    assertEquals(false, i.getDirty());
  }

  @Test
  public void testSimpleTour1() {
    // 1,2,3 -> 1+5+6 = 12
    List<Integer> tour = new ArrayList<>(Arrays.asList(1, 2, 3));
    Individual i = new Individual(tour);
    assertEquals(12.0, i.getCost(problem), 0);
  }

  @Test
  public void testSimpleTour2() {
    // 1,2,3,2,1 -> 1+5+7+3+0 = 16
    List<Integer> tour = new ArrayList<>(Arrays.asList(1, 2, 3, 2, 1));
    Individual i = new Individual(tour);
    assertEquals(16.0, i.getCost(problem), 0);
  }

  @Test
  public void testSimpleTour3() {
    // 3,1,2,1,2,3 -> 6+1+3+1+5+8=24
    List<Integer> tour = new ArrayList<>(Arrays.asList(3, 1, 2, 1, 2, 3));
    Individual i = new Individual(tour);
    assertEquals(24.0, i.getCost(problem), 0);
  }

  @Test
  public void testWrapCost() {
    // same loop, just starting at different position
    List<Integer> tour = new ArrayList<>(Arrays.asList(1, 2, 3));
    Individual i = new Individual(tour);
    assertEquals(12.0, i.getCost(problem), 0);

    List<Integer> tour2 = new ArrayList<>(Arrays.asList(2, 3, 1));
    Individual j = new Individual(tour2);
    assertEquals(12.0, j.getCost(problem), 0);

    assertEquals(i.getCost(problem), j.getCost(problem), 0);
  }

  /**
   * Test that the copy ctor actually performs a deep copy of everything. i.e. changing the copy
   * should have no result on the original
   */
  @Test
  public void testCopyCtorDeepCopy() {
    // Create an Individual and a copy using the copy ctor
    // They should be the same
    Individual src = new Individual(Arrays.asList(2, 1, 3), problem);
    Individual copy = new Individual(src);
    assertEquals(src.getGenotype(), copy.getGenotype());
    assertEquals(src.getCost(problem), copy.getCost(problem));
    assertFalse(src.getGenotype() == copy.getGenotype());
    assertTrue(src.equals(copy));

    // Changing the copy shouldn't affect the src
    Collections.sort(copy.getGenotype());
    assertEquals(Arrays.asList(2, 1, 3), src.getGenotype());
    assertEquals(Arrays.asList(1, 2, 3), copy.getGenotype());
    assertFalse(src.getGenotype().equals(copy.getGenotype()));
    assertFalse(src.equals(copy));
  }
}
