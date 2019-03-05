package visualizer;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import models.Rectangle;

public class Controller {
    @FXML
    private Pane mainPanel;
    private Record record;

    public void initialize() {
        mainPanel.setBackground(new Background(new BackgroundFill(Color.web("#000"), CornerRadii.EMPTY, Insets.EMPTY)));
        this.record = new Record(System.in);
    }

    public void setTitle(Stage stage) {
        stage.setTitle("Placed " + this.record.getLabels().size() + "/" + this.record.getPointCount());
    }

    public void redraw() {
        this.mainPanel.getChildren().clear();
        double scaleFactor = (Math.min(this.mainPanel.getHeight(), this.mainPanel.getWidth())) / this.record.getExtent();

        this.record.getLabels().forEach((key, value) -> {
            Rectangle rectangle = key.getRectangle();

            javafx.scene.shape.Rectangle shape = new javafx.scene.shape.Rectangle();
            shape.setLayoutY(this.mainPanel.getHeight() - (rectangle.getYMin() - Math.abs(this.record.getyMin()) + rectangle.getHeight()) * scaleFactor);
            shape.setLayoutX((rectangle.getXMin() - Math.abs(this.record.getxMin())) * scaleFactor);
            shape.setWidth(rectangle.getWidth() * scaleFactor);
            shape.setHeight(rectangle.getHeight() * scaleFactor);

            if (value.size() == 0) {
                shape.setStroke(Color.WHITE);
            } else {
                shape.setStroke(Color.RED);
            }

            shape.setFill(Color.TRANSPARENT);

            javafx.scene.shape.Circle circle = new Circle();
            circle.setCenterX((key.getPOI().getX() - Math.abs(this.record.getxMin())) * scaleFactor);
            circle.setCenterY(this.mainPanel.getHeight() - (key.getPOI().getY() - Math.abs(this.record.getyMin())) * scaleFactor);
            circle.setRadius(1);
            circle.setFill(Color.GREEN);

            this.mainPanel.getChildren().add(shape);
            this.mainPanel.getChildren().add(circle);
        });
    }
}
