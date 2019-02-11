package interfaces;

import interfaces.models.SquareInterface;

public interface AbstractAlgorithmInterface<T extends SquareInterface> {
    /**
     * Place all labels with the height being maximized.
     *
     * @Modifies nodes
     * @param nodes {@link AbstractAlgorithmInterface}
     */
    void solve(AbstractCollectionInterface<T> nodes);
}
