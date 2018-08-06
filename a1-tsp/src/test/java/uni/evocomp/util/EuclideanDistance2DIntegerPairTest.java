package uni.evocomp.util;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class EuclideanDistance2DIntegerPairTest {

  IntegerPair nullIntegerPair;
  IntegerPair realIntegerPair;
  double delta;

  @Before
  public void setUp() throws Exception {
    realIntegerPair = new IntegerPair(0, 0);
    delta = 0.001;
  }

  @Test(expected = NullPointerException.class)
  public void testNullI() {
    Util.euclideanDistance2D(nullIntegerPair, realIntegerPair);
  }

  @Test(expected = NullPointerException.class)
  public void testNullJ() {
    Util.euclideanDistance2D(realIntegerPair, nullIntegerPair);
  }

  @Test(expected = NullPointerException.class)
  public void testNullBoth() {
    Util.euclideanDistance2D(nullIntegerPair, nullIntegerPair);
  }

  @Test
  public void testSameIntegerPair() {
    assertEquals(1.0, Util.euclideanDistance2D(realIntegerPair, realIntegerPair), delta);
  }

  @Test
  public void testDifferentIntegerPairs() {
    assertEquals(6.0, Util.euclideanDistance2D(new IntegerPair(1, 1), new IntegerPair(5, 5)),
        delta);
  }

  @Test
  public void testDifferentIntegerPairsReverse() {
    assertEquals(6.0, Util.euclideanDistance2D(new IntegerPair(5, 5), new IntegerPair(1, 1)),
        delta);
  }

  @Test
  public void testBothNegative() {
    assertEquals(6.0, Util.euclideanDistance2D(new IntegerPair(-1, -1), new IntegerPair(-5, -5)),
        delta);
  }

  @Test
  public void testOneNegative() {
    assertEquals(6.0, Util.euclideanDistance2D(new IntegerPair(-1, -1), new IntegerPair(3, 3)),
        delta);
  }
}
