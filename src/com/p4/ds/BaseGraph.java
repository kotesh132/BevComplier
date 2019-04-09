package com.p4.ds;

import com.p4.ids.IGraph;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public abstract class BaseGraph<V, E> implements IGraph<V, E> {

    protected Set<E> edges;
    protected Set<V> vertices;
    protected Map<E, BaseEdge> edgeMap;

    public BaseGraph() {
        this.edgeMap = new LinkedHashMap<>();
        this.edges = new LinkedHashSet<>();
        this.vertices = new LinkedHashSet<>();
    }

    public boolean addEdge(E edge, V src, V dst) {
        vertices.add(src);
        vertices.add(dst);
        if (edges.add(edge)) {
            edgeMap.put(edge, new BaseEdge(src, dst));
        }
        return false;
    }

    public boolean addVertex(V vertex) {
        return vertices.add(vertex);
    }

    @Override
    public E getEdge(V src, V dst) {
        for (E edge : edges) {
            BaseEdge baseEdge = edgeMap.get(edge);
            if (baseEdge.getSource().equals(src) && baseEdge.getDestination().equals(dst)){
                return edge;
            }
        }
        return null;
    }

    @Override
    public boolean containsEdge(V src, V dst) {
        return getEdge(src, dst) != null;
    }


    @Override
    public Set<E> getEdges() {
        return edges;
    }

    @Override
    public int getVertexCount() {
        return vertices.size();
    }

    @Override
    public Set<V> getVertices() {
        return vertices;
    }

}
