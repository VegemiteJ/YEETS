package uni.evocomp.a1;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class Main {

  public static void main(String[] args) {
    TSPProblem problem = new TSPProblem();
    TSPIO io = new TSPIO();
    try (Reader r = new FileReader("tests/eil51.tsp")) {
      problem = io.read(r);
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println(
        "Problem \"" + problem.getName() + "\" has " + problem.getSize() + " cities");
  }
}
