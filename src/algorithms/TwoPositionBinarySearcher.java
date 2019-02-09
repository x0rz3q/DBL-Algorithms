package algorithms;

import interfaces.AbstractCollectionInterface;
import interfaces.models.RectangleInterface;

public class TwoPositionBinarySearcher<T extends RectangleInterface> extends BinarySearcher<T> {
    @Override
    boolean isSolvable(AbstractCollectionInterface<T> nodes, float height) {
        System.out.println("TwoPositionBinarySearcher.isSolvable() height: " + height);
        return height <= 500;
    }

    @Override
    void getSolution(AbstractCollectionInterface<T> nodes, float height) {

    }
}
