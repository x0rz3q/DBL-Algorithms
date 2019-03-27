package Parser;

import interfaces.AbstractCollectionInterface;
import interfaces.models.LabelInterface;
import models.PlacementModelEnum;

import java.util.List;

public class DataRecord {
    public List<LabelInterface> labels;
    public AbstractCollectionInterface collection;
    public double aspectRatio;
    public String aspectRatioString;
    public PlacementModelEnum placementModel;
    public double height = 0.0d;
}
