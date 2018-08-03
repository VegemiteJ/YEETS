package uni.evocomp.a1;

import java.util.List;
import java.util.Random;

/**
 * Random stub class designed to create custom Random objects with predictable behaviour
 * 
 * set[type] will take a List of [type] and return those in order each time next[type] is called
 * 
 * The advantage of using this class is that it can act as a random object but generate predictable behaviour
 * 
 * @author joshuafloh
 *
 */
public class RandomStub extends Random {
  // Fields
  private List<Integer> ints;
  private int intCounter;
  private List<Float> floats;
  private int floatCounter;
  
  // Methods
  // TODO: Implement these
  @Override
  public int nextInt() {
    return 0;
  }
  @Override
  public float nextFloat() {
    return 0;
  }
  
  public void setInt(List<Integer> list) {
    this.ints = list;
  }
  public void setFloats(List<Float> list) {
    this.floats = list;
  }
}
