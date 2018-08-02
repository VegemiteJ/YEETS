package uni.evocomp.a1;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TSPIO {
  TSPIO() {
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
   * @throws IOException
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
      Map<Integer, Point> points = new HashMap<>();
      List<List<Double>> weights = new ArrayList<>();
      for (int i = 1; (line = br.readLine()) != null || line.trim().equals("EOF"); i++) {
        // Read header
        if (header) {
          String[] split = line.trim().split(" : ");
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
              //						points = new ArrayList<>(dimension);
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
          if (line.equals("EOF")) {
            break;
          }

          String[] split = line.trim().split(" "); // should be {n, x, y}
          if (split.length < 3) {
            throw new IOException("Bad body format on line " + i + ": " + line);
          }
          points.put(
              Integer.parseInt(split[0]),
              new Point(Integer.parseInt(split[1]), Integer.parseInt(split[2])));
        }
      }

      // Finished reading lines, construct weights based on points
      for (int i = 1; i <= dimension; i++) {
        List<Double> temp = new ArrayList<>();
        for (int j = 1; j <= dimension; j++) {
          temp.add(euclideanDistance2D(points.get(i), points.get(j)));
        }
        weights.add(temp);
      }
      return new TSPProblem(name, comment, type, edgeWeightType, weights);
    }
  }

  /**
   * As described by the documentation (elib.zib.de/pub/mp-testdata/tsp/tsplib/doc.ps) section 2.1
   *
   * <pre>
   * Let x[i], y[i] ... be the coordinates of node i.
   * In the 2-dimensional case the distance between two points i and j is computed as follows:
   * xd = x[i] - x[j];
   * yd = y[i] - y[j];
   * dij = nint( sqrt( xd*xd + yd*yd) + 0.5 );
   * </pre>
   *
   * @param i
   * @param j
   * @return the 2D euclidean distance between a and b
   */
  private double euclideanDistance2D(Point i, Point j) {
    double xd = i.getX() - j.getX();
    double yd = i.getY() - j.getY();
    return Math.round(Math.sqrt(xd * xd + yd * yd) + 0.5);
  }

  /**
   * @param r a Reader to a ".opt.tour" file
   * @return an Individual whose genotype is the optimal solution
   * @throws IOException
   * @throws NullPointerException
   */
  public Individual readSolution(Reader r) throws IOException, NullPointerException {
    // Read each line of the reader into a string
    try (BufferedReader br = new BufferedReader(r)) {
      boolean header = true; // 1 for header, 0 for body
      String line = new String();
      String name = new String();
      String comment = new String();
      String type = new String();
      String edgeWeightType = new String();
      int dimension = 0;
      List<Integer> solution = new ArrayList<>();
      for (int i = 1; (line = br.readLine()) != null || line.trim().equals("EOF"); i++) {
        // Read header
        if (header) {
          String[] split = line.trim().split(" : ");
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
              //						points = new ArrayList<>(dimension);
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

          String[] split = line.trim().split(" "); // should be a single entry
          if (split.length != 1) {
            throw new IOException("Bad body format on line " + i + ": " + line);
          }
          solution.add(Integer.parseInt(split[0]));
        }
      }
      return new Individual(solution);
    }
  }

  // TODO : Fill in after we have Instance
  // TODO : Figure out whether we even need this function
  /** @param instance the instance object to write */
  public void write() {
    System.out.println("Writing instance");
  }
}
