package uni.evocomp.a1;

import static org.junit.Assert.*;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import uni.evocomp.util.Util;

/**
 * 
 * @author Namdrib
 *
 */
public class TSPIOTest {

  private TSPProblem problem;
  private TSPIO io;
  private String input;

  @Before
  public void setUp() throws Exception {
    io = new TSPIO();
    input = new String();
  }

  @Test
  public void testReadNormalShort() {
    input = Util.createMap("normal3", "comment", "TSP", 3, "EUC_2D",
        Arrays.asList("1 0 0", "2 1 0", "3 0 1"));

    try {
      problem = io.read(new BufferedReader(new StringReader(input)));
    } catch (Exception ex) {
      fail("io.read() exception");
    }
    assertEquals("normal3", problem.getName());
    assertEquals("comment", problem.getComment());
    assertEquals("TSP", problem.getType());
    assertEquals(3, problem.getSize());
    assertEquals("EUC_2D", problem.getEdgeWeightType());
    assertEquals(1.0, problem.getWeights().get(0).get(0), 0.001);
  }

  @Test
  public void testReadNoEOF() {
    input = Util.createMap("normal3", "comment", "TSP", 3, "EUC_2D",
        Arrays.asList("1 0 0", "2 1 0", "3 0 1"), false);

    try {
      problem = io.read(new BufferedReader(new StringReader(input)));
    } catch (Exception ex) {
      fail("io.read() exception");
    }
  }

  @Test
  public void testReadDelim() {
    input = " NAME:hello\n" + "COMMENT  :hello\n" + "TYPE\t: \tTSP\n" + "DIMENSION\t :  2\n"
        + "  EDGE_WEIGHT_TYPE :EUC_2D\n" + "\tNODE_COORD_SECTION\t\n" + "\t1  1 \t0  \n" + "2 0 1\n";

    try {
      problem = io.read(new BufferedReader(new StringReader(input)));
    } catch (Exception ex) {
      ex.printStackTrace(System.out);
      fail("io.read() exception");
    }
  }

  @Test
  public void testReadNormalNegative() {
    input = Util.createMap("normal3", "comment", "TSP", 3, "EUC_2D",
        Arrays.asList("1 -1 -1", "2 -1 -1", "3 -1 -1"));

    try {
      problem = io.read(new BufferedReader(new StringReader(input)));
    } catch (Exception ex) {
      fail("io.read() exception");
    }
    assertEquals(1.0, problem.getWeights().get(0).get(0), 0.001);
  }

  @Test
  public void testReadNormalShortDouble() {
    input = Util.createMap("normal3", "comment", "TSP", 3, "EUC_2D",
        Arrays.asList("1 0.5 0.5", "2 1.5 0.5", "3 0.5 1.5"));

    try {
      problem = io.read(new BufferedReader(new StringReader(input)));
    } catch (Exception ex) {
      fail("io.read() exception");
    }
    assertEquals(1.0, problem.getWeights().get(0).get(0), 0.001);
  }

  @Test
  public void testReadNormalShortScientific() {
    input = Util.createMap("normal3", "comment", "TSP", 3, "EUC_2D",
        Arrays.asList("1 2e+02 4.0e+02", "2 1e+2 1e0", "3 1e-2 10e-02"));

    try {
      problem = io.read(new BufferedReader(new StringReader(input)));
    } catch (Exception ex) {
      fail("io.read() exception");
    }
  }
}
