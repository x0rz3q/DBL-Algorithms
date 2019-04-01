package Parser;

public class TestDataRecord extends DataRecord {

    public double optHeight, reqHeight;

    TestDataRecord (DataRecord dataRecord, double optHeight, double reqHeight) {
        this.labels = dataRecord.labels;
        this.collection = dataRecord.collection;
        this.aspectRatio = dataRecord.aspectRatio;
        this.placementModel = dataRecord.placementModel;
        this.height = dataRecord.height;
        this.optHeight = optHeight;
        this.reqHeight = reqHeight;
        this.xMin = dataRecord.xMin;
        this.yMin = dataRecord.yMin;
        this.yMax = dataRecord.yMax;
        this.xMax = dataRecord.xMax;
    }
}
