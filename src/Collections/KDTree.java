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

    /**
     * Constructor only for the client, initializing the root
     * @param nodes Collection of {@link SquareInterface} to be contained in the tree
     */
    public KDTree(Collection<SquareInterface> nodes) {
        this(nodes, 0);
    }

    KDTree(Collection<SquareInterface> nodes, int depth) {
        this.nodes = nodes;
        this.depth = depth;
        isLeaf = false;
        setDataLimit(1);
        buildTree(this.nodes, this.depth);
    }

    /**
     * Builds a subtree
     * @param nodes what nodes to put in this subtree
     * @param depth depth of the tree
     * @return
     */
    private KDTree buildTree(Collection<SquareInterface> nodes, int depth) {return null;}

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
