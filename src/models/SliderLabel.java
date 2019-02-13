package models;

public class SliderLabel extends AbstractLabel {
    protected double shift;

    public SliderLabel(double x, double y, double size, double shift, int ID) {
        super(x, y, size, ID);
        this.setEdgeLength(size, shift);
    }

    public void setEdgeLength(double edgeLength, double shift) throws IllegalArgumentException {
        if (edgeLength < 0) {
            throw new IllegalArgumentException("SliderLabel.setEdgeLength.pre violated: edgeLength < 0");
        }

        if (shift > 1 || shift < 0) {
            throw new IllegalArgumentException("SliderLabel.setEdgeLength.pre violated: shift > 1 || shift < 0");
        }

        this.setAnchor(new Anchor(this.poi.getX() + edgeLength * shift, this.poi.getY()));
        this.edgeLength = edgeLength;
        this.shift = shift;
    }

    public double getShift() {
        return this.shift;
    }

    public String getPlacement() {
        return Double.toString(this.getShift());
    }
}
