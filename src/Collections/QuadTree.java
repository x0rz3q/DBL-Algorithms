package Collections;

import interfaces.models.GeometryInterface;
import interfaces.models.RectangleInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class QuadTree extends AbstractCollection
{
    /** boundary for this specific quad tree. e.g. root has boundary {0, 10k} for x and y **/
    private RectangleInterface boundary;
    /** children quadtrees **/
    private QuadTree NW;
    private QuadTree NE;
    private QuadTree SE;
    private QuadTree SW;
    /** if has not been subdivided **/
    private boolean leaf; // leaf == leaves.size() < 2
    /** leaves of this quad tree **/
    private Collection<GeometryInterface> leaves;
    /** limit of leaves per tree **/
    private int maxLeaves;

    QuadTree() {
        super();
        this.leaves = new ArrayList<>();
        this.leaf = true;
    }

    QuadTree(Collection nodes) {
        super(nodes);
    }

    /**
     * Sets the boundary of this quad tree.
     * @param r a square
     */
    private void setBoundary(RectangleInterface r) {
        this.boundary = r;
    }

    /**
     * Sets the amount of leaves allowed in a quad tree
     * @param l
     * @pre {@code l > 0}
     */
    private void setLeafLimit(int l) throws IllegalArgumentException{
        this.maxLeaves = l;
    }

    /**
     * Subdivides the QuadTree in 4 sub-trees
     */
    private void subDivide() {
        // TODO: USE RECTANGLE CLASS WHEN ITS IMPLEMENTED
        NW = subDivide(boundary); // ((Xmin,Ymax), center)
        NE = subDivide(boundary); // ((Xmax - width/2, Ymax),(Xmax, Ymax - heigh/2))
        SW = subDivide(boundary); // ((Xmin, Ymin + height/2),(Xmin + width/2, Ymin))
        SE = subDivide(boundary); // (center, (Xmax, Ymin))
        leaves.clear();
        this.count = NW.size() + NE.size() + SW.size() + SE.size();
        leaf = false;
    }

    /**
     * Produce one of the 4 subidivisons with boundary b
     * @param b the boundary of the subdivision
     * @return the new subdivision
     */
    private QuadTree subDivide(RectangleInterface b) {
        QuadTree t = new QuadTree(getLeaves()); // add root leaves to subtree
        t.setBoundary(b); // set the new boundary
        t.setLeafLimit(this.maxLeaves); // keep same limit
        return t;
    }

    /**
     * Getter for objects in the leaves
     * @return Collection over GeometryInterface
     */
    private Collection<GeometryInterface> getLeaves() {
        return this.leaves;
    }

    @Override
    public void insert(GeometryInterface node) throws NullPointerException {
        if (node == null) {
            throw new NullPointerException("QuadTree.insert() null node provided");
        }
        if (!this.boundary.intersects(node)) { // if node is not in this quad tree
            return; // do nothing
        }

        this.leaves.add(node); // add it to the leaves
        this.count++; // more leaves
        if (this.leaves.size() > this.maxLeaves) { // over the limit
            subDivide(); // make children
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
        Collection<GeometryInterface> allLeaves = new ArrayList<>();
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
        return boundary.intersects(node);
    }

    @Override
    public int size() {
        return super.getSize();
    }

    @Override
    public Iterator iterator() {
        return query2D(boundary).iterator(); // iterate whole subtree
    }
}
