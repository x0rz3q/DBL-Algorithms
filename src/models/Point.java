package models;

import interfaces.models.GeometryInterface;
import interfaces.models.PointInterface;

public class Point implements PointInterface {
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public double getX() {
        return this.x;
    }

    @Override
    public double getY() {
        return this.y;
    }

    @Override
    public double getDistanceTo(PointInterface point) {
        return this.getDistanceTo(point.getX(), point.getY());
    }

    @Override
    public double getDistanceTo(double x, double y) {
        return Math.sqrt(Math.pow(x - this.x, 2) + Math.pow(this.y - y, 2));
    }

    @Override
    public PointInterface getBottomLeft() {
        return this;
    }

    @Override
    public PointInterface getBottomRight() {
        return this;
    }

    @Override
    public PointInterface getTopLeft() {
        return this;
    }

    @Override
    public PointInterface getTopRight() {
        return this;
    }

    @Override
    public PointInterface getCenter() {
        return this;
    }

    @Override
    public boolean equals(GeometryInterface geometry) {
        return geometry.getXMax() == this.getXMax() &&
                geometry.getXMin() == this.getXMin() &&
                geometry.getYMax() == this.getYMax() &&
                geometry.getYMin() == this.getYMin();
    }

    @Override
    public double getXMax() {
        return this.getX();
    }

    @Override
    public double getXMin() {
        return this.getX();
    }

    @Override
    public double getYMax() {
        return this.getY();
    }

    @Override
    public double getYMin() {
        return this.getY();
    }

    @Override
    public boolean intersects(GeometryInterface geometry) {
        return this.equals(geometry);
    }

    @Override
    public boolean touch(GeometryInterface geometry) {
        return this.equals(geometry);
    }

    @Override
    public boolean intersectOrTouch(GeometryInterface geometry) {
        return this.equals(geometry);
    }
}
