package uni.evocomp.a1;

import java.awt.Point;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Swap implements Mutate {

  /**
   * Perform a swap on position n and m of i's genotype
   * 
   * @param i Individual on which to perform a mutation operation
   * @param n first index to swap
   * @param m last index to swap
   */
  private void swap(Individual i, int n, int m) {
    try {
      Collections.swap(i.getGenotype(), n, m);
    } catch (IndexOutOfBoundsException ex) {
      return;
    }
  }

  @Override
  public void run(Individual i, List<Point> pairs) {
    for (Iterator<Point> it = pairs.iterator(); it.hasNext();) {
      Point p = it.next();
      swap(i, (int) p.getX(), (int) p.getY());
    }
  }

}
