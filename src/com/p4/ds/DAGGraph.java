package com.p4.ds;


import com.p4.ids.IDAGEdge;
import com.p4.ids.IDAGGraph;
import com.p4.utils.Utils.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class DAGGraph<V, E> extends BaseGraph<V, E> implements IDAGGraph<V, E> {

    public DAGGraph() {
        super();
    }

    public List<V> topologicalSort() {
        List<V> topoSortedList = new ArrayList<>();
        Stack<V> stack = new Stack<>();
        Set<V> visited = new HashSet<>();

        for (V vertex : getVertices()) {
            if (!visited.contains(vertex)) {
                toposort(vertex, visited, stack);
            }
        }
        while (!stack.isEmpty()) {
            topoSortedList.add(stack.pop());
        }
        return topoSortedList;
    }

    private void toposort(V vertex, Set<V> visited, Stack<V> stack) {
        visited.add(vertex);
        for (V adjnodes : adjacentOutGoingVertices(vertex)) {
            if (!visited.contains(adjnodes)) {
                toposort(adjnodes, visited, stack);
            }
        }
        stack.push(vertex);
    }


    public List<V> getPredecessors(V vertex) {
        List<E> es = edgesComingInto(vertex);
        List<V> predecessors = new ArrayList<>();
        for (E e : es) {
            predecessors.add((V) ((IDAGEdge) e).getSource());
        }

        return predecessors;

    }

    public Pair<Integer, List<V>> getCriticalPath() {
        List<V> criticalPath = new ArrayList<>();
        List<V> topoSortedVerticies = topologicalSort();
        HashMap<V, Pair<Integer, V>> distMap = new HashMap<>();

        for (V sortedVertex : topoSortedVerticies) {

            List<Pair<Integer, V>> pairs = new ArrayList<>();

            List<E> incomingEdges = edgesComingInto(sortedVertex);
            for (E incomingEdge : incomingEdges) {
                Integer distToSortedVertex = 0;
                V source = (V) ((IDAGEdge) incomingEdge).getSource();
                if (distMap.containsKey(source)) {
                    distToSortedVertex = distMap.get(source).first();
                }
                int delay = distToSortedVertex + ((IDAGEdge) incomingEdge).getDelay();
                pairs.add(new Pair<Integer, V>(delay, source));
            }

            if (!pairs.isEmpty()) {
                Optional<Pair<Integer, V>> max = pairs.stream()
                        .max(Comparator.comparing(Pair::first));
                if (max.isPresent()) {
                    Pair<Integer, V> distToNode = max.get();
                    distMap.put(sortedVertex, distToNode);
                }
            } else {
                distMap.put(sortedVertex, new Pair<Integer, V>(0, sortedVertex));
            }
        }

        int maxDist = -1;
        V vertex = null;
        for (Entry<V, Pair<Integer, V>> vPairEntry : distMap.entrySet()) {

            if (vPairEntry.getValue().first() > maxDist) {
                maxDist = vPairEntry.getValue().first();
                vertex = vPairEntry.getKey();
            }
        }

        int latency = maxDist + 1;

        while (maxDist > 0) {
            criticalPath.add(vertex);
            maxDist = distMap.get(vertex).first();
            vertex = distMap.get(vertex).second();
        }
        Collections.reverse(criticalPath);


        return new Pair<Integer, List<V>>(latency, criticalPath);
    }

    @Override
    public List<V> adjacentOutGoingVertices(V currentVertex) {
        List<E> outgoingEdges = edgesGoingOutOf(currentVertex);
        List<V> outVertices = new ArrayList<>();
        for (E outgoingEdge : outgoingEdges) {
            BaseEdge baseEdge = edgeMap.get(outgoingEdge);
            outVertices.add((V) baseEdge.getDestination());
        }
        return outVertices;
    }


    public List<E> edgesGoingOutOf(V currentVertex) {
        List<E> outGoingEdges = new ArrayList<>();
        for (E e : edges) {
            BaseEdge baseEdge = edgeMap.get(e);
            if (baseEdge.getSource() == currentVertex) {
                outGoingEdges.add(e);
            }
        }
        return outGoingEdges;
    }

    public List<E> edgesComingInto(V currentVertex) {
        List<E> incomingEdges = new ArrayList<>();
        for (E e : edges) {
            BaseEdge baseEdge = edgeMap.get(e);
            if (baseEdge.getDestination() == currentVertex) {
                incomingEdges.add(e);
            }
        }
        return incomingEdges;
    }

    public boolean isReachable(V src, V dst) {

        if (src.equals(dst)) {
            return true;
        }

        Queue<V> queue = new LinkedList<>();
        queue.offer(src);

        V temp = null;
        while(!queue.isEmpty()){
            temp = queue.poll();
            List<V> outGoingVertices = adjacentOutGoingVertices(temp);
            for (V outGoingVertex : outGoingVertices) {
                if (outGoingVertex.equals(dst)) {
                    return true;
                }
                queue.offer(outGoingVertex);
            }
        }
        return false;
    }


}
