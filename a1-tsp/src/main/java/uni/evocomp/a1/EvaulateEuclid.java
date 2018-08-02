package uni.evocomp.a1;

import java.util.List;

public class EvaulateEuclid implements Evaluate {
    @Override
    public double evaluate(TSPProblem problem, Individual individual) {
        double cost = 0;
        List<List<Double>> weights = problem.getWeights();
        for (int i = 0; i < problem.getSize()-1; i++) {
            cost += weights.get(i).get(i+1);
        }
        return cost;
    }
}
