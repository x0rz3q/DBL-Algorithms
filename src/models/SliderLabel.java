package models;

public class SliderLabel extends AbstractLabel {
    protected double shift;

    public SliderLabel(double x, double y, double height, double aspectRatio, double shift, int ID) {
        super(x, y, height, aspectRatio, ID);
        this.shift = shift;
        this.setHeight(height);
    }

    public double getShift() {
        return this.shift;
    }

    /**
     * Set the shift of the label.
     *
     * @param shift double
     * @throws IllegalArgumentException if {@code shift > 0 || shift < 0}
     */
    public void setShift(double shift) throws IllegalArgumentException {
        if (shift > 1 || shift < 0) {
            throw new IllegalArgumentException("SliderLabel.setEdgeLength.pre violated: shift > 1 || shift < 0");
        }

        this.rectangle = new Rectangle(
                this.poi.getX() + this.height * this.aspectRatio * (this.shift - 1),
                this.poi.getY(),
                this.poi.getX() + this.height * this.aspectRatio * this.shift,
                this.poi.getY() + this.height);
        this.shift = shift;
    }

    public String getPlacement() {
        return Double.toString(this.getShift());
    }

    @Override
    public Rectangle getRectangle() {
        return this.rectangle;
    }

    @Override
    public void setHeight(double height) {
        this.height = height;
        this.setShift(this.shift);
    }

    @Override
    public void setWidth(double width) {
        this.setHeight(width / this.aspectRatio);
    }
}
