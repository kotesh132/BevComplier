package com.p4.drmt.ilp.som;

import com.p4.ds.ListMap;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
public class SomSpec {

    public enum ControllerType {
        NUM_READ_CONTROLLERS("numReadControllers"),
        NUM_WRITE_CONTROLLERS("numWriteControllers"),
        NUM_HASH_CONTROLLERS("numHashControllers"),
        NUM_CAM_CONTROLLERS("numCamControllers"),
        NUM_TCAM_CONTROLLERS("numTcamControllers"),
        NUM_SRAM_CONTROLLERS("numSramControllers");
        

        private String controllerType;

        ControllerType(String controllerType) {
            this.controllerType = controllerType;
        }

        public String getControllerType() {
            return controllerType;
        }

        public static ControllerType typeOf(String controllerType) {
            for (ControllerType type : ControllerType.values()) {
                if (controllerType.equals(type.getControllerType())) {
                    return type;
                }
            }
            return null;
        }
    }

    int numKSeg;
    int numDSeg;

    private Map<String, Integer> controllerTypesMap;
    int numSram;
    int numTcam;

    int numTcamRow;
    int numTcamColumn;

    int somId;


    public SomSpec() {
        controllerTypesMap = new LinkedHashMap<>();
    }

    public SomSpec(int numKSeg, int numDSeg, int numSram, int numTcam) {
        this();
        this.numKSeg = numKSeg;
        this.numDSeg = numDSeg;
        this.numSram = numSram;
        this.numTcam = numTcam;
    }

    public SomSpec(int numKSeg, int numDSeg, int numSram, int numTcamRow, int numTcamColumn, int somId) {
        this();
        this.numKSeg = numKSeg;
        this.numDSeg = numDSeg;
        this.numSram = numSram;
        this.numTcamRow = numTcamRow;
        this.numTcamColumn = numTcamColumn;
        this.numTcam = numTcamRow * numTcamColumn;
        this.somId = somId;
    }


    //We can remove this constructor as we no longer need numOfSoms
    public SomSpec(int numKSeg, int numDSeg, int numSram, int numTcam, int numOfSoms) {
        this(numKSeg, numDSeg, numSram, numTcam);
    }

    public void addControllerTypeNum(ControllerType controllerType, int count) {
        controllerTypesMap.put(controllerType.getControllerType(), count);
    }

    int getControllerTypeNum(ControllerType controllerType) {
        Integer count = controllerTypesMap.get(controllerType.getControllerType());
        return count == null ? 0 : count;
    }

    public void setControllerTypesMap(Map<String, Integer> controllerTypesMap){
        this.controllerTypesMap = controllerTypesMap;
        for (String controllerType : controllerTypesMap.keySet()) {
            if (ControllerType.typeOf(controllerType) == null ){
                throw new RuntimeException("Trying to add invalid controllerType "+ controllerType);
            }
        }
    }

    //This class may not be needed as we can take table details depth/width in multiples of numTcamRow/numTcamColumn
    //Or this can be uses later to store common parameters of all SOM clusters or parameters at SOM level
    @Getter
    @Setter
    @NoArgsConstructor
    public static class SomSpecConstants {

        int numKSeg;
        int numDSeg;

        int ksegWidth;
        int dsegWidth;

        int numTcamRow;
        int numTcamColumn;

        int wordsPerTcam;
        int wordsPerSram;

        int bitWidthSram;

    }

    /**
     * This class stores information about placement of logical tables physically in a SOM.
     * This class can be further improved to store what segments to table mappings etc..
     */
    public static class SomStore {

        SomSpec somSpec;
        Set<TableDetail> tableDetails;

        TableDetail[][] tcamAssignmetToTable;
        ListMap<Integer, TableDetail> kSegmentToTable;


        SomStore(SomSpec somSpec) {
            this.somSpec = somSpec;
            this.tableDetails = new HashSet<>();
            tcamAssignmetToTable = new TableDetail[somSpec.numTcamRow][somSpec.numTcamColumn];
            kSegmentToTable = new ListMap<>(LinkedHashMap.class, ArrayList.class);

            for (int i = 0; i < somSpec.numTcamRow; i++) {
                for (int j = 0; j < somSpec.numTcamColumn; j++) {
                    tcamAssignmetToTable[i][j] = null;
                }
            }
        }

        public void addTableDetail(TableDetail tableDetail) {
            tableDetails.add(tableDetail);
        }


        public void assignTcamToTableDetail(int x, int y, TableDetail tableDetail) throws Exception {
            if (tcamAssignmetToTable[x][y] == null) {
                tcamAssignmetToTable[x][y] = tableDetail;
            } else {
                throw new Exception("Tcam grid (" + x + "," + y + ") allocated to a different table id : " + tcamAssignmetToTable[x][y].tableId + " while trying to assign for table id : " + tableDetail.tableId);
            }
        }

        public SomSpec getSomSpec() {
            return somSpec;
        }

        /**
         * @return 2-d array coordinate refers TCAM location in the TCAM grid
         */
        public TableDetail[][] getTcamAssignmetToTable() {
            return tcamAssignmetToTable;
        }

        public void addTableDetailOnKSegment(int segmentNumber, TableDetail tableDetail) {
            //TODO, need to check for a scenario where two tables from a disjointSet cannot be allocated to same segment number
            kSegmentToTable.add(segmentNumber, tableDetail);
        }

        public void validate() {
            //TODO implement this method to do all kinds of validations once this object is build or incrementally
            //This can be used for unit testing as well.
        }
    }

}
