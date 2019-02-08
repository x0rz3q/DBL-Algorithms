package interfaces;

import interfaces.models.RectangleInterface;

public interface AbstractAlgorithmInterface<T extends RectangleInterface> {
    /**
     * @Modifies nodes
     * @param nodes
     */
    void solve(AbstractCollectionInterface<T> nodes);
}
