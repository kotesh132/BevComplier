package com.p4.drmt.cfg;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.drmt.alu.ConditionalConfigGenerator;
import com.p4.p416.applications.CFGNode;
import com.p4.tools.graph.Edge;
import com.p4.tools.graph.Graph;

import lombok.Data;

@Data
public class GraphMarker {
	
	private static final Logger L = LoggerFactory.getLogger(GraphMarker.class);
	private final CFGNode start;
	private final Set<CFGNode> ends;
	
	public static GraphMarker unit(CFGNode node){
		return GraphMarker.of(node, node);
	}
	
	public static GraphMarker of(CFGNode s,
			CFGNode   e) {
		Set<CFGNode> es = new LinkedHashSet<>();
		es.add(e);
		return new GraphMarker(s, es);
	}
	
	public static void join(GraphMarker n1, GraphMarker n2){
		for(CFGNode n:n1.ends){
			n.getNextCFGNodes().add(n2.start);
		}
	}
	
	public Graph<CFGNode> buildGraph(){
		HashSet<Edge<CFGNode>> edges = new LinkedHashSet<>();
		HashSet<CFGNode> visited = new HashSet<>();
		LinkedList<CFGNode> q = new LinkedList<>();
		q.push(start);
		visited.add(start);
		while(!q.isEmpty()){
			CFGNode s = q.pop();
			for(CFGNode e:s.getNextCFGNodes()){
				if(!visited.contains(e)){
					Edge<CFGNode> edge = new Edge<CFGNode>(s, e);
					L.info(s.cFGNodeSummary()+" -> "+e.cFGNodeSummary());
					edges.add(edge);
					q.add(e);
					visited.add(e);
				}else{
					//System.out.println("Cycle");
					//System.out.println(s.cFGNodeSummary());
					//System.out.println(e.cFGNodeSummary());
				}
				
			}
		}
		Graph<CFGNode> graph = new Graph<>(edges);
		return graph;
		
	}
}
