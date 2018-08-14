package uni.evocomp.a1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
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

    Individual i = io.readSolution(new BufferedReader(new StringReader(input)));
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
    try {
      localProblem = io.read(new BufferedReader(new FileReader("tests/pcb442.tsp")));
    } catch (IOException e) {
      e.printStackTrace();
      fail();
    }

    Individual i =
        new Individual(
            Arrays.asList(
                1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
                24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44,
                45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65,
                66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86,
                87, 88, 89, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105,
                106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122,
                123, 124, 125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139,
                140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150, 151, 152, 153, 154, 155, 156,
                157, 158, 159, 160, 161, 162, 163, 164, 165, 166, 167, 168, 169, 170, 171, 172, 173,
                174, 175, 176, 177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189, 190,
                191, 192, 193, 194, 195, 196, 197, 198, 199, 200, 201, 202, 203, 204, 205, 206, 207,
                208, 209, 210, 211, 212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223, 224,
                225, 226, 227, 228, 229, 230, 231, 232, 233, 234, 235, 236, 237, 238, 239, 240, 241,
                242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255, 256, 257, 258,
                259, 260, 261, 262, 263, 264, 265, 266, 267, 268, 269, 270, 271, 272, 273, 274, 275,
                276, 277, 278, 279, 280, 281, 282, 283, 284, 285, 286, 287, 288, 289, 290, 291, 292,
                293, 294, 295, 296, 297, 298, 299, 300, 301, 302, 303, 304, 305, 306, 307, 308, 309,
                310, 311, 312, 313, 314, 315, 316, 317, 318, 319, 320, 321, 322, 323, 324, 325, 326,
                327, 328, 329, 330, 331, 332, 333, 334, 335, 336, 337, 338, 339, 340, 341, 342, 343,
                344, 345, 346, 347, 348, 349, 350, 351, 352, 353, 354, 355, 356, 357, 358, 359, 360,
                361, 362, 363, 364, 365, 366, 367, 368, 369, 370, 371, 372, 373, 374, 375, 376, 377,
                378, 379, 380, 381, 382, 383, 384, 385, 386, 387, 388, 389, 390, 391, 392, 393, 394,
                395, 396, 397, 398, 399, 400, 401, 402, 403, 404, 405, 406, 407, 408, 409, 410, 411,
                412, 413, 414, 415, 416, 417, 418, 419, 420, 421, 422, 423, 424, 425, 426, 427, 428,
                429, 430, 431, 432, 433, 434, 435, 436, 437, 438, 439, 440, 441, 442),
            localProblem);
    assertEquals(221440.0, i.getCost(localProblem), 0.0000000001);
  }
}
