package Collections;

import interfaces.models.SquareInterface;
import models.BoundingBox;

import java.util.Collection;

public class KDTree extends AbstractCollection{
    @Override
    public void insert(SquareInterface node) throws NullPointerException {

    }

    @Override
    public void remove(SquareInterface node) throws NullPointerException {

    }

    @Override
    public Collection query2D(SquareInterface range) {
        return null;
    }

    @Override
    public Collection<SquareInterface> query2D(BoundingBox range) {
        return null;
    }

    @Override
    public Boolean intersects(SquareInterface node) {
        return null;
    }

    @Override
    public Boolean intersects(BoundingBox node) {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }
}
