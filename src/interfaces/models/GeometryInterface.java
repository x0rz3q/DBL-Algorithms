package interfaces.models;

public interface GeometryInterface {
    /**
     * Check if geometries overlap.
     *
     * @param geometry {@link GeometryInterface}
     * @return Boolean
     */
    Boolean intersects(GeometryInterface geometry);
}
