package Collections;

import interfaces.models.GeometryInterface;
import interfaces.models.RectangleInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class QuadTree extends AbstractCollection
{
    /** boundary for this specific quad tree. e.g. root has boundary {0, 10k} for x and y **/
    private RectangleInterface boundary;
    /** children quadtrees **/
    public QuadTree NW;
    public QuadTree NE;
    public QuadTree SE;
    public QuadTree SW;
    /** if has not been subdivided **/
    public boolean leaf; // leaf == leaves.size() < 2
    /** leaves of this quad tree **/
    private Collection<GeometryInterface> leaves;
    QuadTree() {
        super();
        leaves = new ArrayList<>();
        leaf = true;
    }

    QuadTree(Collection nodes) {
        super(nodes);
    }

    /**
     * Sets the boundary of this quad tree.
     * @param r a square
     */
    void setBoundary(RectangleInterface r) {
        this.boundary = r;
    }

    /**
     * Subdivides the QuadTree
     */
    private void subDivide() {
        NW = new QuadTree(getLeaves());
        NE = new QuadTree(getLeaves());
        SE = new QuadTree(getLeaves());
        SW = new QuadTree(getLeaves());
        leaves.clear();
        leaf = false;
    }

    private Collection<GeometryInterface> getLeaves() {
        return leaves;
    }

    @Override
    public void insert(GeometryInterface node) throws NullPointerException {
        if (node == null) {
            throw new NullPointerException("QuadTree.insert() null node provided");
        }
        
    }

    @Override
    public void insert(Collection nodes) throws NullPointerException {
        if (nodes == null) {
            throw new NullPointerException("QuadTree.insert() null Collection provided");
        }
    }

    @Override
    public void remove(GeometryInterface node) throws NullPointerException {

    }

    @Override
    public Collection query2D(RectangleInterface range) {
        return null;
    }

    @Override
    public Boolean intersects(GeometryInterface node) {
        return null;
    }

    @Override
    public int size() {
        return super.getSize();
    }

    @Override
    public Iterator iterator() {
        return null;
    }
}
