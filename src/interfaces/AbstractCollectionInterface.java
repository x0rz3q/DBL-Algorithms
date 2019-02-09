package interfaces;

import interfaces.models.RectangleInterface;

import java.util.Collection;

public interface AbstractCollectionInterface<T> extends Iterable<T> {
    /**
     * Insert node into collection.
     *
     * @param node {@link T}
     * @pre {@code node <> null}
     * @post {@code this.intersects(node)}
     * @throws NullPointerException if {@code node == null}
     */
    void insert(T node) throws NullPointerException;

    /**
     * Insert nodes into collection
     *
     * @pre {@code nodes <> null}
     * @post {@code (\forall i; nodes.has(i); this.intersects(nodes.get(i)))}
     * @throws NullPointerException if {@code nodes == null}
     */
    void insert(Collection<T> nodes) throws NullPointerException;

    /**
     * Remove node from collection.
     *
     * @param node {@link T}
     * @pre {@code node <> null}
     * @post {@code !this.intersects(node)}
     * @throws NullPointerException if {@code node == null}
     */
    void remove(T node) throws NullPointerException;

    /**
     * Get all items that intersect with a given range.
     *
     * @param range {@link RectangleInterface}
     * @return List
     * @post {@code (\forall i; \result.has(i); range.contains(\result.get(i)))}
     */
    Collection<T> query2D(RectangleInterface range);

    /**
     * Check if an item intersects with given node.
     *
     * @param node {@link T}
     * @return Boolean
     */
    Boolean intersects(T node);

    /**
     * Get the size of the collection.
     *
     * @return int
     */
    int size();
}