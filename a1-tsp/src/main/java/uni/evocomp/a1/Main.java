//package uni.evocomp.a1;
//
//import java.io.FileInputStream;
//import java.io.FileReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.Reader;
//import java.lang.reflect.Constructor;
//import java.lang.reflect.InvocationTargetException;
//import java.util.Iterator;
//import java.util.Properties;
//import uni.evocomp.util.Util;
//
//public class Main {
//
//  public static TSPProblem problem; // this changes
//  public static final TSPIO io = new TSPIO();
//  public static final String[] testNames = {"eil51", "eil76", "eil101", "kroA100", "kroC100",
//      "kroD100", "lin105", "pcb442", "pr2392", "st70", "usa13509"};
//  public static final String testSuffix = ".tsp";
//  public static final String tourSuffix = ".opt.tour";
//
//  /**
//   *
//   * A typical evolutionary algorithm. Pass in different implementations of each argument to result
//   * in a new algorithm.
//   *
//   * <pre>
//   * INITIALISE population with random candidate solutions;
//   * EVALUATE each candidate;
//   * REPEAT UNTIL (TERMINATION CONDITION is satisfied) DO
//   *   1. SELECT parents;
//   *   2. RECOMBINE pairs of parents;
//   *   3. MUTATE resulting offspring;
//   *   4. EVALUATE new candidates;
//   *   5. SELECT individuals for next generation;.
//   * OD
//   * </pre>
//   *
//   * @param problem an object representing the problem
//   * @param evaluate a class to define how to evaluate the fitness of an <code>Individual</code>
//   * @param selectParents a class to define how to pair parents together
//   * @param recombine a class to define how to recombine parents to produce offspring
//   * @param mutate a class to define how to mutate the resulting offspring
//   * @param selectSurvivors a class to define how to select <code>Individuals</code> for next round
//   * @return the <code>Individual</code> with the best fitness
//   */
//  public static Individual evolutionaryAlgorithm(TSPProblem problem, Evaluate evaluate,
//      SelectParents selectParents, Recombine recombine, Mutate mutate,
//      SelectSurvivors selectSurvivors, int populationSize, int individualSize) {
//
//    Population population = new Population(populationSize, individualSize);
//    // TODO : do something with the fitness
//    for (Iterator<Individual> it = population.getPopulation().iterator(); it.hasNext();) {
//      double fitness = evaluate.evaluate(problem, it.next());
//    }
//
//    // TODO : define terminal condition
//    while (true) {
//      // TODO : select parents from the population
//
//      // TODO : recombine pairs of parents
//
//      // TODO : mutate resultant offspring
//      // mutate.run(individual, List<IntegerPair>);
//
//      // TODO : evaluate resultant candidates
//      // evaluate.evaluate(problem, individual);
//
//      // TODO : select individuals for next generation
//      // selectSurvivors.selectSurvivors(population, problem);
//
//    }
//  }
//
//  // TODO : Benchmark function
//  public static void benchmark(String propertiesFileName, int populationSize, int timesToRun) {
//    // TODO : Read a .properties file to figure out which implementations to use
//    // and instantiate one of each using Evaluate, SelectParents, Recombine, Mutate and
//    // SelectSurvivors
//    Properties prop = new Properties();
//
//    Evaluate evaluate = null;
//    SelectParents selectParents = null;
//    Recombine recombine = null;
//    Mutate mutate = null;
//    SelectSurvivors selectSurvivors = null;
//
//    // Create the objects from the properties file
//    try (InputStream input = new FileInputStream(propertiesFileName)) {
//      prop.load(input);
//
//      evaluate = (Evaluate) Util.classFromName(prop.getProperty("Evaluate", "EvaluateEuclid"));
//      selectParents = (SelectParents) Util.classFromName(prop.getProperty("SelectParents", ""));
//      recombine =
//          (Recombine) Util.classFromName(prop.getProperty("Recombine", "EdgeRecombination"));
//      mutate = (Mutate) Util.classFromName(prop.getProperty("Mutate", "Swap"));
//      selectSurvivors = (SelectSurvivors) Util
//          .classFromName(prop.getProperty("SelectSurvivors", "TournamentSelection"));
//    } catch (Exception ex) {
//      ex.printStackTrace();
//    }
//
//    // TODO : record metrics
//    for (int i = 0; i < timesToRun; i++) {
//      evolutionaryAlgorithm(problem, evaluate, selectParents, recombine, mutate, selectSurvivors,
//          populationSize, problem.getSize());
//    }
//  }
//
//  public static void main(String[] args) {
//    problem = new TSPProblem();
//    try (Reader r = new FileReader("tests/eil51.tsp")) {
//      problem = io.read(r);
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//
//    benchmark("config.properties", 50, 50); // this fails because some implementations DNE
//  }
//}
