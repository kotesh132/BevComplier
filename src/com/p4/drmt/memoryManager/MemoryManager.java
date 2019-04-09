package com.p4.drmt.memoryManager;

import com.p4.drmt.cfg.DRMTConstants;
import com.p4.drmt.cfg.KeyMeta;
import com.p4.drmt.ilp.IUnitDAGVertex;
import com.p4.drmt.memoryManager.IMemoryAllocatorStrategy.MemoryAllocationMode;
import com.p4.ds.DisjointSet;
import com.p4.ds.ListMap;
import com.p4.drmt.ilp.som.PrettyTablePrinter;
import com.p4.ids.IDisjointSet;
import com.p4.p416.applications.Symbol;
import com.p4.p416.applications.Target.MemoryType;
import com.p4.p416.applications.Type;
import com.p4.p416.iface.IExtendedContext;
import com.p4.utils.FileUtils;
import com.p4.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.p4.p416.applications.Target.MemoryType.*;

public class MemoryManager {

    private static final Logger L = LoggerFactory.getLogger(MemoryManager.class);

    private static MemoryManager memoryManager;
    private Map<IMemoryInstance, Integer> memoryInstanceToOffsetMap;
    public static final FetchDisjointSets fds = new FetchDisjointSets();
    private static final int BIT_VECTOR_LENGTH = 256;
    private static final int BYTE_VECTOR_LENGTH = 2048;
    private static final int SCRATCH_BYTE_VECTOR_LENGTH = 2 * DRMTConstants.numkseg * DRMTConstants.maxBytes;
    //TODO Hack to get it working
    public static File outputFile = null;


    /**
     * Allocates memory in Packet Byte/Bit Vector, Config Byte/bit Vector, Scratch Byte Vector
     *
     * @param contextExt          its the extendedContext in the AbstractSyntaxTree. Memory allocation happens for all the variables used in this sub-tree
     * @param schedule            its the DRMT schedule generated. This information is needed to allocate scratch space memory.
     * @param additionalInstances any specific MemoryInstances to be allocated explicitly, for eg., fields used to extract varbits.
     */
    public static void allocateMemory(IExtendedContext contextExt, ListMap<Integer, IUnitDAGVertex> schedule,
                                      Set<IMemoryInstance> additionalInstances) {
    	L.info("allocating memory in MemoryManager");
        ReferencedVariableVisitor referencedVariableVisitor = new ReferencedVariableVisitor();
        referencedVariableVisitor.visit(contextExt.getContext());

        Map<IMemoryInstance, Symbol> referencedSymbols = referencedVariableVisitor.getReferencedSymbols();
        L.debug("collected all the referenced variables");

        List<IDisjointSet<IMemoryInstance>> memoryInstances = fds.getDisjointSets(referencedSymbols);

        DisjointSet<IMemoryInstance> additionalMemoryInstancesSet = getFilteredAdditionalMemoryInstanceSet(additionalInstances, memoryInstances);

        memoryInstances.add(additionalMemoryInstancesSet);
        allocateMemory(memoryInstances, schedule);
        //L.info("Valid Instances = "+ Utils.summary(fds.getFirstLastisValidInstances()));
        L.info("done with MemoryManager");
    }


    private static DisjointSet<IMemoryInstance> getFilteredAdditionalMemoryInstanceSet(Set<IMemoryInstance> additionalInstances, List<IDisjointSet<IMemoryInstance>> memoryInstances) {
        List<IMemoryInstance> filteredAdditionalMemoryInstances = new ArrayList<>();
        for (IMemoryInstance additionalInstance : additionalInstances) {
            boolean additionalInstanceExists = false;
            for (IDisjointSet<IMemoryInstance> memoryInstanceSet : memoryInstances) {
                if (memoryInstanceSet.contains(additionalInstance)) {
                    additionalInstanceExists = true;
                    break;
                }
            }
            if (!additionalInstanceExists) {
                filteredAdditionalMemoryInstances.add(additionalInstance);
            }
        }

        DisjointSet<IMemoryInstance> additionalMemoryInstancesSet = new DisjointSet<>();
        additionalMemoryInstancesSet.addEquivalenceSet(filteredAdditionalMemoryInstances);
        return additionalMemoryInstancesSet;
    }

    public static void allocateMemory(List<IDisjointSet<IMemoryInstance>> memoryInstances, ListMap<Integer, IUnitDAGVertex> schedule) {
        MemoryManager memoryManager = getInstance();
        if (memoryInstances != null) {
            memoryManager.allocate(memoryInstances);
        }
        if (memoryInstances != null && schedule != null) {
            memoryManager.allocateScratchMemory(memoryInstances, schedule);
        }
    }

    private void allocateScratchMemory(List<IDisjointSet<IMemoryInstance>> memoryInstances, ListMap<Integer, IUnitDAGVertex> schedule) {

        IMemoryAllocatorStrategy allocator = IMemoryAllocatorStrategy.getMemoryAllocationStrategy(MemoryAllocationMode.ScratchMemoryMode,
                memoryInstances, MemoryType.TypeSrcByte, memoryInstanceToOffsetMap, SCRATCH_BYTE_VECTOR_LENGTH, schedule);
        try {
            allocator.allocate();
        } catch (InsufficientMemoryException e) {
            L.error(e.getMessage());
        }
    }

    private static MemoryManager getInstance() {
        if (memoryManager == null) {
            memoryManager = new MemoryManager();
        }
        return memoryManager;
    }

    private MemoryManager() {
        memoryInstanceToOffsetMap = new HashMap<>();
    }

    protected void allocate(List<IDisjointSet<IMemoryInstance>> memoryInstances) {
    	L.debug("allocating Pkt and Cfg memory");
        allocatePktBitMemory(memoryInstances);
        allocatePktByteMemory(memoryInstances);

        allocateCfgBitMemory(memoryInstances);
        allocateCfgByteMemory(memoryInstances);
    }

    private void allocatePktBitMemory(List<IDisjointSet<IMemoryInstance>> memoryInstances) {
        allocateBitMemory(TypePktBit, memoryInstances);
    }

    private void allocateCfgBitMemory(List<IDisjointSet<IMemoryInstance>> memoryInstances) {
        allocateBitMemory(TypeCfgBit, memoryInstances);
    }

    private void allocatePktByteMemory(List<IDisjointSet<IMemoryInstance>> memoryInstances) {
        allocateByteMemory(TypePktByte, memoryInstances);
    }

    private void allocateCfgByteMemory(List<IDisjointSet<IMemoryInstance>> memoryInstances) {
        allocateByteMemory(TypeCfgByte, memoryInstances);
    }

    private void allocateBitMemory(MemoryType memoryType, List<IDisjointSet<IMemoryInstance>> memoryInstances) {
        IMemoryAllocatorStrategy allocator = IMemoryAllocatorStrategy.getMemoryAllocationStrategy(MemoryAllocationMode.AlignedMode,
                memoryInstances, memoryType, memoryInstanceToOffsetMap, BIT_VECTOR_LENGTH, null);
        try {
            allocator.allocate();
        } catch (InsufficientMemoryException e) {
            L.error(e.getMessage());
        }

    }

    private void allocateByteMemory(MemoryType memoryType, List<IDisjointSet<IMemoryInstance>> memoryInstances) {
        IMemoryAllocatorStrategy memoryAllocationStrategy = IMemoryAllocatorStrategy.getMemoryAllocationStrategy(MemoryAllocationMode.AlignedMode,
                memoryInstances, memoryType, memoryInstanceToOffsetMap, BYTE_VECTOR_LENGTH, null);

        try {
            memoryAllocationStrategy.allocate();
        } catch (InsufficientMemoryException e) {
            L.error(e.getMessage());
        }
    }

    private Map<IMemoryInstance, Integer> getIMemoryInstanceToOffsetMap() {
        return memoryInstanceToOffsetMap;
    }

    public static int getOffset(String instanceName, Type type) {

        if (memoryManager == null) {
            throw new RuntimeException("Memory still not allocated. Allocate memory first by calling MemoryManager.allocateMemory");
        }
        Map<IMemoryInstance, Integer> memoryInstanceToOffsetMap = memoryManager.getIMemoryInstanceToOffsetMap();
        Integer offset = memoryInstanceToOffsetMap.get(new MemoryInstance(instanceName, type));
        if (offset == null) {
            throw new RuntimeException("Memory not allocated for " + instanceName + " " + type.getSymbolName());
        }
        return offset;
    }

    public static void clear() {
    	L.debug("clearing memoryManager");
        memoryManager = null;
    }


    public static void printMemoryAllocated() {

    	L.debug("printing allocatedMemory");
        Map<IMemoryInstance, Integer> memoryInstanceToOffsetMap = memoryManager.memoryInstanceToOffsetMap;

        printAllocation(memoryInstanceToOffsetMap, TypePktBit);
        printAllocation(memoryInstanceToOffsetMap, TypePktByte);

        printAllocation(memoryInstanceToOffsetMap, TypeCfgBit);
        printAllocation(memoryInstanceToOffsetMap, TypeCfgByte);

    }

    private static void printAllocation(Map<IMemoryInstance, Integer> memoryInstanceToOffsetMap, MemoryType memoryType) {
        System.out.println("Memory Allocation in memoryType " + memoryType.summary());
        System.out.println("*********************************************************************************");
        if(outputFile!=null){
            FileUtils.AppendToFile(outputFile, "Memory Allocation in memoryType " + memoryType.summary()+"\n");
            FileUtils.AppendToFile(outputFile, "*********************************************************************************\n");
        }
        List<IMemoryInstance> pktBitAllocation = memoryInstanceToOffsetMap.keySet()
                .stream()
                .filter(memoryInstance -> memoryInstance.getMemoryType() == memoryType)
                .sorted(Comparator.comparing(memoryInstanceToOffsetMap::get))
                .collect(Collectors.toList());

        ListMap<Integer, IMemoryInstance> offSetToIMemoryInstancesSorted = new ListMap<>(LinkedHashMap.class, ArrayList.class);
        for (IMemoryInstance memoryInstance : pktBitAllocation) {
            offSetToIMemoryInstancesSorted.add(memoryInstanceToOffsetMap.get(memoryInstance), memoryInstance);
        }

        boolean isBit = memoryType == TypePktBit | memoryType == TypeCfgBit | memoryType == TypeSrcBit;


        int colLength = 4;
        int rowLength = isBit ? 256 / colLength : 2048 / colLength;

        String[][] printString = new String[rowLength][colLength];
        int maxCellsOccupied = 0;
        for (Integer offset : offSetToIMemoryInstancesSorted.keySet()) {
            List<IMemoryInstance> memoryInstances = offSetToIMemoryInstancesSorted.get(offset);

            for (IMemoryInstance memoryInstance : memoryInstances) {
                int size = memoryInstance.getSize();
                for (int i = 0; i < size; i++) {
                    int row = (offset + i) / colLength;
                    int col = (offset + i) % colLength;
                    if (printString[row][col] == null) {
                        printString[row][col] = (offset + i) + ":" + memoryInstance.getInstanceName();
                    } else {
                        printString[row][col] = printString[row][col] + ", " + memoryInstance.getInstanceName();
                    }
                    if (offset + i + 1 > maxCellsOccupied) {
                        maxCellsOccupied = offset + i + 1;
                    }
                }
            }
        }

        int newRowLength = (int) Math.ceil((1.0 * maxCellsOccupied) / colLength);
        String[][] newPrintString = new String[newRowLength][colLength];
        for (int i = 0; i < newRowLength; i++) {
            System.arraycopy(printString[i], 0, newPrintString[i], 0, colLength);
        }


        PrettyTablePrinter tablePrinter = new PrettyTablePrinter(System.out);
        tablePrinter.print(newPrintString);
        if(outputFile!=null){
            FileUtils.AppendToFile(outputFile, tablePrinter.emit(newPrintString));
            FileUtils.AppendToFile(outputFile, "*********************************************************************************\n");
        }
        System.out.println("*********************************************************************************");
    }
}
