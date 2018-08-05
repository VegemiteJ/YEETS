package uni.evocomp.a1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import uni.evocomp.util.IntegerPair;

/**
 * 
 * @author Namdrib
 *
 */
public class InsertTest {

  private List<Integer> original;
  private Mutate m;

  @Before
  public void setUp() throws Exception {
    original = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    m = new Insert();
  }

  @Test
  public void testInsertEmptyList() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(, i, new ArrayList<>());
    assertEquals(original, i.getGenotype());
  }

  @Test
  public void testInsertValid() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(, i, new ArrayList<>(Arrays.asList(new IntegerPair(1, 4))));
    assertEquals(Arrays.asList(1, 2, 5, 3, 4), i.getGenotype());
  }

  @Test
  public void testInsertSameIndex() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(, i, new ArrayList<>(Arrays.asList(new IntegerPair(2, 2))));
    assertEquals(original, i.getGenotype());
  }

  @Test
  public void testInsertBadOrder() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(, i, new ArrayList<>(Arrays.asList(new IntegerPair(4, 1))));
    assertEquals(Arrays.asList(1, 2, 5, 3, 4), i.getGenotype());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testInsertOutOfBoundsLow() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(, i, new ArrayList<>(Arrays.asList(new IntegerPair(-1, 4))));
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testInsertOutOfBoundsHigh() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(, i, new ArrayList<>(Arrays.asList(new IntegerPair(1, 10))));
  }

  @Test
  public void testInsertMultipleValid() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(, i, new ArrayList<>(Arrays.asList(new IntegerPair(0, 3), new IntegerPair(3, 4))));
    assertEquals(Arrays.asList(1, 4, 2, 3, 5), i.getGenotype());
  }

  @Test
  public void testInsertMultipleInverse() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(, i, new ArrayList<>(Arrays.asList(new IntegerPair(0, 3), new IntegerPair(3, 0))));
    assertEquals(Arrays.asList(1, 3, 4, 2, 5), i.getGenotype());
  }

  @Test(expected = NullPointerException.class)
  public void testInsertNullIndividual() {
    Individual i = null;
    m.run(, i, new ArrayList<>(Arrays.asList(new IntegerPair(0, 4))));
  }

  @Test(expected = NullPointerException.class)
  public void testInsertNullPairs() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(, i, null);
  }
}
