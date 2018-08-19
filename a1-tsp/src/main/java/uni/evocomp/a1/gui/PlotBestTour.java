package uni.evocomp.a1.gui;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import uni.evocomp.a1.Global;
import uni.evocomp.a1.Individual;
import uni.evocomp.a1.TSPIO;
import uni.evocomp.a1.TSPProblem;
import uni.evocomp.a1.logging.BenchmarkStatsTracker;
import uni.evocomp.util.DoublePair;

public class PlotBestTour extends Application {

  public static String loadName = "usa13509_Jump";
  public static boolean plotProvidedBestTour = false;

  public static void main(String[] args) {
    if (args.length > 0) {
      loadName = args[0];
    }
    if (args.length > 1) {
      plotProvidedBestTour = true;
    }
    launch(args);
  }

  private List<Circle> plotCities(
      List<DoublePair> cities, Integer initialCity, int xSize, int ySize, int xOff, int yOff) {
    double maxX =
        cities
            .stream()
            .map(city -> city.first)
            .mapToDouble(v -> v)
            .max()
            .orElseThrow(NoSuchElementException::new);
    double maxY =
        cities
            .stream()
            .map(city -> city.second)
            .mapToDouble(v -> v)
            .max()
            .orElseThrow(NoSuchElementException::new);
    List<Circle> positions = new ArrayList<>();
    for (int i = 0; i < cities.size(); i++) {
      DoublePair city = cities.get(i);
      double newX = city.first * (xSize / maxX) + xOff;
      double newY = city.second * (ySize / maxY) + yOff;
      Circle c = new Circle(newX, newY, 5.0);
      if (i == initialCity - 1) {
        c.setFill(Color.RED);
      }
      positions.add(c);
    }
    return positions;
  }

  private List<Line> plotTour(List<Integer> genotype, List<Circle> locs) {
    List<Line> lines = new ArrayList<>();
    for (int i = 0; i < genotype.size() - 1; i++) {
      int next = i + 1;
      int cityI = genotype.get(i) - 1;
      int cityJ = genotype.get(next) - 1;

      double startX = locs.get(cityI).getCenterX();
      double startY = locs.get(cityI).getCenterY();
      double endX = locs.get(cityJ).getCenterX();
      double endY = locs.get(cityJ).getCenterY();

      Line l = new Line(startX, startY, endX, endY);
      l.setStroke(Color.BLUE);
      lines.add(l);
    }
    double startX = locs.get(genotype.get(0) - 1).getCenterX();
    double startY = locs.get(genotype.get(0) - 1).getCenterY();
    double endX = locs.get(genotype.get(genotype.size() - 1) - 1).getCenterX();
    double endY = locs.get(genotype.get(genotype.size() - 1) - 1).getCenterY();
    Line l = new Line(startX, startY, endX, endY);
    l.setStroke(Color.RED);
    lines.add(l);

    return lines;
  }

  @Override
  public void start(Stage stage) {
    BenchmarkStatsTracker bst = null;
    try {
      bst = BenchmarkStatsTracker.deserialise(loadName);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    TSPProblem problem = null;
    TSPIO io = new TSPIO();;
    FileReader fr1 = null;
    try {
      fr1 = new FileReader("tests/usa13509.tsp");
      problem = io.read(fr1);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

//    TSPProblem problem = bst.getProblem();
    Individual providedBest = bst.getProvidedBestTour();
    Individual bestFoundBySearch = bst.getBestTourFound();

    Individual plotting = providedBest;
    if (!plotProvidedBestTour) {
      plotting = bestFoundBySearch;
    }

    if (providedBest != null) {
      System.out.println("Best Provided Tour Cost: " + providedBest.getCost(problem));
    }
    System.out.println("Best Tour Found Cost: " + bestFoundBySearch.getCost(problem));
    List<Integer> genotype = plotting.getGenotype();
    List<DoublePair> cities = problem.getPoints();
    Group box = new Group();

    List<Circle> cityPoints = plotCities(cities, genotype.get(0), 1200, 800, 100, 50);
    for (Circle c : cityPoints) {
      box.getChildren().add(c);
    }
    List<Line> lines = plotTour(genotype, cityPoints);
    for (Line l : lines) {
      box.getChildren().add(l);
    }

    final Scene scene = new Scene(box, 1400, 900);
    scene.setFill(null);

    stage.setScene(scene);
    stage.show();
  }
}
