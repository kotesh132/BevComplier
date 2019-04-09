package com.p4.tools.graph.structs;

import java.util.List;

import com.p4.utils.Summarizable;

public interface Node extends Summarizable{
	public List<Node> getChildren();
}
