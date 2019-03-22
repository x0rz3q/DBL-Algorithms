package distance;

import Parser.Pair;
import interfaces.models.PointInterface;

import static java.lang.Math.abs;
import static java.lang.Math.max;

public class TwoPositionDistance extends AbstractDistance{

    private double aspectRatio;

    @Override
    public double calculate(PointInterface p1, PointInterface p2) {
        double h = abs(p2.getY() - p1.getY());
        double w = abs(p2.getX() - p1.getX()) / aspectRatio;
        return max(h, w);
    }

    public Pair<Double, Boolean> calculateAndIsWidth(PointInterface p1, PointInterface p2) {
        double h = abs(p2.getY() - p1.getY());
        double w = abs(p2.getX() - p1.getX()) / aspectRatio;
        if (h > w) {
            return new Pair<>(h, false);
        }
        return new Pair<>(w, true);
    }


    /**
     * Set aspect ratio
     * @param newAspectRatio
     */
    public void setAspectRatio(double newAspectRatio) {
        aspectRatio = newAspectRatio;
    }
}
