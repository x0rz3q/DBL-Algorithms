package models;

import interfaces.AbstractCollectionInterface;
import interfaces.models.LabelInterface;

public class OutputRecord {
    public AbstractCollectionInterface<LabelInterface> points;
    public Float aspectRatio;
    public PlacementModelEnum placementModel;
    public Float height;
}
