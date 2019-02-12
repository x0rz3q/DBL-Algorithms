package interfaces.models;

public interface SquareInterface {
    /**
     * Get the bottom left of the square.
     *
     * @return SquareInterface
     */
    SquareInterface getBottomLeft();

    /**
     *
     * Get the bottom right of the square.
     *
     * @return SquareInterface
     */
    SquareInterface getBottomRight();

    /**
     * Get the top left of the square.
     *
     * @return SquareInterface
     */
    SquareInterface getTopLeft();

    /**
     * Get the top right of the square.
     *
     * @return SquareInterface
     */
    SquareInterface getTopRight();

    /**
     * Get the center of the square.
     *
     * @return SquareInterface
     */
    SquareInterface getCenter();

    /**
     * Get the anchor of the square.
     *
     * @return AnchorInterface
     */
    AnchorInterface getAnchor();

    /**
     * Set the anchor of the square.
     *
     * @param anchor {@link AnchorInterface}
     */
    void setAnchor(AnchorInterface anchor);

    /**
     * Get the edge length of the square.
     *
     * @return Double
     */
    Double getEdgeLength();

    void setEdgeLength(Double edgeLength) throws IllegalArgumentException;

    /**
     * Check for equality
     *
     * @param square {@link SquareInterface}
     * @return Boolean
     * @post {@code \result == this.getEdgeLength() == square.getEdgeLength() &&
     *              this.getXMax() == square.getXMax() &&
     *              this.getYMax() == square.getYMax() &&
     *              this.getXMin() == square.getXMin() &&
     *              this.getYMin() == square.getYMin()}
     */
    Boolean equals(SquareInterface square);

    /**
     * Get maximal X coordinate from square.
     *
     * @return Double
     */
    Double getXMax();

    /**
     * Get maximal Y coordinate from square.
     *
     * @return Double
     */
    Double getYMax();

    /**
     * Get minimal X coordinate from square.
     *
     * @return Double
     */
    Double getXMin();

    /**
     * Get minimal Y coordinate from square.
     *
     * @return Double
     */
    Double getYMin();

    /**
     * Check if Square intersects with another square.
     *
     * @param square {@link SquareInterface}
     * @return Boolean
     */
    Boolean intersects(SquareInterface square);
}
