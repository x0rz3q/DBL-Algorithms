//package models;
//
//import interfaces.models.SquareInterface;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class BoundingBoxTest {
//    private BoundingBox instance;
//    private Double xMin = 10.0;
//    private Double yMin = 10.0;
//    private Double xMax = 20.0;
//    private Double yMax = 20.0;
//
//    @BeforeEach
//    void setUp() {
//        this.instance = new BoundingBox(this.xMin, this.yMin, this.xMax, this.yMax);
//    }
//
//    @Test
//    void getBottomLeft() {
//        SquareInterface bottomLeft = instance.getBottomLeft();
//        assertEquals(this.xMin, bottomLeft.getXMin());
//        assertEquals(this.yMin, bottomLeft.getYMin());
//    }
//
//    @Test
//    void getTopRight() {
//        SquareInterface topRight = instance.getTopRight();
//        assertEquals(this.xMax, topRight.getXMin());
//        assertEquals(this.yMax, topRight.getYMin());
//    }
//
//    @Test
//    void getBottomRight() {
//        SquareInterface bottomRight = instance.getBottomRight();
//        assertEquals(this.xMax, bottomRight.getXMin());
//        assertEquals(this.yMin, bottomRight.getYMin());
//    }
//
//    @Test
//    void getTopLeft() {
//        SquareInterface topLeft = instance.getTopLeft();
//        assertEquals(this.xMin, topLeft.getXMin());
//        assertEquals(this.yMax, topLeft.getYMin());
//    }
//
//    @Test
//    void getCenter() {
//        Double x = this.xMax - (this.xMax - this.xMin) / 2;
//        Double y = this.yMax - (this.yMax - this.yMin) / 2;
//
//        SquareInterface center = instance.getCenter();
//        assertEquals(x, center.getXMin());
//        assertEquals(y, center.getYMin());
//    }
//
//    @Test
//    void getXMin() {
//        assertEquals(this.instance.getXMin(), this.xMin);
//    }
//
//    @Test
//    void getYMin() {
//        assertEquals(this.instance.getYMin(), this.yMin);
//    }
//
//    @Test
//    void getXMax() {
//        assertEquals(this.instance.getXMax(), this.xMax);
//    }
//
//    @Test
//    void getYMax() {
//        assertEquals(this.instance.getYMax(), this.yMax);
//    }
//
//    @Test
//    void intersects() {
//        assertTrue(this.instance.intersects(10, 10, 15, 15));
//    }
//
//    @Test
//    void intersects1() {
//        assertTrue(this.instance.intersects(new BoundingBox(10, 10, 21, 11)));
//    }
//
//    @Test
//    void intersects2() {
//        assertTrue(this.instance.intersects(new Point(11, 11.2)));
//    }
//
//    @Test
//    void notIntersects() {
//        assertFalse(this.instance.intersects(9, 9, 9.99, 9.99));
//    }
//
//    @Test
//    void notIntersects1() {
//        assertFalse(this.instance.intersects(new BoundingBox(100, 100, 100, 100)));
//    }
//
//    @Test
//    void notIntersects2() {
//        assertFalse(this.instance.intersects(new Point(20.000001, 20.00001)));
//    }
//}