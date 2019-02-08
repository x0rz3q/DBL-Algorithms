package interfaces;

import interfaces.models.RectangleInterface;

import java.util.List;

public interface AbstractCollectionInterface<T> extends Iterable<T> {
    /**
     * Insert node into collection.
     *
     * @param node T
     * @pre {@code node <> null}
     * @post {@code this.intersects(node)}
     */
    void insert(T node) throws NullPointerException;

    /**
     * Remove node from collection.
     *
     * @param node T
     * @pre {@code node <> null}
     * @post {@code !this.intersects(node)}
     */
    void remove(T node) throws NullPointerException;

    /**
     * Get all items that intersect with a given range.
     *
     * @param range {@link RectangleInterface}
     * @return List
     * @post {@code (\forall i; \result.has(i); range.contains(\result.get(i)))}
     */
    List<T> query2D(RectangleInterface range);

    /**
     * Check if an item intersects with given node.
     * @param node
     * @return
     */
    Boolean intersects(T node);
    int size();
}