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
    private boolean leaf; // leaf == data.size() <= dataLimit
    /** data of this quad tree **/
    private Collection<GeometryInterface> data;
    /** limit of data per tree **/
    private int dataLimit;

    QuadTree() {
        super();
        this.data = new ArrayList<>();
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
     * Sets the amount of data allowed in a leaf of quadtree
     * @param l
     * @pre {@code l > 0}
     */
    private void setDataLimit(final int l) {
        this.dataLimit = l;
    }

    /**
     * Subdivides the QuadTree in 4 sub-trees
     * @post NW <> null && NE <> null && SW <> null && SE <> null &&
     * data.size() == 0 && count == 0 && leaf == false
     */
    private void subDivide() {
        // TODO: USE RECTANGLE CLASS WHEN ITS IMPLEMENTED
        this.NW = subDivide(this.boundary); // ((Xmin,Ymax), center)
        this.NE = subDivide(this.boundary); // ((Xmax - width/2, Ymax),(Xmax, Ymax - heigh/2))
        this.SW = subDivide(this.boundary); // ((Xmin, Ymin + height/2),(Xmin + width/2, Ymin))
        this.SE = subDivide(this.boundary); // (center, (Xmax, Ymin))
        this.data.clear();
        this.count = 0;
        this.leaf = false;
    }

    /**
     * Produce one of the 4 subdivisons with boundary b
     * @param b the boundary of the subdivision
     * @return QuadTree, the new subdivision
     */
    private QuadTree subDivide(RectangleInterface b) {
        QuadTree t = new QuadTree(getData()); // add root data to subtree
        t.setBoundary(b); // set the new boundary
        t.setDataLimit(this.dataLimit); // keep same limit
        return t;
    }

    /**
     * Getter for data in a leaf
     * @return Collection over GeometryInterface
     * @post \return.size() <= maxDataLimit
     */
    private Collection<GeometryInterface> getData() {
        return this.data;
    }

    @Override
    public void insert(GeometryInterface node) throws NullPointerException {
        if (node == null) {
            throw new NullPointerException("QuadTree.insert() null node provided");
        }
        if (!intersects(node)) { // if node is not in this quad tree
            return; // do nothing
        }
        this.count++; // subtree wil contain node, so increment
        if (this.leaf) { // if this is a leaf
            this.data.add(node);
            if (this.data.size() > this.dataLimit) { // over the limit
                subDivide(); // make children
            }
        } else { // not a leaf, so insert in subrees
            NW.insert(node);
            NE.insert(node);
            SE.insert(node);
            SW.insert(node);
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
        // not supported yet or at all, needs discussion
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
            for (GeometryInterface leave : getData())
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
