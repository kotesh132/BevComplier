package com.p4.drmt.ilp.som;

import ilog.concert.IloException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class SomSegmentCrossbarTablePlacementSolverTest extends SomFullCrossbarTablePlacementSolverTest {

    @Before
    @Override
    public void setup() {
        super.setup();
    }

    @Test
    public void solve() {

        setUpSolve();
        List<String> expectedTableToSomAndSegment = new ArrayList<>();

        SomSegmentCrossbarTablePlacementSolver solver = new SomSegmentCrossbarTablePlacementSolver(disjointTableDetails, somSpecs);


        try {
            ITablePlacementResult result = solver.solve();
            if (result.containsSolution()) {

                expectedTableToSomAndSegment.add("1:1:[0, 2]");
                expectedTableToSomAndSegment.add("2:1:[1, 3]");
                expectedTableToSomAndSegment.add("5:2:[0, 1]");
                expectedTableToSomAndSegment.add("6:2:[2, 3]");
                expectedTableToSomAndSegment.add("3:2:[2, 3]");
                expectedTableToSomAndSegment.add("4:1:[0, 3]");
                expectedTableToSomAndSegment.add("7:0:[0, 1]");
                for (List<TableDetail> disjointTableDetail : disjointTableDetails) {
                    for (TableDetail tableDetail : disjointTableDetail) {
                        String tableToSomToSegments = tableDetail.tableId + ":" + tableDetail.getAssignedSomId() + ":" + tableDetail.getAssignedSegments();
                        if (!expectedTableToSomAndSegment.contains(tableToSomToSegments)) {
                            Assert.fail("tableId:somNum:segmentsAssigned " + tableToSomToSegments + " is not the expected placement");
                        }
                    }
                }

            } else {
                Assert.fail("Table placement problem could not be solved with ILP");
            }

        } catch (IloException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Override
    public void solveForOneSomAndTcamPlacement() {
        //do nothing, yet to be implemented
    }

    @Test
    @Override
    public void solveForTwoSomAndTcamPlacement() {
        //do nothing, yet to be implemented
    }

    @After
    @Override
    public void tearDown(){
        super.tearDown();
    }

}