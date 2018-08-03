package uni.evocomp.a1;

import java.util.List;

public class EvaluateEuclid implements Evaluate {
    @Override
    public double evaluate(TSPProblem problem, Individual individual) {
        double cost = 0;
        List<List<Double>> weights = problem.getWeights();
        List<Integer> genotype = individual.getGenotype();
        for (int i = 0; i < genotype.size()-1; i++) {
            cost += weights.get(genotype.get(i)-1).get(genotype.get(i+1)-1);
        }
        return cost;
    }
}
