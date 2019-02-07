package interfaces.models;

public interface PointInterface {
    Float getX();
    Float getY();
    Integer distance(PointInterface p);
    Boolean equals(PointInterface p);
}
