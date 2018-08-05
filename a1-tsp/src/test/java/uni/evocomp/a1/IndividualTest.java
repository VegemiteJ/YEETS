package uni.evocomp.a1;

import static org.junit.Assert.*;
import java.util.Arrays;
import org.junit.Test;

public class IndividualTest {

  private boolean didThrowException(Individual i) {
    try {
      i.assertIsValidTour();
    } catch (Exception exc) {
      System.out.println("Caught exception as expected. Message Exception: " + exc.getMessage());
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

}
