package models;

import interfaces.models.PointInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RectangleTest {
    private Rectangle instance;
    private double xMin = 10.0;
    private double yMin = 10.0;
    private double xMax = 20.0;
    private double yMax = 20.0;

    @BeforeEach
    void setUp() {
        this.instance = new Rectangle(this.xMin, this.yMin, this.xMax, this.yMax);
    }

    @Test
    void getBottomLeft() {
        PointInterface bottomLeft = instance.getBottomLeft();
        assertEquals(this.xMin, bottomLeft.getXMin());
        assertEquals(this.yMin, bottomLeft.getYMin());
    }

    @Test
    void getTopRight() {
        PointInterface topRight = instance.getTopRight();
        assertEquals(this.xMax, topRight.getXMin());
        assertEquals(this.yMax, topRight.getYMin());
    }

    @Test
    void getBottomRight() {
        PointInterface bottomRight = instance.getBottomRight();
        assertEquals(this.xMax, bottomRight.getXMin());
        assertEquals(this.yMin, bottomRight.getYMin());
    }

    @Test
    void getTopLeft() {
        PointInterface topLeft = instance.getTopLeft();
        assertEquals(this.xMin, topLeft.getXMin());
        assertEquals(this.yMax, topLeft.getYMin());
    }

    @Test
    void getCenter() {
        double x = this.xMax - (this.xMax - this.xMin) / 2;
        double y = this.yMax - (this.yMax - this.yMin) / 2;

        PointInterface center = instance.getCenter();
        assertEquals(x, center.getXMin());
        assertEquals(y, center.getYMin());
    }

    @Test
    void getXMin() {
        assertEquals(this.instance.getXMin(), this.xMin);
    }

    @Test
    void getYMin() {
        assertEquals(this.instance.getYMin(), this.yMin);
    }

    @Test
    void getXMax() {
        assertEquals(this.instance.getXMax(), this.xMax);
    }

    @Test
    void getYMax() {
        assertEquals(this.instance.getYMax(), this.yMax);
    }

    @Test
    void intersects() {
        assertTrue(this.instance.intersects(10, 10, 15, 15));
    }

    @Test
    void intersects1() {
        assertTrue(this.instance.intersects(new Rectangle(10, 10, 21, 11)));
    }

    @Test
    void intersects2() {
        assertTrue(this.instance.intersects(new Point(11, 11.2)));
    }

    @Test
    void notIntersects() {
        assertFalse(this.instance.intersects(9, 9, 9.99, 9.99));
    }

    @Test
    void notIntersects1() {
        assertFalse(this.instance.intersects(new Rectangle(100, 100, 100, 100)));
    }

    @Test
    void notIntersects2() {
        assertFalse(this.instance.intersects(new Point(20.000001, 20.00001)));
    }
}