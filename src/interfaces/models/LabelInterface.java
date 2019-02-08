package interfaces.models;

public interface LabelInterface extends RectangleInterface {
    /**
     * Serialize as a string.
     *
     * @return String
     */
    String toString();

    /**
     * Get the Point of Interest.
     *
     * @return PointInterface
     */
    PointInterface getPOI();

    /**
     * Get the aspect ratio.
     *
     * @return Float
     */
    Float getAspectRatio();

    /**
     * Set the size based on height.
     *
     * @param height Float
     * @pre {@code height >= 0}
     * @post {@code this.getHeight() == height && this.getWidth() / this.getHeight() == this.getAspectRatio()}
     * @throws IllegalArgumentException if {@code height < 0}
     */
    void setHeight(Float height) throws IllegalArgumentException;

    /**
     * Set the size based on width.
     *
     * @param width Float
     * @pre {@code width >= 0}
     * @post {@code this.getWidth() == width && this.getWidth() / this.getHeight() == this.getAspectRatio()}
     * @throws IllegalArgumentException if {@code width < 0}
     */
    void setWidth(Float width) throws IllegalArgumentException;

    /**
     * Set the size.
     *
     * @param height Float
     * @param width Float
     * @pre {@code width >= 0 && height >= 0 && width / height == this.getAspectRatio()}
     * @post {@code this.getWidth() == width && this.getHeight() == height &&
     *          this.getWidth() / this.getHeight() == this.getAspectRatio()}
     * @throws IllegalArgumentException if {@code height < 0 || width < 0 || width / height <> this.getAspectRatio()}
     */
    void setSize(Float height, Float width) throws IllegalArgumentException;
}
