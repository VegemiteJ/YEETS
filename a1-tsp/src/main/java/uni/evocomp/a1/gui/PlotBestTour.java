package uni.evocomp.a1.gui;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import uni.evocomp.a1.Individual;
import uni.evocomp.a1.TSPIO;
import uni.evocomp.a1.TSPProblem;
import uni.evocomp.a1.evaluate.Evaluate;
import uni.evocomp.a1.evaluate.EvaluateEuclid;
import uni.evocomp.util.DoublePair;
import uni.evocomp.util.Pair;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

public class PlotBestTour extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  private List<Circle> plotCities(List<DoublePair> cities, int xSize, int ySize, int xOff, int yOff) {
    double maxX = cities.stream().map(city -> city.first).mapToDouble(v -> v).max().orElseThrow(NoSuchElementException::new);
    double maxY = cities.stream().map(city -> city.second).mapToDouble(v -> v).max().orElseThrow(NoSuchElementException::new);
    List<Circle> positions = new ArrayList<>();
    for (DoublePair city : cities) {
      double newX = city.first * (xSize / maxX) + xOff;
      double newY = city.second * (ySize / maxY) + yOff;
      Circle c = new Circle(newX, newY, 5.0);
      positions.add(c);
    }
    return positions;
  }

  private List<Line> plotTour(List<Integer> genotype, List<Circle> locs) {
    List<Line> lines = new ArrayList<>();
    for (int i=0; i<genotype.size()-1; i++) {
      int next = i+1;
      int cityI = genotype.get(i)-1;
      int cityJ = genotype.get(next)-1;

      double startX = locs.get(cityI).getCenterX();
      double startY = locs.get(cityI).getCenterY();
      double endX = locs.get(cityJ).getCenterX();
      double endY = locs.get(cityJ).getCenterY();

      Line l = new Line(startX, startY, endX, endY);
      l.setStroke(Color.BLUE);
      lines.add(l);
    }
    double startX = locs.get(genotype.get(0)-1).getCenterX();
    double startY = locs.get(genotype.get(0)-1).getCenterY();
    double endX = locs.get(genotype.get(genotype.size()-1)-1).getCenterX();
    double endY = locs.get(genotype.get(genotype.size()-1)-1).getCenterY();
    Line l = new Line(startX, startY, endX, endY);
    l.setStroke(Color.RED);
    lines.add(l);

    return lines;
  }

  @Override
  public void start(Stage stage) {
    final String[] testNames = {
            "tests/eil51"};
//            "tests/eil76",
//            "tests/eil101",
//            "tests/kroA100",
//            "tests/kroC100",
//            "tests/kroD100",
//            "tests/lin105",
//            "tests/pcb442",
//            "tests/pr2392",
//            "tests/usa13509"};
    final String testSuffix = ".tsp";
    final String tourSuffix = ".opt.tour";

    Individual best = null;
    try {
      best = Individual.deserialise();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

    Evaluate evaluator = new EvaluateEuclid();
    TSPIO io = new TSPIO();
    ArrayList<Pair<TSPProblem, Individual>> benchmarks = new ArrayList<>();
    for (String testString : testNames) {
      TSPProblem problem = null;
      try (FileReader fr1 = new FileReader(testString + testSuffix);
           FileReader fr2 = new FileReader(testString + tourSuffix)) {
        problem = io.read(fr1);
        Individual solution = io.readSolution(fr2);
        solution.setCost(evaluator.evaluate(problem, solution));
        benchmarks.add(new Pair<>(problem, solution));
      } catch (IOException e) {
//        e.printStackTrace();
        benchmarks.add(new Pair<>(problem, null));
      }
    }

    List<DoublePair> cities = benchmarks.get(0).first.getPoints();

    Group box = new Group();
    List<Integer> genotype = best.getGenotype();
    List<Circle> cityPoints = plotCities(cities, 1200, 800, 100, 50);
    for (Circle c : cityPoints) {
      box.getChildren().add(c);
    }
    List<Line> lines = plotTour(genotype, cityPoints);
    for (Line l : lines) {
      box.getChildren().add(l);
    }

    final Scene scene = new Scene(box,1400, 900);
    scene.setFill(null);

    stage.setScene(scene);
    stage.show();
  }
}
