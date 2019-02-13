package models;

import interfaces.models.AnchorInterface;
import interfaces.models.SquareInterface;

public abstract class AbstractSquare implements SquareInterface {
    protected AnchorInterface anchor;
    protected Double edgeLength;

    /**
     * Constructor
     *
     * @param anchor {@link AnchorInterface}
     * @param edgeLength double
     * @pre {@code edgeLength >= 0 && anchor <> null}
     * @throws IllegalArgumentException if {@code edgeLength < 0}
     * @throws NullPointerException if {@code anchor == null}
     */
    public AbstractSquare(AnchorInterface anchor, double edgeLength) throws IllegalArgumentException, NullPointerException {
        if (anchor == null) {
            throw new NullPointerException("AbstractSquare.constructor.pre violated: anchor == null");
        }

        if (edgeLength < 0) {
            throw new IllegalArgumentException("AbstractSquare.constructor.pre violated: " + edgeLength + "< 0");
        }

        this.anchor = anchor;
        this.edgeLength = edgeLength;
    }

    @Override
    public AnchorInterface getAnchor() {
        return anchor;
    }

    @Override
    public Double getEdgeLength() {
        return edgeLength;
    }

    @Override
    public void setAnchor(AnchorInterface anchor) {
        this.anchor = anchor;
    }

    @Override
    public Boolean equals(SquareInterface square) {
        return this.getAnchor().equals(square.getAnchor()) && this.getEdgeLength().equals(square.getEdgeLength());
    }

    @Override
    public Double getXMax() {
        return this.getXMin() + this.getEdgeLength();
    }

    @Override
    public Double getYMax() {
        return this.getYMin() + this.getEdgeLength();
    }

    @Override
    public Double getXMin() {
        return this.getAnchor().getX();
    }

    @Override
    public Double getYMin() {
        return this.getAnchor().getY();
    }

    @Override
    public Boolean intersects(SquareInterface square) {
        return this.getXMin() <= square.getXMax() && square.getXMin() <= this.getXMax() &&
                this.getYMin() <= square.getYMax() && square.getYMin() <= this.getYMax();

    }
}
