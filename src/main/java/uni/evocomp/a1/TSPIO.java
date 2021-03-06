package uni.evocomp.a1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import uni.evocomp.util.DoublePair;
import uni.evocomp.util.Util;

public class TSPIO {

  public TSPIO() {
    ;
  }

  /**
   *
   *
   * <pre>
   * NAME : eil51
   * COMMENT : 51-city problem (Christofides/Eilon)
   * TYPE : TSP
   * DIMENSION : 51
   * EDGE_WEIGHT_TYPE : EUC_2D
   * NODE_COORD_SECTION
   * 1 37 52
   * ...
   * EOF
   * </pre>
   *
   * @param r the TSP problem file to read
   * @return a TSPProblem object with the weights initialised
   */
  public TSPProblem read(Reader r) throws IOException, NullPointerException {
    // Read each line of the reader into a string
    try (BufferedReader br = new BufferedReader(r)) {
      boolean header = true; // 1 for header, 0 for body
      String line = new String();
      String name = new String();
      String comment = new String();
      String type = new String();
      String edgeWeightType = new String();
      int dimension = 0;
      int cityCounter = 0; // used to break early of numCities > dimension
      Map<Integer, DoublePair> points = new HashMap<>();
      List<List<Double>> weights = new ArrayList<>();

      for (int i = 1; (line = br.readLine()) != null; i++) {
        line = line.trim();
        if (line.equals("EOF")) {
          break;
        }
        if (line.isEmpty()) {
          continue;
        }
        // Read header
        if (header) {
          String[] split = line.trim().split("(\\s*):(\\s*)");
          switch (split[0]) {
            case "NAME":
              if (split.length < 2) {
                throw new IOException("Bad " + split[0] + " format on line " + i + ": " + line);
              }
              name = split[1];
              break;
            case "COMMENT":
              if (split.length < 2) {
                throw new IOException("Bad " + split[0] + " format on line " + i + ": " + line);
              }
              comment = split[1];
              break;
            case "TYPE":
              if (split.length < 2) {
                throw new IOException("Bad " + split[0] + " format on line " + i + ": " + line);
              }
              type = split[1];
              break;
            case "DIMENSION":
              if (split.length < 2) {
                throw new IOException("Bad " + split[0] + " format on line " + i + ": " + line);
              }
              dimension = Integer.parseInt(split[1]);
              break;
            case "EDGE_WEIGHT_TYPE":
              if (split.length < 2) {
                throw new IOException("Bad " + split[0] + " format on line " + i + ": " + line);
              }
              edgeWeightType = split[1];
              break;
            case "NODE_COORD_SECTION":
              header = false;
              break;
            case "EOF":
              break;
            default:
              break;
          }
        } else // read body
        {
          String[] split = line.trim().split("(\\s)+"); // should be {n, x, y}
          if (split.length < 3) {
            throw new IOException("Bad body format on line " + i + ": " + line);
          }
          Integer cityIdx = Integer.parseInt(split[0]);
          Double xCoord = Double.valueOf(split[1]);
          Double yCoord = Double.valueOf(split[2]);
          if (cityIdx <= 0) {
            throw new IOException("Bad body format (City Idx) on line " + i + ": " + line);
          }
          points.put(cityIdx, new DoublePair(xCoord, yCoord));
          cityCounter++;
          if (cityCounter > dimension) {
            break;
          }
        }
      }
      
      // Store the coordinates of the cities in the tour
      // city 1 at coords[0], city n at coords[n-1]
      List<DoublePair> coords = new ArrayList<>();

      // Finished reading lines, construct weights based on points
      for (int i = 1; i <= dimension; i++) {
        coords.add(points.get(i));
        List<Double> temp = new ArrayList<>();
        for (int j = 1; j <= dimension; j++) {
          Double distance = Util.euclideanDistance2D(points.get(i), points.get(j));
          temp.add(distance);
        }
        weights.add(temp);
      }
      return new TSPProblem(name, comment, type, edgeWeightType, coords, weights);
    }
  }

  /**
   * @param r a Reader to a ".opt.tour" file
   * @return an Individual whose genotype is the optimal solution
   */
  public Individual readSolution(Reader r) throws IOException, NullPointerException {
    // Read each line of the reader into a string
    try (BufferedReader br = new BufferedReader(r)) {
      boolean header = true; // 1 for header, 0 for body
      String line = new String();
      @SuppressWarnings("unused")
      String name = new String();
      @SuppressWarnings("unused")
      String comment = new String();
      @SuppressWarnings("unused")
      String type = new String();
      @SuppressWarnings("unused")
      int dimension = 0;
      List<Integer> solution = new ArrayList<>();

      for (int i = 1; (line = br.readLine()) != null; i++) {
        line = line.trim();
        if (line.equals("EOF") || line.contains("-1")) {
          break;
        }
        if (line.isEmpty()) {
          continue;
        }
        // Read header
        if (header) {
          String[] split = line.trim().split("(\\s*):(\\s*)");
          switch (split[0]) {
            case "NAME":
              if (split.length < 2) {
                throw new IOException("Bad " + split[0] + " format on line " + i + ": " + line);
              }
              name = split[1];
              break;
            case "COMMENT":
              if (split.length < 2) {
                throw new IOException("Bad " + split[0] + " format on line " + i + ": " + line);
              }
              comment = split[1];
              break;
            case "TYPE":
              if (split.length < 2) {
                throw new IOException("Bad " + split[0] + " format on line " + i + ": " + line);
              }
              type = split[1];
              break;
            case "DIMENSION":
              if (split.length < 2) {
                throw new IOException("Bad " + split[0] + " format on line " + i + ": " + line);
              }
              dimension = Integer.parseInt(split[1]);
              break;
            case "TOUR_SECTION":
              header = false;
              break;
            case "EOF":
              break;
            default:
              break;
          }
        } else // read body
        {
          if (line.equals("EOF")) {
            break;
          }

          String[] split = line.trim().split("(\\s)+"); // should be a single entry
          if (split.length != 1) {
            throw new IOException("Bad body format on line " + i + ": " + line);
          }
          Integer cityIdx = Integer.parseInt(split[0]);
          if (cityIdx <= 0) {
            throw new IOException("Bad body format (Negative City Index) on line " + i + ": " + line);
          }
          solution.add(cityIdx);
        }
      }
      return new Individual(solution);
    }
  }

  // TODO : Fill in after we have Instance
  // TODO : Figure out whether we even need this function
  /**
   * Writes an <code>Instance</code> into a writer.
   * 
   * Passing a writer (as opposed to just directly writing to a file) makes it easier to test, as a
   * StringWriter can be passed in.
   * 
   * @param instance instance to write
   * @param w writer into which the instance is written
   */
  public void write(Individual instance, Writer w) {
    System.out.println("Writing instance");
  }
}
