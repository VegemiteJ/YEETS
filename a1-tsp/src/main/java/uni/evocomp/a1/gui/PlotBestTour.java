package uni.evocomp.a1.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import uni.evocomp.util.DoublePair;

public class PlotBestTour extends Application {
  public static void main(String[] args) {
    launch(args);
  }

  private void plotCities(List<DoublePair> cities) {
    of r
  }

  @Override
  public void start(Stage stage) {
    VBox box = new VBox();
    final Scene scene = new Scene(box,1400, 900);
    scene.setFill(null);

    Line line = new Line();
    line.setStartX(0.0f);
    line.setStartY(0.0f);
    line.setEndX(100.0f);
    line.setEndY(100.0f);

    box.getChildren().add(line);

    stage.setScene(scene);
    stage.show();
  }
}
