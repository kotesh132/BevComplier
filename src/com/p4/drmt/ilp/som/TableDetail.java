package com.p4.drmt.ilp.som;


import com.p4.drmt.ilp.som.SomSpec.ControllerType;
import com.p4.pktgen.enums.KeyType;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Getter
@Setter
@NoArgsConstructor
public class TableDetail {

    int numKSeg;
    int numDseg;

    int tableId;
    
    KeyType keytype;

    int numSrams;
    int numTcams;
    
    int keyWidth;
    int dataWidth;
    int numEntries;

    int depthInTcamUnits;

    int assignedSomId;
    List<Integer> assignedSegments;

    boolean usesHash;
    boolean usesCam;

    boolean somAssigned;

    Map<ControllerType, Integer> controllerTypesMap;

    public TableDetail(int tableId, int numKSeg, int numDseg, int numSrams, int depthInTcamUnits) {
        this.tableId = tableId;
        this.numKSeg = numKSeg;
        this.numDseg = numDseg;
        this.numSrams = numSrams;
        this.depthInTcamUnits = depthInTcamUnits;
        controllerTypesMap = new HashMap<>();
        assignedSegments = new ArrayList<>();
        somAssigned = false;
        usesHash = false;
        usesCam = false;
    }

    public void addControllerTypeNum(ControllerType controllerType, int count) {
        if (controllerTypesMap == null) {
            controllerTypesMap = new HashMap<>();
        }
        controllerTypesMap.put(controllerType, count);
        updateFields(controllerType);

    }

    public int getWidthInTcamUnits() {
        return numKSeg;
    }

    public int getDepthInTcamUnits() {
        return depthInTcamUnits;
    }

    private void updateFields(ControllerType controllerType) {
        switch (controllerType) {
            case NUM_HASH_CONTROLLERS:
                this.usesHash = true;
                break;
            case NUM_CAM_CONTROLLERS:
                this.usesCam = true;
                this.numTcams = depthInTcamUnits * numKSeg;
                break;
        }
    }

    int getControllerTypeNum(ControllerType controllerType) {
        Integer count = controllerTypesMap.get(controllerType);
        return count == null ? 0 : count;
    }

    public void setAssignedSomId(int assignedSomId) {
        if (!somAssigned) {
            this.assignedSomId = assignedSomId;
            this.somAssigned = true;
        }
    }

    public void assignSegment(int segment) {
        this.assignedSegments.add(segment);
    }


    @Data
    public static class TableSpec {

        int tableId;

        int keyWidth;
        String keyType;
        int dataWidth;
        int numEntries;

        int disjointBucket;
        Map<String, Integer> controllerTypesMap;

    }

    public static class TableData {

        TableSpec tableSpec;
        Map<BitSet, BitSet> tableEntries;

        TableData(TableSpec tableSpec) {

            this.tableSpec = tableSpec;
            tableEntries = new HashMap<>();
        }

        public void loadTableWithRandomData() {

            int numberOfBitsPerDsegment = 80;
            for (int i = 0; i < tableSpec.getNumEntries(); i++) {
                BitSet key = getRandomBitSetOfLength(tableSpec.getKeyWidth());
                BitSet data = getRandomBitSetOfLength(tableSpec.getDataWidth());
                if (!tableEntries.containsKey(key)) {
                    tableEntries.put(key, data);
                } else {
                    i--;
                }
            }
        }


        private static BitSet getRandomBitSetOfLength(int numOfBits) {
            Random random = new Random();
            BitSet bitSet = new BitSet();

            for (int i = 0; i < numOfBits; i++) {
                bitSet.set(i, random.nextInt(2));
            }

            return bitSet;
        }
    }

}
