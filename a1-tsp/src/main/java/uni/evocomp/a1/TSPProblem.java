package uni.evocomp.a1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import uni.evocomp.util.DoublePair;
import uni.evocomp.util.Matrix;

// TODO : See whether we need to have this actually handle the IO, or whether IO
// is separate and gets passed to this.
public class TSPProblem implements Serializable {

  protected static final long serialVersionUID = 1290380191019123112L;

  private String name;
  private String comment;
  private String type; // tour or TSP
  private String edgeWeightType; // For this assignment, will always be EUC_2D, but it can be EUC_3D
  private List<DoublePair> points; // points for all the cities (x, y)
  transient private Matrix weights;

  public TSPProblem() {
    name = "";
    comment = "";
    type = "";
    edgeWeightType = "";
    points = null;
  }

  public TSPProblem(List<List<Double>> weights) {
    this();
    this.weights = new Matrix(weights);
  }

  public TSPProblem(Matrix weights) {
    this();
    this.weights = new Matrix(weights);
  }

  /**
   * Copy constructor for TSPProblem
   *
   * @param src source TSPProblem object to copy construct
   */
  public TSPProblem(TSPProblem src) {
    this.name = new String(src.getName());
    this.comment = new String(src.getComment());
    this.type = new String(src.getType());
    this.edgeWeightType = new String(src.getEdgeWeightType());
    points = new ArrayList<>(src.getPoints());
    this.weights = new Matrix(src.getWeights());
  }

  public TSPProblem(String name, String comment, String type, String edgeWeightType,
      Matrix weights) {
    this.name = name;
    this.comment = comment;
    this.type = type;
    this.edgeWeightType = edgeWeightType;
    this.weights = weights;
  }

  public TSPProblem(String name, String comment, String type, String edgeWeightType,
      List<DoublePair> points, List<List<Double>> weights) {
    this.name = name;
    this.comment = comment;
    this.type = type;
    this.edgeWeightType = edgeWeightType;
    this.points = new ArrayList<>(points);
    this.weights = new Matrix(weights);
  }

  public String getName() {
    return name;
  }

  public String getComment() {
    return comment;
  }

  public String getType() {
    return type;
  }

  public String getEdgeWeightType() {
    return edgeWeightType;
  }

  public Matrix getWeights() {
    return weights;
  }

  /** @return the number of cities/towns/nodes in the problem */
  public int getSize() {
    return weights.size();
  }

  public List<DoublePair> getPoints() {
    return points;
  }
}
