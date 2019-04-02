



public class EuclideanDistance extends AbstractDistance {
    @Override
    public double calculate(PointInterface p1, PointInterface p2) {
        return p1.getDistanceTo(p2);
    }
}
