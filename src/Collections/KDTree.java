package Collections;

import distance.AbstractDistance;
import interfaces.models.GeometryInterface;
import interfaces.models.PointInterface;
import models.Point;
import models.Rectangle;

import java.util.*;

/**
 * Implementation of KDTree with only 2 Dimensions
 *
 * @author Juris
 */
public class KDTree extends AbstractCollection {
    /**
     * depth of the root/leaf of the KDTree
     **/
    int depth; // depth == 0 for root.
    /**
     * children of this tree
     **/
    KDTree left;
    KDTree right;
    /**
     * SquareInterfaces anchor on which the split happens
     **/
    PointInterface splitter;
    /**
     * comparators for sorting collections of SquareInterface
     **/
    private Comparator<GeometryInterface> verticalC = verticalComparator();
    private Comparator<GeometryInterface> horizontalC = horizontalComparator();

    /**
     * Constructor for initializing a non-leaf KDTree (e.g. root).
     * used by builder.
     *
     * @param nodes Collection of {@link GeometryInterface} to be contained in the tree
     * @param depth Depth of this root.
     */
    private KDTree(List<GeometryInterface> nodes, int depth, int limit) {
        this.nodes = new ArrayList<>();
        this.count = nodes.size();
        this.depth = depth;
        setDataLimit(limit);
        buildTree(nodes, depth);
    }

    /**
     * Constructor for initializing the root of KDTree.
     * used by client.
     *
     * @param nodes Collection of {@link GeometryInterface} to be contained in the tree
     */
    public KDTree(List<GeometryInterface> nodes, int limit) {
        this(nodes, 0, limit);
    }

    /**
     * Constructor without parameters for initializing empty KDTree object
     * used by client
     */
    public KDTree() {
        this(new ArrayList<>(), 0, 1);
    }

    /**
     * Provides a SquareInterface Comparator for sorting anchors vertically.
     *
     * @return Comparator sorting by Y anchor coordinate
     */
    private static Comparator<GeometryInterface> verticalComparator() {
        return new Comparator<GeometryInterface>() {
            public int compare(GeometryInterface s1, GeometryInterface s2) {
                return -1 * Double.compare(s1.getBottomLeft().getY(), s2.getBottomLeft().getY());
            }
        };
    }

    /**
     * Provides a SquareInterface Comparator for sorting anchors horizontally.
     *
     * @return Comparator sorting by X anchor coordinate
     */
    private static Comparator<GeometryInterface> horizontalComparator() {
        return new Comparator<GeometryInterface>() {
            public int compare(GeometryInterface s1, GeometryInterface s2) {
                return Double.compare(s1.getBottomLeft().getX(), s2.getBottomLeft().getX());
            }
        };
    }

    /**
     * Finds nearest neighbour of node n
     *
     * @param dist       distance function
     * @param t          tree in which to search
     * @param node       object to look for nearest neighbour
     * @param cd         current closest distance
     * @param cn         current closest node
     * @param ignorables nodes to ingore during the search
     */
    private static GeometryInterface nearest(KDTree t, AbstractDistance dist, GeometryInterface node,
                                             Double cd, GeometryInterface cn, Set<GeometryInterface> ignorables) {
        // else
        /* check for closer stuff in t */
        for (GeometryInterface o : t.nodes) {
            if (!ignorables.contains(o) && !o.equals(node)) { // if not a neighbour already
                Double newDist = dist.calculate(node.getBottomLeft(), o.getBottomLeft()); // calc distance
                if (newDist < cd) { // if better, update
                    cd = newDist;
                    cn = o;
                }
            }
        }
        if (t.isLeaf()) { // at leaf, have inspected nodes already, so return cur best
            return cn;
        } // not at leaf
        // go down deeper
        if (t.inLeft(node)) { // node in left
            // search left first
            cn = nearest(t.left, dist, node, cd, cn, ignorables);
            // update cd
            if (cn != null) cd = dist.calculate(cn.getBottomLeft(), node.getBottomLeft());
            /* if node is a splitter of this tree or not (a leaf and no data stored and bounding box further away than current smallest dist) */
            if (node.getBottomLeft().equals(t.splitter) ||
                    !(t.right.isLeaf() && t.right.nodes.isEmpty() && t.right.distanceToSplitter(node, dist) > cd)) {
                cn = nearest(t.right, dist, node, cd, cn, ignorables);
            }
        } else { // node in right
            // search right first
            cn = nearest(t.right, dist, node, cd, cn, ignorables);
            // update cd
            if (cn != null) cd = dist.calculate(cn.getBottomLeft(), node.getBottomLeft());
            /* if node is a splitter of this tree or not (a leaf and no data stored and bounding box further away than current smallest dist) */
            if (node.getBottomLeft().equals(t.splitter) ||
                    !(t.left.isLeaf() && t.left.nodes.isEmpty() && t.left.distanceToSplitter(node, dist) > cd)) {
                cn = nearest(t.left, dist, node, cd, cn, ignorables);
            }
        }
        return cn;
    }

    /**
     * Builds a subtree
     *
     * @param nodes what nodes to put in this subtree
     * @param depth depth of the root
     */
    private void buildTree(List<GeometryInterface> nodes, int depth) {
        if (nodes.size() + this.nodes.size() <= this.dataLimit) { // if below data limit
            this.nodes.addAll(nodes);
            this.depth = depth;
        } else {
            if (depth % 2 == 0) { // even depth
                // split vertically
                java.util.Collections.sort(nodes, verticalC);
            } else { // odd depth

                // split horizontally
                java.util.Collections.sort(nodes, horizontalC);
            }
            int medianIndex = (int) Math.floor(nodes.size() / 2.0);
            splitter = nodes.get(medianIndex).getBottomLeft();
            left = new KDTree(nodes.subList(0, medianIndex), depth + 1, this.dataLimit);
            right = new KDTree(nodes.subList(medianIndex, nodes.size()), depth + 1, this.dataLimit);
        }
    }

    @Override
    public boolean insert(GeometryInterface node) throws NullPointerException {
        if (node == null) {
            throw new NullPointerException(this.getClass().toString() + ".insert() got null element");
        }
        this.count++;

        if (this.isLeaf() || this.intersects(node)) { // leaf or on splitter line
            if (this.isLeaf() && this.nodes.size() == this.dataLimit) { // leaf is full
                List<GeometryInterface> solo = new ArrayList<>();
                solo.add(node); // pass on this node to its children
                buildTree(solo, this.depth);
            } else { // not full, add it to the list
                this.nodes.add(node);
            }
        } else { // only in one or none
            PointInterface btmLeft = node.getBottomLeft();
            double rangeDimension, splitterDimension; // dimensions to check for node and the splitter

            if (this.depth % 2 == 0) { // vertical check
                /* left and right is inverted in Y axis */
                rangeDimension = -1 * Math.abs(btmLeft.getY());
                splitterDimension = -1 * Math.abs(this.splitter.getY());
            } else { // horizontal check
                rangeDimension = btmLeft.getX();
                splitterDimension = this.splitter.getX();
            }

            if (rangeDimension < splitterDimension) { // insert in left
                this.left.insert(node);
            } else { // insert in right subtree
                this.right.insert(node);
            }
        }
        return true; // always can insert
    }

    @Override
    public Collection<GeometryInterface> query2D(Rectangle range) {
        return query2D(this, range);
    }

    /**
     * Search for elements in some range
     *
     * @param subTree subtree on which query is done
     * @param range   range in which to search for elements
     * @return collection of SquareInterfaces such that range.intersects(element)
     */
    private Collection<GeometryInterface> query2D(KDTree subTree, Rectangle range) {
        Collection<GeometryInterface> leavesInRange = new ArrayList<>();
        for (GeometryInterface d : subTree.nodes) { // add the data which intersects
            if (range.intersects(d)) {
                leavesInRange.add(d);
            }
        }
        if (subTree.left == null) { // leaf, nowhere to go
            return leavesInRange; // return what we have
        }
        if (subTree.intersects(range)) { // intersects the splitter line
            // query both children
            leavesInRange.addAll(query2D(subTree.left, range));
            leavesInRange.addAll(query2D(subTree.right, range));
        } else { // otherwise only on one side check which to query
            if (subTree.inLeft(range)) {
                leavesInRange.addAll(query2D(subTree.left, range));
            } else {
                leavesInRange.addAll(query2D(subTree.right, range));
            }
        }
        return leavesInRange;
    }

    /**
     * Gives a set of elements in the tree such that they are n nearest neighbours according to
     * the distance function
     *
     * @param dist distance function for calculating nearby points
     * @param n    amount of neighbours to return
     * @param node node too look for the neighbours around for
     * @return set of SquareInterface s.t. closest n neighbours
     * @throws IllegalArgumentException if n >= this.size()
     */
    public Set<GeometryInterface> nearestNeighbours(AbstractDistance dist, int n,
                                                    GeometryInterface node) throws IllegalArgumentException {
        if (n >= this.size()) {
            throw new IllegalArgumentException("KDTree.nearestNeighbours asked for " + n + " neighbours for tree of size " + size() + ".");
        }
        /* closest n neighbours of node */
        Set<GeometryInterface> neighbours = new HashSet<>();
        /* closest distance */
        Double cd;
        /* closest node with distance cd */
        GeometryInterface cn;
        for (int i = 0; i < n; i++) { // do nearest neighbour n times, ignoring the ones in the set
            cd = Double.MAX_VALUE; // default values
            cn = null;
            /* add nearest neighbour s.t. not in neighbours already */
            neighbours.add(nearest(this, dist, node, cd, cn, neighbours));
        }
        return neighbours;
    }

    /**
     * Checks if node is in left subtree
     * @throws IllegalArgumentException if this.intersects(node)
     * @param node the element to search for
     * @pre !this.intersects(node)
     * @return whether the node is located in this.left subtree
     */
    private boolean inLeft(GeometryInterface node) throws IllegalArgumentException {
        if (this.intersects(node)) throw new IllegalArgumentException("KDTree.inLeft called " +
                "on node which is in both subtrees");
        if (this.isLeaf()) return false;
        double rangeDimension, splitterDimension;
        if (this.depth % 2 == 0) {
            /* left and right is inverted in Y axis */
            rangeDimension = -1 * Math.abs(node.getBottomLeft().getY());
            splitterDimension = -1 * Math.abs(this.splitter.getY());
        } else { // horizontal check
            rangeDimension = node.getBottomLeft().getX();
            splitterDimension = this.splitter.getX();
        }

        return rangeDimension < splitterDimension;
    }

    /**
     * Checks whether this tree is a leaf
     * @return true if {@code this} is a leaf
     */
    public boolean isLeaf() {
        return this.splitter == null;
    }

    /**
     * Calculates distance to the splitter line
     */
    private double distanceToSplitter(GeometryInterface node, AbstractDistance dist) {
        PointInterface projection;
        if (this.depth % 2 == 0) {
            projection = new Point(node.getBottomLeft().getX(), this.splitter.getY());
        } else {
            projection = new Point(this.splitter.getX(), node.getBottomLeft().getY());
        }
        return dist.calculate(projection, node.getBottomLeft());
    }

    @Override
    protected boolean intersects(GeometryInterface node) {
        return intersects(new Rectangle(node.getXMin(), node.getYMin(), node.getXMax(), node.getYMax()));
    }

    /**
     * @param node {@link Rectangle}
     * @return true if node is on the splitter line
     */
    @Override
    protected boolean intersects(Rectangle node) {
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

