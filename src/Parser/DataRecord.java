package Parser;

import interfaces.AbstractCollectionInterface;
import interfaces.models.LabelInterface;
import models.PlacementModelEnum;

import java.util.List;

public class DataRecord {
    public List<LabelInterface> labels;
    public AbstractCollectionInterface collection;
    public Float aspectRatio;
    public PlacementModelEnum placementModel;
    public Double height = 0.0d;
}
