package Collections;

import interfaces.models.SquareInterface;
import models.Anchor;
import models.Point;
import models.Square;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuadTreeTest extends AbstractCollectionTest {

    @Override
    protected void setInstance() {
        Anchor anchor = new Anchor(0, 0);
        SquareInterface boundary = new Square(anchor, 10000);
        this.instance = new QuadTree(boundary);
    }

    @Test
    void insert() {
        SquareInterface point1 = new Point(5000, 5000);
        this.instance.insert(point1);
        SquareInterface range = new Square(new Anchor(4000, 4000), 2000);
        Collection<SquareInterface> points = this.instance.query2D(range);
        assertTrue(points.contains(point1));
    }

    /**
     * Not implemented yet
    @Test
    void remove() {
    }
     */

    @Test
    void query2D() {
    }

    @Test
    void intersects() {
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
}