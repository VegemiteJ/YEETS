package uni.evocomp.util;

import com.sun.javaws.exceptions.InvalidArgumentException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class MatrixTest {

  @Before
  public void setUp() throws Exception {

  }

  @Test
  public void testZeroInit() throws InvalidArgumentException {
    Matrix m = new Matrix(3,3, null);
    for (int i =0; i<3; i++) {
      for (int j =0; j<3; j++) {
        assertEquals(m.get(i,j),0.0, 0.00000000001);
      }
    }
  }

  @Test
  public void testValidCase() throws InvalidArgumentException {
    ArrayList<ArrayList<Double>> a = new ArrayList<>();
    for (int i =0; i<3; i++) {
      ArrayList<Double> b = new ArrayList<>(Arrays.asList(1.,2.,3.));
      a.add(b);
    }

    List<List<Double>> cpy = new ArrayList<>(a);

    Matrix m =new Matrix(3,3, cpy);

    for (int i = 0; i < 3; i++) {
      for (int j=0;j<3;j++) {
        assertEquals(m.get(i,j), a.get(i).get(j), 0.00000000001);
      }
    }
  }

  @Test
  public void testInvalidSize() {
    try {
      Matrix m = new Matrix(-1,-1,null);
    } catch (InvalidArgumentException e) {
      return;
    }
    fail();
  }
}
