package Collections;

import interfaces.models.SquareInterface;
import models.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class KDTreeTest extends AbstractCollectionTest{
    List<SquareInterface> points;

    @Override
    protected void setInstance() {

    }

    @Test
    void createOnePoint() {
        points = new ArrayList<>();
        points.add(new Point(5000, 5000));
        instance = new KDTree(points, 1);
        assertTrue(instance.nodes.contains(points.get(0)));
        assertNull(((KDTree) instance).left);
    }

    @Test
    void insert1() {
        points = new ArrayList<>();
        SquareInterface p = new Point(5000, 5000);
        instance = new KDTree(points, 1);
        assertEquals(0, instance.size());
        instance.insert(p);
        assertEquals(1, instance.size());
        assertTrue(instance.nodes.contains(p));
        assertNull(((KDTree) instance ).left);
        SquareInterface p1 = new Point(6000, 6000);
        instance.insert(p1);
        assertFalse(instance.nodes.contains(p1));
        // not null children
        assertTrue(((KDTree) instance).left != null || ((KDTree) instance).right != null);
        // only one child has the node
        assertFalse(((KDTree) instance).left.nodes.contains(p1) && ((KDTree) instance).right.nodes.contains(p1));
        assertTrue(((KDTree) instance).left.nodes.contains(p1) || ((KDTree) instance).right.nodes.contains(p1));
        SquareInterface p2 = new Point(3000, 3000);
        instance.insert(p2);
        assertEquals(3, instance.size());
    }

    @Test
    void insert2() {}

    @Test
    void query2D() {}

    @Test
    void query2D2() {}
}
