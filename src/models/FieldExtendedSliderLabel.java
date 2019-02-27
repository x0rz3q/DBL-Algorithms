package models;

public class FieldExtendedSliderLabel extends SliderLabel {

    // the x coordinate of the first point in the sequence of connecting labels
    private int sequenceStartX;

    // the place in the sequence of connecting labels, 0 is the first label
    private int sequenceIndex;

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
    public int getSequenceStartX () {
        return sequenceStartX;
    }

    /**
     * returns the index of the label in the sequence of connecting labels
     */
    public int getSequenceIndex () {
        return sequenceIndex;
    }

    @Override
    public double getXMax() {
        if (!isExtended) {
            return super.getXMax();
        } else {
            return sequenceStartX + sequenceIndex * width;
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
     * set the label by definition of sequenceStartX sequence to which it belongs
     *
     * @param sequenceStartX the x coordinate of the first point in the sequence of connecting labels
     * @param sequenceIndex the index of the label in the sequence of connecting labels
     * @param width the width of all labels in the sequence of connecting labels
     * @post label is set by {@code (sequenceStartX + (sequenceIndex-1) * width, poi.x) and (sequenceStartX + sequenceIndex * width, poi.height)}
     * @throws IllegalArgumentException when label is not located on top of Poi
     */
    public void setFieldExtended(int sequenceStartX, int sequenceIndex, double width) {
        if (sequenceStartX < 0 || sequenceIndex < 0 || width < 0) {
            throw new IllegalArgumentException("FieldExtendedSliderLabel.setRectangle parameters negative");
        }

        if (sequenceStartX + (sequenceIndex-1) * width > poi.getX() || sequenceStartX + sequenceIndex * width < poi.getX()) {
            throw new IllegalArgumentException("FieldExtendedSliderLabel.setRectangle rectangle not above Poi");
        }

        this.rectangle = new Rectangle(
                sequenceStartX + (sequenceIndex - 1) * width,
                this.poi.getY(),
                sequenceStartX + sequenceIndex * width,
                this.poi.getY() + width / aspectRatio
        );

        this.sequenceStartX = sequenceStartX;
        this.sequenceIndex = sequenceIndex;
        this.width = width;
        this.height = width / aspectRatio;
        this.isExtended = true;

        if (sequenceIndex == 0) {
            this.shift = 0;
        } else {
            this.shift = (sequenceStartX - this.getXMax()) / width + sequenceIndex;
        }
    }
}
