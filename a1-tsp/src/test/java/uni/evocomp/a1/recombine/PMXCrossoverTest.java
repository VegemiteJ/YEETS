package uni.evocomp.a1.recombine;

import org.junit.Before;
import org.junit.Test;
import uni.evocomp.a1.Individual;
import uni.evocomp.util.IntegerPair;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;

public class PMXCrossoverTest {
    private PMXCrossover crossover;

    @Before
    public void setUp() {
        crossover = new PMXCrossover();
    }

    @Test
    public void testCrossover1() {
        Individual a = new Individual(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        Individual b = new Individual(Arrays.asList(9, 3, 7, 8, 2, 6, 5, 1, 4));
        Individual child = crossover.recombine(a, b, new IntegerPair(3, 7));
        assertEquals(Arrays.asList(9, 3, 2, 4, 5, 6, 7, 1, 8), child.getGenotype());
    }

    @Test
    public void testCrossover2() {
        Individual a = new Individual(Arrays.asList(1, 2, 3, 4, 5, 6, 7));
        Individual b = new Individual(Arrays.asList(5, 4, 6, 7, 2, 1, 3));
        Individual child = crossover.recombine(a, b, new IntegerPair(2, 6));
        assertEquals(Arrays.asList(2, 7, 3, 4, 5, 6, 1), child.getGenotype());
    }

    @Test
    public void testCrossover3() {
        Individual a = new Individual(Arrays.asList(6, 7, 8, 9, 1, 2, 3, 4, 5));
        Individual b = new Individual(Arrays.asList(6, 5, 1, 4, 9, 3, 7, 8, 2));
        Individual child = crossover.recombine(a, b, new IntegerPair(7, 2));
        assertEquals(Arrays.asList(6, 7, 1, 8, 9, 3, 2, 4, 5), child.getGenotype());
    }

    @Test
    public void testCrossover4() {
        Individual a = new Individual(Arrays.asList(6, 7, 8, 9, 1, 2, 3, 4, 5));
        Individual b = new Individual(Arrays.asList(6, 5, 1, 4, 9, 3, 7, 8, 2));
        Individual child = crossover.recombine(a, b, new IntegerPair(3, 3));
        assertEquals(Arrays.asList(6, 5, 1, 4, 9, 3, 7, 8, 2), child.getGenotype());
    }

    @Test
    public void testCrossover5() {
        Individual a = new Individual(Arrays.asList(6, 7, 8, 9, 1, 2, 3, 4, 5));
        Individual b = new Individual(Arrays.asList(6, 5, 1, 4, 9, 3, 7, 8, 2));
        Individual child = crossover.recombine(a, b, new IntegerPair(1,0));
        assertEquals(Arrays.asList(6, 7, 8, 9, 1, 2, 3, 4, 5), child.getGenotype());
    }

    @Test
    public void testCrossover6() {
        Individual a = new Individual(Arrays.asList(6, 7, 8, 9, 1, 2, 3, 4, 5));
        Individual b = new Individual(Arrays.asList(6, 5, 1, 4, 9, 3, 7, 8, 2));
        Individual child = crossover.recombine(a, b, new IntegerPair(3, 3));
        assertEquals(Arrays.asList(6, 5, 1, 4, 9, 3, 7, 8, 2), child.getGenotype());
        child = crossover.recombine(b, a, new IntegerPair(3, 3));
        assertEquals(Arrays.asList(6, 7, 8, 9, 1, 2, 3, 4, 5), child.getGenotype());
    }
}
