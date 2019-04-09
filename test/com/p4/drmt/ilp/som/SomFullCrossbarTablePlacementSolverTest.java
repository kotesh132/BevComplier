package com.p4.drmt.ilp.som;

import com.p4.drmt.ilp.som.SomSpec.SomStore;
import com.p4.drmt.ilp.som.SomSpec.ControllerType;
import ilog.concert.IloException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SomFullCrossbarTablePlacementSolverTest {

    protected List<List<TableDetail>> disjointTableDetails;
    protected List<SomSpec> somSpecs;

    @Before
    public void setup() {
        disjointTableDetails = new ArrayList<>();
        somSpecs = new ArrayList<>();
    }

    protected void setUpSolve() {
        //Here we modeled for 1 in 4 line rate and hence possibility of 4 disjoint sets
        List<Integer> bucket0 = Arrays.asList(1, 2, 5, 6);
        List<Integer> bucket1 = Arrays.asList();
        List<Integer> bucket2 = Arrays.asList(3, 4);
        List<Integer> bucket3 = Arrays.asList(7);

        List<TableDetail> disjointSetBucket0 = new ArrayList<>();
        List<TableDetail> disjointSetBucket1 = new ArrayList<>();
        List<TableDetail> disjointSetBucket2 = new ArrayList<>();
        List<TableDetail> disjointSetBucket3 = new ArrayList<>();

        for (int i = 1; i < 8; i++) {
            TableDetail tableDetail = new TableDetail(i, 2, 1, 2, 0);
            tableDetail.addControllerTypeNum(ControllerType.NUM_READ_CONTROLLERS, 1);
            tableDetail.addControllerTypeNum(ControllerType.NUM_SRAM_CONTROLLERS, 1);
            if (bucket0.contains(i)) {
                disjointSetBucket0.add(tableDetail);
            } else if (bucket1.contains(i)) {
                disjointSetBucket1.add(tableDetail);
            } else if (bucket2.contains(i)) {
                disjointSetBucket2.add(tableDetail);
            } else if (bucket3.contains(i)) {
                disjointSetBucket3.add(tableDetail);
            }
        }


        disjointTableDetails.add(disjointSetBucket0);
        disjointTableDetails.add(disjointSetBucket1);
        disjointTableDetails.add(disjointSetBucket2);
        disjointTableDetails.add(disjointSetBucket3);

        SomSpec somSpec1 = new SomSpec(4, 4, 32, 2, 8, 0);
        somSpec1.addControllerTypeNum(ControllerType.NUM_READ_CONTROLLERS, 4);
        somSpec1.addControllerTypeNum(ControllerType.NUM_WRITE_CONTROLLERS, 4);
        somSpec1.addControllerTypeNum(ControllerType.NUM_HASH_CONTROLLERS, 4);
        somSpec1.addControllerTypeNum(ControllerType.NUM_TCAM_CONTROLLERS, 4);
        somSpec1.addControllerTypeNum(ControllerType.NUM_SRAM_CONTROLLERS, 4);

        SomSpec somSpec2 = new SomSpec(4, 4, 32, 2, 8, 1);
        somSpec2.addControllerTypeNum(ControllerType.NUM_READ_CONTROLLERS, 4);
        somSpec2.addControllerTypeNum(ControllerType.NUM_WRITE_CONTROLLERS, 4);
        somSpec2.addControllerTypeNum(ControllerType.NUM_HASH_CONTROLLERS, 4);
        somSpec2.addControllerTypeNum(ControllerType.NUM_TCAM_CONTROLLERS, 4);
        somSpec2.addControllerTypeNum(ControllerType.NUM_SRAM_CONTROLLERS, 4);

        SomSpec somSpec3 = new SomSpec(4, 4, 32, 2, 8, 2);
        somSpec3.addControllerTypeNum(ControllerType.NUM_READ_CONTROLLERS, 4);
        somSpec3.addControllerTypeNum(ControllerType.NUM_WRITE_CONTROLLERS, 4);
        somSpec3.addControllerTypeNum(ControllerType.NUM_HASH_CONTROLLERS, 4);
        somSpec3.addControllerTypeNum(ControllerType.NUM_TCAM_CONTROLLERS, 4);
        somSpec3.addControllerTypeNum(ControllerType.NUM_SRAM_CONTROLLERS, 4);

        SomSpec somSpec4 = new SomSpec(4, 4, 32, 2, 8, 3);
        somSpec4.addControllerTypeNum(ControllerType.NUM_READ_CONTROLLERS, 4);
        somSpec4.addControllerTypeNum(ControllerType.NUM_WRITE_CONTROLLERS, 4);
        somSpec4.addControllerTypeNum(ControllerType.NUM_HASH_CONTROLLERS, 4);
        somSpec4.addControllerTypeNum(ControllerType.NUM_TCAM_CONTROLLERS, 4);
        somSpec4.addControllerTypeNum(ControllerType.NUM_SRAM_CONTROLLERS, 4);

        somSpecs.add(somSpec1);
        somSpecs.add(somSpec2);
        somSpecs.add(somSpec3);
        somSpecs.add(somSpec4);
    }

    @Test
    public void solve() {

        setUpSolve();
        SomFullCrossbarTablePlacementSolver solver = new SomFullCrossbarTablePlacementSolver(disjointTableDetails, somSpecs);


        try {
            ITablePlacementResult result = solver.solve();
            if (result.containsSolution()) {

                Map<Integer, Integer> expectedTableIdToSomIDMap = new HashMap<>();
                expectedTableIdToSomIDMap.put(1, 2);
                expectedTableIdToSomIDMap.put(2, 3);
                expectedTableIdToSomIDMap.put(3, 0);
                expectedTableIdToSomIDMap.put(4, 0);
                expectedTableIdToSomIDMap.put(5, 2);
                expectedTableIdToSomIDMap.put(6, 0);
                expectedTableIdToSomIDMap.put(7, 0);

                for (List<TableDetail> disjointTableDetail : disjointTableDetails) {
                    for (TableDetail tableDetail : disjointTableDetail) {
                        Integer expectedSOM = expectedTableIdToSomIDMap.get(tableDetail.tableId);
                        int assignedSOM = tableDetail.getAssignedSomId();
                        Assert.assertEquals("for table: " + tableDetail.tableId + " expected SOM: " + expectedSOM + " assigned SOM: " + assignedSOM, (int) expectedSOM, assignedSOM);
                    }
                }
            } else {
                Assert.fail("Table placement problem could not be solved with ILP");
            }

        } catch (IloException e) {
            e.printStackTrace();
        }
    }


    protected void setUpSolveForOneSomAndTcamPlacement() {
        //Here we modeled for 1 in 4 line rate and hence possibility of 4 disjoint sets
        List<Integer> bucket0 = Arrays.asList(1);

        List<TableDetail> disjointSetBucket0 = new ArrayList<>();

        TableDetail tableDetail = new TableDetail(1, 3, 2, 2, 3);
        tableDetail.addControllerTypeNum(ControllerType.NUM_READ_CONTROLLERS, 1);
        tableDetail.addControllerTypeNum(ControllerType.NUM_SRAM_CONTROLLERS, 1);
        tableDetail.addControllerTypeNum(ControllerType.NUM_TCAM_CONTROLLERS, 1);
        disjointSetBucket0.add(tableDetail);

        TableDetail tableDetail1 = new TableDetail(2, 2, 2, 2, 3);
        tableDetail1.addControllerTypeNum(ControllerType.NUM_READ_CONTROLLERS, 1);
        tableDetail1.addControllerTypeNum(ControllerType.NUM_SRAM_CONTROLLERS, 1);
        tableDetail1.addControllerTypeNum(ControllerType.NUM_TCAM_CONTROLLERS, 1);
        disjointSetBucket0.add(tableDetail1);


        disjointTableDetails.add(disjointSetBucket0);

        SomSpec somSpec1 = new SomSpec(8, 8, 32, 2, 8, 0);
        somSpec1.addControllerTypeNum(ControllerType.NUM_READ_CONTROLLERS, 4);
        somSpec1.addControllerTypeNum(ControllerType.NUM_WRITE_CONTROLLERS, 4);
        somSpec1.addControllerTypeNum(ControllerType.NUM_HASH_CONTROLLERS, 4);
        somSpec1.addControllerTypeNum(ControllerType.NUM_TCAM_CONTROLLERS, 4);
        somSpec1.addControllerTypeNum(ControllerType.NUM_SRAM_CONTROLLERS, 4);


        somSpecs.add(somSpec1);
    }

    @Test
    public void solveForOneSomAndTcamPlacement() {
        setUpSolveForOneSomAndTcamPlacement();

        SomFullCrossbarTablePlacementSolver solver = new SomFullCrossbarTablePlacementSolver(disjointTableDetails, somSpecs);

        try {
            ITablePlacementResult result = solver.solve();
            if (result.containsSolution()) {

                List<SomStore> somStores = result.getSomStores();
                result.printSolution();
            } else {
                Assert.fail("Table placement problem could not be solved with ILP");
            }

        } catch (IloException e) {
            e.printStackTrace();
        }
    }


    protected void setUpSolveForTwoSomAndTcamPlacement() {
        //Here we modeled for 1 in 4 line rate and hence possibility of 4 disjoint sets
        List<Integer> bucket0 = Arrays.asList(1);

        List<TableDetail> disjointSetBucket0 = new ArrayList<>();

        TableDetail tableDetail = new TableDetail(1, 3, 2, 2, 3);
        tableDetail.addControllerTypeNum(ControllerType.NUM_READ_CONTROLLERS, 1);
        tableDetail.addControllerTypeNum(ControllerType.NUM_SRAM_CONTROLLERS, 1);
        tableDetail.addControllerTypeNum(ControllerType.NUM_TCAM_CONTROLLERS, 1);
        disjointSetBucket0.add(tableDetail);

        TableDetail tableDetail1 = new TableDetail(2, 2, 2, 2, 3);
        tableDetail1.addControllerTypeNum(ControllerType.NUM_READ_CONTROLLERS, 1);
        tableDetail1.addControllerTypeNum(ControllerType.NUM_SRAM_CONTROLLERS, 1);
        tableDetail1.addControllerTypeNum(ControllerType.NUM_TCAM_CONTROLLERS, 1);
        disjointSetBucket0.add(tableDetail1);

        TableDetail tableDetail2 = new TableDetail(3, 2, 2, 2, 3);
        tableDetail2.addControllerTypeNum(ControllerType.NUM_READ_CONTROLLERS, 1);
        tableDetail2.addControllerTypeNum(ControllerType.NUM_SRAM_CONTROLLERS, 1);
        tableDetail2.addControllerTypeNum(ControllerType.NUM_TCAM_CONTROLLERS, 1);
        disjointSetBucket0.add(tableDetail2);

        TableDetail tableDetail3 = new TableDetail(4, 2, 2, 2, 4);
        tableDetail3.addControllerTypeNum(ControllerType.NUM_READ_CONTROLLERS, 1);
        tableDetail3.addControllerTypeNum(ControllerType.NUM_SRAM_CONTROLLERS, 1);
        tableDetail3.addControllerTypeNum(ControllerType.NUM_TCAM_CONTROLLERS, 1);
        disjointSetBucket0.add(tableDetail3);


        disjointTableDetails.add(disjointSetBucket0);

        SomSpec somSpec1 = new SomSpec(8, 8, 32, 2, 8, 0);
        somSpec1.addControllerTypeNum(ControllerType.NUM_READ_CONTROLLERS, 4);
        somSpec1.addControllerTypeNum(ControllerType.NUM_WRITE_CONTROLLERS, 4);
        somSpec1.addControllerTypeNum(ControllerType.NUM_HASH_CONTROLLERS, 4);
        somSpec1.addControllerTypeNum(ControllerType.NUM_TCAM_CONTROLLERS, 4);
        somSpec1.addControllerTypeNum(ControllerType.NUM_SRAM_CONTROLLERS, 4);

        SomSpec somSpec2 = new SomSpec(8, 8, 32, 2, 8, 1);
        somSpec2.addControllerTypeNum(ControllerType.NUM_READ_CONTROLLERS, 4);
        somSpec2.addControllerTypeNum(ControllerType.NUM_WRITE_CONTROLLERS, 4);
        somSpec2.addControllerTypeNum(ControllerType.NUM_HASH_CONTROLLERS, 4);
        somSpec2.addControllerTypeNum(ControllerType.NUM_TCAM_CONTROLLERS, 4);
        somSpec2.addControllerTypeNum(ControllerType.NUM_SRAM_CONTROLLERS, 4);

        somSpecs.add(somSpec1);
        somSpecs.add(somSpec2);
    }

    @Test
    public void solveForTwoSomAndTcamPlacement() {
        setUpSolveForTwoSomAndTcamPlacement();

        SomFullCrossbarTablePlacementSolver solver = new SomFullCrossbarTablePlacementSolver(disjointTableDetails, somSpecs);

        try {
            ITablePlacementResult result = solver.solve();
            if (result.containsSolution()) {

                List<SomStore> somStores = result.getSomStores();
                result.printSolution();
            } else {
                Assert.fail("Table placement problem could not be solved with ILP");
            }

        } catch (IloException e) {
            e.printStackTrace();
        }
    }


    @After
    public void tearDown() {
        disjointTableDetails.clear();
        somSpecs.clear();
    }

}