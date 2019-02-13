package models;

import interfaces.models.AnchorInterface;
import interfaces.models.SquareInterface;

public class Square extends AbstractSquare {
    public Square(AnchorInterface anchor, double edgeLength) {
        super(anchor, edgeLength);
    }

    @Override
    public SquareInterface getBottomLeft() {
        return new Point(this.getXMin(), this.getYMin());
    }

    @Override
    public SquareInterface getBottomRight() {
        return new Point(this.getXMin(), this.getYMax());
    }

    @Override
    public SquareInterface getTopLeft() {
        return new Point(this.getXMax(), this.getYMin());
    }

    @Override
    public SquareInterface getTopRight() {
        return new Point(this.getXMax(), this.getYMax());
    }

    @Override
    public SquareInterface getCenter() {
        Double x = anchor.getX() + edgeLength / 2;
        Double y = anchor.getY() + edgeLength / 2;

        return new Point(x, y);
    }

    /**
     * Reject setting edgeLength for a point.
     *
     * @param edgeLength Double
     * @throws IllegalArgumentException if {@code edgeLength <= 0}
     * @pre {@code edgeLength > 0}
     */
    @Override
    public void setEdgeLength(Double edgeLength) throws IllegalArgumentException {
        if (edgeLength <= 0) {
            throw new IllegalArgumentException("Square.setEdgeLength.pre violated: edgelength <= 0");
        }

        this.edgeLength = edgeLength;
    }
}
