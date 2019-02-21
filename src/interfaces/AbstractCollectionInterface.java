package interfaces;

import interfaces.models.SquareInterface;
import models.BoundingBox;

import java.util.Collection;

public interface AbstractCollectionInterface {
    /**
     * Insert node into collection.
     *
     * @param node {@link SquareInterface}
     * @pre {@code node <> null}
     * @post {@code this.intersects(node)}
     * @throws NullPointerException if {@code node == null}
     */
    Boolean insert(SquareInterface node) throws NullPointerException;

    /**
     * Get all items that intersect with a given range.
     *
     * @param range {@link SquareInterface}
     * @return List
     * @post {@code (\forall i; \result.has(i); range.intersects(\result.get(i)))}
     */
    Collection<SquareInterface> query2D(SquareInterface range);

    /**
     * Get all items that intersect with the given range
     * @param range {@link BoundingBox}
     * @return collection of objects in contained in the range
     * @post {@code (\forall i; \result.has(i); range.intersects(\result.get(i)))}
     */
    Collection<SquareInterface> query2D(BoundingBox range);
    /**
     * Get the size of the collection.
     *
     * @return int
     */
    int size();
}
