package uni.evocomp.a1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

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
  }

  @SuppressWarnings("unlikely-arg-type")
  @Test(expected = java.lang.AssertionError.class)
  public void testEqualsOverrideSad() {
    Individual i = new Individual(Arrays.asList(1, 2, 3, 4), 0.0);
    Individual j = new Individual(Arrays.asList(1, 2, 4, 3), 1.0);

    assertFalse(i.equals(j));
    assertFalse(j.equals(i));
    assertFalse(i.equals("Hello"));
    assertFalse(i.equals(1));
    assertEquals(i, j); // this should fail
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
}
