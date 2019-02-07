package interfaces.models;

public interface LabelInterface {
    String toString();
    PointInterface getPOI();
    Float getAspectRatio();
    void setHeight(Float height) throws IllegalArgumentException;
    RectangleInterface getRectangle();
}
