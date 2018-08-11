package uni.evocomp.a1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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
}
