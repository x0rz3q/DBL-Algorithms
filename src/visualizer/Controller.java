package visualizer;


import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.Rectangle;

import java.util.Map;

public class Controller {
    @FXML
    private Pane mainPanel;
    private Record record;

    public void initialize() {
        mainPanel.setBackground(new Background(new BackgroundFill(Color.web("#000"), CornerRadii.EMPTY, Insets.EMPTY)));
        this.record = Record.parse();
    }

    public void setTitle(Stage stage) {
        stage.setTitle("Placed " + this.record.actualPointCount + "/" + this.record.pointCount + " intersected " + this.record.intersections);
    }

    public void redraw() {
        this.mainPanel.getChildren().clear();
        double scaleFactor = (Math.min(this.mainPanel.getHeight(), this.mainPanel.getWidth())) / this.record.extent;

        for (Map.Entry<Rectangle, Boolean> entry : this.record.rectangles.entrySet()) {
            Rectangle rectangle = entry.getKey();

            javafx.scene.shape.Rectangle shape = new javafx.scene.shape.Rectangle();
            shape.setLayoutY(this.mainPanel.getHeight() - (rectangle.getYMin() + Math.abs(this.record.shiftY) + rectangle.getHeight()) * scaleFactor);
            shape.setLayoutX(rectangle.getXMin() * scaleFactor);
            shape.setWidth(rectangle.getWidth() * scaleFactor);
            shape.setHeight(rectangle.getHeight() * scaleFactor);

            if(entry.getValue()) {
                shape.setStroke(Color.RED);
            } else {
                shape.setStroke(Color.WHITE);
            }

            shape.setFill(Color.TRANSPARENT);

            this.mainPanel.getChildren().add(shape);
        }
    }
}
