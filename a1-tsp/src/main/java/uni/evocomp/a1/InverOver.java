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
import uni.evocomp.a1.recombine.Recombine;
import uni.evocomp.a1.selectparents.SelectParents;
import uni.evocomp.a1.selectsurvivors.SelectSurvivors;
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

random initialization of the population P
while (not satied termination-condition) do {
    for each individual Si 2 P do {
        S0 = Si
        select (randomly) a city c from S0
        repeat {
            if (rand()  p) {
                select the city c0 from the remaining cities in S0
            }
            else { 
                select (randomly) an individual from P
                assign to c0 the 'next' city to the city c in the selected individual
            }
            if (the next city or the previous city of city c in S0 is c0) {
                exit from repeat loop
                inverse the section from the next city of city c to the city c0 in S0
                c = c0
            }
            if (eval(S0)  eval(Si)) {
            Si = S0
            }
        }
    }
 }