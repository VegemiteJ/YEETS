package uni.evocomp.util;

import static org.junit.Assert.*;
import java.lang.reflect.InvocationTargetException;
import org.junit.Before;
import org.junit.Test;
import uni.evocomp.a1.Mutate;
import uni.evocomp.a1.SelectParents;

public class ClassFromNameTest {

  @Before
  public void setUp() throws Exception {}

  @Test
  public void testMutateSwap() throws ClassNotFoundException, InstantiationException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    Mutate m = (Mutate) Util.classFromName("uni.evocomp.a1.Swap");
    assertTrue(m instanceof Mutate);
  }

  @Test(expected = ClassCastException.class)
  public void testWrongClass() throws ClassNotFoundException, InstantiationException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    @SuppressWarnings("unused")
    SelectParents sp = Util.classFromName("uni.evocomp.a1.Swap");
  }

  @Test(expected = ClassNotFoundException.class)
  public void testMutateBad() throws ClassNotFoundException, InstantiationException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    Util.classFromName("BadClass");
  }

  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testInterface() throws ClassNotFoundException, InstantiationException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException {
    Util.classFromName("uni.evocomp.a1.Mutate");
  }
}
