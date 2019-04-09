package com.p4.ids;


import com.p4.utils.Utils.Pair;

import java.util.List;

public interface IDAGGraph<V, E> extends IGraph<V, E> {

    List<V> topologicalSort();

    Pair<Integer, List<V>> getCriticalPath();

    List<V> getPredecessors(V vertex);

    List<E> edgesGoingOutOf(V currentVertex);

    List<E> edgesComingInto(V currentVertex);

    List<V> adjacentOutGoingVertices(V currentVertex);
}
