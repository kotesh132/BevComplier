package com.p4.tools.graph.structs;

import java.util.List;
import java.util.Set;

import com.p4.tools.graph.Edge;

public class StructUtil {

	public static void populateEdges(Node root, Set<Edge<Node>> ret){
		List<Node> children = root.getChildren(); 
		for(Node child:children){
			ret.add(new Edge<Node>(root, child));
			populateEdges(child, ret);
		}
		
	}
}
