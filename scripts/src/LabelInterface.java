



public interface LabelInterface extends GeometryInterface {
    /**
     * Serialize as a string.
     *
     * @return String
     */
    String toString();

    /**
     * Type the relative positioning to the POI
     * {NW, SW, NE, SE} or {0 <-> 1}
     *
     * @return String
     */
    String getPlacement();

    /**
     * Get the Point of Interest.
     *
     * @return SquareInterface
     */
    PointInterface getPOI();

    /**
     * Get unique ID.
     *
     * @return Integer
     */
    Integer getID();

    /**
     * Get rectangle.
     *
     * @return Rectangle
     */
    Rectangle getRectangle();
}
