

public interface PointInterface extends GeometryInterface {
    /**
     * Get the X coordinate of the point.
     * This is equal to getXMax() and getXMin()
     *
     * @return double
     */
    double getX();

    /**
     * Get the Y coordinate of the point.
     * This is equal to getYMax() and getYMin()
     *
     * @return double
     */
    double getY();

    /**
     * Get the euclidean distance between two points
     *
     * @param point {@link PointInterface}
     * @return double
     */
    double getDistanceTo(PointInterface point);

    /**
     * Get the euclidean distance between two coordinates
     *
     * @param x double
     * @param y double
     * @return double
     */
    double getDistanceTo(double x, double y);
}
