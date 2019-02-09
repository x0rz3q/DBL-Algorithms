package interfaces.models;

public interface RectangleInterface extends GeometryInterface {
    /**
     * Check if the objects are equal.
     *
     * @param rectangle {@link RectangleInterface}
     * @return Boolean
     * @post {@code \result == this.getXMin() == rectangle.getXMin() &&
     *              this.getYMin() == rectangle.getYMin() &&
     *              this.getXMax() == rectangle.getXMax() &&
     *              this.getYMax() == rectangle.getYMax()}
     */
    Boolean equals(RectangleInterface rectangle);

    /**
     * Get the height.
     *
     * @return Float
     * @post {@code \result == this.getYMax() - this.getYMin()}
     */
    Float getHeight();

    /**
     * Get the width.
     *
     * @return Float
     * @post {@code \result == this.getXMax() - this.getXMin()}
     */
    Float getWidth();

    /**
     * Get the minimal X coordinate.
     * .
     * @return Float
     */
    Float getXMin();

    /**
     * Get the maximal X coordinate.
     *
     * @return Float
     */
    Float getXMax();

    /**
     * Get the minimal Y coordinate.
     *
     * @return Float
     */
    Float getYMin();

    /**
     * Get the maximal Y coordinate.
     *
     * @return Float
     */
    Float getYMax();

    /**
     * Get the bottom left point.
     *
     * @return PointInterface
     * @post {@code \result.getX() == this.getXmin() && \result.getY() == this.getYMin()}
     */
    PointInterface getBottomLeft();

    /**
     * Get the top left point.
     *
     * @return PointInterface
     * @post {@code \result.getX() == this.getXmin() && \result.getY() == this.getYMax()}
     */
    PointInterface getTopLeft();

    /**
     * Get the bottom right point.
     *
     * @return PointInterface
     * @post {@code \result.getX() == this.getXMax() && \result.getY() == this.getYMax()}
     */
    PointInterface getBottomRight();

    /**
     * Get the top right point.
     *
     * @return PointInterface
     * @post {@code \result.getX() == this.getXMax() && \result.getY() == this.getYMax()}
     */
    PointInterface getTopRight();

    /**
     * Get the center point
     *
     * @return PointInterface
     * @post {@code \result.getX() == this.getXMax() - (this.getXMax() - this.getXMin()) / 2 &&
     *          \result.getY() == this.getYMax() - (this.getYMax() - this.getYMin() /2)}
     */
    PointInterface getCenter();
}
