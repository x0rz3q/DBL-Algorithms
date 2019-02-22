package models;

import interfaces.models.LabelInterface;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionLabelTest {
    private LabelInterface instance;
    private double size;

    @BeforeEach
    protected void setInstance() {
        this.instance = new PositionLabel(0, 0, 0.0, 1,1, DirectionEnum.NE);
        this.size = 0.0;
    }

    protected PositionLabel cast() {
        return (PositionLabel)this.instance;
    }

    @Test
    void testSetEdgeNE() {
        this.cast().setHeight(5);
        this.cast().setDirection(DirectionEnum.NE);
        assertEquals(0, this.cast().getXMin());
        assertEquals(5.0, this.cast().getXMax());
        assertEquals(0, this.cast().getYMin());
        assertEquals(5.0, this.cast().getYMax());
    }

    @Test
    void testSetEdgeNW() {
        this.cast().setHeight(5);
        this.cast().setDirection(DirectionEnum.NW);
        assertEquals(-5, this.cast().getXMin());
        assertEquals(0, this.cast().getXMax());
        assertEquals(0, this.cast().getYMin());
        assertEquals(5, this.cast().getYMax());
    }
}