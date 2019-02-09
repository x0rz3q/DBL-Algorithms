package Collections;

import interfaces.models.GeometryInterface;
import interfaces.models.RectangleInterface;

import java.util.Collection;
import java.util.Iterator;

public class QuadTree extends AbstractCollection
{
    @Override
    public void insert(GeometryInterface node) throws NullPointerException {

    }

    @Override
    public void insert(Collection nodes) throws NullPointerException {

    }

    @Override
    public void remove(GeometryInterface node) throws NullPointerException {

    }

    @Override
    public Collection query2D(RectangleInterface range) {
        return null;
    }

    @Override
    public Boolean intersects(GeometryInterface node) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Iterator iterator() {
        return null;
    }
}
