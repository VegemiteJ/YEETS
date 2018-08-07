package uni.evocomp.a1.recombine;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import uni.evocomp.a1.Individual;
import uni.evocomp.util.Pair;

public class RecombineDummyTest {

  @Before
  public void setUp() {
    
  }


  @Test
  public void test() {
    Individual i0 = new Individual();
    Individual i1 = new Individual();
    RecombineDummy dummy = new RecombineDummy();
    Pair<Individual,Individual> result = dummy.recombine(i0, i1);
    assertEquals(i0, result.first);
    assertEquals(i1, result.second);
  }

}
