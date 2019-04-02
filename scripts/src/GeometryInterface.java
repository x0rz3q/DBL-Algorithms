

public interface GeometryInterface {
    /**
     * Get the bottom left of the geometry.
     *
     * @return PointInterface
     */
    PointInterface getBottomLeft();

    /**
     * Get the bottom right of the geometry.
     *
     * @return PointInterface
     */
    PointInterface getBottomRight();

    /**
     * Get the top left of the geometry.
     *
     * @return PointInterface
     */
    PointInterface getTopLeft();

    /**
     * Get the top of the right of the geometry.
     *
     * @return PointInterface
     */
    PointInterface getTopRight();

    /**
     * Get the center of the geometry.
     *
     * @return PointInterface
     */
    PointInterface getCenter();

    /**
     * Check if two geometries are equal
     *
     * @param geometry {@link GeometryInterface}
     * @return PointInterface
     */
    boolean equals(GeometryInterface geometry);

    /**
     * Get the maximal X coordinate of the geometry.
     *
     * @return PointInterface
     */
    double getXMax();

    /**
     * Get the minimal X coordinate of the geometry.
     *
     * @return PointInterface
     */
    double getXMin();

    /**
     * Get the maximal Y coordinate of the geometry.
     *
     * @return PointInterface
     */
    double getYMax();

    /**
     * Get the minimal Y coordinate of the geometry.
     *
     * @return PointInterface
     */
    double getYMin();

    /**
     * Check if two geometries intersect each other. Note that border touches
     * are not considered intersections.
     *
     * @param geometry {@link GeometryInterface}
     * @return boolean
     */
    boolean intersects(GeometryInterface geometry);

    /**
     * Check if two geometries touch each other.
     *
     * @param geometry {@link GeometryInterface}
     * @return boolean
     */
    boolean touch(GeometryInterface geometry);

    /**
     * Check if two geometries touch or intersect.
     *
     * @param geometry {@link GeometryInterface}
     * @return boolean
     */
    boolean intersectOrTouch(GeometryInterface geometry);
}
