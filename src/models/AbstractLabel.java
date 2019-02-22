package models;

import interfaces.models.GeometryInterface;
import interfaces.models.LabelInterface;
import interfaces.models.PointInterface;

public abstract class AbstractLabel implements LabelInterface {
    protected Rectangle rectangle;
    protected int ID;
    protected double aspectRation;
    protected PointInterface poi;
    protected double height;

    public AbstractLabel(double x, double y, double size, double aspectRation, int ID) {
        this.poi = new Point(x, y);
        this.ID = ID;
        this.aspectRation = aspectRation;
    }

    @Override
    public PointInterface getPOI() {
        return this.poi;
    }

    public boolean equals(LabelInterface square) {
        return super.equals(square) && this.getPOI().equals(square.getPOI());
    }

    @Override
    public Integer getID() {
        return ID;
    }

    @Override
    public PointInterface getBottomLeft() {
        return this.rectangle.getBottomLeft();
    }

    @Override
    public PointInterface getBottomRight() {
        return this.rectangle.getBottomRight();
    }

    @Override
    public PointInterface getTopLeft() {
        return this.rectangle.getTopLeft();
    }

    @Override
    public PointInterface getTopRight() {
        return this.rectangle.getTopRight();
    }

    @Override
    public PointInterface getCenter() {
        return this.rectangle.getCenter();
    }

    @Override
    public boolean equals(GeometryInterface geometry) {
        return this.rectangle.equals(geometry);
    }

    @Override
    public double getXMax() {
        return this.rectangle.getXMax();
    }

    @Override
    public double getXMin() {
        return this.rectangle.getXMin();
    }

    @Override
    public double getYMax() {
        return this.rectangle.getYMax();
    }

    @Override
    public double getYMin() {
        return this.rectangle.getYMin();
    }

    @Override
    public boolean intersects(GeometryInterface geometry) {
        return this.rectangle.intersects(geometry);
    }

    @Override
    public boolean touch(GeometryInterface geometry) {
        return this.rectangle.touch(geometry);
    }

    @Override
    public boolean intersectOrTouch(GeometryInterface geometry) {
        return this.rectangle.intersectOrTouch(geometry);
    }

    /**
     * Set the height of the label.
     *
     * @param height double
     * @post {@code this.height == height && this.width == height * this.aspectRatio}
     */
    public void setHeight(double height) {
        this.height = height;
    }

    /**
     * Set the width of the label.
     *
     * @param width double
     * @post {@code this.height == width / this.aspectRatio && this.width == width}
     */
    public void setWidth(double width) {
        this.height = width / this.aspectRation;
    }
}
