package models;

import interfaces.models.GeometryInterface;
import interfaces.models.PointInterface;

public class PositionLabel extends AbstractLabel {
    protected DirectionEnum direction;

    public PositionLabel(double x, double y, double size, double aspectRation, int ID, DirectionEnum direction) {
        super(x, y, size, aspectRation, ID);
        this.setDirection(direction);
    }

    public DirectionEnum getDirection() {
        return this.direction;
    }

    public void setDirection(DirectionEnum direction) throws IllegalArgumentException {
        PointInterface point = this.poi;

        switch (direction) {
            case NE:
                this.rectangle = new Rectangle(point.getX(), point.getY(),
                        point.getX() + this.rectangle.getWidth(), point.getY() + this.rectangle.getHeight());
                break;
            case NW:
                this.rectangle = new Rectangle(point.getX() - this.rectangle.getWidth(),
                                        point.getY(),
                                        point.getX(),
                                        point.getY() + this.rectangle.getHeight()
                                    );
                break;
            default:
                throw new IllegalArgumentException("Not supported direction");
        }
    }

    @Override
    public String getPlacement() {
        return this.direction.toString();
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
}
