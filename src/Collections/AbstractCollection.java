package Collections;

import interfaces.AbstractCollectionInterface;

public abstract class AbstractCollection implements AbstractCollectionInterface {
    protected int count;
    protected int dataLimit;

    public AbstractCollection() {
        this.count = 0;
    }

    public int getSize() {
        return this.count;
    }

    public void setDataLimit(int d) {this.dataLimit = d;}
}
