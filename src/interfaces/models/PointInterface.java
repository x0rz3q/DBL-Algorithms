package interfaces.models;

public interface PointInterface {
    /**
     * Get the X coordinate.
     *
     * @return Float
     */
    Float getX();

    /**
     * Get the Y coordinate.
     *
     * @return Float
     */
    Float getY();

    /**
     * Get the euclidean distance.
     *
     * @param point {@link PointInterface}
     * @return Float
     * @post {@code \result == sqrt((this.getX() - point.getX())^2 + (this.getY() - point.getY())^2)}
     */
    Float distance(PointInterface point);

    /**
     * Equality check.
     *
     * @param point {@link PointInterface}
     * @return Boolean
     * @post {@code \result == this.getX() == point.getX() && this.getY() == point.getY()}
     */
    Boolean equals(PointInterface point);
}
