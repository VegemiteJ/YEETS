package uni.evocomp.a1.logging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import uni.evocomp.a1.Individual;
import uni.evocomp.a1.TSPProblem;
import uni.evocomp.util.Util;

public class BenchmarkStatsTrackerTest {

  @Test
  public void testInit() {
    TSPProblem problem = new TSPProblem();
    BenchmarkStatsTracker bst = new BenchmarkStatsTracker("testInit", problem);
    bst.startSingleRun();

    Individual knownBest = new Individual(Arrays.asList(1, 6, 5, 4, 3, 2), 0.5);
    bst.setSolutionTour(knownBest);

    assertEquals(problem, bst.getProblem());
    assertEquals("testInit", bst.getComment());
    assertEquals(0, bst.getBestToursFromEveryRun().size());
    assertEquals(0, bst.getAvgCost(), 0.0);
    assertEquals(Double.MAX_VALUE, bst.getMinCost(), 0.0);
    assertEquals(Double.MIN_VALUE, bst.getMaxCost(), 0.0);
    assertEquals(knownBest, bst.getProvidedBestTour());
  }

  @Test
  public void testNewBestIndividualAdded() {
    TSPProblem problem = new TSPProblem();
    BenchmarkStatsTracker bst = new BenchmarkStatsTracker("testNewBestIndividualAdded", problem);

    bst.startSingleRun();
    Individual newBest = new Individual(Arrays.asList(1, 2), 5.0);
    bst.newBestIndividualForSingleRun(newBest, 0L);
    bst.endSingleRun(0L);

    assertEquals(5.0, bst.getAvgCost(), 0.0);
    assertEquals(5.0, bst.getMaxCost(), 0.0);
    assertEquals(5.0, bst.getMinCost(), 0.0);
    assertEquals(newBest, bst.getBestTourFound());
    assertEquals(newBest, bst.getBestToursFromEveryRun().get(0));
    assertEquals(newBest, bst.getBestToursFromMaxRun().get(0));
    assertEquals(newBest, bst.getBestToursFromMinRun().get(0));
  }

  @Test
  public void testCorrectMinMaxAvgStdDevStats() {
    TSPProblem problem = new TSPProblem();
    BenchmarkStatsTracker bst = new BenchmarkStatsTracker("testCorrectMinMaxAvgStats", problem);

    Individual worst = new Individual(Arrays.asList(1, 2, 3), 5.0);
    Individual best = new Individual(Arrays.asList(3, 1, 2), 2.0);
    Individual middle = new Individual(Arrays.asList(2, 3, 1), 3.0);

    bst.startSingleRun();
    bst.newBestIndividualForSingleRun(best, 0L);
    bst.endSingleRun(0L);

    bst.startSingleRun();
    bst.newBestIndividualForSingleRun(worst, 0L);
    bst.endSingleRun(0L);

    bst.startSingleRun();
    bst.newBestIndividualForSingleRun(middle, 0L);
    bst.endSingleRun(0L);

    assertEquals(1.2472191289246, bst.getStandardDeviation(), 0.0000001);
    assertEquals(10.0 / 3, bst.getAvgCost(), 0.0000001);
    assertEquals(5.0, bst.getMaxCost(), 0.0000001);
    assertEquals(2.0, bst.getMinCost(), 0.0000001);
    assertEquals(best, bst.getBestTourFound());
    assertEquals(worst, bst.getBestToursFromMaxRun().get(0));
    assertEquals(best, bst.getBestToursFromMinRun().get(0));

    List<Individual> bestPerRun = bst.getBestToursFromEveryRun();
    assertEquals(best, bestPerRun.get(0));
    assertEquals(worst, bestPerRun.get(1));
    assertEquals(middle, bestPerRun.get(2));
  }

  @Test
  public void testAverageCostOnlySetForFinalBestRunInARun() {
    TSPProblem problem = new TSPProblem();
    BenchmarkStatsTracker bst =
        new BenchmarkStatsTracker("testAverageCostOnlySetForFinalBestRunInARun", problem);

    Individual a = new Individual(Arrays.asList(1, 2, 3), 5.0);
    Individual b = new Individual(Arrays.asList(3, 1, 2), 9.0);
    Individual c = new Individual(Arrays.asList(2, 3, 1), 2.0);
    Individual d = new Individual(Arrays.asList(3, 2, 1), 1.0);
    Individual e = new Individual(Arrays.asList(2, 1, 3), 4.0);

    bst.startSingleRun();
    bst.newBestIndividualForSingleRun(b, 0L);
    bst.newBestIndividualForSingleRun(a, 0L);
    bst.endSingleRun(0L);

    bst.startSingleRun();
    bst.newBestIndividualForSingleRun(c, 0L);
    bst.newBestIndividualForSingleRun(e, 0L);
    bst.newBestIndividualForSingleRun(d, 0L);
    bst.endSingleRun(0L);

    // Best Tour is from d in Run 2 = 1.0
    // Worst Best Tour is from a in Run 1 = 5.0
    // Average = Best(Run 1) + Best(Run 2) / 2 = 3.0

    assertEquals(d, bst.getBestTourFound());
    assertEquals(1.0, bst.getMinCost(), 0.0000001);
    assertEquals(5.0, bst.getMaxCost(), 0.0000001);
    assertEquals(3.0, bst.getAvgCost(), 0.0000001);
  }

  @Test
  public void testWriteCsvStats() {
    TSPProblem problem = new TSPProblem();
    BenchmarkStatsTracker bst =
        new BenchmarkStatsTracker("testWriteCsvStats", problem);

    Individual a = new Individual(Arrays.asList(1, 2, 3), 5.0);
    Individual b = new Individual(Arrays.asList(3, 1, 2), 9.0);
    Individual c = new Individual(Arrays.asList(2, 3, 1), 2.0);
    Individual d = new Individual(Arrays.asList(3, 2, 1), 1.0);
    Individual e = new Individual(Arrays.asList(2, 1, 3), 4.0);

    bst.startSingleRun();
    bst.newBestIndividualForSingleRun(b, 0L);
    bst.newBestIndividualForSingleRun(a, 0L);
    bst.endSingleRun(0L);

    bst.startSingleRun();
    bst.newBestIndividualForSingleRun(c, 0L);
    bst.newBestIndividualForSingleRun(e, 0L);
    bst.newBestIndividualForSingleRun(d, 0L);
    bst.endSingleRun(0L);

    // Best Tour is from d in Run 2 = 1.0
    // Worst Best Tour is from a in Run 1 = 5.0
    // Average = Best(Run 1) + Best(Run 2) / 2 = 3.0

    assertEquals(d, bst.getBestTourFound());
    assertEquals(1.0, bst.getMinCost(), 0.0000001);
    assertEquals(5.0, bst.getMaxCost(), 0.0000001);
    assertEquals(3.0, bst.getAvgCost(), 0.0000001);
    assertEquals(2.0, bst.getStandardDeviation(), 0.0000001);

    bst.writeToFile();
    assertTrue(new File("testWriteCsvStats.csv").exists());
    // Delete
    new File("testWriteCsvStats.csv").delete();
  }

  @Test
  public void testSerializeExpectedUsage() throws IOException, ClassNotFoundException {
    TSPProblem problem = new TSPProblem(Util.createOnesMatrix(2));
    BenchmarkStatsTracker bst = new BenchmarkStatsTracker("testSerializeExpectedUsage", problem);

    Individual a = new Individual(Arrays.asList(1, 2, 3), 5.0);
    Individual b = new Individual(Arrays.asList(3, 1, 2), 9.0);
    Individual c = new Individual(Arrays.asList(2, 3, 1), 2.0);
    Individual d = new Individual(Arrays.asList(3, 2, 1), 1.0);
    Individual e = new Individual(Arrays.asList(2, 1, 3), 4.0);

    bst.startSingleRun();
    bst.newBestIndividualForSingleRun(b, 0L);
    bst.newBestIndividualForSingleRun(a, 0L);
    bst.endSingleRun(0L);

    bst.startSingleRun();
    bst.newBestIndividualForSingleRun(c, 0L);
    bst.newBestIndividualForSingleRun(e, 0L);
    bst.newBestIndividualForSingleRun(d, 0L);
    bst.endSingleRun(0L);

    BenchmarkStatsTracker.serialise(bst);
    assertTrue(new File("testSerializeExpectedUsage.ser").exists());

    BenchmarkStatsTracker bstRead = BenchmarkStatsTracker.deserialise("testSerializeExpectedUsage");

    // Best Tour is from d in Run 2 = 1.0
    // Worst Best Tour is from a in Run 1 = 5.0
    // Average = Best(Run 1) + Best(Run 2) / 2 = 3.0
    assertEquals(d, bstRead.getBestTourFound());
    assertEquals(1.0, bstRead.getMinCost(), 0.0000001);
    assertEquals(5.0, bstRead.getMaxCost(), 0.0000001);
    assertEquals(3.0, bstRead.getAvgCost(), 0.0000001);

    // Delete
    new File("testSerializeExpectedUsage.ser").delete();
  }

  @Test
  public void testSerializeCustomNameUsage() throws IOException, ClassNotFoundException {
    TSPProblem problem = new TSPProblem(Util.createOnesMatrix(2));
    BenchmarkStatsTracker bst = new BenchmarkStatsTracker("testSerializeCustomNameUsage", problem);

    Individual a = new Individual(Arrays.asList(1, 2, 3), 5.0);
    Individual b = new Individual(Arrays.asList(3, 1, 2), 9.0);
    Individual c = new Individual(Arrays.asList(2, 3, 1), 2.0);
    Individual d = new Individual(Arrays.asList(3, 2, 1), 1.0);
    Individual e = new Individual(Arrays.asList(2, 1, 3), 4.0);

    bst.startSingleRun();
    bst.newBestIndividualForSingleRun(b, 0L);
    bst.newBestIndividualForSingleRun(a, 0L);
    bst.endSingleRun(0L);

    bst.startSingleRun();
    bst.newBestIndividualForSingleRun(c, 0L);
    bst.newBestIndividualForSingleRun(e, 0L);
    bst.newBestIndividualForSingleRun(d, 0L);
    bst.endSingleRun(0L);

    BenchmarkStatsTracker.serialise(bst, "CustomName");
    assertTrue(new File("CustomName.ser").exists());

    BenchmarkStatsTracker bstRead = BenchmarkStatsTracker.deserialise("CustomName");

    // Best Tour is from d in Run 2 = 1.0
    // Worst Best Tour is from a in Run 1 = 5.0
    // Average = Best(Run 1) + Best(Run 2) / 2 = 3.0
    assertEquals(d, bstRead.getBestTourFound());
    assertEquals(1.0, bstRead.getMinCost(), 0.0000001);
    assertEquals(5.0, bstRead.getMaxCost(), 0.0000001);
    assertEquals(3.0, bstRead.getAvgCost(), 0.0000001);

    // Delete
    new File("CustomName.ser").delete();
  }

  @Test
  public void testAddedGenerations() {
    TSPProblem problem = new TSPProblem(Util.createOnesMatrix(2));
    BenchmarkStatsTracker bst = new BenchmarkStatsTracker("testAddedGenerations", problem);

    Individual a = new Individual(Arrays.asList(1, 2, 3), 5.0);
    Individual b = new Individual(Arrays.asList(3, 1, 2), 9.0);
    Individual c = new Individual(Arrays.asList(2, 3, 1), 2.0);
    Individual d = new Individual(Arrays.asList(3, 2, 1), 1.0);
    Individual e = new Individual(Arrays.asList(2, 1, 3), 4.0);

    bst.bestIndividualForThisGeneration(b, 0);
    bst.bestIndividualForThisGeneration(a, 1);
    bst.bestIndividualForThisGeneration(c, 2);
    bst.bestIndividualForThisGeneration(d, 3);
    bst.bestIndividualForThisGeneration(e, 4);

    try {
      assertEquals(b,bst.getBestIndividualPerGeneration(0));
      assertEquals(a,bst.getBestIndividualPerGeneration(1));
      assertEquals(c,bst.getBestIndividualPerGeneration(2));
      assertEquals(d,bst.getBestIndividualPerGeneration(3));
      assertEquals(e,bst.getBestIndividualPerGeneration(4));
    } catch (Exception e1) {
      e1.printStackTrace();
      fail();
    }

    for (int i=0; i<100; i++) {
      bst.bestIndividualForThisGeneration(b, i);
    }
    for (int i=100; i<200; i++) {
      bst.bestIndividualForThisGeneration(a, i);
    }
    for (int i=200; i<500; i++) {
      bst.bestIndividualForThisGeneration(d, i);
    }
    for (int i=500; i<2000; i++) {
      bst.bestIndividualForThisGeneration(e, i);
    }
    bst.bestIndividualForThisGeneration(c, 2000);

    try {
      assertEquals(a,bst.getBestIndividualPerGeneration(100));
      assertEquals(d,bst.getBestIndividualPerGeneration(200));
      assertEquals(b,bst.getBestIndividualPerGeneration(76));
      assertEquals(e,bst.getBestIndividualPerGeneration(500));
      assertEquals(c,bst.getBestIndividualPerGeneration(2000));
    } catch (Exception e1) {
      e1.printStackTrace();
      fail();
    }
  }

  @Test
  public void testEAFileWrite() {
    TSPProblem problem = new TSPProblem(Util.createOnesMatrix(2));
    BenchmarkStatsTracker bst = new BenchmarkStatsTracker("testAddedGenerations", problem);

    Individual a = new Individual(Arrays.asList(1, 2, 3), 5.0);
    Individual b = new Individual(Arrays.asList(3, 1, 2), 9.0);
    Individual c = new Individual(Arrays.asList(2, 3, 1), 2.0);
    Individual d = new Individual(Arrays.asList(3, 2, 1), 1.0);
    Individual e = new Individual(Arrays.asList(2, 1, 3), 4.0);

    for (int i=0; i<2000; i++) {
      bst.bestIndividualForThisGeneration(b, i);
    }
    for (int i=2000; i<5000; i++) {
      bst.bestIndividualForThisGeneration(a, i);
    }
    for (int i=5000; i<10000; i++) {
      bst.bestIndividualForThisGeneration(d, i);
    }
    for (int i=10000; i<20000; i++) {
      bst.bestIndividualForThisGeneration(e, i);
    }
    bst.bestIndividualForThisGeneration(c, 20000);

    bst.writeEAGensToFile();
    assertTrue(new File("testAddedGenerations.gen").exists());
    // Delete
    new File("testAddedGenerations.gen").delete();

  }

  @Test
  public void testExpectedUsageEAFileWrite() {
    TSPProblem problem = new TSPProblem(Util.createOnesMatrix(2));
    BenchmarkStatsTracker bst = new BenchmarkStatsTracker("testAddedGenerationsAlt", problem);

    Individual a = new Individual(Arrays.asList(1, 2, 3), 5.0);
    Individual b = new Individual(Arrays.asList(3, 1, 2), 9.0);
    Individual d = new Individual(Arrays.asList(3, 2, 1), 1.0);
    Individual e = new Individual(Arrays.asList(2, 1, 3), 4.0);

    bst.bestIndividualForThisGeneration(a, 2000);
    bst.bestIndividualForThisGeneration(b, 5000);
    bst.bestIndividualForThisGeneration(d, 10000);
    bst.bestIndividualForThisGeneration(e, 20000);

    bst.writeEAGensToFile();
    assertTrue(new File("testAddedGenerationsAlt.gen").exists());
    // Delete
    new File("testAddedGenerationsAlt.gen").delete();
  }

  @Test
  public void testPartialFailureEAFileWrite() {
    TSPProblem problem = new TSPProblem(Util.createOnesMatrix(2));
    BenchmarkStatsTracker bst = new BenchmarkStatsTracker("testAddedGenerationsAltAlt", problem);

    Individual a = new Individual(Arrays.asList(1, 2, 3), 5.0);
    Individual e = new Individual(Arrays.asList(2, 1, 3), 4.0);

    bst.bestIndividualForThisGeneration(a, 2000);
    bst.bestIndividualForThisGeneration(e, 20000);

    bst.writeEAGensToFile();
    assertTrue(new File("testAddedGenerationsAltAlt.gen").exists());
    // Delete
    new File("testAddedGenerationsAltAlt.gen").delete();
  }
}
