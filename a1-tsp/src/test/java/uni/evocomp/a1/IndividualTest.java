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

  @Test
  public void testValidTourWhenEmpty() {
    Individual i = new Individual();
    assertFalse(didThrowException(i));
  }

  @Test
  public void testValidTourWhenNormal() {
    Individual i = new Individual(Arrays.asList(1, 5, 2, 3, 4), 0.0);
    assertFalse(didThrowException(i));
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

  @Test(expected = Test.None.class)
  public void testSerialise() throws IOException, ClassNotFoundException {
    // Create and serialise an object
    List<Integer> array = Arrays.asList(1, 2, 3, 4);
    Individual i = new Individual(array, 0.0);
    Individual.serialise(i);
    
    // Read back and check same
    Individual recreate = Individual.deserialise();
    assertEquals(0.0, recreate.getCost(), 0);
    assertEquals(array, recreate.getGenotype());
    
    // Clean up the file created by serialise()
    File file = new File(Individual.serialLocation);
    assertTrue(file.delete());
  }
}
