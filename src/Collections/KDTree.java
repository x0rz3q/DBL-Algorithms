package Collections;

import interfaces.models.SquareInterface;
import models.BoundingBox;

import java.util.Collection;

public class KDTree extends AbstractCollection{
    Collection<SquareInterface> nodes;
    boolean isLeaf; /** does it directly contain the nodes **/
    int depth;
    /** children of this tree **/
    KDTree left;
    KDTree right;
    SquareInterface splitter;

    /**
     * Constructor for initializing a non-leaf KDTree (e.g. root).
     * used by client and builder.
     * @param nodes Collection of {@link SquareInterface} to be contained in the tree
     * @param depth Depth of this root.
     */
    public KDTree(Collection<SquareInterface> nodes, int depth) {
        isLeaf = false;
        this.depth = depth;
        buildTree(nodes, depth);
    }

    /**
     * Constructor for initializing a leaf.
     * @param nodes contained in the leaf
     * @param depth depth of the leaf
     * @param limit limit of data of when to split down further
     */
    KDTree(Collection<SquareInterface> nodes, int depth, int limit) {
        this.depth = depth;
        this.nodes = nodes;
        this.isLeaf = true;
        setDataLimit(limit);
    }

    /**
     * Builds a subtree
     * @param nodes what nodes to put in this subtree
     * @param depth depth of the root
     * @return
     */
    private KDTree buildTree(Collection<SquareInterface> nodes, int depth) {
        if (nodes.size() <= this.dataLimit) { // if below data limit
            return new KDTree(nodes, depth, this.dataLimit); // construct a leaf
        } else {
            if (depth % 2 == 0) { // even depth
                // split vertically
            } else { // odd depth
                // split horizontally
            }
        }
        return null;
    }

    @Override
    public void insert(SquareInterface node) throws NullPointerException {

    }

    @Override
    public void remove(SquareInterface node) throws NullPointerException {

    }

    @Override
    public Collection query2D(SquareInterface range) {
        return null;
    }

    @Override
    public Collection<SquareInterface> query2D(BoundingBox range) {
        return null;
    }

    @Override
    public Boolean intersects(SquareInterface node) {
        return null;
    }

    @Override
    public Boolean intersects(BoundingBox node) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }
}
