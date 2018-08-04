package uni.evocomp.util;

import java.awt.Point;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
  public static double euclideanDistance2D(Point i, Point j) {
    double xd = i.getX() - j.getX();
    double yd = i.getY() - j.getY();
    return Math.round(Math.sqrt(xd * xd + yd * yd) + 0.5);
  }

  public static double euclideanDistance2D(IntegerPair i, IntegerPair j) {
    double xd = i.first - j.first;
    double yd = i.second - j.second;
    return Math.round(Math.sqrt(xd * xd + yd * yd) + 0.5);
  }

  /**
   * 
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
