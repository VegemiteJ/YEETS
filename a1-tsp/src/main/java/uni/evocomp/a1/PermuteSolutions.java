package uni.evocomp.a1;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Utility class to go through every possible solution for a given map. Brute force, so try to keep
 * the map size down.
 * 
 * Used to make it easier to create test tours and to know the cost, etc.
 * 
 * @author Namdrib
 *
 */
public class PermuteSolutions {

  /**
   * Helper function that creates the next permutation of a Comparable[] c
   * 
   * Inspired by http://codeforces.com/blog/entry/3980
   * 
   * @param c the comparable for which the next permutation should be generated
   * @return the next permutation of c if there is one, otherwise <code>null</code>
   */
  private static Integer[] nextPermutation(final Integer[] c) {
    // 1. finds the largest k, that c[k] < c[k+1]
    int first = getFirst(c);
    if (first == -1)
      return null; // no greater permutation

    // 2. find last index toSwap, that c[k] < c[toSwap]
    int toSwap = c.length - 1;
    while (c[first].compareTo(c[toSwap]) >= 0)
      --toSwap;

    // 3. swap elements with indexes first and last
    swap(c, first++, toSwap);

    // 4. reverse sequence from k+1 to n (inclusive)
    toSwap = c.length - 1;
    while (first < toSwap)
      swap(c, first++, toSwap--);
    return c;
  }

  // finds the largest k, that c[k] < c[k+1]
  // if no such k exists (there is not greater permutation), return -1
  private static int getFirst(final Integer[] c) {
    for (int i = c.length - 2; i >= 0; --i)
      if (c[i] < c[i + 1])
        return i;
    return -1;
  }

  // swaps two elements (with indexes i and j) in array
  private static void swap(final Integer[] c, final int i, final int j) {
    final Integer tmp = c[i];
    c[i] = c[j];
    c[j] = tmp;
  }

  /**
   * Generate every possible solution, starting with 1..n until n..1
   * 
   * Prints each tour and its corresponding cost
   * 
   * @param problem the TSPProblem to generate solutions for
   */
  public static void permute(TSPProblem problem) {
    // Generate 1..n
    List<Integer> tour =
        IntStream.rangeClosed(1, problem.getSize()).boxed().collect(Collectors.toList());

    // Generate every permutation, printing each one and its cost
    Integer temp[] = new Integer[tour.size()];
    do {
      Individual i = new Individual(tour, problem);
      System.out.println(i.getGenotype() + " => " + i.getCost(problem));
    } while ((tour = Arrays.asList(nextPermutation(tour.toArray(temp)))) != null);

  }

  public static void main(String[] args) {
    TSPProblem problem = new TSPProblem();
    TSPIO io = new TSPIO();
    String testfile = (args.length > 0 ? args[0] : "tests/custom/test1.tsp");
    System.out.println("Test file is " + testfile);
    try (Reader r = new FileReader(testfile)) {
      problem = io.read(r);
    } catch (IOException e) {
      e.printStackTrace();
    }

    permute(problem);
  }
}
