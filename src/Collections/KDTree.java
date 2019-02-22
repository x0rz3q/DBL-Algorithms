package Collections;

import distance.AbstractDistance;
import interfaces.models.GeometryInterface;
import interfaces.models.PointInterface;
import models.Rectangle;

import java.util.*;

/**
 * Implementation of KDTree with only 2 Dimensions
 * @author Juris
 */
public class KDTree extends AbstractCollection {
    /** depth of the root/leaf of the KDTree **/
    int depth; // depth == 0 for root.
    /** children of this tree **/
    KDTree left;
    KDTree right;
    /**  SquareInterfaces anchor on which the split happens **/
    PointInterface splitter;
    /** comparators for sorting collections of SquareInterface **/
    private Comparator<GeometryInterface> verticalC = verticalComparator();
    private Comparator<GeometryInterface> horizontalC = horizontalComparator();

    /**
     * Constructor for initializing a non-leaf KDTree (e.g. root).
     * used by builder.
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
     * @param nodes Collection of {@link GeometryInterface} to be contained in the tree
     */
    public KDTree(List<GeometryInterface> nodes, int limit) {
        this(nodes,0, limit);
    }

    /**
     * Builds a subtree
     * @param nodes what nodes to put in this subtree
     * @param depth depth of the root
     * @return
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
            int medianIndex = (int) Math.floor(nodes.size() / 2);
            splitter = nodes.get(medianIndex).getBottomLeft();
            left = new KDTree(nodes.subList(0, medianIndex), depth + 1, this.dataLimit);
            right = new KDTree(nodes.subList(medianIndex, nodes.size()), depth + 1, this.dataLimit);
        }
    }
    /**
     * Provides a SquareInterface Comparator for sorting anchors vertically.
     * @return Comparator sorting by Y anchor coordinate
     */
    private static Comparator<GeometryInterface> verticalComparator(){
        return new Comparator<GeometryInterface>() {
            public int compare(GeometryInterface s1, GeometryInterface s2) {
                return Double.compare(s1.getBottomLeft().getY(), s2.getBottomLeft().getY());
            }
        };
    }
    /**
     * Provides a SquareInterface Comparator for sorting anchors horizontally.
     * @return Comparator sorting by X anchor coordinate
     */
    private static Comparator<GeometryInterface> horizontalComparator() {
        return new Comparator<GeometryInterface>() {
            public int compare(GeometryInterface s1, GeometryInterface s2) {
                return Double.compare(s1.getBottomLeft().getX(), s2.getBottomLeft().getX());
            }
        };
    }

    @Override
    public boolean insert(GeometryInterface node) throws NullPointerException {
        if (node == null) {
            throw new NullPointerException(this.getClass().toString() + ".insert() got null element");
        }
        this.count ++;

        if (splitter == null || this.intersects(node)) { // leaf or on splitter line
            if (splitter == null && this.nodes.size() == this.dataLimit) { // leaf is full
                List<GeometryInterface> solo = new ArrayList<>();
                solo.add(node); // pass on this node to its children
                buildTree(solo, this.depth);
            } else { // not full, add it to the list
                this.nodes.add(node);
            }

        } else { // only in one or none
            PointInterface btmLeft = node.getBottomLeft();
            double rangeDimension,  splitterDimension; // dimensions to check for node and the splitter

            if (this.depth % 2 == 0) { // vertical check
                rangeDimension = btmLeft.getY();
                splitterDimension = this.splitter.getY();
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
     * @param subTree subtree on which query is done
     * @param range range in which to search for elements
     * @return Collection of SquareInterfaces such that range.intersects(element)
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
            PointInterface btmLeft = range.getBottomLeft();
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

    /**
     * Gives a set of elements in the tree such that they are n nearest neighbours according to
     * the distance function
     * @throws IllegalArgumentException if n > this.size()
     * @param dist distance function for calculating nearby points
     * @param n amount of neighbours to return
     * @param node node too look for the neighbours around for
     * @return set of SquareInterface s.t. closest n neighbours
     */
    public Set<GeometryInterface> nearestNeighbours(AbstractDistance dist, int n, GeometryInterface node) throws IllegalArgumentException {
        if (n > this.size()) {
            throw new IllegalArgumentException("KDTree.nearestNeighbours asked for "+ n + " neighbours for tree of size " + size() + ".");
        }
        /* closest n neighbours of node */
        Set<GeometryInterface> neighbours = new HashSet<GeometryInterface>();
        /* closest distance */
        double cd;
        /* closest node with distance cd */
        GeometryInterface cn;
        for (int i = 0; i < n; i++) { // do nearest neighbour n times, ignoring the ones in the set
            cd = Double.MAX_VALUE; // default values
            cn = null;
            /* check for closer stuff in root */
            if (!this.nodes.isEmpty()) { 
                 for (GeometryInterface o : this.nodes) {
                    if (!neighbours.contains(o)) { // if not a neighbour already
                         double newDist = dist.calculate(node.getBottomLeft(), o.getBottomLeft()); // calc distance
                         if (newDist < cd) { // if better, update
                            cd = newDist;
                            cn = o;
                         }
                    }
                 }
            }
            neighbours.add(nearest(dist, node, cd, cn, neighbours)); 
        } 
        return neighbours;
    }
    /**
     * @param dist distance function
     * @param node object to look for nearest neighbour
     * @param cd current closest distance
     * @param cn current closest node
     * @param ignorables nodes to ingore during the search
     */
    private GeometryInterface nearest(AbstractDistance dist, GeometryInterface node, double cd, GeometryInterface cn, Set<GeometryInterface> ignorables) {
        return cn; 
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
