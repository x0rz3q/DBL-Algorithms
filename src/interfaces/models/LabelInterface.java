package interfaces.models;

public interface LabelInterface extends SquareInterface {
    /**
     * Serialize as a string.
     *
     * @return String
     */
    String toString();

    /**
     * Get the Point of Interest.
     *
     * @return SquareInterface
     */
    SquareInterface getPOI();
}
