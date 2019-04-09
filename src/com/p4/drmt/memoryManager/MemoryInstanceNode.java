package com.p4.drmt.memoryManager;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MemoryInstanceNode implements IMemoryInstanceNode {

    private IMemoryInstance memoryInstance;
    private Map<IMemoryInstance, IMemoryInstanceNode> children = new LinkedHashMap<>();

    public MemoryInstanceNode(IMemoryInstance instance) {
        this.memoryInstance = instance;
    }

    @Override
    public void insertChildrenNode(IMemoryInstance childMemoryInstance, IMemoryInstanceNode childNode) {
        this.children.put(childMemoryInstance, childNode);
    }

    @Override
    public Map<IMemoryInstance, IMemoryInstanceNode> getChildren() {
        return this.children;
    }

    @Override
    public IMemoryInstance getMemoryInstance() {
        return this.memoryInstance;
    }

    @Override
    public void setMemoryInstance(IMemoryInstance memoryInstance) {
        this.memoryInstance = memoryInstance;
    }

    public void getAllLeafNodes(IMemoryInstanceNode value, List<IMemoryInstance> memoryInstances, Map<IMemoryInstance, Boolean> visited) {

        Map<IMemoryInstance, IMemoryInstanceNode> children = value.getChildren();

        if (value.getChildren() == null || value.getChildren().isEmpty()) {
            if (!visited.containsKey(value.getMemoryInstance()) || !visited.get(value.getMemoryInstance())) {
                memoryInstances.add(value.getMemoryInstance());
                visited.put(value.getMemoryInstance(), true);
            }
        }

        for (Entry<IMemoryInstance, IMemoryInstanceNode> entry : children.entrySet()) {

            getAllLeafNodes(entry.getValue(), memoryInstances, visited);

        }

    }

}
