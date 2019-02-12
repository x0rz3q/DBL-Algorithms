package models;

import interfaces.AbstractCollectionInterface;
import interfaces.models.LabelInterface;
import interfaces.models.SquareInterface;

import java.util.List;

public class DataRecord {
    public List<SquareInterface> points;
    public AbstractCollectionInterface labels;
    public Float aspectRatio;
    public PlacementModelEnum placementModel;
    public Float height = 0.0f;
}
