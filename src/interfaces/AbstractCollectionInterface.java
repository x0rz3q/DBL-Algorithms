package interfaces;

import java.util.List;

public interface AbstractCollectionInterface<T> extends Iterable<T> {
    void insert(T node);
    void remove(T node);
    List<T> query2D(T node);
    Boolean intersects(T node);
    int size();
}