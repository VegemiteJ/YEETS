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
import org.junit.Test;
import uni.evocomp.util.Matrix;
import uni.evocomp.util.Util;

public class IndividualTest {

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
    new Individual(Arrays.asList(1, 5, 2, 3, 4), 0.0);
  }

  @Test
  public void testInvalidTourBadIndex() {
    Individual i = new Individual(Arrays.asList(1, 5, 2, -1, 4), 0.0);
    Boolean result = didThrowException(i);
    assertTrue(result);
  }

  @Test
  public void testInvalidTourMissingIndex() {
    Individual i = new Individual(Arrays.asList(1, 5, 2, 4), 0.0);
    assertTrue(didThrowException(i));
  }

  @Test
  public void testEqualsOverrideHappy() {
    Individual i = new Individual(Arrays.asList(1, 2, 3, 4), 0.0);
    Individual j = new Individual(Arrays.asList(1, 2, 3, 4), 1.0);

    assertTrue(i.equals(j));
    assertTrue(j.equals(i));
    assertEquals(i, j);
    assertEquals(j, i);
  }

  @SuppressWarnings("unlikely-arg-type")
  @Test
  public void testEqualsOverrideSad() {
    Individual i = new Individual(Arrays.asList(1, 2, 3, 4), 0.0);
    Individual j = new Individual(Arrays.asList(1, 2, 4, 3), 1.0);

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
    List<Integer> array = Arrays.asList(1, 2, 3, 4);
    Individual i = new Individual(array, 0.0);
    Individual.serialise(i, outName);

    // Verify the file has been written
    assertTrue(new File(outName).exists());

    // Read back and check same
    Individual recreate = Individual.deserialise(outName);
    assertEquals(0.0, recreate.getCost(new TSPProblem()), 0);
    assertEquals(array, recreate.getGenotype());

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

    double cost = i.getCost(p);
    assertEquals(false, i.getDirty());
  }
}
