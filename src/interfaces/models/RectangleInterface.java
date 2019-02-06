package interfaces.models;

public interface RectangleInterface {
    boolean contains(PointInterface point);
    boolean intersects(RectangleInterface rectangle);

    Integer distanceTo(PointInterface point);
    Integer distanceTo(RectangleInterface rectangle);
    Integer getWidth();
    Integer getHeight();
}
