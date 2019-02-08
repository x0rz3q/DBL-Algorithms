package interfaces.models;

public interface LabelInterface extends RectangleInterface {
    String toString();
    PointInterface getPOI();
    Float getAspectRatio();
    void setHeight(Float height);
    void setWidth(Float width);
    void setSize(Float height, Float width) throws IllegalArgumentException;
}
