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
     * Remove a node from collection
     *
     * @param node {@link GeometryInterface} to remove
     * @throws NullPointerException if node == null
     * @return whether removal was successful
     */
    boolean remove(GeometryInterface node) throws NullPointerException;

    /**
     * Get all items that intersect with a given range.
     *
     * @param range {@link Rectangle}
     * @return List
     * @post {@code (\forall i; \result.has(i); range.intersects(\result.get(i)))}
     */
    Collection<GeometryInterface> query2D(Rectangle range);

    /**
     * Get the size of the collection.
     *
     * @return int
     */
    int size();
}
