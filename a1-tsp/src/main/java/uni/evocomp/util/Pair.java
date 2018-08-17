package uni.evocomp.util;

import java.io.Serializable;

public class Pair<T1, T2> implements Serializable {
  public T1 first;
  public T2 second;

  public Pair(T1 first, T2 second) {
    this.first = first;
    this.second = second;
  }

  public String toString() {
    return "(" + first.toString() + ", " + second.toString() + ")";
  }

  @Override public boolean equals(Object o) {
    if (!(o instanceof Pair))
      return false;
    Pair<T1, T2> other = (Pair<T1, T2>) o;
    return this.first == other.first && this.second == other.second;
  }
}
