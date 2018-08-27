package uni.evocomp.util;

import java.util.List;

public class Bounds {
  public static <T> Boolean inBounds(List<T> container, Integer idx1) {
    return idx1 >= 0 && idx1 < container.size();
  }
}
