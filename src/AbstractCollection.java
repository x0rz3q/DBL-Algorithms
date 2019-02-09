import interfaces.AbstractCollectionInterface;
import interfaces.models.GeometryInterface;

import java.util.Collection;

public abstract class AbstractCollection<T extends GeometryInterface> implements AbstractCollectionInterface<T>{
    protected int count;
    protected T root;

    public AbstractCollection() {
        this.count = 0;
    }

    public AbstractCollection(Collection<T> nodes) {
        this();
        this.insert(nodes);
    }

    public int getSize() {
        return this.count;
    }
}
