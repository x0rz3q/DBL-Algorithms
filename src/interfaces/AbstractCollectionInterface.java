package interfaces;

import interfaces.models.GeometryInterface;
import models.Rectangle;

import java.util.Collection;

public interface AbstractCollectionInterface {
    /**
     * Insert node into collection.
     *
     * @param node {@link GeometryInterface}
     * @throws NullPointerException if {@code node == null}
     * @pre {@code node <> null}
     * @post {@code this.intersects(node)}
     */
    boolean insert(GeometryInterface node) throws NullPointerException;

    /**
     * Remove node from collection.
     *
     * @param node {@link GeometryInterface}
     * @throws NullPointerException if {@code node == null}
     * @pre {@code node <> null}
     * @post {@code !this.intersects(node)}
     */
    void remove(GeometryInterface node) throws NullPointerException;

    /**
     * Get all items that intersect with a given range.
     *
     * @param range {@link GeometryInterface}
     * @return List
     * @post {@code (\forall i; \result.has(i); range.intersects(\result.get(i)))}
     */
    Collection<GeometryInterface> query2D(GeometryInterface range);

    /**
     * Check if an item intersects with given node.
     *
     * @param node {@link GeometryInterface}
     * @return boolean
     */
    boolean intersects(GeometryInterface node);

    /**
     * Check if an item intersects with given node
     *
     * @param node {@link Rectangle}
     * @return boolean
     */
    boolean intersects(Rectangle node);

    /**
     * Get the size of the collection.
     *
     * @return int
     */
    int size();
}