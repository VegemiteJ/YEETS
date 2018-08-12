package uni.evocomp.a1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
}
