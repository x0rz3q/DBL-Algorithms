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
        return query2D(this, range);
    }

    /**
     * retrieves all GeometryInterfaces in the intersection of given subTree and range
     *
     * @param subTree subtree that is being searched
     * @param range range that is searched for
     * @post {@code \result == (\forall i; i.intesect(subTree); i.instersects(range))}
     */
    private Collection query2D(QuadTree subTree, RectangleInterface range) {
        List allLeaves = new ArrayList<>();
        if (subTree.leaf) {
            for (GeometryInterface leave : getLeaves())
                if (leave.intersects(range))
                    allLeaves.add(leave);
        } else {
            if (subTree.NE.intersects(range))
                allLeaves.addAll(query2D(subTree.NE, range));
            if (subTree.NW.intersects(range))
                allLeaves.addAll(query2D(subTree.NW, range));
            if (subTree.SE.intersects(range))
                allLeaves.addAll(query2D(subTree.SE, range));
            if (subTree.SW.intersects(range))
                allLeaves.addAll(query2D(subTree.SW, range));
        }
        return allLeaves;

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
