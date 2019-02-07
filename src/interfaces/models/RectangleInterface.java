package interfaces.models;

public interface RectangleInterface {
    Boolean contains(PointInterface point);
    Boolean intersects(RectangleInterface rectangle);
    Boolean equals(RectangleInterface rectangle);
    Float getHeight();
    Float getWidth();
    Float getXMin();
    Float getXMax();
    Float getYMin();
    Float getYMax();
    PointInterface getBottomLeft();
    PointInterface getTopLeft();
    PointInterface getBottomRight();
    PointInterface getTopRight();
    PointInterface getCenter();
}
