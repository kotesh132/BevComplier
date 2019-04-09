package com.p4.p416.applications;

import java.util.Set;

import com.p4.drmt.cfg.CFGBuildingBlock;

public interface CFGNode {
	public Set<CFGNode> getNextCFGNodes(); 
	public CFGNode getJumpNode();
	public boolean isJumpNode();
	public CFGNode getTableApplyNode();
	public boolean isTableApplyNode();
	public String cFGNodeSummary();
	public CFGBuildingBlock getCFGBuildingBlock();
}
