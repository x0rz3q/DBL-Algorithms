package models;

import interfaces.models.SquareInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

abstract class AbstractSquareTest {
    protected SquareInterface instance;
    protected Anchor anchor;
    protected Double edgeLength;
    protected abstract void setInstance();

    protected void equals(SquareInterface p, SquareInterface q) {
        assertEquals(p.getAnchor().getX(), q.getAnchor().getX());
        assertEquals(p.getAnchor().getX(), q.getAnchor().getX());
        assertEquals(p.getEdgeLength(), q.getEdgeLength());
    }

    @BeforeEach
    void setup() {
        setInstance();
    }

    @Test
    void getAnchor() {
        assertEquals(this.instance.getAnchor(), anchor);
    }

    @Test
    void getEdgeLength() {
        assertEquals(this.instance.getEdgeLength(), edgeLength);
    }

    @Test
    void setAnchor() {
        Anchor anchor = new Anchor(51.2, 52.2);
        this.instance.setAnchor(anchor);
        assertEquals(this.instance.getAnchor(), anchor);
    }

    @Test
    void equals() {
        assertTrue(instance.equals(instance));
    }

    @Test
    void getXMax() {
        Double value = anchor.getX() + edgeLength;
        assertEquals(value, instance.getXMax());
    }

    @Test
    void getYMax() {
        Double value = anchor.getY() + edgeLength;
        assertEquals(value, instance.getYMax());
    }

    @Test
    void getXMin() {
        assertEquals(anchor.getX(), instance.getXMin());
    }

    @Test
    void getYMin() {
        assertEquals(anchor.getY(), instance.getYMin());
    }
}