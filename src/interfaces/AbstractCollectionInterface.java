package interfaces;

import interfaces.models.NodeInterface;

import java.util.List;

public interface AbstractCollectionInterface {
    void insert(NodeInterface element);
    void remove(NodeInterface node);
    List<NodeInterface> query(NodeInterface node);
    Boolean intersects(NodeInterface node);
}