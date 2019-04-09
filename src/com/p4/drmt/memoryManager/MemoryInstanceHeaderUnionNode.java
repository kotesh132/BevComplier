package com.p4.drmt.memoryManager;

import com.p4.ds.DisjointSet;
import com.p4.ids.IDisjointSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class MemoryInstanceHeaderUnionNode extends MemoryInstanceNode implements IMemoryInstanceHeaderUnionNode {


    public MemoryInstanceHeaderUnionNode(IMemoryInstance instance) {
        super(instance);
    }

    @Override
    public IDisjointSet<IMemoryInstance> getHeaderFieldsAsDisjointSet(Map<IMemoryInstance, Boolean> visited) {

        IDisjointSet<IMemoryInstance> disjointSet = new DisjointSet<>();

        for (Entry<IMemoryInstance, IMemoryInstanceNode> memoryInstanceEntry : getChildren().entrySet()) {
            List<IMemoryInstance> memoryInstances = new ArrayList<>();
            getAllLeafNodes(memoryInstanceEntry.getValue(), memoryInstances, visited);

            disjointSet.addEquivalenceSet(memoryInstances);
        }

        return disjointSet;
    }


}
