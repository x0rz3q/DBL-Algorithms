package models;

import interfaces.models.AnchorInterface;
import interfaces.models.SquareInterface;

public class Point extends AbstractSquare {
    public Point(AnchorInterface anchor) {
        super(anchor, 0.0);
    }

    public Point(double x, double y) {
        super(new Anchor(x, y), 0.0);
    }

    @Override
    public SquareInterface getBottomLeft() {
        return this;
    }

    @Override
    public SquareInterface getBottomRight() {
        return this;
    }

    @Override
    public SquareInterface getTopLeft() {
        return this;
    }

    @Override
    public SquareInterface getTopRight() {
        return this;
    }

    @Override
    public SquareInterface getCenter() {
        return this;
    }

    /**
     * Reject setting edgeLength for a point.
     *
     * @param edgeLength Double
     * @pre {@code edgeLength  == 0}
     * @throws IllegalArgumentException if {@code edgeLength <> 0}
     */
    @Override
    public void setEdgeLength(Double edgeLength) throws IllegalArgumentException {
        if (edgeLength != 0) {
            throw new IllegalArgumentException("Point.setEdgeLength.pre violated: edgeLength <> 0");
        }
    }

    public Double getX() {
        return this.getAnchor().getX();
    }

    public Double getY() {
        return this.getAnchor().getY();
    }
}
