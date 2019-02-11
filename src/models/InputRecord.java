package models;

import interfaces.AbstractCollectionInterface;
import interfaces.models.SquareInterface;

public class InputRecord {
    public AbstractCollectionInterface<SquareInterface> points;
    public Float aspectRatio;
    public PlacementModelEnum placementModel;
}
