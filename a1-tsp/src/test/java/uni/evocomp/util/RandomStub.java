package uni.evocomp.util;

import java.util.List;
import java.util.Random;

/**
 * Random stub class designed to create custom Random objects with predictable behaviour
 * 
 * set[type] will take a List of [type] and return those in order each time next[type] is called
 * 
 * The advantage of using this class is that it can act as a random object but generate predictable
 * behaviour
 * 
 * TODO: Right now this only works with ints and floats, potentially look at using templates to make
 * it work for any type in the future?
 * 
 * @author joshuafloh
 *
 */
public class RandomStub extends Random {
  // Fields
  private List<Integer> ints;
  private int intCounter;
  private List<Double> doubles;
  private int doubleCounter;

  // Methods
  @Override
  public int nextInt(int bound) {
    int i = ints.get(intCounter % ints.size());
    intCounter++;
    return i;
  }

  @Override
  public double nextDouble() {
    Double d = doubles.get(doubleCounter % doubles.size());
    doubleCounter++;
    return d;
  }

  public void setInt(List<Integer> list) {
    this.ints = list;
  }

  public void setDoubles(List<Double> list) {
    this.doubles = list;
  }
}
