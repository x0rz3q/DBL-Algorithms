package models;

public class SliderLabel extends AbstractLabel {
    protected double shift;

    public SliderLabel(double x, double y, double height, double aspectRatio, double shift, int ID) {
        super(x, y, height, aspectRatio, ID);
        this.shift = shift;
        this.setHeight(height);
    }

    public void setShift(double shift) {
        if (shift > 1 || shift < 0) {
            throw new IllegalArgumentException("SliderLabel.setEdgeLength.pre violated: shift > 1 || shift < 0");
        }

        this.rectangle = new Rectangle(this.poi.getX(), this.poi.getY(),
                                        this.poi.getX() + this.height*this.aspectRation * this.shift,
                                        this.poi.getY() + this.height);
    }

    public double getShift() {
        return this.shift;
    }

    public String getPlacement() {
        return Double.toString(this.getShift());
    }

    @Override
    public void setHeight(double height) {
        this.height = height;
        this.setShift(this.shift);
    }
}
