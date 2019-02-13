package Parser;

import interfaces.AbstractCollectionInterface;
import interfaces.models.SquareInterface;
import models.PlacementModelEnum;
import models.Point;

import java.util.List;

public class DataRecord {

    protected List<CoordinatedPoint> pointsOrig;
    public List<Point> points;
    public AbstractCollectionInterface collection;
    public Float aspectRatio;
    public PlacementModelEnum placementModel;
    public Float height = 0.0f;

    protected DataRecord () {}


    static class CoordinatedPoint{

        final Point point;
        final int x;
        final int y;

        CoordinatedPoint (Point point, int x, int y) {
            this.point = point;
            this.x = x;
            this.y = y;
        }
    }
}
