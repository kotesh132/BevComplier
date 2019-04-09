package com.p4.tools.graph.structs;

import java.util.ArrayList;
import java.util.List;

import com.p4.utils.Summarizable;

import lombok.Data;

@Data
public class TableNode implements Node, Summarizable{
	public final String name;
	public final DummyNode child;
	public Node parent = null;
	@Override
	public List<Node> getChildren() {
		List<Node> children = new ArrayList<Node>();
		children.add(child);
		return children;
	}
	@Override
	public String summary() {
		return name;
	}
}
