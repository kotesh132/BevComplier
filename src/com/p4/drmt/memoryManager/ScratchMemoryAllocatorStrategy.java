package com.p4.drmt.memoryManager;

import com.p4.drmt.cfg.DRMTConstants;
import com.p4.drmt.cfg.KeyMeta;
import com.p4.drmt.ilp.IUnitDAGVertex;
import com.p4.drmt.struct.IAction;
import com.p4.drmt.struct.IField;
import com.p4.drmt.struct.IMatchNode;
import com.p4.drmt.struct.ITable;
import com.p4.drmt.struct.IUnit;
import com.p4.ds.ListMap;
import com.p4.p416.applications.Target.MemoryType;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ScratchMemoryAllocatorStrategy implements IAlignedMemoryAllocatorStrategy {
	
	private static final Logger L = LoggerFactory.getLogger(ScratchMemoryAllocatorStrategy.class);

    private List<List<IUnitDAGVertex>> matches = null;
    private int maxAvailableMemory;
    private final int dataSegmentSize = DRMTConstants.maxBytes; //bytes
    private final int numDataSegmentSizeUnitsInScratchMemory = 2 * DRMTConstants.numkseg;
    private Map<IMemoryInstance, Integer> memoryInstanceToOffsetMap;

    public ScratchMemoryAllocatorStrategy(ListMap<Integer, IUnitDAGVertex> schedule, Map<IMemoryInstance, Integer> memoryInstanceToOffsetMap, int maxAvailableMemory) {

        this.maxAvailableMemory = maxAvailableMemory;
        this.memoryInstanceToOffsetMap = memoryInstanceToOffsetMap;
        matches = schedule.values()
                .stream()
                .filter(vertices -> !vertices.isEmpty() && vertices.get(0).isMatch())
                .collect(Collectors.toList());

    }

    @Override
    public int allocateBitMemory() {
        return 0;
    }

    @Override
    public int allocateByteMemory() throws InsufficientMemoryException {
        if (matches == null) {
            return 0;
        }

        int dataSegmentNumber = 0;
        for (List<IUnitDAGVertex> matchList : matches) {
            for (IUnitDAGVertex match : matchList) {
                IUnit unit = match.getUnit();

                assert unit instanceof IMatchNode;
                IMatchNode matchActionNode = (IMatchNode) unit;

                matchActionNode.setOffset(dataSegmentNumber);

                ITable table = matchActionNode.getTable();


                List<IAction> actions = table.getActions();

                int maxOffset = 0;
                for (IAction action : actions) {
                    int localOffset = 0;
                    List<IField> parameters = action.getParameters();
                    for (IField parameter : parameters) {

                        memoryInstanceToOffsetMap.put(new MemoryInstance(parameter.getProgName(), parameter.getFieldType()), localOffset);

                        localOffset += (int) Math.ceil((1.0 * parameter.getSize()) / 8);

                        AlignmentType alignmentType = AlignmentType.alignmentTypeOf(parameter.getSize());
                        localOffset = IAlignedMemoryAllocatorStrategy.getAlignedSlot(alignmentType, localOffset);

                        if (localOffset > maxAvailableMemory) {
                            throw new InsufficientMemoryException(MemoryType.TypeSrcByte, localOffset, maxAvailableMemory);
                        }
                    }

                    if (maxOffset < localOffset) {
                        maxOffset = localOffset;
                    }

                }
                int dataSegmentsNeededForThisMatch = (int) Math.ceil((1.0 * maxOffset) / dataSegmentSize);
                dataSegmentNumber += dataSegmentsNeededForThisMatch;

                dataSegmentNumber = dataSegmentNumber % numDataSegmentSizeUnitsInScratchMemory;

            }
        }
        return 0;

    }


    @Override
    public int allocate() throws InsufficientMemoryException {
    	L.debug("allocating Scratch memory");
        return allocateByteMemory();
    }
}
