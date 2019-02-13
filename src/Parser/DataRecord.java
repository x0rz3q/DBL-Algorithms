package Parser;

import interfaces.AbstractCollectionInterface;
import interfaces.models.LabelInterface;
import models.PlacementModelEnum;

import java.util.List;

public class DataRecord {
    public List<LabelInterface> points;
    public AbstractCollectionInterface collection;
    protected Float aspectRatio;
    public PlacementModelEnum placementModel;
    public Double height = 0.0d;
}
