package uni.evocomp.a1.logging;

import static org.junit.Assert.*;

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

    Individual knownBest = new Individual(Arrays.asList(1,6,5,4,3,2), 0.5);
    bst.setSolutionTour(knownBest);

    assertEquals(problem, bst.getProblem());
    assertEquals("testInit", bst.getComment());
    assertEquals(0, bst.getBestToursFromEveryRun().size());
    assertEquals(0, bst.getAvgCost(),0.0);
    assertEquals(Double.MAX_VALUE, bst.getMinCost(),0.0);
    assertEquals(Double.MIN_VALUE, bst.getMaxCost(),0.0);
    assertEquals(knownBest, bst.getProvidedBestTour());
  }


  @Test
  public void testNewBestIndividualAdded() {
    TSPProblem problem = new TSPProblem();
    BenchmarkStatsTracker bst = new BenchmarkStatsTracker("testNewBestIndividualAdded", problem);

    bst.startSingleRun();
    Individual newBest = new Individual(Arrays.asList(1,2), 5.0);
    bst.newBestIndividualForSingleRun(newBest, 0);
    bst.endSingleRun();

    assertEquals(5.0, bst.getAvgCost(), 0.0);
    assertEquals(5.0, bst.getMaxCost(), 0.0);
    assertEquals(5.0, bst.getMinCost(), 0.0);
    assertEquals(newBest, bst.getBestTourFound());
    assertEquals(newBest, bst.getBestToursFromEveryRun().get(0));
    assertEquals(newBest, bst.getBestToursFromMaxRun().get(0));
    assertEquals(newBest, bst.getBestToursFromMinRun().get(0));
  }

  @Test
  public void testCorrectMinMaxAvgStats() {
    TSPProblem problem = new TSPProblem();
    BenchmarkStatsTracker bst = new BenchmarkStatsTracker("testCorrectMinMaxAvgStats", problem);

    Individual worst = new Individual(Arrays.asList(1,2,3), 5.0);
    Individual best = new Individual(Arrays.asList(3,1,2), 2.0);
    Individual middle = new Individual(Arrays.asList(2,3,1), 3.0);

    bst.startSingleRun();
    bst.newBestIndividualForSingleRun(best,0);
    bst.endSingleRun();

    bst.startSingleRun();
    bst.newBestIndividualForSingleRun(worst,0);
    bst.endSingleRun();

    bst.startSingleRun();
    bst.newBestIndividualForSingleRun(middle,0);
    bst.endSingleRun();

    assertEquals(10.0/3, bst.getAvgCost(), 0.0000001);
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
    BenchmarkStatsTracker bst = new BenchmarkStatsTracker("testAverageCostOnlySetForFinalBestRunInARun", problem);

    Individual a = new Individual(Arrays.asList(1,2,3), 5.0);
    Individual b = new Individual(Arrays.asList(3,1,2), 9.0);
    Individual c = new Individual(Arrays.asList(2,3,1), 2.0);
    Individual d = new Individual(Arrays.asList(3,2,1), 1.0);
    Individual e = new Individual(Arrays.asList(2,1,3), 4.0);

    bst.startSingleRun();
    bst.newBestIndividualForSingleRun(b,0);
    bst.newBestIndividualForSingleRun(a,0);
    bst.endSingleRun();

    bst.startSingleRun();
    bst.newBestIndividualForSingleRun(c,0);
    bst.newBestIndividualForSingleRun(e,0);
    bst.newBestIndividualForSingleRun(d,0);
    bst.endSingleRun();

    // Best Tour is from d in Run 2 = 1.0
    // Worst Best Tour is from a in Run 1 = 5.0
    // Average = Best(Run 1) + Best(Run 2) / 2 = 3.0

    assertEquals(d, bst.getBestTourFound());
    assertEquals(1.0, bst.getMinCost(), 0.0000001);
    assertEquals(5.0, bst.getMaxCost(), 0.0000001);
    assertEquals(3.0, bst.getAvgCost(), 0.0000001);
  }

  @Test
  public void testSerializeExpectedUsage() throws IOException, ClassNotFoundException {
    TSPProblem problem = new TSPProblem(Util.createOnesMatrix(2));
    BenchmarkStatsTracker bst = new BenchmarkStatsTracker("testSerializeExpectedUsage", problem);

    Individual a = new Individual(Arrays.asList(1,2,3), 5.0);
    Individual b = new Individual(Arrays.asList(3,1,2), 9.0);
    Individual c = new Individual(Arrays.asList(2,3,1), 2.0);
    Individual d = new Individual(Arrays.asList(3,2,1), 1.0);
    Individual e = new Individual(Arrays.asList(2,1,3), 4.0);

    bst.startSingleRun();
    bst.newBestIndividualForSingleRun(b,0);
    bst.newBestIndividualForSingleRun(a,0);
    bst.endSingleRun();

    bst.startSingleRun();
    bst.newBestIndividualForSingleRun(c,0);
    bst.newBestIndividualForSingleRun(e,0);
    bst.newBestIndividualForSingleRun(d,0);
    bst.endSingleRun();

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

    Individual a = new Individual(Arrays.asList(1,2,3), 5.0);
    Individual b = new Individual(Arrays.asList(3,1,2), 9.0);
    Individual c = new Individual(Arrays.asList(2,3,1), 2.0);
    Individual d = new Individual(Arrays.asList(3,2,1), 1.0);
    Individual e = new Individual(Arrays.asList(2,1,3), 4.0);

    bst.startSingleRun();
    bst.newBestIndividualForSingleRun(b,0);
    bst.newBestIndividualForSingleRun(a,0);
    bst.endSingleRun();

    bst.startSingleRun();
    bst.newBestIndividualForSingleRun(c,0);
    bst.newBestIndividualForSingleRun(e,0);
    bst.newBestIndividualForSingleRun(d,0);
    bst.endSingleRun();

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
}