package uni.evocomp.a1;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import uni.evocomp.util.Util;

/**
 * Tests the Util class of static helper functions
 * 
 * @author Namdrib
 *
 */
public class InBoundsTest {

  List<Integer> aList;

  @Before
  public void setUp() throws Exception {
    aList = new ArrayList<>(Arrays.asList(0, 0, 0, 0));
  }

  @Test(expected = NullPointerException.class)
  public void testNullContainer() {
    Util.inBounds(null, new Integer(1));
  }

  @Test(expected = NullPointerException.class)
  public void testNullIndex() {
    Util.inBounds(aList, null);
  }

  @Test
  public void testValidInteger() {
    assertTrue(Util.inBounds(aList, new Integer(1)));
  }

  @Test
  public void testValidIntegerZero() {
    assertTrue(Util.inBounds(aList, new Integer(0)));
  }

  @Test
  public void testValidIntegerLast() {
    assertTrue(Util.inBounds(aList, new Integer(aList.size() - 1)));
  }

  @Test
  public void testInvalidIntegerLow() {
    assertFalse(Util.inBounds(aList, new Integer(-1)));
  }

  @Test
  public void testInvalidIntegerHigh() {
    assertFalse(Util.inBounds(aList, new Integer(aList.size())));
  }

  @Test
  public void testValidInt() {
    assertTrue(Util.inBounds(aList, 1));
  }

  @Test
  public void testValidIntZero() {
    assertTrue(Util.inBounds(aList, 0));
  }

  @Test
  public void testValidIntLast() {
    assertTrue(Util.inBounds(aList, aList.size() - 1));
  }

  @Test
  public void testInvalidIntLow() {
    assertFalse(Util.inBounds(aList, -1));
  }

  @Test
  public void testInvalidIntHigh() {
    assertFalse(Util.inBounds(aList, aList.size()));
  }

  @Test
  public void testEmptyList() {
    assertFalse(Util.inBounds(new ArrayList<>(), 0));
  }
}
