package uni.evocomp.a1;

import java.awt.Point;
import java.util.List;

/**
 * An interface to handle representing mutation operations. The run method should be overriden in
 * implementations to call a private function.
 * 
 * All implementations should be stateless.
 * 
 * @author Namdrib
 *
 */
public interface Mutate {
  /**
   * Perform a list of mutation operations on an Individual
   * 
   * @param i Individual on which to perform a mutation operation
   * @param pairs List of Points whose x and y dictate which indices to use when mutating
   */
  public void run(Individual i, List<Point> pairs);
}
