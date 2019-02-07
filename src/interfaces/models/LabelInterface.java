package interfaces.models;

import models.Point;

public interface LabelInterface extends RectangleInterface {
    String toString();
    Point getPOI();
    Float getAspectRatio();
    void setHeight(Float height) throws IllegalArgumentException;
}
