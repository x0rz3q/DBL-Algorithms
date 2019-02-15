package models;

import interfaces.models.SquareInterface;

public class BoundingBox {
    private Point bottomLeft;
    private Point topRight;

    public BoundingBox(Point bottomLeft, Point topRight) {
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;
    }

    public BoundingBox(double xMin, double yMin, double xMax, double yMax) {
        this.bottomLeft = new Point(xMin, yMin);
        this.topRight = new Point(xMax, yMax);
    }

    public BoundingBox(SquareInterface square) {
        this(square.getXMin(), square.getYMin(), square.getXMax(), square.getYMax());
    }

    public Point getBottomLeft() {
        return bottomLeft;
    }

    public Point getTopRight() {
        return topRight;
    }

    public Point getBottomRight() {
        return new Point(this.getTopRight().getX(), this.getBottomLeft().getY());
    }

    public Point getTopLeft() {
        return new Point(this.getBottomLeft().getX(), this.getTopRight().getY());
    }

    public Point getCenter() {
        Double x = this.getTopRight().getX() - (this.getTopRight().getX() - this.getBottomLeft().getX()) / 2;
        Double y = this.getTopRight().getY() - (this.getTopRight().getY() - this.getBottomLeft().getY()) / 2;

        return new Point(x, y);
    }

    public Double getXMin() {
        return this.getBottomLeft().getX();
    }

    public Double getYMin() {
        return this.getBottomLeft().getY();
    }

    public Double getXMax() {
        return this.topRight.getX();
    }

    public Double getYMax() {
        return this.topRight.getY();
    }

    public Boolean intersects(double xMin, double yMin, double xMax, double yMax) {
        return this.getXMin() < xMax && xMin < this.getXMax() &&
                this.getYMin() < yMax && yMin < this.getYMax();
    }

    public Boolean intersects(BoundingBox bbox) {
        return this.intersects(bbox.getXMin(), bbox.getYMin(), bbox.getXMax(), bbox.getYMax());
    }

    public Boolean intersects(SquareInterface square) {
        return this.intersects(square.getXMin(), square.getYMin(), square.getXMax(), square.getYMax());
    }

    public Boolean touch(SquareInterface square) {
        return this.touch(square.getXMin(), square.getYMin(), square.getXMax(), square.getYMax());
    }

    public Boolean touch(BoundingBox bbox) {
        return this.touch(bbox.getXMin(), bbox.getYMin(), bbox.getXMax(), bbox.getYMax());
    }

    public Boolean touch(double xMin, double yMin, double xMax, double yMax) {
        return (this.getYMin() <= yMax && this.getYMax() >= yMin &&
                (this.getXMin().equals(xMax) || this.getXMax().equals(xMin)))
                || (this.getXMin() <= xMax && this.getXMax() >= xMin &&
                (this.getYMin().equals(yMax) || this.getYMax().equals(yMin)));
    }

    public Boolean intersectOrTouch(SquareInterface square) {
        return this.touch(square.getXMin(), square.getYMin(), square.getXMax(), square.getYMax()) ||
                this.intersects(square.getXMin(), square.getYMin(), square.getXMax(), square.getYMax());
    }

    public Boolean intersectOrTouch(BoundingBox bbox) {
        return this.touch(bbox.getXMin(), bbox.getYMin(), bbox.getXMax(), bbox.getYMax()) ||
            this.intersects(bbox.getXMin(), bbox.getYMin(), bbox.getXMax(), bbox.getYMax());
    }

    public Boolean intersectOrTouch(double xMin, double yMin, double xMax, double yMax) {
        return this.intersects(xMin, yMin, xMax, yMax) ||
                this.touch(xMin, yMin, xMax, yMax);
    }
}
