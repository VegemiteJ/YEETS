package uni.evocomp.a1;

import static org.junit.Assert.*;

import java.util.Arrays;
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
    Individual i = new Individual(Arrays.asList(1,5,2,3,4), 0.0);
    assertFalse(didThrowException(i));
  }

  @Test
  public void testInvalidTourBadIndex() {
    Individual i = new Individual(Arrays.asList(1,5,2,-1,4), 0.0);
    Boolean result = didThrowException(i);
    assertTrue(result);
  }

  @Test
  public void testInvalidTourMissingIndex() {
    Individual i = new Individual(Arrays.asList(1,5,2,4), 0.0);
    assertTrue(didThrowException(i));
  }

}