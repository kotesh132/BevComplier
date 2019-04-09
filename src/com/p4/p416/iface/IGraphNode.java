package com.p4.p416.iface;

import java.util.List;

public interface IGraphNode {
	
	void insertChildrenNode(IGraphNode node);
	
	List<IGraphNode> getChildrenNodes();
	
}
