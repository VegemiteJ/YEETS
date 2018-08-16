package uni.evocomp.a1.recombine;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import uni.evocomp.a1.Individual;
import uni.evocomp.util.IntegerPair;
import uni.evocomp.util.Pair;

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
  public void testRecombine() {
    Individual a = new Individual(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
    Individual b = new Individual(Arrays.asList(9, 3, 7, 8, 2, 6, 5, 1, 4));
    Individual child = edgeRecombine.recombineSingle(a, b);
  }
}
