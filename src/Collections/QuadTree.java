package Collections;

import interfaces.models.SquareInterface;
import models.BoundingBox;

import java.util.ArrayList;
import java.util.Collection;

public class QuadTree extends AbstractCollection {
    private static final int QT_NODE_CAPACITY = 4;

    private BoundingBox boundary;
    private QuadTree NW = null;
    private QuadTree NE = null;
    private QuadTree SE = null;
    private QuadTree SW = null;
    private Collection<SquareInterface> data;

    public QuadTree(BoundingBox boundary) {
        this.boundary = boundary;
        this.data = new ArrayList<>();
    }

    public QuadTree(BoundingBox bbox, Collection<? extends SquareInterface> nodes) {
        this(bbox);

        for(SquareInterface node : nodes) {
            this.insert(node);
        }
    }

    public QuadTree(SquareInterface square) {
        this(new BoundingBox(square));
    }

    public QuadTree(SquareInterface square, Collection<? extends SquareInterface> nodes) {
        this(new BoundingBox(square), nodes);
    }

    private void subdivide() {
        this.NW = new QuadTree(new BoundingBox (
            this.boundary.getBottomLeft().getX(),
            this.boundary.getCenter().getY(),
            this.boundary.getCenter().getX(),
            this.boundary.getTopRight().getY()
        ));

        this.NE = new QuadTree(new BoundingBox (
            this.boundary.getCenter(),
            this.boundary.getTopRight()
        ));

        this.SW = new QuadTree(new BoundingBox (
            this.boundary.getBottomLeft(),
            this.boundary.getCenter()
        ));

        this.SE = new QuadTree(new BoundingBox (
            this.boundary.getCenter().getX(),
            this.boundary.getBottomLeft().getY(),
            this.boundary.getBottomRight().getX(),
            this.boundary.getCenter().getY()
        ));
    }

    public Boolean insert(SquareInterface square) {
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
    public void remove(SquareInterface node) throws NullPointerException {

    }

    @Override
    public Collection<SquareInterface> query2D(SquareInterface range) {
        return this.query2D(new BoundingBox(range));
    }

    public Collection<SquareInterface> query2D(BoundingBox bbox) {
        Collection<SquareInterface> data = new ArrayList<>();

        for (SquareInterface square : this.data) {
            if (bbox.intersects(square))
                data.add(square);
        }

        if (this.NW == null) {
            return data;
        }

        data.addAll(this.NW.query2D(bbox));
        data.addAll(this.NE.query2D(bbox));
        data.addAll(this.SE.query2D(bbox));
        data.addAll(this.SW.query2D(bbox));

        return data;
    }

    public Boolean intersects(SquareInterface square) {
        return this.boundary.intersects(square);
    }

    @Override
    public Boolean intersects(BoundingBox node) {
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