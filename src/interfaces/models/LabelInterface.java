package interfaces.models;

public interface LabelInterface extends SquareInterface {
    /**
     * Serialize as a string.
     *
     * @return String
     */
    String toString();

    /**
     * Get the Point of Interest.
     *
     * @return SquareInterface
     */
    SquareInterface getPOI();

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
