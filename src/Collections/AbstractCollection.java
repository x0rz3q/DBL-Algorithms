package Collections;

import interfaces.AbstractCollectionInterface;
import interfaces.models.SquareInterface;
import models.BoundingBox;

import java.util.Collection;
import java.util.List;

public abstract class AbstractCollection implements AbstractCollectionInterface {
    protected int count;
    protected int dataLimit;
    List<SquareInterface> nodes;

    public AbstractCollection() {
        this.count = 0;
    }

    public int getSize() {
        return this.count;
    }

    public void setDataLimit(int d) {this.dataLimit = d;}

    /**
     * Check if an item intersects with given node.
     *
     * @param node {@link SquareInterface}
     * @return Boolean
     */
    protected abstract Boolean intersects(SquareInterface node);

    /**
     * Check if an item intersects with given node
     * @param node {@link BoundingBox}
     * @return Boolean
     */
    protected abstract Boolean intersects(BoundingBox node);
}
