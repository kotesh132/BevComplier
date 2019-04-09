package com.p4.drmt.cfg;

import java.util.LinkedHashSet;
import java.util.Set;

import com.p4.p416.applications.CFGNode;
import com.p4.tools.graph.Edge;
import com.p4.tools.graph.Graph;

import lombok.Data;

@Data
public class CFGBuildingBlock {
	private final Graph<CFGNode> graph;
	private final GraphMarker marker;
	
	public CFGBuildingBlock(Graph<CFGNode> graph, GraphMarker marker){
		validate(graph, marker);
		this.graph = graph;
		this.marker = marker;
	}
	
	private void validate(Graph<CFGNode> graph2, GraphMarker marker2) {
		Set<CFGNode> nodes = graph2.getNodes();
		if(!nodes.contains(marker2.getStart())){
			throw new IllegalArgumentException("Marker Nodes don't point to nodes in the graph");
		}
		for(CFGNode node:marker2.getEnds()){
			if(!nodes.contains(node)){
				throw new IllegalArgumentException("Marker Nodes don't point to nodes in the graph");
			}
			if(node.getNextCFGNodes().size()>0){
				throw new IllegalArgumentException("Ends are already pointing to some Node");
			}
		}
	}
	
	public static CFGBuildingBlock unitOf(CFGNode node){
		Graph<CFGNode> graph = Graph.singleNodeGraph(node);
		GraphMarker marker = GraphMarker.unit(node);
		return new CFGBuildingBlock(graph, marker);
	}

	public static CFGBuildingBlock linkDisjoint(CFGBuildingBlock b1, CFGBuildingBlock b2){
		if(Graph.areDisjoint(b1.graph, b2.graph)){
			Set<Edge<CFGNode>> edges = new LinkedHashSet<>();
			Set<CFGNode> nodes = new LinkedHashSet<>();
			//New Graph will have all nodes and edges of the 2 graphs
			edges.addAll(b1.graph.getEdges());edges.addAll(b2.graph.getEdges());
			nodes.addAll(b1.graph.getNodes());nodes.addAll(b2.graph.getNodes());
			//Connect Ends to Start
			for(CFGNode e: b1.marker.getEnds()){
				e.getNextCFGNodes().add(b2.marker.getStart());
				edges.add(new Edge<CFGNode>(e, b2.marker.getStart()));
			}
			GraphMarker marker = new GraphMarker(b1.marker.getStart(), b2.marker.getEnds());
			return new CFGBuildingBlock(new Graph<>(edges, nodes), marker);
		}
		throw new IllegalArgumentException("Graphs to link are already connected");
	}
	
	public static CFGBuildingBlock linkDisjoint(CFGBuildingBlock b1, Set<CFGBuildingBlock> b2){
		Set<Edge<CFGNode>> edges = new LinkedHashSet<>();
		Set<CFGNode> nodes = new LinkedHashSet<>();
		Set<CFGNode> ends = new LinkedHashSet<>();
		edges.addAll(b1.graph.getEdges());
		nodes.addAll(b1.graph.getNodes());
		for(CFGBuildingBlock sbBlock:b2){
			if(!Graph.areDisjoint(b1.graph, sbBlock.graph)){
				throw new IllegalArgumentException("Graphs to link are already connected");
			}
			//b1.marker.getEnds().add(sbBlock.marker.getStart());
			for(CFGNode n:b1.marker.getEnds()){
				edges.add(new Edge<CFGNode>(n, sbBlock.marker.getStart()));
			}
			edges.addAll(sbBlock.graph.getEdges());
			nodes.addAll(sbBlock.graph.getNodes());
			ends.addAll(sbBlock.marker.getEnds());
		}
		return new CFGBuildingBlock(new Graph<>(edges, nodes), new GraphMarker(b1.marker.getStart(), ends));
	}
}
