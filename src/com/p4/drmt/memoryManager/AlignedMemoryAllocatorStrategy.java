package com.p4.drmt.memoryManager;

import com.p4.ds.DisjointSet;
import com.p4.ids.IDisjointSet;
import com.p4.p416.applications.Target.MemoryType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.p4.drmt.memoryManager.IAlignedMemoryAllocatorStrategy.AlignmentType.ONE;
import static com.p4.p416.applications.Target.MemoryType.TypeCfgByte;
import static com.p4.p416.applications.Target.MemoryType.TypePktByte;
import static com.p4.p416.applications.Target.MemoryType.TypeSrcByte;

/**
 * It is the responsibility of the caller to pass correct input
 * No validation done in this class as of now
 */

class AlignedMemoryAllocatorStrategy implements IAlignedMemoryAllocatorStrategy {

    private List<IDisjointSet<IMemoryInstance>> memoryInstances;


    private Map<IMemoryInstance, Integer> memoryInstanceToOffsetMap;
    private MemoryType memoryType;
    private int maxAvailableMemory;


    AlignedMemoryAllocatorStrategy(List<IDisjointSet<IMemoryInstance>> memoryInstances,
                                   MemoryType memoryType,
                                   Map<IMemoryInstance, Integer> memoryInstanceToOffsetMap,
                                   int maxAvailableMemory) {
        this.memoryInstances = memoryInstances;
        this.memoryType = memoryType;
        this.memoryInstanceToOffsetMap = memoryInstanceToOffsetMap;
        this.maxAvailableMemory = maxAvailableMemory;
    }

    /**
     * @return overall memory used by the allocator
     */
    public int allocateMemory(List<IDisjointSet<IMemoryInstance>> filteredIMemoryInstances, AlignmentType alignmentType, int initialOffset) {

        int globalOffset = initialOffset;

        for (IDisjointSet<IMemoryInstance> memoryInstanceDisjointSet : filteredIMemoryInstances) {
            Collection<Set<IMemoryInstance>> memoryInstanceSets = memoryInstanceDisjointSet.getEquivalenceSets();

            int localOffsetMax = globalOffset;
            for (Set<IMemoryInstance> memoryInstanceSet : memoryInstanceSets) {
                int localOffset = globalOffset;

                for (IMemoryInstance memoryInstance : memoryInstanceSet) {
                    memoryInstanceToOffsetMap.put(memoryInstance, localOffset);
                    localOffset += memoryInstance.getSize();
                    localOffset = IAlignedMemoryAllocatorStrategy.getAlignedSlot(alignmentType, localOffset);
                }

                if (localOffsetMax < localOffset) {
                    localOffsetMax = localOffset;
                }
            }

            globalOffset = localOffsetMax;

        }
        return globalOffset;
    }

    @Override
    public int allocateBitMemory() {
        List<IDisjointSet<IMemoryInstance>> bitIMemoryInstances = MemoryInstanceFilter.filterBySizeAndMemoryType(this.memoryInstances, ONE.getVal(), memoryType);
        return allocateMemory(bitIMemoryInstances, ONE, 0);
    }

    @Override
    public int allocateByteMemory() {
        int bytesUsed = 0;
        for (AlignmentType alignmentType : AlignmentType.values()) {
            bytesUsed = IAlignedMemoryAllocatorStrategy.getAlignedSlot(alignmentType, bytesUsed);
            List<IDisjointSet<IMemoryInstance>> byteIMemoryInstances = MemoryInstanceFilter.filterBySizeAndMemoryType(this.memoryInstances, alignmentType.getVal(), memoryType);
            bytesUsed = allocateMemory(byteIMemoryInstances, alignmentType, bytesUsed);
        }

        return bytesUsed;
    }

    @Override
    public int allocate() throws InsufficientMemoryException {

        int memoryUsed = 0;
        if (memoryType.isBitType()) {
            memoryUsed = allocateBitMemory();
        } else {
            memoryUsed = allocateByteMemory();
        }

        if (memoryUsed > maxAvailableMemory) {
            throw new InsufficientMemoryException(memoryType, memoryUsed, maxAvailableMemory);
        }

        return memoryUsed;
    }


    public static class MemoryInstanceFilter {


        static List<IDisjointSet<IMemoryInstance>> filterBySizeAndMemoryType(List<IDisjointSet<IMemoryInstance>> memoryInstances, int size, MemoryType type) {

            List<IDisjointSet<IMemoryInstance>> filteredIMemoryInstances = new ArrayList<>();


            for (IDisjointSet<IMemoryInstance> memoryInstance : memoryInstances) {
                Collection<Set<IMemoryInstance>> memoryInstanceSets = memoryInstance.getEquivalenceSets();

                IDisjointSet<IMemoryInstance> filteredIMemoryInstancesDsjSet = new DisjointSet<>();

                for (Set<IMemoryInstance> memoryInstanceSet : memoryInstanceSets) {
                    List<IMemoryInstance> memoryInstanceList = new ArrayList<>();

                    for (IMemoryInstance instance : memoryInstanceSet) {
                        MemoryType memoryType = instance.getMemoryType();

                        if (memoryType == type) {
                            memoryInstanceList.add(instance);
                        }
                    }

                    if (!type.isBitType()) {
                        Predicate<IMemoryInstance> sizeFilter = (size <= 2) ?
                                instance -> instance.getSize() == size :
                                instance -> instance.getSize() > 2;

                        List<IMemoryInstance> memoryInstancesFilteredBySize = memoryInstanceList.stream()
                                .filter(sizeFilter)
                                .collect(Collectors.toList());

                        filteredIMemoryInstancesDsjSet.addEquivalenceSet(memoryInstancesFilteredBySize);
                    } else {

                        filteredIMemoryInstancesDsjSet.addEquivalenceSet(memoryInstanceList);
                    }
                }


                if (!filteredIMemoryInstancesDsjSet.isEmpty()) {
                    filteredIMemoryInstances.add(filteredIMemoryInstancesDsjSet);
                }

            }
            return filteredIMemoryInstances;

        }
    }

}
