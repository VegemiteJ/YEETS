package uni.evocomp.a1;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EvaluateEuclidTest extends TestCase {
    Evaluate e;
    TSPProblem testProblem;

    protected void setUp() throws Exception {
        super.setUp();
        List<List<Double>> weights = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            weights.add(new ArrayList<>());
            for (int j = 0; j < 3; j++) {
                weights.get(i).add((double) 2);
            }
        }
        testProblem = new TSPProblem("", "", "", "", weights);

        e = new EvaluateEuclid();
    }

    @Test
    public void testSimpleTour() {
        List<Integer> tour = new ArrayList<>(Arrays.asList(1, 2, 3));
        Individual i = new Individual(tour);
        double cost = e.evaluate(testProblem, i);
        assertEquals(cost, 4.0);
    }
}
