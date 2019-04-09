package com.p4.tools.graph.structs;

import lombok.Data;
@Data
public class ControlGraph {

	public final String name;
	
	public final Node root;
	
	public Node currentNode;
	
	public ControlGraph(String name){
		this.name = name;
		this.root = new DummyNode(name+"_root");
		this.currentNode = this.root;
	}
	
}
