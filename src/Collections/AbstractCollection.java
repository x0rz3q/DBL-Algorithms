package Collections;

import interfaces.AbstractCollectionInterface;
import interfaces.models.SquareInterface;

import java.util.Collection;

public abstract class AbstractCollection implements AbstractCollectionInterface {
    protected int count;

    public AbstractCollection() {
        this.count = 0;
    }

    public int getSize() {
        return this.count;
    }
}
