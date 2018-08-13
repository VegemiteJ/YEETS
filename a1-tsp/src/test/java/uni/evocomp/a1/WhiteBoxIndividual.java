package uni.evocomp.a1;

import java.util.List;

/**
 * Used purely for testing, hence it's location in test.*.
 * Exposes protected data members for white box testing.
 *
 * */
public class WhiteBoxIndividual extends Individual {

  public WhiteBoxIndividual() {
    super();
  }

  /**
   * Copy constructor for WhiteBoxIndividual
   *
   * @param src source WhiteBoxIndividual object to copy construct
   */
  public WhiteBoxIndividual(WhiteBoxIndividual src) {
    super(src);
  }

  /**
   * @param n initialise to have a tour of n cities
   */
  public WhiteBoxIndividual(int n) {
    super(n);
  }

  /**
   * @param n initialise to have a tour of n cities
   * @param problem problem to evaluate initial cost against
   */
  public WhiteBoxIndividual(int n, TSPProblem problem) {
    super(n, problem);
  }

  /**
   * @param genotype initial tour for the WhiteBoxIndividual
   * @param initialCost Initial cost of tour
   */
  public WhiteBoxIndividual(List<Integer> genotype, Double initialCost) {
    super(genotype, initialCost);
  }

  /**
   * @param genotype initial tour for the WhiteBoxIndividual
   */
  public WhiteBoxIndividual(List<Integer> genotype) {
    super(genotype);
  }

  /**
   * @param genotype initial tour for the WhiteBoxIndividual
   * @param problem problem to evaluate initial cost against
   */
  public WhiteBoxIndividual(List<Integer> genotype, TSPProblem problem) {
    super(genotype, problem);
  }
  
  public boolean getDirty() {
    return this.dirty;
  }
}
