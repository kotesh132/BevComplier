package com.p4.ids;

import java.util.Set;

public interface IGraph<V, E> {

    boolean addEdge(E edge, V src, V dst);

    boolean addVertex(V vertex);

    E getEdge(V src, V dst);

    boolean containsEdge(V src, V dst);

    Set<E> getEdges();

    int getVertexCount();

    Set<V> getVertices();

    boolean isReachable(V src, V dst);
}
