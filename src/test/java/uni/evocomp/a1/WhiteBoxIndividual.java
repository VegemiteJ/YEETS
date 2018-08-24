package uni.evocomp.a1;

import java.util.List;

/**
 * Used purely for testing, hence it's location in test.*.
 * <p>
 * Exposes protected data members for white box testing.
 *
 */
public class WhiteBoxIndividual extends Individual {

  /**
   * 
   */
  private static final long serialVersionUID = -2859327363229532819L;

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
   * @param problem problem to evaluate initial cost against
   */
  public WhiteBoxIndividual(TSPProblem problem) {
    super(problem);
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
