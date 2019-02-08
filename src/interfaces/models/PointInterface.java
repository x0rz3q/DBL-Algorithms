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
     * @param p {@link PointInterface}
     * @return Float
     * @post {@code \result = sqrt((this.getX() - p.getX())^2 + (this.getY() - p.getY())^2)}
     */
    Float distance(PointInterface p);

    /**
     * Equality check.
     *
     * @param p {@link PointInterface}
     * @return Boolean
     * @post {@code \result = this.getX() == p.getX() && this.getY() == p.getY()}
     */
    Boolean equals(PointInterface p);
}
