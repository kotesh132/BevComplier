package com.p4.drmt.memoryManager;


import com.p4.drmt.ilp.IUnitDAGVertex;
import com.p4.ds.ListMap;
import com.p4.ids.IDisjointSet;
import com.p4.p416.applications.Target.MemoryType;

import java.util.List;
import java.util.Map;

public interface IMemoryAllocatorStrategy {

    enum MemoryAllocationMode {

        //This mode divides byte vector into three parts.
        // first part stores all 1 byte fields, Each field is aligned at 1 byte
        // second part stores 2 byte fields. Each field is aligned at 2 bytes
        // third part stores >2 byte fields. Each field is aligned at 4 bytes
        // Also, only referenced fields will be allocated.
        // isValid fields are allocated for all headers/header unions
        AlignedMode("AlignedMode"),


        ScratchMemoryMode("ScratchMemoryMode");


        private String mode;

        MemoryAllocationMode(String mode) {
            this.mode = mode;
        }

        @Override
        public String toString() {
            return mode;
        }


    }

    int allocate() throws InsufficientMemoryException;

    static IMemoryAllocatorStrategy getMemoryAllocationStrategy(MemoryAllocationMode mode,
                                                                List<IDisjointSet<IMemoryInstance>> memoryInstances,
                                                                MemoryType memoryType,
                                                                Map<IMemoryInstance, Integer> memoryInstanceToOffsetMap,
                                                                int maxAvailableMemory, ListMap<Integer, IUnitDAGVertex> schedule) {

        switch (mode) {
            case AlignedMode:
                return new AlignedMemoryAllocatorStrategy(memoryInstances, memoryType, memoryInstanceToOffsetMap, maxAvailableMemory);

            case ScratchMemoryMode:
                return new ScratchMemoryAllocatorStrategy(schedule, memoryInstanceToOffsetMap, maxAvailableMemory);
        }

        return null;

    }

}
