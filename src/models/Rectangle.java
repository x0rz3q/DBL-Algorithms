package models;

import interfaces.models.GeometryInterface;
import interfaces.models.PointInterface;

public class Rectangle implements GeometryInterface {
    private PointInterface bottomLeft;
    private PointInterface topRight;

    public Rectangle(double xMin, double yMin, double xMax, double yMax) {
        this.bottomLeft = new Point(xMin, yMin);
        this.topRight = new Point(xMax, yMax);
    }

    public Rectangle(PointInterface bottomLeft, PointInterface topRight) {
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;
    }

    @Override
    public PointInterface getBottomLeft() {
        return this.bottomLeft;
    }

    @Override
    public PointInterface getBottomRight() {
        return new Point(this.topRight.getX(), this.bottomLeft.getY());
    }

    @Override
    public PointInterface getTopLeft() {
        return new Point(this.bottomLeft.getX(), this.topRight.getY());
    }

    @Override
    public PointInterface getTopRight() {
        return this.topRight;
    }

    @Override
    public PointInterface getCenter() {
        double x = (this.getXMax() - this.getXMin()) / 2;
        double y = (this.getYMax() - this.getYMin()) / 2;

        return new Point(x, y);
    }

    @Override
    public boolean equals(GeometryInterface geometry) {
        return this.getBottomLeft().equals(geometry.getBottomLeft()) &&
               this.getBottomRight().equals(geometry.getTopRight());
    }

    @Override
    public double getXMax() {
        return this.getBottomRight().getXMax();
    }

    @Override
    public double getXMin() {
        return this.getBottomLeft().getXMin();
    }

    @Override
    public double getYMax() {
        return this.getTopRight().getYMax();
    }

    @Override
    public double getYMin() {
        return this.getBottomLeft().getYMin();
    }

    @Override
    public boolean intersects(GeometryInterface geometry) {
        return this.intersects(geometry.getXMin(), geometry.getYMin(),
                                geometry.getXMax(), geometry.getYMax());
    }

    public boolean intersects(double xMin, double yMin, double xMax, double yMax) {
        return this.getXMin() < xMax && xMin < this.getXMax() &&
                this.getYMin() < yMax && yMin < this.getYMax();
    }

    @Override
    public boolean touch(GeometryInterface geometry) {
        return this.touch(geometry.getXMin(), geometry.getYMin(),
                geometry.getXMax(), geometry.getYMax());
    }

    public boolean touch(double xMin, double yMin, double xMax, double yMax) {
        return (this.getYMin() <= yMax && this.getYMax() >= yMin &&
                (this.getXMin() == xMax || this.getXMax() == xMin))
                || (this.getXMin() <= xMax && this.getXMax() >= xMin &&
                (this.getYMin() == yMax || this.getYMax() == yMin));
    }

    @Override
    public boolean intersectOrTouch(GeometryInterface geometry) {
        return this.intersects(geometry) || this.touch(geometry);
    }

    public boolean intersectOrTouch(double xMin, double yMin, double xMax, double yMax) {
        return this.intersects(xMin, yMin, xMax, yMax) &&
               this.touch(xMin, yMin, xMax, yMax);
    }

    public double getWidth() {
        return Math.abs(this.getXMax() - this.getXMin());
    }

    public double getHeight() {
        return Math.abs(this.getYMax() - this.getYMin());
    }
}
