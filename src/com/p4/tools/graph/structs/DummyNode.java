package com.p4.tools.graph.structs;

import java.util.ArrayList;
import java.util.List;

import com.p4.utils.Summarizable;

import lombok.Data;

@Data
public class DummyNode implements Node, Summarizable{

	public final String name;
	
	public final List<Node> children = new ArrayList<>();
	
	public Node parent;
	
	public void addSingleTableNode(TableNode tn){
		children.add(tn);
	}
	
	public void addCondTableNode(DummyNode n1, DummyNode n2){
		children.add(n1);
		children.add(n2);
	}
	
	@Override
	public String summary() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DummyNode other = (DummyNode) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
	
}
