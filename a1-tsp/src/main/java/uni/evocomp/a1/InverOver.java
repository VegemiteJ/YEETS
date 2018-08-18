package uni.evocomp.a1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import uni.evocomp.a1.evaluate.Evaluate;
import uni.evocomp.a1.logging.BenchmarkStatsTracker;
import uni.evocomp.a1.mutate.Mutate;
import uni.evocomp.a1.mutate.Invert;
import uni.evocomp.a1.recombine.Recombine;
import uni.evocomp.a1.selectparents.SelectParents;
import uni.evocomp.a1.selectsurvivors.SelectSurvivors;
import uni.evocomp.a1.Population;
import uni.evocomp.util.Pair;

/**
 * Class for InverOver
 * 
 * Able to ___
 * 
 * @author Nehal
 *
 */

public class InverOver {

    Integer numGenerations; 

    public int population(TSPProblem problem, int populationSize, int maxGenerations, double probability) {
        Population population = new Population(problem, populationSize);
        numGenerations = 1;
        List<Individual> populationList;
        populationList.addAll(population.getPopulation());

        while (numGenerations <= 20000) do {
            
            for (Individual individual : population.getPopulation()) do {
                Individual s0 = Individual(individual);

                int len = s0.getGenotype().size();
                int index =  ThreadLocalRandom.current().nextInt(0, len);
                int city = s0.getGenotype().get(index);
                List<Integer> visitedCityIndex = new ArrayList<Integer>();
                visitedCityIndex.add(index);
                Mutate mutator = new Invert();

                while (true) {
                    int cDash;
                    if (ThreadLocalRandom.current().nextDouble(1) <= probability) {
                        int indexNext;
                        do {
                            indexNext = ThreadLocalRandom.current().nextInt(0, len);
                        } while (visitedCityIndex.contains(indexNext)) ;
                        cDash = s0.getGenotype().get(indexNext);    
                    }
                    else { 
                        int iDashIndex = ThreadLocalRandom.current().nextInt(0, populationSize);
                        Individual iDash = populationList.get(iDashIndex); 
                        
                        cDash = iDash.getGenotype().get((iDash.getGenotype().indexOf(city) + 1 ) % len);
                    }
                    int cityIndexLast = (s0.getGenotype().indexOf(cDash) - 1 + len) % len;
                    int cityIndexNext = (s0.getGenotype().indexOf(cDash) + 1) % len;
                    int cityLast = (s0.getGenotype().get(cityIndexLast);
                    int cityNext = (s0.getGenotype().get(cityIndexNext);
                    if ((cDash == cityLast) || (cDash == cityNext)) {
                        break;
                    }
                    mutator.run(problem, s0, new IntegerPair (cityIndexNext, s0.getGenotype().indexOf(cDash)));
                    city = cDash;
                }
                if (s0.getCost(problem) <= individual.getCost(problem)) {
                    individual = s0;
                }
            }
        }
    }
    public int getNumGenerations() {
        return numGenerations;
    }
}
    


