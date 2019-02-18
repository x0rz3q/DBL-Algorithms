package Collections;

import interfaces.models.SquareInterface;
import models.Anchor;
import models.Point;
import models.Square;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class QuadTreeTest extends AbstractCollectionTest {

    @Override
    protected void setInstance() {
        Anchor anchor = new Anchor(0, 0);
        SquareInterface boundary = new Square(anchor, 10000);
        this.instance = new QuadTree(boundary);
        this.instance.setDataLimit(1);
    }

    @Test
    void insert() {
        SquareInterface point1 = new Point(5000, 5000);
        this.instance.insert(point1);
        SquareInterface range = new Square(new Anchor(4000, 4000), 2000);
        Collection<SquareInterface> points = this.instance.query2D(range);
        assertTrue(points.contains(point1));
    }

    @Test
    void insert2() {
        SquareInterface point1 = new Point(5000, 5000);
        SquareInterface point2 = new Point(6000, 6000);
        this.instance.insert(point1);
        this.instance.insert(point2);
        SquareInterface range = new Square(new Anchor(4000, 4000), 2000);
        Collection<SquareInterface> points = this.instance.query2D(range);
        assertTrue(points.contains(point1));
        assertFalse(points.contains(point2));
    }

    @Test
    void insert3() {
        SquareInterface point1 = new Point(4000, 4000);
        SquareInterface point2 = new Point(6000, 6000);
        SquareInterface point3 = new Point(5000, 5000);
        this.instance.insert(point1);
        this.instance.insert(point2);
        this.instance.insert(point3);
        SquareInterface range = new Square(new Anchor(4000, 4000), 2000);
        Collection<SquareInterface> points = this.instance.query2D(range);
        assertFalse(points.contains(point1));
        assertFalse(points.contains(point2));
        assertTrue(points.contains(point3));
    }

    /**
     * Not implemented yet
    @Test
    void remove() {
    }
     */

    // Test if query2D is empty if range does not include any points.
    @Test
    void query2D() {
        SquareInterface point1 = new Point(5000, 5000);
        SquareInterface point2 = new Point(3000, 3000);
        SquareInterface point3 = new Point(9000, 1);
        SquareInterface point4 = new Point(7000, 8000);
        this.instance.insert(point1);
        this.instance.insert(point2);
        this.instance.insert(point3);
        this.instance.insert(point4);
        SquareInterface queryRange = new Square(new Anchor(2500, 2500), 1);
        Collection<SquareInterface> points = this.instance.query2D(queryRange);
        assertTrue(points.isEmpty());
    }

    // Test if query2D returns 2 points contained in the range
    @Test
    void query2D2() {
        SquareInterface point1 = new Point(5000, 5000);
        SquareInterface point2 = new Point(3000, 3000);
        SquareInterface point3 = new Point(9000, 1);
        SquareInterface point4 = new Point(7000, 8000);
        this.instance.insert(point1);
        this.instance.insert(point2);
        this.instance.insert(point3);
        this.instance.insert(point4);
        SquareInterface queryRange = new Square(new Anchor(2500, 2500), 3000);
        Collection<SquareInterface> points = this.instance.query2D(queryRange);
        assertTrue(points.contains(point1));
        assertTrue(points.contains(point2));
    }

    // Test if point in boundary returns true on intersect method
    @Test
    void intersects() {
        SquareInterface point = new Point(5000, 5000);
        assertTrue(this.instance.intersects(point));
    }

    // Test if point outside boundary returns false on intersect method
    // TODO: Fix that points can be instantiated with negative coordinates
    @Test
    void intersects2() {
        SquareInterface point = new Point(-2000, 5000);
        assertFalse(this.instance.intersects(point));
    }

    // Test if point outside boundary returns false on intersect method
    // TODO: Fix that points can be instantiated with coordinates > 10000
    @Test
    void intersects3() {
        SquareInterface point = new Point(11000, 5000);
        assertFalse(this.instance.intersects(point));
    }

    @Test
    void size() {
        assertEquals(0, this.instance.getSize());
    }

    @Test
    void size2() {
        SquareInterface point1 = new Point(10, 50);
        SquareInterface point2 = new Point(500, 500);
        SquareInterface point3 = new Point(2000, 3000);
        this.instance.insert(point1);
        this.instance.insert(point2);
        this.instance.insert(point3);
        assertEquals(3, this.instance.getSize());
    }

    @Test
    void testSquare() {
        SquareInterface s1 = new Square(new Anchor(10, 10), 1);
        SquareInterface s2 = new Square(new Anchor(11, 11), 1);
        this.instance.insert(s1);
        this.instance.insert(s2);
    }
}