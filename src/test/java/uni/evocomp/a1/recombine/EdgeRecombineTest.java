package uni.evocomp.a1.recombine;

import static junit.framework.Assert.assertEquals;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import uni.evocomp.a1.Individual;
import uni.evocomp.util.Pair;
import uni.evocomp.util.RandomStub;

public class EdgeRecombineTest {
  private EdgeRecombine edgeRecombine;

  @Before
  public void setUp() {
    edgeRecombine = new EdgeRecombine();
  }

  @Test
  public void testTableConstruction() {
    Individual a = new Individual(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
    Individual b = new Individual(Arrays.asList(9, 3, 7, 8, 2, 6, 5, 1, 4));
    EdgeRecombine.Table table = edgeRecombine.constructTable(a, b);

    assertEquals(
        table.entry(1).edges,
        new ArrayList<>(Arrays.asList(
          new Pair<>(2, false),
          new Pair<>(4, false),
          new Pair<>(5, false),
          new Pair<>(9, false)
        )));

    assertEquals(
        table.entry(2).edges,
        new ArrayList<>(Arrays.asList(
            new Pair<>(1, false),
            new Pair<>(3, false),
            new Pair<>(6, false),
            new Pair<>(8, false)
        )));

    assertEquals(
        table.entry(3).edges,
        new ArrayList<>(Arrays.asList(
            new Pair<>(2, false),
            new Pair<>(4, false),
            new Pair<>(7, false),
            new Pair<>(9, false)
        )));

    assertEquals(
        table.entry(4).edges,
        new ArrayList<>(Arrays.asList(
            new Pair<>(1, false),
            new Pair<>(3, false),
            new Pair<>(5, false),
            new Pair<>(9, false)
        )));

    assertEquals(
        table.entry(5).edges,
        new ArrayList<>(Arrays.asList(
            new Pair<>(1, false),
            new Pair<>(4, false),
            new Pair<>(6, true)
        )));

    assertEquals(
        table.entry(6).edges,
        new ArrayList<>(Arrays.asList(
            new Pair<>(2, false),
            new Pair<>(5, true),
            new Pair<>(7, false)
        )));

    assertEquals(
        table.entry(7).edges,
        new ArrayList<>(Arrays.asList(
            new Pair<>(3, false),
            new Pair<>(6, false),
            new Pair<>(8, true)
        )));

    assertEquals(
        table.entry(8).edges,
        new ArrayList<>(Arrays.asList(
            new Pair<>(2, false),
            new Pair<>(7, true),
            new Pair<>(9, false)
        )));

    assertEquals(
        table.entry(9).edges,
        new ArrayList<>(Arrays.asList(
            new Pair<>(1, false),
            new Pair<>(3, false),
            new Pair<>(4, false),
            new Pair<>(8, false)
        )));
  }

  @Test
  public void testRecombineWorks() {
    Individual a = new Individual(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
    Individual b = new Individual(Arrays.asList(9, 3, 7, 8, 2, 6, 5, 1, 4));
    edgeRecombine.recombine(a, b);
  }

  @Test
  public void testRecombineFull() {
    Individual a = new Individual(Arrays.asList(1, 2, 3, 4, 5));
    Individual b = new Individual(Arrays.asList(2, 1, 3, 5, 4));

    // test table.
    EdgeRecombine.Table table = edgeRecombine.constructTable(a, b);
    assertEquals(
        table.entry(1).edges,
        new ArrayList<>(Arrays.asList(
            new Pair<>(2, true),
            new Pair<>(3, false),
            new Pair<>(5, false)
        )));
    assertEquals(
        table.entry(2).edges,
        new ArrayList<>(Arrays.asList(
            new Pair<>(1, true),
            new Pair<>(3, false),
            new Pair<>(4, false)
        )));
    assertEquals(
        table.entry(3).edges,
        new ArrayList<>(Arrays.asList(
            new Pair<>(1, false),
            new Pair<>(2, false),
            new Pair<>(4, false),
            new Pair<>(5, false)
        )));
    assertEquals(
        table.entry(4).edges,
        new ArrayList<>(Arrays.asList(
            new Pair<>(2, false),
            new Pair<>(3, false),
            new Pair<>(5, true)
        )));
    assertEquals(
        table.entry(5).edges,
        new ArrayList<>(Arrays.asList(
            new Pair<>(1, false),
            new Pair<>(3, false),
            new Pair<>(4, true)
        )));

    // test crossovered child object.
    RandomStub random = new RandomStub();
    random.setInt(Arrays.asList(
               // Choice  | Choices | Reason        | Partial result
           0,  // 1       | All     | Random        | [1]
           0,  // 2       | 2+,3,5  | Common edge   | [1, 2]
           1,  // 4       | 3, 4    | Random choice | [1, 2, 4]
           0,  // 5       | 3, 5    | Common edge   | [1, 2, 4, 5]
           0   // 3       | 3       | Last element  | [1, 2, 4, 5, 3]
    ));
    Individual child = edgeRecombine.recombine(a, b, random);
    assertEquals(child.getGenotype(), Arrays.asList(1, 2, 4, 5, 3));
  }

  @Test(expected = Test.None.class)
  public void testFuzzed() {
    for (int n = 3; n < 300; n++) {
      Individual a = new Individual(n);
      Individual b = new Individual(n);
      Individual child = edgeRecombine.recombine(a, b);
      try {
        child.assertIsValidTour();
      } catch (Exception e) {
        System.out.println("n: " + n);
        System.out.println("a: " + a.getGenotype().toString());
        System.out.println("b: " + b.getGenotype().toString());
        System.out.println("child: " + child.getGenotype().toString());
        throw e;
      }
    }
  }
}
