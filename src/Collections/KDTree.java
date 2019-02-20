package Collections;

import interfaces.models.AnchorInterface;
import interfaces.models.SquareInterface;
import models.BoundingBox;
import models.Point;
import models.Square;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

public class KDTree extends AbstractCollection{
    /** depth of the root/leaf of the KDTree **/
    int depth; // depth == 0 for root.
    /** children of this tree **/
    KDTree left;
    KDTree right;
    /** node for on which the split happens **/
    AnchorInterface splitter;
    /** comparators for sorting collections of SquareInterface **/
    private Comparator<SquareInterface> verticalC = verticalComparator();
    private Comparator<SquareInterface> horizontalC = horizontalComparator();

    /**
     * Constructor for initializing a non-leaf KDTree (e.g. root).
     * used by builder.
     * @param nodes Collection of {@link SquareInterface} to be contained in the tree
     * @param depth Depth of this root.
     */
    private KDTree(List<SquareInterface> nodes, int depth, int limit) {
        this.count = nodes.size();
        this.nodes = new ArrayList<>();
        this.depth = depth;
        setDataLimit(limit);
        buildTree(nodes, depth);
    }
    /**
     * Constructor for initializing a non-leaf KDTree (e.g. root).
     * used by client.
     * @param nodes Collection of {@link SquareInterface} to be contained in the tree
     */
    public KDTree(List<SquareInterface> nodes, int limit) {
        this.count = nodes.size();
        this.depth = 0;
        setDataLimit(limit);
        buildTree(nodes, depth);
    }

    /**
     * Builds a subtree
     * @param nodes what nodes to put in this subtree
     * @param depth depth of the root
     * @return
     */
    private void buildTree(List<SquareInterface> nodes, int depth) {
        if (nodes.size() <= this.dataLimit) { // if below data limit
            this.nodes = nodes;
            this.depth = depth;
        } else {
            if (depth % 2 == 0) { // even depth
               // split vertically
               java.util.Collections.sort(nodes, verticalC);
            } else { // odd depth

                // split horizontally
                java.util.Collections.sort(nodes, horizontalC);
            }
            int medianIndex = (int) Math.floor(nodes.size() / 2);
            splitter = nodes.get(medianIndex).getAnchor();
            left = new KDTree(nodes.subList(0, medianIndex), depth + 1, this.dataLimit);
            right = new KDTree(nodes.subList(medianIndex, nodes.size()), depth + 1, this.dataLimit);
        }
    }
    /**
     * Provides a SquareInterface Comparator for sorting anchors vertically.
     * @return Comparator sorting by Y anchor coordinate
     */
    private static Comparator<SquareInterface> verticalComparator(){
        return new Comparator<SquareInterface>() {
            public int compare(SquareInterface s1, SquareInterface s2) {
                if (s1.getAnchor().getY() > s2.getAnchor().getY()) return 1;
                if (s1.getAnchor().getY() < s2.getAnchor().getY()) return -1;
                return 0;
            }
        };
    }
    /**
     * Provides a SquareInterface Comparator for sorting anchors horizontally.
     * @return Comparator sorting by X anchor coordinate
     */
    private static Comparator<SquareInterface> horizontalComparator(){
       return new Comparator<SquareInterface>() {
            public int compare(SquareInterface s1, SquareInterface s2) {
                if (s1.getAnchor().getX() > s2.getAnchor().getX()) return 1;
                if (s1.getAnchor().getX() < s2.getAnchor().getX()) return -1;
                return 0;
            }
        };
    }

    @Override
    public void insert(SquareInterface node) throws NullPointerException {
        if (intersects(node)) { // in both
            this.nodes.add(node); // store in root
            return;
        } else { // only in one or none
            AnchorInterface btmLeft = node.getAnchor();
            double rangeDimension,  splitterDimension; // dimensions to check for node and the splitter

            if (this.depth % 2 == 0) { // vertical check
                rangeDimension = btmLeft.getY();
                splitterDimension = this.splitter.getY();
            } else { // horizontal check
                rangeDimension = btmLeft.getX();
                splitterDimension = this.splitter.getX();
            }

            if (rangeDimension < splitterDimension) { // insert in left
                this.insertInSubtree(left, node);
            } else { // insert in right subtree
                this.insertInSubtree(right, node);
            }
        }
    }


    @Override
    public void remove(SquareInterface node) throws NullPointerException {}

    /**
     * Inserts a node in a subtree
     * @param subTree subtree in which to insert
     * @param node node to insert in the subtree
     */
    private static void insertInSubtree(KDTree subTree, SquareInterface node) {
        if (subTree.left == null) { // is leaf
            subTree.nodes.add(node);
            if (subTree.size() > subTree.dataLimit) { // leaf is full 
                subTree = new KDTree(subTree.nodes, subTree.depth, subTree.dataLimit); 
            }
        } else { // not leaf, go down the tree
            subTree.insert(node); 
        }
    }

    @Override
    public Collection<SquareInterface> query2D(SquareInterface range) {
        return query2D(new BoundingBox(range.getXMin(), range.getYMin(), range.getXMax(), range.getYMax()));
    }

    @Override
    public Collection<SquareInterface> query2D(BoundingBox range) {
        return query2D(this, range);
    }

    private Collection<SquareInterface> query2D(KDTree subTree, BoundingBox range) {
        Collection<SquareInterface> leavesInRange = new ArrayList<>();
        for (SquareInterface d : subTree.nodes) { // add the data which intersects
            if (range.intersects(d)) {
                leavesInRange.add(d);
            }
        }
        if (subTree.left == null) { // leaf, nowhere to go
            return leavesInRange;
        }
        if (subTree.intersects(range)) { // intersects the splitter line
            // query both children
            leavesInRange.addAll(query2D(subTree.left, range));
            leavesInRange.addAll(query2D(subTree.right, range));
        } else { // check which to query
            Point btmLeft = range.getBottomLeft();
            double rangeDimension, splitterDimension;

            if (subTree.depth % 2 == 0) { // vertical check
                rangeDimension = btmLeft.getY();
                splitterDimension = subTree.splitter.getY();
            } else { // horizontal check
                rangeDimension = btmLeft.getX();
                splitterDimension = subTree.splitter.getX();
            }
            // query according to the location of the range box
            if (rangeDimension < splitterDimension) {
                leavesInRange.addAll(query2D(subTree.left, range));
            } else {
                leavesInRange.addAll(query2D(subTree.right, range));
            }
        }
        return leavesInRange;
    }

    @Override
    public Boolean intersects(SquareInterface node) {
        return intersects(new BoundingBox(node.getXMin(), node.getYMin(), node.getXMax(), node.getYMax()));
    }

    /**
     * @param node {@link BoundingBox}
     * @return true if bounding box intersects to both children
     */
    @Override
    public Boolean intersects(BoundingBox node) {
        if (depth % 2 == 0) {
            return splitter.getY() < node.getYMax() && splitter.getY() > node.getYMin();
        } else {
            return splitter.getX() < node.getXMax() && splitter.getX() > node.getXMin();
        }
    }

    @Override
    public int size() {
        return super.getSize();
    }
}
