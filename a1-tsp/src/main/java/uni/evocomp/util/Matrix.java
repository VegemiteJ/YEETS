package uni.evocomp.util;

import com.sun.javaws.exceptions.InvalidArgumentException;
import java.text.DecimalFormat;
import java.util.List;

public class Matrix {
  /* Variables */
  // The dimension of the matrix: rows by cols.
  private int rDim, cDim;
  // The matrix is stored as a 1D array of n*n values.
  // To access element [i,j], read from index n*i+j.
  // (i and j are zero-indexed).
  private Double[] array;

  public Matrix(Matrix src) {
    this.rDim = src.rDim;
    this.cDim = src.cDim;
    this.array = new Double[rDim * cDim];
    for (int i = 0; i < rDim; i++) {
      for (int j = 0; j < cDim; j++) {
        array[rDim * i + j] = src.get(i, j);
      }
    }
  }

  /* Matrix.java
   * James Kortman    | a1648090
   *
   * A two-dimensional array container for floating-point numbers.
   * Provides methods to read from file, generate at random, and read values
   * from coordinates within the matrix.
   */
  public Matrix(int rDim, int cDim, List<List<Double>> weights) throws InvalidArgumentException {
    if (rDim < 0 || cDim < 0) {
      throw new InvalidArgumentException(new String[] {"Dimensions less than 0"});
    }
    this.rDim = rDim;
    this.cDim = cDim;
    this.array = new Double[rDim * cDim];
    furnishArray(array, weights, rDim, cDim);
  }

  public Matrix(List<List<Double>> weights) {
    this.rDim = weights.size();
    this.cDim = weights.get(0).size();
    this.array = new Double[rDim * cDim];
    furnishArray(array, weights, rDim, cDim);
  }

  private void furnishArray(Double[] array, List<List<Double>> weights, int rDim, int cDim) {
    for (int i = 0; i < rDim; i++) {
      for (int j = 0; j < cDim; j++) {
        if (weights != null) {
          array[rDim * i + j] = weights.get(i).get(j);
        } else {
          array[rDim * i + j] = 0.0;
        }
      }
    }
  }

  public int size() {
    return this.rDim;
  }

  // Getter to read a value at a coordinate in the matrix.
  // Provided a valid set of coordinates i,j where 0 <= i,j < n,
  // returns the value at row i, column j of the contained matrix.
  // If i or j are outside of the valid range, throws
  // IllegalArgumentException.
  public double get(int i, int j) throws IndexOutOfBoundsException {
    if (i < 0 || i >= rDim || j < 0 || j >= cDim) {
      throw new IndexOutOfBoundsException("coordinates out of bounds");
    }
    return array[rDim * i + j];
  }

  // Setter to set values to coordinates in the matrix.
  // Provided a valid set of coordinates i,j where 0 <= i,j < n,
  // and a double val, sets element [i,j] of the matrix to val.
  // If i or j are outside of the valid range, throws IllegalArgumentException.
  public void set(int i, int j, double val) throws IndexOutOfBoundsException {
    if (i < 0 || i >= rDim || j < 0 || j >= cDim) {
      throw new IndexOutOfBoundsException("coordinates out of bounds");
    }
    array[rDim * i + j] = val;
  }

  // Prints the entire matrix to stdout for debugging purposes.
  public void print() {
    DecimalFormat df = new DecimalFormat("#0.00");
    for (int i = 0; i < rDim; i += 1) {
      System.out.print("  [ ");
      for (int j = 0; j < cDim; j += 1) {
        System.out.print("" + df.format(this.get(i, j)));
        if (j < cDim - 1) {
          System.out.print(", ");
        }
      }
      System.out.print(" ]\n");
    }
  }
}
