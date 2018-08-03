package uni.evocomp.a1;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import uni.evocomp.util.IntegerPair;
import uni.evocomp.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JumpTest extends TestCase {

  private List<Integer> original;
  private Mutate m;

  @Before
  public void setUp() throws Exception {
    original = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));
    m = new Jump();
  }

  @Test
  public void testJumpValid() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(i, new ArrayList<>(Arrays.asList(new IntegerPair(0, 4))));
    // Expect {2,3,4,1,5,6,7,8}
    assertEquals(i.getGenotype(), Arrays.asList(2, 3, 4, 5, 1, 6, 7, 8));
  }

  @Test
  public void testJumpSameIndex() {
    Individual i = new Individual(new ArrayList<>(original));
    m.run(i, new ArrayList<>(Arrays.asList(new IntegerPair(3, 3))));
    // Expect {1,2,3,4,5,6,7,8}
    assertEquals(i.getGenotype(), Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8));
  }

  @Test
  public void testSwapOutOfBoundsLow() {
    Individual i = new Individual(new ArrayList<>(original));
    try {
      m.run(i, new ArrayList<>(Arrays.asList(new IntegerPair(-1, 4))));
    } catch (IndexOutOfBoundsException exc) {
      return;
    }
    fail();
  }

  @Test
  public void testSwapOutOfBoundsHigh() {
    Individual i = new Individual(new ArrayList<>(original));
    try {
      m.run(i, new ArrayList<>(Arrays.asList(new IntegerPair(1, 10))));
    } catch (IndexOutOfBoundsException exc) {
      return;
    }
    fail();
  }
}
