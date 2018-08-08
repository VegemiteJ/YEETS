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
import uni.evocomp.util.DoublePair;

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
      int cityI = genotype.get(i);
      int cityJ = genotype.get(next);

      double startX = locs.get(cityI).getCenterX();
      double startY = locs.get(cityI).getCenterY();
      double endX = locs.get(cityJ).getCenterX();
      double endY = locs.get(cityJ).getCenterY();

      Line l = new Line(startX, startY, endX, endY);
      l.setStroke(Color.BLUE);
      lines.add(l);
    }
    double startX = locs.get(genotype.get(0)).getCenterX();
    double startY = locs.get(genotype.get(0)).getCenterY();
    double endX = locs.get(genotype.get(genotype.size()-1)).getCenterX();
    double endY = locs.get(genotype.get(genotype.size()-1)).getCenterY();
    Line l = new Line(startX, startY, endX, endY);
    l.setStroke(Color.RED);
    lines.add(l);

    return lines;
  }

  @Override
  public void start(Stage stage) {
    Group box = new Group();

    List<DoublePair> cities = new ArrayList<>(Arrays.asList(new DoublePair(300.0,250.0),new DoublePair(250.0,350.0),new DoublePair(100.0,550.0),new DoublePair(600.0,550.0)));
    List<Integer> genotype = new ArrayList<>(Arrays.asList(0,1,2,3));
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
