package uni.evocomp.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Util {
  public static <T> Boolean inBounds(List<T> container, Integer idx1) {
    return idx1 >= 0 && idx1 < container.size();
  }

  /**
   * Calculate Euclidean distance between two Points, as described by the documentation
   * (elib.zib.de/pub/mp-testdata/tsp/tsplib/doc.ps) section 2.1
   *
   * <pre>
   * Let x[i], y[i] ... be the coordinates of node i.
   * In the 2-dimensional case the distance between two points i and j is computed as follows:
   * xd = x[i] - x[j];
   * yd = y[i] - y[j];
   * dij = nint( sqrt( xd*xd + yd*yd) + 0.5 );
   * </pre>
   *
   * @param i
   * @param j
   * @return the 2D Euclidean distance between a and b
   */
  public static double euclideanDistance2D(IntegerPair i, IntegerPair j) {
    double xd = i.first - j.first;
    double yd = i.second - j.second;
    return Math.floor(Math.sqrt(xd * xd + yd * yd) + 0.5);
  }

  public static double euclideanDistance2D(DoublePair i, DoublePair j) {
    double xd = i.first - j.first;
    double yd = i.second - j.second;
    return Math.floor(Math.sqrt(xd * xd + yd * yd) + 0.5);
  }

  /**
   * Wrapper of createMap(String, String, String, int, String, List<String>, boolean) where the
   * eof is present
   */
  public static String createMap(
      String name,
      String comment,
      String type,
      int dimension,
      String edgeWeightType,
      List<String> coords) {
    return createMap(name, comment, type, dimension, edgeWeightType, coords, true);
  }

  /**
   * Create a mock test map in the form of a String. Puts "EOF" at the end
   *
   * @param name
   * @param comment
   * @param type
   * @param dimension
   * @param edgeWeightType
   * @param coords a list whose elements are of the format "n x y", where n is an int and x and y
   *     are doubles.
   * @return a String representing a TSPLIB file contents
   */
  public static String createMap(
      String name,
      String comment,
      String type,
      int dimension,
      String edgeWeightType,
      List<String> coords,
      boolean eof) {
    String out =
        "NAME : "
            + name
            + "\nCOMMENT : "
            + comment
            + "\nTYPE : "
            + type
            + "\nDIMENSION : "
            + dimension
            + "\nEDGE_WEIGHT_TYPE : "
            + edgeWeightType
            + "\nNODE_COORD_SECTION\n";
    for (String s : coords) {
      out += s + "\n";
    }
    if (eof) {
      out += "EOF\n";
    }
    return out;
  }

  /**
   * Create a mock test solution in the form of a String. Puts "-1", "EOF" at the end
   *
   * @param name
   * @param comment
   * @param type
   * @param dimension
   * @param cities a list whose elements are of the format "n x y", where n is an int and x and y
   *     are doubles.
   * @return a String representing a TSPLIB file contents
   */
  public static String createSolutionMap(
      String name,
      String comment,
      String type,
      int dimension,
      List<String> cities,
      boolean eof) {
    String out =
        "NAME : "
            + name
            + "\nCOMMENT : "
            + comment
            + "\nTYPE : "
            + type
            + "\nDIMENSION : "
            + dimension
            + "\nTOUR_SECTION\n";
    for (String s : cities) {
      out += s + "\n";
    }
    out += "-1\n";
    if (eof) {
      out += "EOF\n";
    }
    return out;
  }

  public static Matrix createOnesMatrix(int n) {
    List<List<Double>> weights = new ArrayList<>();
    for (int i=0;i<n;i++) {
      List<Double> weightsRow = new ArrayList<>();
      for (int j=0;j<n;j++) {
        weightsRow.add(1.0);
      }
      weights.add(weightsRow);
    }

    return new Matrix(n,n,weights);
  }

  /**
   * @param <T>
   * @param name name of class object to create
   * @return an object of type <code><T></code>
   * @throws ClassNotFoundException
   * @throws InstantiationException
   * @throws IllegalAccessException
   * @throws IllegalArgumentException
   * @throws InvocationTargetException
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T> T classFromName(String name)
      throws ClassNotFoundException, InstantiationException, IllegalAccessException,
          IllegalArgumentException, InvocationTargetException {
    Class cl = Class.forName(name, true, Thread.currentThread().getContextClassLoader());
    Constructor constructors[] = cl.getConstructors();
    T out = (T) constructors[0].newInstance(new Object[0]);
    return out;
  }
}
