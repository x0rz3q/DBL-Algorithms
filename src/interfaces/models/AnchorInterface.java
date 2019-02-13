package interfaces.models;

public interface AnchorInterface {
    /**
     * Get X coordinate of anchor.
     *
     * @return Double
     */
    Double getX();

    /**
     * Get Y coordinate of anchor.
     *
     * @return Double
     */
    Double getY();

    /**
     * Get euclidean distance between two anchors.
     *
     * @param anchor {@link AnchorInterface}
     * @return Double
     */
    Double distance(AnchorInterface anchor);

    /**
     * Check if two anchors are equal.
     *
     * @param anchor {@link AnchorInterface}
     * @return Boolean
     */
    Boolean equals(AnchorInterface anchor);
}
