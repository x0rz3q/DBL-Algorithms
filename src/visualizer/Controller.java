package visualizer;


import interfaces.models.LabelInterface;
import interfaces.models.SquareInterface;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;
import models.PositionLabel;
import org.w3c.dom.css.Rect;

import java.util.Collection;
import java.util.HashMap;
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
        stage.setTitle("Placed " + this.record.actualPointCount + "/" + this.record.pointCount);
    }

    public void redraw() {
        this.mainPanel.getChildren().clear();
        double scaleFactor = (this.mainPanel.getHeight()) / this.record.extent;

        for (Map.Entry<LabelInterface, Collection<SquareInterface>> value : this.record.intersects.entrySet()) {
            LabelInterface label = value.getKey();
            Rectangle rectangle = new Rectangle();
            rectangle.setLayoutX(label.getXMin() * scaleFactor);
            rectangle.setLayoutY((this.mainPanel.getHeight() - label.getYMin() * scaleFactor) - this.record.height * scaleFactor);
            rectangle.setHeight(this.record.height * scaleFactor);
            rectangle.setWidth(this.record.height * scaleFactor * this.record.aspectRatio);

            mainPanel.getChildren().add(rectangle);
            rectangle.setStroke(Color.WHITE);
        }
    }
}
