package com.p4.drmt.memoryManager;


import java.util.Map;

public interface IMemoryInstanceNode {

    IMemoryInstance getMemoryInstance();

    void setMemoryInstance(IMemoryInstance memoryInstance);

    void insertChildrenNode(IMemoryInstance childMemoryInstance, IMemoryInstanceNode childNode);

    Map<IMemoryInstance, IMemoryInstanceNode> getChildren();

}