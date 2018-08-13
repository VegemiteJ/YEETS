package uni.evocomp.util;

import java.io.Serializable;

public class Pair<T1, T2> implements Serializable {
  public T1 first;
  public T2 second;

  public Pair(T1 first, T2 second) {
    this.first = first;
    this.second = second;
  }
}
