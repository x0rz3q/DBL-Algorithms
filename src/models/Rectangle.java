package models;

import interfaces.models.GeometryInterface;
import interfaces.models.PointInterface;

import java.util.ArrayList;

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

        return new Point(this.getXMax() - x, this.getYMax() - y);
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

    /**
     * Method for checking whether rectangle collides with other rectangle
     * @pre target and object rectangle same size
     * @param target
     * @return true iff target and object rectangle (partially) coincide
     */
    public boolean checkCollision(Rectangle target) {
        if (Math.max(this.bottomLeft.getX(), target.bottomLeft.getX()) < Math.min(this.topRight.getX(), target.topRight.getX())) {
            return true;
        }
        if (Math.max(this.bottomLeft.getY(), target.bottomLeft.getY()) < Math.min(this.topRight.getY(), target.topRight.getY())) {
            return true;
        }
        return false;
    }

    /**
     * Method for finding all integer coordinate-points inside a rectangle
     * @pre sw & ne defined & sw.x < ne.x & sw.y < ne.y
     * @return array of all points with integer coordinates inside rectangle object
     */
    public Point[] getInternal() {
        ArrayList<Point> internalPoints = new ArrayList<>();
        int xLowerBound;
        int xUpperBound;
        int yLowerBound;
        int yUpperBound;
        if (this.bottomLeft.getX() == Math.ceil(this.bottomLeft.getX())) { xLowerBound = (int) this.bottomLeft.getX() + 1; } else { xLowerBound = (int) Math.ceil(this.bottomLeft.getX()); }
        if (this.topRight.getX() == Math.floor(this.topRight.getX())) { xUpperBound = (int) this.topRight.getX() - 1; } else { xUpperBound = (int) Math.floor(this.topRight.getX()); }
        if (this.bottomLeft.getY() == Math.ceil(this.bottomLeft.getY())) { yLowerBound = (int) this.bottomLeft.getY() + 1; } else { yLowerBound = (int) Math.ceil(this.bottomLeft.getY()); }
        if (this.topRight.getY() == Math.floor(this.topRight.getY())) { yUpperBound = (int) this.topRight.getY() - 1; } else { yUpperBound = (int) Math.floor(this.topRight.getY()); }

        for (int x = xLowerBound; x <= xUpperBound; x++) {
            for (int y = yLowerBound; y <= yUpperBound; y++) {
                internalPoints.add(new Point(x, y));
            }
        }

        Point[] internalPointArray = new Point[internalPoints.size()];
        internalPointArray = internalPoints.toArray(internalPointArray);
        return internalPointArray;
    }

    /**
     * Returns the integer boundary points on the specified sides
     * @param north
     * @param east
     * @param south
     * @param west
     * @return array of points containing the integer points on the specified boundaries
     */
    public Point[] getBoundary(boolean north, boolean east, boolean south, boolean west) {
        ArrayList<Point> boundaryPoints = new ArrayList<>();
        int xLowerBound = (int) Math.ceil(this.bottomLeft.getX());
        int xUpperBound = (int) Math.floor(this.topRight.getX());
        int yLowerBound = (int) Math.ceil(this.bottomLeft.getY());
        int yUpperBound = (int) Math.floor(this.topRight.getY());

        if (north && (this.topRight.getY()) == Math.floor(this.topRight.getY())) {
            for (int x = xLowerBound; x <= xUpperBound; x++) {
                boundaryPoints.add(new Point(x, this.topRight.getY()));
            }
        }
        if (east && (this.topRight.getX()) == Math.floor(this.topRight.getX())) {
            for (int y = yLowerBound; y <= yUpperBound; y++) {
                boundaryPoints.add(new Point(this.topRight.getX(), y));
            }
        }
        if (south && (this.bottomLeft.getY()) == Math.floor(this.bottomLeft.getY())) {
            for (int x = xLowerBound; x <= xUpperBound; x++) {
                boundaryPoints.add(new Point(x, this.bottomLeft.getY()));
            }
        }
        if (west && (this.bottomLeft.getX()) == Math.floor(this.bottomLeft.getX())) {
            for (int y = yLowerBound; y <= yUpperBound; y++) {
                boundaryPoints.add(new Point(this.bottomLeft.getX(), y));
            }
        }

        Point[] boundaryPointArray = new Point[boundaryPoints.size()];
        boundaryPointArray = boundaryPoints.toArray(boundaryPointArray);
        return boundaryPointArray;
    }
}
