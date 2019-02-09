package interfaces;

import interfaces.models.RectangleInterface;

public interface AbstractAlgorithmInterface<T extends RectangleInterface> {
    /**
     * Place all labels with the height being maximized.
     *
     * @Modifies nodes
     * @param nodes {@link AbstractAlgorithmInterface}
     */
    void solve(AbstractCollectionInterface<T> nodes);
}
