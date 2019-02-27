package models;

public class FieldExtendedSliderLabel extends SliderLabel {

    public int a;
    public int b;
    private boolean isExtended = false;
    private double width;

    public FieldExtendedSliderLabel(double x, double y, double height, double aspectRatio, double shift, int ID) {
        super(x, y, height, aspectRatio, shift, ID);
    }

    @Override
    public double getXMax() {
        if (!isExtended) {
            return super.getXMax();
        } else {
            return a + b * width;
        }
    }

    @Override
    public void setHeight(double height) {
        this.height = height;
        if (height == 0) {
            this.setShift(0);
        } else {
            this.setShift(this.shift);
        }
    }

    @Override
    public void setWidth(double width) {
        this.width = width;
        if (width == 0) {
            this.setHeight(0);
        } else {
            this.setHeight(width / this.aspectRatio);
        }
    }

    @Override
    public void setShift(double shift) throws IllegalArgumentException {
        super.setShift(shift);
        isExtended = false;
    }

    public void setFieldExtended(int a, int b, double width) {
        if (a < 0 || b < 0 || width < 0) {
            throw new IllegalArgumentException("FieldExtendedSliderLabel.setRectangle parameters negative");
        }

        if (a + (b-1) * width > poi.getX() || a + b * width < poi.getX()) {
            throw new IllegalArgumentException("FieldExtendedSliderLabel.setRectangle rectangle not above Poi");
        }

        this.rectangle = new Rectangle(
                a + (b - 1) * width,
                this.poi.getY(),
                a + b * width,
                this.poi.getY() + width / aspectRatio
        );

        this.a = a;
        this.b = b;
        this.width = width;
        this.height = width / aspectRatio;
        this.isExtended = true;
        // @TODO decide how to calculate shift

        if (b == 0) {
            shift = 0;
        } else {
            shift = (a - this.getXMax()) / width + b;
        }
    }
}
