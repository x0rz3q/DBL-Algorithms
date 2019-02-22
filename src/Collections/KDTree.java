package Collections;

import interfaces.models.GeometryInterface;
import models.Rectangle;

import java.util.Collection;

public class KDTree extends AbstractCollection{
    @Override
    public boolean insert(GeometryInterface node) throws NullPointerException {
        return false;
    }

    @Override
    public void remove(GeometryInterface node) throws NullPointerException {

    }

    @Override
    public Collection query2D(GeometryInterface range) {
        return null;
    }

    @Override
    public boolean intersects(GeometryInterface node) {
        return false;
    }

    @Override
    public boolean intersects(Rectangle node) {
        return false;
    }

    @Override
    public int size() {
        return 0;
    }
}
