package models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionLabelTest extends AbstractSquareTest {
    @Override
    protected void setInstance() {
        this.anchor = new Anchor(10, 10);
        this.instance = new PositionLabel(10, 10,0.0, DirectionEnum.NE, 0);
        this.edgeLength = 0.0;
    }

    protected PositionLabel cast() {
        return (PositionLabel)this.instance;
    }

    @Test
    void testSetEdgeNE() {
        this.cast().setEdgeLength(5, DirectionEnum.NE);
        assertEquals(10.0, (double)this.cast().getXMin());
        assertEquals(15.0, (double)this.cast().getXMax());
        assertEquals(10.0, (double)this.cast().getYMin());
        assertEquals(15.0, (double)this.cast().getYMax());
    }

    @Test
    void testSetEdgeSE() {
        this.cast().setEdgeLength(5, DirectionEnum.SE);
        assertEquals(10, (double)this.cast().getXMin());
        assertEquals(15, (double)this.cast().getXMax());
        assertEquals(5, (double)this.cast().getYMin());
        assertEquals(10, (double)this.cast().getYMax());
    }

    @Test
    void testSetEdgeNW() {
        this.cast().setEdgeLength(5, DirectionEnum.NW);
        assertEquals(5, (double)this.cast().getXMin());
        assertEquals(10, (double)this.cast().getXMax());
        assertEquals(10, (double)this.cast().getYMin());
        assertEquals(15, (double)this.cast().getYMax());
    }

    @Test
    void testSetEdgeSW() {
        this.cast().setEdgeLength(5, DirectionEnum.SW);
        assertEquals(5, (double)this.cast().getXMin());
        assertEquals(10, (double)this.cast().getXMax());
        assertEquals(5, (double)this.cast().getYMin());
        assertEquals(10, (double)this.cast().getYMax());
    }
}