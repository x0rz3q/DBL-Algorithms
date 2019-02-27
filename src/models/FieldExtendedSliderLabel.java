package models;

public class FieldExtendedSliderLabel extends SliderLabel {

    // the x coordinate of the first point in the sequence of connecting labels
    private int a;

    // the place in the sequence of connecting labels, 0 is the first label
    private int b;

    // indicates whether the point is defined by field extended coordinates
    private boolean isExtended = false;

    // width saved such that it does not needto be recalculated from width
    private double width;

    public FieldExtendedSliderLabel(double x, double y, double height, double aspectRatio, double shift, int ID) {
        super(x, y, height, aspectRatio, shift, ID);
        width = height * aspectRatio;
    }

    /**
     * returns the x coordinate of the first point in the sequence of connecting labels
     */
    public int getA () {
        return a;
    }

    /**
     * returns the index of the label in the sequence of connecting labels
     */
    public int getB () {
        return b;
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

    /**
     * set the label by definition of a sequence to which it belongs
     *
     * @param a the x coordinate of the first point in the sequence of connecting labels
     * @param b the index of the label in the sequence of connecting labels
     * @param width the width of all labels in the sequence of connecting labels
     * @post label is set by {@code (a + (b-1) * width, poi.x) and (a + b * width, poi.height)}
     * @throws IllegalArgumentException when label is not located on top of Poi
     */
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

        if (b == 0) {
            this.shift = 0;
        } else {
            this.shift = (a - this.getXMax()) / width + b;
        }
    }
}
