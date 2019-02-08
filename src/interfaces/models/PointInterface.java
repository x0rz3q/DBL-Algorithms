package interfaces.models;

public interface PointInterface {
    Float getX();
    Float getY();
    Float distance(PointInterface p);
    Boolean equals(PointInterface p);
}
