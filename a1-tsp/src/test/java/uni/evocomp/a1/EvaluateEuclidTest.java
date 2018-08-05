package uni.evocomp.a1;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EvaluateEuclidTest {
    Evaluate e;
    TSPProblem testProblem;

    @Before
    public void setUp() throws Exception {
        List<List<Double>> weights = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            weights.add(new ArrayList<>());
            for (int j = 0; j < 3; j++) {
                weights.get(i).add((double) 3*i + j);
            }
        }
        //weights in matrix form
        //0,1,2
        //3,4,5
        //6,7,8
        testProblem = new TSPProblem("", "", "", "", weights);

        e = new EvaluateEuclid();
    }

    @Test
    public void testSimpleTour1() {
        //1,2,3 -> 1+5 = 6
        List<Integer> tour = new ArrayList<>(Arrays.asList(1, 2, 3));
        Individual i = new Individual(tour, 0.0);
        double cost = e.evaluate(testProblem, i);
        Assert.assertEquals(cost, 6.0,0);
    }

    @Test
    public void testSimpleTour2() {
        //1,2,3,2,1 -> 1+5+7+3 = 16
        List<Integer> tour = new ArrayList<>(Arrays.asList(1, 2, 3, 2, 1));
        Individual i = new Individual(tour, 0.0);
        double cost = e.evaluate(testProblem, i);
        Assert.assertEquals(cost, 16.0,0);
    }

    @Test
    public void testSimpleTour3() {
        //3,1,2,1,2,3 -> 6+1+3+1+5=16
        List<Integer> tour = new ArrayList<>(Arrays.asList(1, 2, 3, 2, 1));
        Individual i = new Individual(tour, 0.0);
        double cost = e.evaluate(testProblem, i);
        Assert.assertEquals(cost, 16.0,0);
    }

    //TODO: Failure tests

    //This is more of an integration test
    @Test
    public void testFileAgainstBest() throws IOException {
        //Read a problem file and it's best tour
        TSPProblem problem;
        Individual bestTour;

        TSPIO io = new TSPIO();
        problem = io.read(new FileReader("tests/pcb442.tsp"));
        bestTour = io.readSolution(new FileReader("tests/pcb442.opt.tour"));

        double cost = e.evaluate(problem, bestTour);

        System.out.println(cost);

    }


}
