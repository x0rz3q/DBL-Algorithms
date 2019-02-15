package models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SquareTest extends AbstractSquareTest {
    @Override
    protected void setInstance() {
        this.edgeLength = 5.0;
        this.anchor = new Anchor(10, 10);
        this.instance = new Square(this.anchor, this.edgeLength);
    }

    @Test
    void getBottomLeft() {
        equals(this.instance.getBottomLeft(), new Point(this.instance.getXMin(), this.instance.getYMin()));
    }

    @Test
    void getBottomRight() {
        equals(this.instance.getBottomRight(), new Point(this.instance.getXMin(), this.instance.getYMax()));
    }

    @Test
    void getTopLeft() {
        equals(this.instance.getTopLeft(), new Point(this.instance.getXMax(), this.instance.getYMin()));
    }

    @Test
    void getTopRight() {
        equals(this.instance.getTopRight(), new Point(this.instance.getXMax(), this.instance.getYMax()));
    }

    @Test
    void getCenter() {
        Double x = this.anchor.getX() + this.edgeLength / 2;
        Double y = this.anchor.getY() + this.edgeLength / 2;
        Point center = new Point(x, y);
        equals(this.instance.getCenter(), center);
    }

    @Test
    void setEdgeLength() {
        Double edgeLength = 1000.0;
        this.instance.setEdgeLength(edgeLength);
        assertEquals(this.instance.getEdgeLength(), edgeLength);
    }

    @Test
    void intersectsSelf() {
        assertTrue(this.instance.intersects(this.instance));
    }

    @Test
    void intersectsLeftBottomCorner() {
        Square square = new Square(new Anchor(11, 11), 5);
        assertTrue(this.instance.intersects(square));
    }

    @Test
    void intersectsLeftTopCorner() {
        Square square = new Square(new Anchor(14, 11), 5);
        assertTrue(this.instance.intersects(square));
    }

    @Test
    void intersectsCenter() {
        Square square = new Square(new Anchor(12.5, 12.5), 5);
        assertTrue(this.instance.intersects(square));
    }

    @Test
    void intersectsFarRight() {
        Square square = new Square(new Anchor(14.999, 14.999), 5);
        assertTrue(this.instance.intersects(square));
    }

    @Test
    void touch() {
        Square square = new Square(new Anchor(15, 15), 5);
        assertTrue(this.instance.touch(square));
    }

    @Test
    void doesNotIntersectTop() {
        Square square = new Square(new Anchor(16, 11), 5);
        assertFalse(this.instance.intersects(square));
    }

    @Test
    void doesNotIntersectCloseByRight() {
        Square square = new Square(new Anchor(15.0001, 15.001), 5);
        assertFalse(this.instance.intersects(square));
    }

    @Test
    void doesNotIntersectCloseByLeft() {
        Square square = new Square(new Anchor(8.99999999, 8.99999), 1);
        assertFalse(this.instance.intersects(square));
    }

    @Test
    void doesNotIntersectFloatingAbove() {
        Square square = new Square(new Anchor(16, 16), 1);
        assertFalse(this.instance.intersects(square));
    }

    @Test
    void doesNotIntersectFloatingBelow() {
        Square square = new Square(new Anchor(4, 4), 1);
        assertFalse(this.instance.intersects(square));
    }
}