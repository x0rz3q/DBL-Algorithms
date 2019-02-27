package Collections;

import interfaces.models.GeometryInterface;
import models.Rectangle;

import java.util.ArrayList;
import java.util.Collection;

public class QuadTree extends AbstractCollection {
    private static final int QT_NODE_CAPACITY = 4;

    private Rectangle boundary;
    private QuadTree NW = null;
    private QuadTree NE = null;
    private QuadTree SE = null;
    private QuadTree SW = null;
    private Collection<GeometryInterface> data;

    public QuadTree(Rectangle boundary) {
        this.boundary = boundary;
        this.data = new ArrayList<>();
    }

    public QuadTree(Rectangle bbox, Collection<? extends GeometryInterface> nodes) {
        this(bbox);

        for (GeometryInterface node : nodes) {
            this.insert(node);
        }
    }

    private void subdivide() {
        this.NW = new QuadTree(new Rectangle(
                this.boundary.getBottomLeft().getX(),
                this.boundary.getCenter().getY(),
                this.boundary.getCenter().getX(),
                this.boundary.getTopRight().getY()
        ));

        this.NE = new QuadTree(new Rectangle(
                this.boundary.getCenter(),
                this.boundary.getTopRight()
        ));

        this.SW = new QuadTree(new Rectangle(
                this.boundary.getBottomLeft(),
                this.boundary.getCenter()
        ));

        this.SE = new QuadTree(new Rectangle(
                this.boundary.getCenter().getX(),
                this.boundary.getBottomLeft().getY(),
                this.boundary.getBottomRight().getX(),
                this.boundary.getCenter().getY()
        ));
    }

    public boolean insert(GeometryInterface square) {
        if (!this.boundary.intersects(square))
            return false;

        if (this.data.size() == QT_NODE_CAPACITY && this.NW == null) {
            this.subdivide();
        }

        int intersectMultiple = 0;

        if (this.NW != null) {
            intersectMultiple += this.NE.intersects(square) ? 1 : 0;
            intersectMultiple += this.NW.intersects(square) ? 1 : 0;
            intersectMultiple += this.SW.intersects(square) ? 1 : 0;
            intersectMultiple += this.SE.intersects(square) ? 1 : 0;
        }

        if (intersectMultiple > 1 || this.data.size() < QT_NODE_CAPACITY) {
            this.data.add(square);
        } else {
            if (this.NW.insert(square)) return true;
            if (this.NE.insert(square)) return true;
            if (this.SE.insert(square)) return true;

            return this.SW.insert(square);
        }

        return true;
    }

    @Override
    public boolean remove(GeometryInterface node) throws NullPointerException {
        if (!this.intersects(node)) { // if node in subtree
            return false;
        }

        if (this.data.contains(node)) {
            this.data.remove(node);
            return true;
        }
        if (this.NE == null) return false; // doesnt contain
        /* not in this node, look in children */
        return this.NE.remove(node) || this.NW.remove(node) || this.SE.remove(node) || this.SW.remove(node);
    }

    public Collection<GeometryInterface> query2D(Rectangle range) {
        Collection<GeometryInterface> data = new ArrayList<>();

        if (!this.intersects(range))
            return data;

        for (GeometryInterface square : this.data) {
            if (range.intersects(square))
                data.add(square);
        }

        if (this.NW == null) {
            return data;
        }

        data.addAll(this.NW.query2D(range));
        data.addAll(this.NE.query2D(range));
        data.addAll(this.SE.query2D(range));
        data.addAll(this.SW.query2D(range));

        return data;
    }

    public boolean intersects(GeometryInterface square) {
        return this.boundary.intersects(square);
    }

    @Override
    public boolean intersects(Rectangle node) {
        return this.boundary.intersects(node);
    }

    @Override
    public int size() {
        int result = this.data.size();

        return this.NW == null ? result : result + this.NW.size() + this.NE.size() + this.SW.size() + this.SE.size();
    }

    @Override
    public int getSize() {
        return size();
    }
}