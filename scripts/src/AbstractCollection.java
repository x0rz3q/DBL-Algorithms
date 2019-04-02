





import java.util.List;

public abstract class AbstractCollection implements AbstractCollectionInterface {
    protected int count;
    protected int dataLimit;
    List<GeometryInterface> nodes;

    public AbstractCollection() {
        this.count = 0;
    }

    public int getSize() {
        return this.count;
    }

    public void setDataLimit(int d) {
        this.dataLimit = d;
    }

    /**
     * Check if an item intersects with given node.
     *
     * @param node {@link GeometryInterface}
     * @return Boolean
     */
    protected abstract boolean intersects(GeometryInterface node);

    /**
     * Check if an item intersects with given node
     * @param node {@link Rectangle}
     * @return Boolean
     */
    protected abstract boolean intersects(Rectangle node);

    public abstract boolean nodeInRange(Rectangle node);
}
