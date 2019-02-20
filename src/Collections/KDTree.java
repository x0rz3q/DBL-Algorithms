package Collections;

import interfaces.models.GeometryInterface;
import models.Rectangle;

import java.util.Collection;

public class KDTree extends AbstractCollection{
    @Override
    public Boolean insert(GeometryInterface node) throws NullPointerException {
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
    public Boolean intersects(GeometryInterface node) {
        return null;
    }

    @Override
    public Boolean intersects(Rectangle node) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }
}
