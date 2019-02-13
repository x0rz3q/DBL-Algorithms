package Parser;

import interfaces.AbstractCollectionInterface;
import interfaces.models.SquareInterface;
import models.PlacementModelEnum;

import java.util.List;

public class DataRecord {

    protected List<CoordinatedPoint> pointsOrig;
    public List<SquareInterface> points;
    public AbstractCollectionInterface labels;
    public Float aspectRatio;
    public PlacementModelEnum placementModel;
    public Float height = 0.0f;

    protected DataRecord () {}

    /**
     * A private
     */
    static class CoordinatedPoint{

        final SquareInterface square;
        final int x;
        final int y;

        CoordinatedPoint (SquareInterface square, int x, int y) {
            this.square = square;
            this.x = x;
            this.y = y;
        }
    }
}
