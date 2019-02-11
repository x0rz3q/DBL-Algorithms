package interfaces.models;

public interface PointInterface extends GeometryInterface {

    /**
     * Set the point ID
     *
     * @param id {@link Integer}
     */
    void setID(int id);

    /**
     * Get the point ID
     *
     * @return Integer
     */
    int getID();

    /**
     * Set the point its x coordinate
     *
     * @param x {@link Float}
     */
    float setX(float x);

    /**
     * Get the X coordinate.
     *
     * @return Float
     */
    Float getX();

    /**
     * Set the point its y coordinate
     *
     * @param y {@link Float}
     */
    float setY(float y);

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
