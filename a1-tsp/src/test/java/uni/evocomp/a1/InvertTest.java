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
public class InvertTest {

  private List<Integer> original;
  private Mutate m;

  @Before
  public void setUp() throws Exception {
    original = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
    m = new Invert();
  }

  @Test
  public void testInvertEmptyList() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(, i, new ArrayList<>());
    assertEquals(original, i.getGenotype());
  }

  @Test
  public void testInvertValid() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(, i, new ArrayList<>(Arrays.asList(new IntegerPair(1, 4))));
    assertEquals(Arrays.asList(1, 5, 4, 3, 2), i.getGenotype());
  }

  @Test
  public void testInvertAdjacent() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(, i, new ArrayList<>(Arrays.asList(new IntegerPair(0, 1))));
    assertEquals(Arrays.asList(2, 1, 3, 4, 5), i.getGenotype());
  }

  @Test
  public void testInvertSameIndex() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(, i, new ArrayList<>(Arrays.asList(new IntegerPair(2, 2))));
    assertEquals(original, i.getGenotype());
  }

  @Test
  public void testInvertBadOrder() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(, i, new ArrayList<>(Arrays.asList(new IntegerPair(4, 1))));
    assertEquals(Arrays.asList(1, 5, 4, 3, 2), i.getGenotype());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testInvertOutOfBoundsLow() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(, i, new ArrayList<>(Arrays.asList(new IntegerPair(-1, 4))));
    assertEquals(original, i.getGenotype());
  }

  @Test(expected = IndexOutOfBoundsException.class)
  public void testInvertOutOfBoundsHigh() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(, i, new ArrayList<>(Arrays.asList(new IntegerPair(1, 10))));
    assertEquals(original, i.getGenotype());
  }

  @Test
  public void testInvertMultipleValid() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(, i, new ArrayList<>(Arrays.asList(new IntegerPair(0, 3), new IntegerPair(3, 4))));
    assertEquals(Arrays.asList(4, 3, 2, 5, 1), i.getGenotype());
  }

  @Test
  public void testInvertMultipleInverse() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(, i, new ArrayList<>(Arrays.asList(new IntegerPair(0, 3), new IntegerPair(3, 0))));
    assertEquals(original, i.getGenotype());
  }

  @Test(expected = NullPointerException.class)
  public void testInvertNullIndividual() {
    Individual i = null;
    m.run(, i, new ArrayList<>(Arrays.asList(new IntegerPair(0, 4))));
  }

  @Test(expected = NullPointerException.class)
  public void testInvertNullPairs() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(, i, null);
  }
}
