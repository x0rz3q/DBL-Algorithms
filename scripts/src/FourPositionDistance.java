



import static java.lang.Math.*;

public class FourPositionDistance extends AbstractDistance {
    private double aspectRatio;

    @Override
    public double calculate(PointInterface p1, PointInterface p2) {
        double h = abs(p2.getY() - p1.getY());
        double w = abs(p2.getX() - p1.getX()) / aspectRatio;
        return max(h, w);
    }

    /**
     * Set aspect ratio
     * @param newAspectRatio
     */
    public void setAspectRatio(double newAspectRatio) {
        aspectRatio = newAspectRatio;
    }
}
