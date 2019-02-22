package Collections;

import interfaces.models.GeometryInterface;
import models.Point;
import models.Rectangle;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class QuadTreeTest extends AbstractCollectionTest {

    @Override
    protected void setInstance() {
        Rectangle boundary = new Rectangle(0,0, 10000, 10000);
        this.instance = new QuadTree(boundary);
        this.instance.setDataLimit(1);
    }

    @Test
    void insert() {
        GeometryInterface point1 = new Point(5000, 5000);
        this.instance.insert(point1);
        GeometryInterface range = new Rectangle(4000, 4000, 6000, 6000);
        Collection<GeometryInterface> points = this.instance.query2D(range);
        assertTrue(points.contains(point1));
    }

    @Test
    void insert2() {
        GeometryInterface point1 = new Point(5000, 5000);
        GeometryInterface point2 = new Point(6000, 6000);
        this.instance.insert(point1);
        this.instance.insert(point2);
        GeometryInterface range = new Rectangle(4000, 4000, 7000, 7000);
        Collection<GeometryInterface> points = this.instance.query2D(range);
        assertTrue(points.contains(point1));
        assertTrue(points.contains(point2));
    }

    @Test
    void insert3() {
        GeometryInterface point1 = new Point(4000, 4000);
        GeometryInterface point2 = new Point(6000, 6000);
        this.instance.insert(point1);
        this.instance.insert(point2);
        GeometryInterface range = new Rectangle(3000, 3000, 7000, 7000);
        Collection<GeometryInterface> points = this.instance.query2D(range);
        assertTrue(points.contains(point1));
        assertTrue(points.contains(point2));
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
        GeometryInterface point1 = new Point(5000, 5000);
        GeometryInterface point2 = new Point(3000, 3000);
        GeometryInterface point3 = new Point(9000, 1);
        GeometryInterface point4 = new Point(7000, 8000);
        this.instance.insert(point1);
        this.instance.insert(point2);
        this.instance.insert(point3);
        this.instance.insert(point4);
        GeometryInterface queryRange = new Rectangle(2500, 2500, 2501, 2501);
        Collection<GeometryInterface> points = this.instance.query2D(queryRange);
        assertTrue(points.isEmpty());
    }

    // Test if query2D returns 2 points contained in the range
    @Test
    void query2D2() {
        GeometryInterface point1 = new Point(5000, 5000);
        GeometryInterface point2 = new Point(3000, 3000);
        GeometryInterface point3 = new Point(9000, 1);
        GeometryInterface point4 = new Point(7000, 8000);
        this.instance.insert(point1);
        this.instance.insert(point2);
        this.instance.insert(point3);
        this.instance.insert(point4);
        GeometryInterface queryRange = new Rectangle(2500, 2500, 5500, 5500);
        Collection<GeometryInterface> points = this.instance.query2D(queryRange);
        assertTrue(points.contains(point1));
        assertTrue(points.contains(point2));
    }

    // Test if point in boundary returns true on intersect method
    @Test
    void intersects() {
        GeometryInterface point = new Point(5000, 5000);
        assertTrue(this.instance.intersects(point));
    }

    // Test if point outside boundary returns false on intersect method
    // TODO: Fix that points can be instantiated with negative coordinates
    @Test
    void intersects2() {
        GeometryInterface point = new Point(-2000, 5000);
        assertFalse(this.instance.intersects(point));
    }

    // Test if point outside boundary returns false on intersect method
    // TODO: Fix that points can be instantiated with coordinates > 10000
    @Test
    void intersects3() {
        GeometryInterface point = new Point(11000, 5000);
        assertFalse(this.instance.intersects(point));
    }

    @Test
    void squareAdding() {
        int count = 0;

        for (int x = 0; x < 100; x+= 10) {
            for (int y = 0; y < 500; y+= 20) {
                GeometryInterface square = new Rectangle(x, y, x+10, y+10);
                assertTrue(this.instance.insert(square));
                count++;
            }
        }

        assertEquals(count, this.instance.size());
    }

    @Test
    void size() {
        assertEquals(0, this.instance.getSize());
    }

    @Test
    void size2() {
        GeometryInterface point1 = new Point(10, 50);
        GeometryInterface point2 = new Point(500, 500);
        GeometryInterface point3 = new Point(2000, 3000);
        this.instance.insert(point1);
        this.instance.insert(point2);
        this.instance.insert(point3);
        assertEquals(3, this.instance.getSize());
    }
}