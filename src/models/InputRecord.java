package models;

import interfaces.AbstractCollectionInterface;
import interfaces.models.PointInterface;

public class InputRecord {
    public AbstractCollectionInterface<PointInterface> points;
    public Float aspectRatio;
    public PlacementModelEnum placementModel;
}
