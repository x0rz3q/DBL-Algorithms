package Collections;

import interfaces.models.SquareInterface;
import models.Anchor;
import models.Square;
import org.junit.jupiter.api.Test;

class QuadTreeTest extends AbstractCollectionTest {

    @Override
    protected void setInstance() {
        Anchor anchor = new Anchor(0, 0);
        SquareInterface boundary = new Square(anchor, 10000);
        this.instance = new QuadTree(boundary);
    }

    @Test
    void insert() {
    }

    @Test
    void insert1() {
    }

    @Test
    void remove() {
    }

    @Test
    void query2D() {
    }

    @Test
    void intersects() {
    }

    @Test
    void size() {
    }
}