package com.p4.drmt.schd;

import com.p4.drmt.struct.IUnit;
import com.p4.drmt.struct.MANode;
import com.p4.drmt.struct.graph.DFGBuilder;
import com.p4.p416.applications.CFGNode;
import com.p4.p416.gen.AbstractBaseExt;
import com.p4.p416.gen.ApplyMethodCallContextExt;
import com.p4.p416.gen.AssignmentStatementContextExt;
import com.p4.tools.graph.Edge;
import com.p4.tools.graph.Graph;
import com.p4.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class DFGHelper {
    private static final Logger L = LoggerFactory.getLogger(DFGHelper.class);

    public static List<Set<IUnit>> filterMANodes(List<MANode> manodes){

        DFGBuilder dfgBuilder = new DFGBuilder();
        Set<Edge<IUnit>> dfgEdges = dfgBuilder.buildDFG(manodes);

        for(Edge<IUnit> edge:dfgEdges){
            L.info(edge.summary());
        }

        Set<IUnit> allNodes = new LinkedHashSet<>();
        manodes.forEach(n -> allNodes.add(n));

        List<Set<IUnit>> parallelNodes = scheduleBuilder(dfgEdges, allNodes);
        L.info("Total Number of Parallel nodes = "+parallelNodes.size());
        return parallelNodes;
    }

    public static List<MANode> getMANodes(List<CFGNode> cnodes) {
        List<MANode> manodes = new ArrayList<>();
        for(CFGNode node: cnodes){
            if(node instanceof AssignmentStatementContextExt){
                manodes.addAll(((AssignmentStatementContextExt) node).allALUOperations());
            }else if(node instanceof ApplyMethodCallContextExt){
                if(node.isTableApplyNode()){
                    manodes.add(((ApplyMethodCallContextExt) node).getMatchNode());
                }else{
                    L.info("apply Node found: "+((AbstractBaseExt)node).getFormattedText());
                }
            }else {
                //L.info(((AbstractBaseExt)node).getFormattedText());
            }
        }
        return manodes;
    }

    private static List<Set<IUnit>> scheduleBuilder(Set<Edge<IUnit>> edges, Set<IUnit> nodes){
        List<Set<IUnit>> ret = new ArrayList<>();
        Graph<IUnit> g = new Graph<>(new LinkedHashSet<>(edges), nodes);
        if(g.hasCycle()){
            throw new IllegalStateException("DFG can't contain cycle");
        }
        Set<IUnit> startingNodes = g.nodesWithoutIncomingEdges();
        ret.add(startingNodes);
        while(!startingNodes.isEmpty()){
            for(IUnit node:startingNodes) {
                L.info(node.summary());
                g.removeEdgeWithSource(node);
            }
            startingNodes = g.nodesWithoutIncomingEdges();
            if(startingNodes.size()>0)
                ret.add(startingNodes);
        }

        return ret;
    }
}
