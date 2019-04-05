package models;

import java.text.DecimalFormat;

public class SliderLabel extends AbstractLabel {

    /*
     * SliderLabels are defined by using a field-extended coordinate system. The first label in a
     * sequence has slider value 0. All labels that can not be placed at slider value 0, must be
     * blocked by another label. This results in a sequence of labels touching eachother. Each
     * label is given an index in this sequence to define the labels coordinates
     *
     * A label for given seqStartX, seqIndex has:
     *      x-Min = seqStartX + (seqIndex - 1) * w
     *      x-Max = seqStartX + seqIndex * w
     *      y-Min = this.poi.getY()
     *      y-Max = this.poi.getY + w / this.aspectRatio
     *
     * This results in not sequentially calculate with decimal coordinates, rather by integral-pair
     * defined coordinates, which reduces the floating point errors.
     */

    // the x coordinate of the first point in the sequence of connecting labels
    private int seqStartX;

    // the place in the sequence of connecting labels, 0 is the first label
    private int seqIndex;

    // the width of this.rectangle
    private double width;

    public SliderLabel(double x, double y, double size, double aspectRatio, int id) {
        super(x, y, size, aspectRatio, id);
        reset();
    }

    /**
     * Parses arguments
     *
     * @param x the x-coordinate of the POI
     * @param y the y-coordinate of the POI
     * @param height the height of the rectangle
     * @param aspectRatio the aspect ratio of the rectangle
     * @param shift the shift value of the rectangle with respect to the POI
     * @return the {@link Rectangle} defined by given parameters
     */
    public static Rectangle parseRectangle (double x, double y, double height, double aspectRatio, double shift) {
        if (x < 0 || x > 10000 || y < 0 || y > 10000) throw new IllegalArgumentException("SliderLabel.parseRectangle has POI coordinates not in [0, 10000] x [0, 10000]");
        if (height <= 0) throw new IllegalArgumentException("SliderLabel.parseRectangle does not allow height <= 0");
        if (aspectRatio <= 0) throw new IllegalArgumentException("SliderLabel.parseRectangle does not allow aspectRatio <= 0");
        if (shift < 0 || shift > 1) throw new IllegalArgumentException("SliderLabel.parseRectangle requires a shift being [0, 1]");

        return new Rectangle(x + (shift - 1) * height * aspectRatio, y, x + shift * height * aspectRatio, y + height);
    }

    /**
     * @return this labels sequence starting x-coordinate
     */
    public int getSeqStartX() {
        return this.seqStartX;
    }

    /**
     * @return this labels index within the sequence of labels
     */
    public int getSeqIndex() {
        return this.seqIndex;
    }

    @Override
    public double getXMax() {
        return this.seqStartX + this.seqIndex * this.width;
    }

    @Override
    public void setHeight(double height) {
        throw new UnsupportedOperationException("SliderLabel does not allow setting height");
    }

    @Override
    public void setWidth(double width) {
        throw new UnsupportedOperationException("SliderLabel does not allow setting width");
    }

    /**
     * sets this label to a rectangle of width and height 0 not in a sequence
     */
    public void reset() {
        this.rectangle = new Rectangle(poi, poi);
        this.height = 0;
        this.width = 0;
        this.seqStartX = (int) poi.getX();
        this.seqIndex = 0;
    }

    /**
     * Sets this label to be a rectangle of width w at index seqIndex in a sequence starting at seqStartX
     *
     * @param seqStartX x-coordinate of first POI in the label sequence
     * @param seqIndex the index at which this label is located within a sequence
     * @param w width of the label
     */
    public void setLabel(int seqStartX, int seqIndex, double w) {
        if (seqStartX + (seqIndex-1) * w > poi.getX() || seqStartX + seqIndex * w < poi.getX()) {
            throw new IllegalArgumentException("FieldExtendedSliderLabel.setRectangle rectangle not above Poi");
        }

        if (w == 0) throw new IllegalArgumentException("FieldExtendedSliderLabel.setRectangle doesn't handle width = 0 rectangles");

        this.seqStartX = seqStartX;
        this.seqIndex = seqIndex;
        this.height = w / aspectRatio;
        this.width = w;
        this.rectangle = new Rectangle(seqStartX + (seqIndex - 1) * w, poi.getY(), seqStartX + seqIndex * w, poi.getY() + w / aspectRatio);
    }

    @Override
    public String getPlacement() {
        DecimalFormat format = new DecimalFormat("0.0000000000000000000000000000");
        return format.format(Math.min(1, Math.max(0, (seqStartX - this.poi.getXMax()) / width + seqIndex)));
    }

    @Override
    public Rectangle getRectangle() {
        return this.rectangle;
    }
}
