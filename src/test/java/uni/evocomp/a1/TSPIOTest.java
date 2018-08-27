package uni.evocomp.a1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Before;
import org.junit.Test;
import uni.evocomp.util.Util;

/** @author Namdrib */
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
  public void testReadNormalShort()
      throws NumberFormatException, NullPointerException, IOException {
    input =
        Util.createMap(
            "normal3", "comment", "TSP", 3, "EUC_2D", Arrays.asList("1 0 0", "2 1 0", "3 0 1"));

    problem = io.read(new BufferedReader(new StringReader(input)));

    assertEquals("normal3", problem.getName());
    assertEquals("comment", problem.getComment());
    assertEquals("TSP", problem.getType());
    assertEquals(3, problem.getSize());
    assertEquals("EUC_2D", problem.getEdgeWeightType());
    assertEquals(0.0, problem.getWeights().get(0, 0), 0.001);
  }

  @Test
  public void testReadNoEOF() throws NumberFormatException, NullPointerException, IOException {
    input =
        Util.createMap(
            "no EOF",
            "comment",
            "TSP",
            3,
            "EUC_2D",
            Arrays.asList("1 0 0", "2 1 0", "3 0 1"),
            false);

    problem = io.read(new BufferedReader(new StringReader(input)));
  }

  @Test
  public void testReadDelim() throws NumberFormatException, NullPointerException, IOException {
    input =
        " NAME:delims\n"
            + "COMMENT  :hello\n"
            + "TYPE\t: \tTSP\n"
            + "DIMENSION\t :  2\n"
            + "  EDGE_WEIGHT_TYPE :EUC_2D\n"
            + "\tNODE_COORD_SECTION\t\n"
            + "\t1  1 \t0  \n"
            + "2 0 1\n";

    problem = io.read(new BufferedReader(new StringReader(input)));
  }

  @Test(expected = NullPointerException.class)
  public void testReadTooFewCities()
      throws NumberFormatException, NullPointerException, IOException {
    input =
        Util.createMap(
            "few cities", "comment", "TSP", 3, "EUC_2D", Arrays.asList("1 0 0", "2 1 0"));

    problem = io.read(new BufferedReader(new StringReader(input)));
  }

  public void testReadTooManyCities()
      throws NumberFormatException, NullPointerException, IOException {
    input =
        Util.createMap(
            "few cities",
            "comment",
            "TSP",
            3,
            "EUC_2D",
            Arrays.asList("1 0 0", "2 1 0", "3 3 3", "4 4 4"));

    problem = io.read(new BufferedReader(new StringReader(input)));
    assertEquals(3, problem.getWeights().size());
  }

  @Test
  public void testReadNormalNegative()
      throws NumberFormatException, NullPointerException, IOException {
    input =
        Util.createMap(
            "negative3",
            "comment",
            "TSP",
            3,
            "EUC_2D",
            Arrays.asList("1 -1 -1", "2 -1 -1", "3 -1 -1"));

    problem = io.read(new BufferedReader(new StringReader(input)));
    assertEquals(0.0, problem.getWeights().get(0, 0), 0.001);
  }

  @Test
  public void testReadNegOneIgnored()
      throws NumberFormatException, NullPointerException, IOException {
    input =
        Util.createSolutionMap(
            "negone_end",
            "comment",
            "TOUR",
            2392,
            Arrays.asList("1","2","3","-1"), true);

    Individual i = io.readSolution(new BufferedReader(new StringReader(input)));
    List<Integer> tour = i.getGenotype();
    assertEquals(3, tour.get(tour.size()-1).intValue());
  }

  @Test(expected = IOException.class)
  public void testReadInvalidSolutionNegative()
      throws NumberFormatException, NullPointerException, IOException {
    input =
        Util.createSolutionMap(
            "negone_end",
            "comment",
            "TOUR",
            2392,
            Arrays.asList("1","-5","-43","-1"), true);

    io.readSolution(new BufferedReader(new StringReader(input)));
  }

  @Test
  public void testReadNormalShortDouble()
      throws NumberFormatException, NullPointerException, IOException {
    input =
        Util.createMap(
            "double",
            "comment",
            "TSP",
            3,
            "EUC_2D",
            Arrays.asList("1 0.5 0.5", "2 1.5 0.5", "3 0.5 1.5"));

    problem = io.read(new BufferedReader(new StringReader(input)));
    assertEquals(0.0, problem.getWeights().get(0, 0), 0.001);
  }

  @Test
  public void testReadNormalShortScientific()
      throws NumberFormatException, NullPointerException, IOException {
    input =
        Util.createMap(
            "scientific",
            "comment",
            "TSP",
            3,
            "EUC_2D",
            Arrays.asList("1 2e+02 4.0e+02", "2 1e+2 1e0", "3 1e-2 10e-02"));

    problem = io.read(new BufferedReader(new StringReader(input)));
  }

  @Test
  public void testVerificationPCB442() {
    TSPProblem localProblem = null;
    try (BufferedReader br = new BufferedReader(new FileReader(Global.testPath + "/pcb442.tsp"))) {
      localProblem = io.read(br);
    } catch (IOException e) {
      e.printStackTrace();
      fail();
    }

    @SuppressWarnings("null")
    Individual i = new Individual(
        IntStream.rangeClosed(1, localProblem.getSize()).boxed().collect(Collectors.toList()),
        localProblem);
    assertEquals(221440.0, i.getCost(localProblem), 0.0);
  }
}
