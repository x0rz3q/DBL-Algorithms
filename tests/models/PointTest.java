package models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTest extends AbstractSquareTest {
    @Override
    protected void setInstance() {
        this.anchor = new Anchor(10, 10);
        this.edgeLength = 0.0;
        this.instance = new Point(anchor);
    }

    @Test
    void getBottomLeft() {
        equals(this.instance.getBottomLeft(), this.instance);
    }

    @Test
    void getBottomRight() {
        equals(this.instance.getBottomRight(), this.instance);
    }

    @Test
    void getTopLeft() {
        equals(this.instance.getTopLeft(), this.instance);
    }

    @Test
    void getTopRight() {
        equals(this.instance.getTopRight(), this.instance);
    }

    @Test
    void getCenter() {
        equals(this.instance.getCenter(), this.instance);
    }

    @Test
    void setEdgeLength() {
        Double edgeLength = 0.0;
        this.instance.setEdgeLength(edgeLength);
        assertEquals(this.instance.getEdgeLength(), edgeLength);
    }

    @Test
    void setEdgeLengthException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> this.instance.setEdgeLength(100.0));
        assertNotNull(exception.getMessage());
    }

    @Test
    void touch() {
        assertTrue(this.instance.touch(this.instance));
    }

    @Test
    void notIntersect() {
        assertFalse(this.instance.intersects(new Point(100, 100)));
    }
}