package models;

import interfaces.models.PointInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PointTest {
    private PointInterface instance;

    @BeforeEach
    protected void setInstance() {
        this.instance = new Point(10, 10);
    }

    public void equals(PointInterface p, PointInterface q) {
        assertEquals(p.getX(), q.getX());
        assertEquals(p.getY(), q.getY());
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
    void touch() {
        assertTrue(this.instance.touch(this.instance));
    }

    @Test
    void notIntersect() {
        assertFalse(this.instance.intersects(new Point(100, 100)));
    }
}