package com.p4.drmt.ilp.som;

import com.p4.drmt.ilp.som.SomSpec.SomStore;
import com.p4.utils.Utils.Pair;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearIntExpr;
import ilog.cplex.IloCplex;
import ilog.cplex.IloCplex.Param;

import java.util.List;

public class SomSegmentCrossbarTablePlacementSolver extends SomFullCrossbarTablePlacementSolver {
    public SomSegmentCrossbarTablePlacementSolver(List<List<TableDetail>> disjointTableDetails, List<SomSpec> somSpecs) {
        super(disjointTableDetails, somSpecs);
    }

    @Override
    public ITablePlacementResult solve() throws IloException {

        cplex = new IloCplex();
        cplex.setOut(null);
        cplex.setParam(Param.TimeLimit, 100 * 6);


        IloIntVar[][] tablePlacement = addFullCrossbarConstraints();
        Pair<IloIntVar[][][][], IloIntVar[][][][]> rowColPosition = addTcamPlacementConstraints(tablePlacement);
        IloIntVar[][][] SKT = addSegmentCrossBarConstraints(tablePlacement);

        //empty objective function such that first feasible solution is provided
        cplex.addMinimize();

        solved = cplex.solve();
        if (solved) {
            for (int somNum = 0; somNum < NUM_OF_SOMS; somNum++) {
                int numKSeg = somSpecs.get(somNum).numKSeg;
                for (int kSegNum = 0; kSegNum < numKSeg; kSegNum++) {
                    for (int tableNum = 0; tableNum < NUM_OF_TABLES; tableNum++) {
                        if (((int) cplex.getValue(SKT[somNum][kSegNum][tableNum])) == 1) {
                            TableDetail tableDetail = tableDetails.get(tableNum);
                            if (tableDetail.isSomAssigned()) {
                                if (tableDetail.getAssignedSomId() == somNum) {
                                    tableDetail.assignSegment(kSegNum);
                                } else {
                                    solved = false;
                                    return this;
                                }
                            } else {
                                tableDetail.setAssignedSomId(somNum);
                                tableDetail.assignSegment(kSegNum);
                            }
                        }
                    }
                }
            }


            try {
                for (int somNum = 0; somNum < NUM_OF_SOMS; somNum++) {
                    SomStore somStore = new SomStore(somSpecs.get(somNum));
                    somStores.add(somStore);

                    for (int tableNum = 0; tableNum < NUM_OF_TABLES; tableNum++) {
                        if (cplex.getValue(tablePlacement[tableNum][somNum]) == 1) {
                            TableDetail tableDetail = tableDetails.get(tableNum);
                            somStore.addTableDetail(tableDetail);

                            if (tableDetail.usesCam) {
                                IloIntVar[][][][] rowInfo = rowColPosition.first();
                                IloIntVar[][][][] colInfo = rowColPosition.second();
                                for (int k = 0; k < tableDetail.depthInTcamUnits; k++) {
                                    for (int l = 0; l < tableDetail.getWidthInTcamUnits(); l++) {
                                        int x = (int) cplex.getValue(rowInfo[tableNum][somNum][k][l]);
                                        int y = (int) cplex.getValue(colInfo[tableNum][somNum][k][l]);
                                        somStore.assignTcamToTableDetail(x, y, tableDetail);

                                    }
                                }
                            }
                        }

                        int numKSeg = somSpecs.get(somNum).numKSeg;
                        for (int kSegNum = 0; kSegNum < numKSeg; kSegNum++) {
                            if (((int) cplex.getValue(SKT[somNum][kSegNum][tableNum])) == 1) {
                                TableDetail tableDetail = tableDetails.get(tableNum);
                                somStore.addTableDetailOnKSegment(kSegNum, tableDetail);
                            }
                        }

                    }
                }
            } catch (Exception e) {
                solved = false;
            }
        }

        return this;
    }

    private IloIntVar[][][] addSegmentCrossBarConstraints(IloIntVar[][] tablePlacement) throws IloException {
        //Indicator variable to say for ith SOM, jth keySegment of it, kth table is assigned or not
        IloIntVar[][][] SKT = new IloIntVar[NUM_OF_SOMS][][];
        for (int somNum = 0; somNum < NUM_OF_SOMS; somNum++) {
            int numKSeg = somSpecs.get(somNum).numKSeg;
            SKT[somNum] = new IloIntVar[numKSeg][];
            for (int kSegNum = 0; kSegNum < numKSeg; kSegNum++) {
                String[] sktNames = new String[NUM_OF_TABLES];
                for (int tableNum = 0; tableNum < NUM_OF_TABLES; tableNum++) {
                    sktNames[tableNum] = "skt[" + somNum + "][" + kSegNum + "][" + tableNum + "]";
                }
                SKT[somNum][kSegNum] = cplex.boolVarArray(NUM_OF_TABLES, sktNames);
            }
        }


        //On a particual SOM and particular segment, not more than one table should be assigned in a disjoint set
        for (int somNum = 0; somNum < NUM_OF_SOMS; somNum++) {
            int numKSeg = somSpecs.get(somNum).numKSeg;
            for (int kSegNum = 0; kSegNum < numKSeg; kSegNum++) {
                int disjointTableIndex = -1;
                for (List<TableDetail> disjointTableDetail : disjointTableDetails) {
                    disjointTableIndex++;
                    if (!disjointTableDetail.isEmpty()) {
                        IloLinearIntExpr constraintNumOfTablesOnASegment = cplex.linearIntExpr();
                        for (TableDetail tableDetail : disjointTableDetail) {
                            int tableNum = tableIdToIndex.get(tableDetail.tableId);
                            constraintNumOfTablesOnASegment.addTerm(SKT[somNum][kSegNum][tableNum], 1);
                        }
                        cplex.addLe(constraintNumOfTablesOnASegment, 1, "constraintOnNumOfTablesPlacesOnASegment_" + kSegNum + "_ofSOM_" + somNum + "_" + disjointTableIndex);
                    }
                }
            }
        }

        //Sum of num of segments for which a particular table is assigned is equal to numOfKSeg needed for that table
        for (int somNum = 0; somNum < NUM_OF_SOMS; somNum++) {
            for (int tableNum = 0; tableNum < NUM_OF_TABLES; tableNum++) {
                int numKSeg = somSpecs.get(somNum).numKSeg;
                IloLinearIntExpr constraintTableAssignedRequiredNumOfSegments = cplex.linearIntExpr();
                for (int kSegNum = 0; kSegNum < numKSeg; kSegNum++) {
                    constraintTableAssignedRequiredNumOfSegments.addTerm(SKT[somNum][kSegNum][tableNum], 1);
                }
                constraintTableAssignedRequiredNumOfSegments.addTerm(tablePlacement[tableNum][somNum], -1 * tableDetails.get(tableNum).numKSeg);
                cplex.addEq(constraintTableAssignedRequiredNumOfSegments, 0, "constraintTableAssignedRequiredNumOfSegment_tableNum_" + tableNum + "_som_" + somNum);
            }
        }


        //TODO Need to handle data segments availablity
        //TODO allocation of K and D segments needs to consider origin point(processor) of matches
        return SKT;

    }
}
