package com.p4.drmt.struct.graph;

import com.p4.drmt.struct.IUnit;
import com.p4.drmt.struct.IVariable;
import com.p4.tools.graph.Edge;
import com.p4.tools.graph.Graph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DFGBuilder<T1 extends IUnit> {
    private static final Logger L = LoggerFactory.getLogger(DFGBuilder.class);
    public Set<Edge<T1>> buildDFG(Graph<T1> graph){
        List<T1> topoNodes = graph.topologicalSort();
        return buildDFG(topoNodes);
    }

    public Set<Edge<T1>> buildDFG(List<T1> topoNodes){
        Set<Edge<T1>> ret = new LinkedHashSet<>();
        for(int i=0; i<topoNodes.size();i++){
            T1 node = topoNodes.get(i);
            for(int j = i-1; j>=0;j--){
                T1 pNode = topoNodes.get(j);
                for(IVariable consumer : node.getConsumers()){
                    if(intersectsWithAny(consumer, pNode.getProducers())){
                        ret.add(new Edge<>(pNode, node));
                        break;
                    }
                }
            }
        }
        return ret;
    }

    private boolean intersectsWithAny(IVariable one, Set<IVariable> others){
        for(IVariable o: others){
            if(one!=null && o !=null) {
                if (one.intersects(o)) {
                    L.info("Found Dependency: "+one.summary()+" and "+ o.summary());
                    return true;
                }
            }
        }
        return false;
    }
}
