    package Collections;

import interfaces.models.AnchorInterface;
import interfaces.models.SquareInterface;
import models.Square;
import models.Anchor;

import java.util.ArrayList;
import java.util.Collection;

public class QuadTree extends AbstractCollection
{
    /** boundary for this specific quad tree. e.g. root has boundary {0, 10k} for x and y **/
    private SquareInterface boundary;
    /** children quadtrees **/
    private QuadTree NW;
    private QuadTree NE;
    private QuadTree SE;
    private QuadTree SW;
    /** if has not been subdivided **/
    private boolean leaf; // leaf == data.size() <= dataLimit
    /** data of this quad tree **/
    private Collection<SquareInterface> data;
    /** limit of data per tree **/
    private int dataLimit;

    public QuadTree(SquareInterface boundary) {
        super();
        this.data = new ArrayList<>();
        this.leaf = true;
        setBoundary(boundary);
    }

    public QuadTree(SquareInterface boundary, Collection<? extends SquareInterface> nodes) {
        this(boundary);
        setDataLimit(1);
        for (SquareInterface s : nodes) {
            insert(s);
        }
    }

    public QuadTree(Collection<? extends SquareInterface> nodes) {
        super();
        setDataLimit(1);
        this.data = new ArrayList<>();
        this.leaf = true;
        for (SquareInterface s : nodes) {
            insert(s);
        }
    }


    /**
     * Sets the boundary of this quad tree.
     * @param r a square
     */
    private void setBoundary(SquareInterface r) {
        this.boundary = r;
    }

    /**
     * Sets the amount of data allowed in a leaf of quadtree.
     * @param l
     * @pre {@code l > 0}
     */
    @Override
    public void setDataLimit(final int l) {
        this.dataLimit = l;
    }

    /**
     * Subdivides the QuadTree in 4 sub-trees
     * @post {@code NW <> null && NE <> null && SW <> null && SE <> null &&
     * data.size() == 0 && count == 0 && leaf == false}
     */
    private void subDivide() {
        SquareInterface NW, NE, SW, SE;
        double newWidth = this.boundary.getEdgeLength() / 2;
        AnchorInterface bottomLeft = this.boundary.getAnchor();

        NW = new Square(new Anchor(bottomLeft.getX(), bottomLeft.getY() + newWidth), newWidth);
        this.NW = subDivide(NW);
        NE = new Square(new Anchor(bottomLeft.getX() + newWidth, bottomLeft.getY() + newWidth), newWidth);
        this.NE = subDivide(NE);
        SW = new Square(bottomLeft, newWidth);
        this.SW = subDivide(SW);
        SE = new Square(new Anchor(bottomLeft.getX() + newWidth, bottomLeft.getY()), newWidth);
        this.SE = subDivide(SE);

        this.data.clear();
        this.leaf = false;
    }

    /**
     * Produce one of the 4 subdivisons with boundary b
     * @param b the boundary of the subdivision
     * @return QuadTree, the new subdivision
     */
    private QuadTree subDivide(SquareInterface b) {
        QuadTree t = new QuadTree(b, getData()); // add root data to subtree
        return t;
    }

    /**
     * Getter for data in a leaf
     * @return Collection over GeometryInterface
     * @post \return.size() <= maxDataLimit
     */
    private Collection<SquareInterface> getData() {
        return this.data;
    }

    @Override
    public void insert(SquareInterface node) throws NullPointerException {

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
    public void remove(SquareInterface node) throws NullPointerException {
        // not supported yet or at all, needs discussion
    }

    @Override
    public Collection query2D(SquareInterface range) {
        return query2D(this, range);
    }

    /**
     * retrieves all GeometryInterfaces in the intersection of given subTree and range
     *
     * @param subTree subtree that is being searched
     * @param range range that is searched for
     * @post {@code \result == (\forall i; i.intesect(subTree); i.instersects(range))}
     */
    private Collection query2D(QuadTree subTree, SquareInterface range) {
        Collection<SquareInterface> allLeaves = new ArrayList<>();
        if (subTree.leaf) {
            for (SquareInterface leave : subTree.getData())
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
    public Boolean intersects(SquareInterface node) {
        return boundary.intersects(node);
    }

    @Override
    public int size() {
        return super.getSize();
    }
}
