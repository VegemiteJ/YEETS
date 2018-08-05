package uni.evocomp.a1;

import static org.junit.Assert.*;
import java.awt.Point;
import org.junit.Before;
import org.junit.Test;
import uni.evocomp.util.Util;

public class EuclideanDistance2DPointTest {

  Point nullPoint;
  Point realPoint;
  double delta;

  @Before
  public void setUp() throws Exception {
    realPoint = new Point(0, 0);
    delta = 0.001;
  }

  @Test(expected = NullPointerException.class)
  public void testNullI() {
    Util.euclideanDistance2D(nullPoint, realPoint);
  }

  @Test(expected = NullPointerException.class)
  public void testNullJ() {
    Util.euclideanDistance2D(realPoint, nullPoint);
  }

  @Test(expected = NullPointerException.class)
  public void testNullBoth() {
    Util.euclideanDistance2D(nullPoint, nullPoint);
  }

  @Test
  public void testSamePoint() {
    assertEquals(1.0, Util.euclideanDistance2D(realPoint, realPoint), delta);
  }

  @Test
  public void testDifferentPoints() {
    assertEquals(6.0, Util.euclideanDistance2D(new Point(1, 1), new Point(5, 5)), delta);
  }

  @Test
  public void testDifferentPointsReverse() {
    assertEquals(6.0, Util.euclideanDistance2D(new Point(5, 5), new Point(1, 1)), delta);
  }

  @Test
  public void testBothNegative() {
    assertEquals(6.0, Util.euclideanDistance2D(new Point(-1, -1), new Point(-5, -5)), delta);
  }

  @Test
  public void testOneNegative() {
    assertEquals(6.0, Util.euclideanDistance2D(new Point(-1, -1), new Point(3, 3)), delta);
  }
}
