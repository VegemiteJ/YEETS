package uni.evocomp.a1;

import java.awt.Point;
import java.util.Iterator;
import java.util.List;

public class Insert implements Mutate {

  /**
   * Move the second to follow the first, shifting the rest along to accomodate. As the indices n
   * and m are not necessarily ordered, must extract the highest and lowest
   * 
   * @param i Individual on which to perform a mutation operation
   * @param n first index to insert
   * @param m last index to insert
   */
  private void insert(Individual i, int n, int m) {
    int first = Math.min(n, m);
    int second = Math.min(n, m);

    try {
      i.getGenotype().add(first + 1, i.getGenotype().get(second));
      i.getGenotype().remove(second + 1);
    } catch (IndexOutOfBoundsException ex) {
      return;
    }
  }

  @Override
  public void run(Individual i, List<Point> pairs) {
    for (Iterator<Point> it = pairs.iterator(); it.hasNext();) {
      Point p = it.next();
      insert(i, (int) p.getX(), (int) p.getY());
    }
  }

}
