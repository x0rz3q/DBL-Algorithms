package models;

public class VarSliderLabel extends AbstractLabel {

    /**
     * Parses arguments
     *
     * @param x the x-coordinate of the POI
     * @param y the y-coordinate of the POI
     * @param height the height of the rectangle
     * @param aspectRatio the aspect ratio of the rectangle
     * @param shift the shift value of the rectangle with respect to the POI
     * @param id the id associated to this label
     */
    public VarSliderLabel(double x, double y, double height, double aspectRatio, double shift, int id) {
        super(x, y, height, aspectRatio, id);

        if (x < 0 || x > 10000 || y < 0 || y > 10000) throw new IllegalArgumentException("SliderLabel.parseRectangle has POI coordinates not in [0, 10000] x [0, 10000]");
        if (height <= 0) throw new IllegalArgumentException("SliderLabel.parseRectangle does not allow height <= 0");
        if (aspectRatio <= 0) throw new IllegalArgumentException("SliderLabel.parseRectangle does not allow aspectRatio <= 0");
        if (shift < 0 || shift > 1) throw new IllegalArgumentException("SliderLabel.parseRectangle requires a shift being [0, 1]");

        this.rectangle = new Rectangle(x + (shift - 1) * height * aspectRatio, y, x + shift * height * aspectRatio, y + height);
    }

    @Override
    public String getPlacement() {
        throw new UnsupportedOperationException("VarSliderLabel does not return placements");
    }

    @Override
    public Rectangle getRectangle() {
        return this.rectangle;
    }
}
