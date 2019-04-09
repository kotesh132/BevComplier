package com.p4.drmt.memoryManager;


import com.p4.ids.IDisjointSet;

import java.util.Map;

public interface IMemoryInstanceHeaderUnionNode extends IMemoryInstanceNode {

    IDisjointSet<IMemoryInstance> getHeaderFieldsAsDisjointSet(Map<IMemoryInstance, Boolean> visited);
}
