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
     * @return SquareInterface
     */
    SquareInterface getAnchor();

    /**
     * Set the anchor of the square.
     *
     * @param anchor {@link SquareInterface}
     */
    void setAnchor(SquareInterface anchor);

    /**
     * Get the edge length of the square.
     *
     * @return Float
     */
    Float getEdgeLength();

    /**
     * Set edge length of the square.
     *
     * @param edgeLength {@link Float}
     */
    void setEdgeLength(Float edgeLength);

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
     * @return Float
     */
    Float getXMax();

    /**
     * Get maximal Y coordinate from square.
     *
     * @return Float
     */
    Float getYMax();

    /**
     * Get minimal X coordinate from square.
     *
     * @return Float
     */
    Float getXMin();

    /**
     * Get minimal Y coordinate from square.
     *
     * @return Float
     */
    Float getYMin();

    /**
     * Check if Square intersects with another square.
     *
     * @param square {@link SquareInterface}
     * @return Boolean
     */
    Boolean intersect(SquareInterface square);
}
