package interfaces.models;

import models.Point;

public interface PointInterface {
    Float getX();
    Float getY();
    Integer distance(Point p);

    boolean equals(Point p);
}
