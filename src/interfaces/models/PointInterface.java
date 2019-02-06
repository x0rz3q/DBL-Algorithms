package interfaces.models;

import models.Point;

public interface PointInterface extends RectangleInterface {
    Integer getX();
    Integer getY();
    Integer distance(Point p);
    boolean equals(Point p);
}
