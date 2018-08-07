package uni.evocomp.a1.evaluate;

import uni.evocomp.a1.Individual;
import uni.evocomp.a1.TSPProblem;

public interface Evaluate {
  public double evaluate(TSPProblem problem, Individual individual);
}
