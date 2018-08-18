package uni.evocomp.a1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import uni.evocomp.a1.mutate.Mutate;
import uni.evocomp.a1.mutate.Invert;
import uni.evocomp.a1.Population;
import uni.evocomp.a1.TSPProblem;
import uni.evocomp.util.IntegerPair;

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
        List<Individual> populationList = new ArrayList<>();
        populationList.addAll(population.getPopulation());

        while (numGenerations <= 20000) {
            
            for (Individual individual : population.getPopulation()) {
                Individual s0 = new Individual(individual);

                int len = s0.getGenotype().size();
                int index =  ThreadLocalRandom.current().nextInt(0, len);
                int city = s0.getGenotype().get(index);
                List<Integer> visitedCityIndex = new ArrayList<Integer>();
                visitedCityIndex.add(index);
                Mutate mutator = new Invert();

                while (true) {
                    int cDash;
                    if (ThreadLocalRandom.current().nextDouble(1) <= probability) {
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
                    int cityLast = (s0.getGenotype().get(cityIndexLast));
                    int cityNext = (s0.getGenotype().get(cityIndexNext));
                    if ((cDash == cityLast) || (cDash == cityNext)) {
                        break;
                    }
                    mutator.run(problem, s0, new IntegerPair (cityIndexNext, s0.getGenotype().indexOf(cDash)));
                    city = cDash;
                }
                if (s0.getCost(problem) <= individual.getCost(problem)) {
                    individual = s0;
                }
            }
        }
    }
    public int getNumGenerations() {
        return numGenerations;
    }
}
    


