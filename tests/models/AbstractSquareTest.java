//package models;
//
//import interfaces.models.GeometryInterface;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//abstract class AbstractSquareTest {
//    protected GeometryInterface instance;
//    protected Anchor anchor;
//    protected Double edgeLength;
//    protected abstract void setInstance();
//
//    protected void equals(GeometryInterface p, GeometryInterface q) {
//        assertEquals(p.getXMax(), q.getXMax());
//        assertEquals(p.getXMin(), q.getXMin());
//        assertEquals(p.getYMax(), q.getYMax());
//        assertEquals(p.getYMin(), q.getYMin());
//    }
//
//    @BeforeEach
//    void setup() {
//        setInstance();
//    }
//
//    @Test
//    void equals() {
//        assertTrue(instance.equals(instance));
//    }
//
//    @Test
//    void getXMax() {
//        double value = anchor.getX() + edgeLength;
//        assertEquals(value, instance.getXMax());
//    }
//
//    @Test
//    void getYMax() {
//        Double value = anchor.getY() + edgeLength;
//        assertEquals(value, instance.getYMax());
//    }
//
//    @Test
//    void getXMin() {
//        assertEquals(anchor.getX(), instance.getXMin());
//    }
//
//    @Test
//    void getYMin() {
//        assertEquals(anchor.getY(), instance.getYMin());
//    }
//}