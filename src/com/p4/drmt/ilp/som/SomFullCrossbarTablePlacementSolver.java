package com.p4.drmt.ilp.som;

import com.p4.drmt.ilp.som.SomSpec.ControllerType;
import com.p4.drmt.ilp.som.SomSpec.SomStore;
import com.p4.utils.Utils.Pair;
import ilog.concert.IloConstraint;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloLinearIntExpr;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;
import ilog.cplex.IloCplex.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SomFullCrossbarTablePlacementSolver implements ITablePlacementResult {

    private static final Logger L = LoggerFactory.getLogger(SomFullCrossbarTablePlacementSolver.class);

    //each sub list indicates a disjoint set and means that tables in a particular disjoint set are accessed in the same clock cycle
    protected List<List<TableDetail>> disjointTableDetails;
    protected List<SomSpec> somSpecs;
    protected IloCplex cplex;
    protected List<TableDetail> tableDetails;
    protected Map<Integer, Integer> tableIdToIndex;
    protected int NUM_OF_TABLES;
    protected int NUM_OF_SOMS;

    protected List<SomStore> somStores;

    boolean solved;

    public SomFullCrossbarTablePlacementSolver(List<List<TableDetail>> disjointTableDetails, List<SomSpec> somSpecs) {

        this.disjointTableDetails = disjointTableDetails;
        this.somSpecs = somSpecs;

        tableDetails = new ArrayList<>();
        tableIdToIndex = new HashMap<>();
        for (List<TableDetail> disjointTableDetail : disjointTableDetails) {
            tableDetails.addAll(disjointTableDetail);
        }

        for (int i = 0; i < tableDetails.size(); i++) {
            tableIdToIndex.put(tableDetails.get(i).tableId, i);
        }

        NUM_OF_TABLES = tableDetails.size();
        NUM_OF_SOMS = somSpecs.size();
        somStores = new ArrayList<>();
        solved = false;

    }

    public ITablePlacementResult solve() throws IloException {


        cplex = new IloCplex();
        if (!L.isDebugEnabled()) {
            cplex.setOut(null);
        }
        cplex.setParam(Param.TimeLimit, 100 * 6);
        IloIntVar[][] tablePlacement = addFullCrossbarConstraints();
        Pair<IloIntVar[][][][], IloIntVar[][][][]> rowColPosition = addTcamPlacementConstraints(tablePlacement);


        //empty objective function such that first feasible solution is provided
        cplex.addMinimize();

        solved = cplex.solve();

        if (solved) {

            for (int i = 0; i < NUM_OF_TABLES; i++) {
                for (int j = 0; j < NUM_OF_SOMS; j++) {
                    int value = (int) cplex.getValue(tablePlacement[i][j]);
                    if (value == 1) {//ith table placed in jth SOM;
                        tableDetails.get(i).setAssignedSomId(j);
                    }
                }
            }

            try {
                for (int somNum = 0; somNum < NUM_OF_SOMS; somNum++) {
                    SomStore somStore = new SomStore(somSpecs.get(somNum));
                    somStores.add(somStore);

                    for (int tableNum = 0; tableNum < NUM_OF_TABLES; tableNum++) {
                        if (Math.round(cplex.getValue(tablePlacement[tableNum][somNum])) == 1) {
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
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                solved = false;
            }

        }else {
            L.debug(cplex.getModel().toString());
        }

        return this;


    }

    protected Pair<IloIntVar[][][][], IloIntVar[][][][]> addTcamPlacementConstraints(IloIntVar[][] tablePlacement) throws IloException {

        //Based on table key width x depth, it is divided in to blocks(each of tcam size)
        //for eg, table of 3 x 2 is divided as below. Each block will fit into a tcam unit
        //+------+------+------+
        //|   A  |  B   |   C  |
        //+------+------+------+
        //|   D  |  E   |   F  |
        //+------+------+------+

        //Each of the block A, B, C etc.. is placed at (x, y) location of TCAM grid
        //Below are constraints, A(x) gives x coordinate in TCAM grid, A(y) gives y coordinate in TCAM grid it is placed in
        // A(x) = B(x) = C(x)  -> all bitwise blocks should be allocated to same row of TCAM (ie.,x coordinate)
        // D(x) = E(x) = C(x)  -> all bitwise blocks should be allocated to same row of TCAM (ie.,x coordinate)
        // A(y) < B(y) < C(y)  -> All wordwise blocks of same bitwise row should be allocated different columns of TCAM
        // D(y) < E(y) < F(y)  -> All wordwise blocks of same bitwise row should be allocated different columns of TCAM
        // if (A(x) = D(x)) then C(y) < D(y) -> if two wordwise blocks are allocated in same row of TCAM, then columns should not be same

        //Above conditions are needed only when that table is placed in the SOM
        //This condition to be applied for those tables which needs to be placed in TCAM


        //variable to store rownum  of TCAM grid in which ith table placed in jth SOM, (k,l) block of that table
        IloIntVar[][][][] R = new IloIntVar[NUM_OF_TABLES][][][];
        for (int tableNum = 0; tableNum < NUM_OF_TABLES; tableNum++) {
            if (tableDetails.get(tableNum).usesCam) {
                R[tableNum] = new IloIntVar[NUM_OF_SOMS][][];
                for (int somNum = 0; somNum < NUM_OF_SOMS; somNum++) {
                    int depth = tableDetails.get(tableNum).depthInTcamUnits;
                    R[tableNum][somNum] = new IloIntVar[depth][];
                    for (int k = 0; k < depth; k++) {
                        int width = tableDetails.get(tableNum).getWidthInTcamUnits();
                        String[] Rnames = new String[width];
                        for (int l = 0; l < width; l++) {
                            Rnames[l] = "R[" + tableDetails.get(tableNum).tableId + "]" + "[" + somNum + "]" + "[" + k + "]" + "[" + l + "]";
                        }
                        R[tableNum][somNum][k] = cplex.intVarArray(width, 0, somSpecs.get(somNum).numTcamRow - 1, Rnames);
                    }
                }
            }
        }

        //variable to store ith table placed in jth SOM, (k,l) block of that table gets stored in which column of TCAM
        IloIntVar[][][][] C = new IloIntVar[NUM_OF_TABLES][][][];
        for (int tableNum = 0; tableNum < NUM_OF_TABLES; tableNum++) {
            if (tableDetails.get(tableNum).usesCam) {
                C[tableNum] = new IloIntVar[NUM_OF_SOMS][][];
                for (int somNum = 0; somNum < NUM_OF_SOMS; somNum++) {
                    C[tableNum][somNum] = new IloIntVar[tableDetails.get(tableNum).depthInTcamUnits][];
                    for (int k = 0; k < tableDetails.get(tableNum).depthInTcamUnits; k++) {
                        String[] Cnames = new String[tableDetails.get(tableNum).getWidthInTcamUnits()];
                        for (int l = 0; l < tableDetails.get(tableNum).getWidthInTcamUnits(); l++) {
                            Cnames[l] = "C[" + tableDetails.get(tableNum).tableId + "]" + "[" + somNum + "]" + "[" + k + "]" + "[" + l + "]";
                        }
                        C[tableNum][somNum][k] = cplex.intVarArray(tableDetails.get(tableNum).getWidthInTcamUnits(), 0, somSpecs.get(somNum).numTcamColumn - 1, Cnames);
                    }
                }
            }
        }


        for (int tableNum = 0; tableNum < NUM_OF_TABLES; tableNum++) {
            if (tableDetails.get(tableNum).usesCam) {
                for (int somNum = 0; somNum < NUM_OF_SOMS; somNum++) {

                    IloRange tablePlacedInThatSOM = cplex.ge(tablePlacement[tableNum][somNum], 1);

                    List<IloConstraint> constraints = new ArrayList<>();

                    //All bitwise blocks should be allocated in the same row
                    int depth = tableDetails.get(tableNum).depthInTcamUnits;
                    for (int k = 0; k < depth; k++) {
                        for (int l = 1; l < tableDetails.get(tableNum).getWidthInTcamUnits(); l++) {
                            IloConstraint rowEqConstraint = cplex.eq(R[tableNum][somNum][k][l - 1], R[tableNum][somNum][k][l]);
                            constraints.add(rowEqConstraint);
                        }
                    }

                    //All wordwise blocks of same bitwise row should be allocated different columns of TCAM
                    for (int k = 0; k < tableDetails.get(tableNum).depthInTcamUnits; k++) {
                        for (int l = 1; l < tableDetails.get(tableNum).getWidthInTcamUnits(); l++) {
                            IloLinearIntExpr colInEqConstraint = cplex.linearIntExpr();
                            colInEqConstraint.addTerm(C[tableNum][somNum][k][l], 1);
                            colInEqConstraint.addTerm(C[tableNum][somNum][k][l - 1], -1);
                            constraints.add(cplex.ge(colInEqConstraint, 1));
                        }
                    }


                    //if two wordwise blocks are allocated in same row of CAM, then columns should not be same
                    for (int k = 0; k < tableDetails.get(tableNum).depthInTcamUnits; k++) {
                        for (int k1 = k + 1; k1 < tableDetails.get(tableNum).depthInTcamUnits; k1++) {
                            IloConstraint rowsEqConstraint = cplex.eq(R[tableNum][somNum][k1][0], R[tableNum][somNum][k][0]);
                            IloLinearIntExpr colsInEqExpr = cplex.linearIntExpr();
                            colsInEqExpr.addTerm(C[tableNum][somNum][k1][0], 1);
                            colsInEqExpr.addTerm(C[tableNum][somNum][k][tableDetails.get(tableNum).getWidthInTcamUnits() - 1], -1);
                            IloRange colsInEqConstraint = cplex.ge(colsInEqExpr, 1);
                            constraints.add(cplex.ifThen(rowsEqConstraint, colsInEqConstraint, "rowsEqThenColsInEqual_"+tableNum+"_"+somNum+"_"+k+"_"+k1));
                        }
                    }

                    IloConstraint rowColConstraints = cplex.and(constraints.toArray(new IloConstraint[0]));
                    cplex.add(cplex.ifThen(tablePlacedInThatSOM, rowColConstraints, "RowColConstraintsIfTable_"+tableNum+"_PlacedInSOM_"+somNum));

                }
            }
        }

        //If two tables are placed in the same SOM, blocks(A or B Or c etc..) from those table cannot be mapped to same tcam in the CAM
        for (int somNum = 0; somNum < NUM_OF_SOMS; somNum++) {
            for (int tableNum = 0; tableNum < NUM_OF_TABLES; tableNum++) {
                for (int tableNum1 = tableNum + 1; tableNum1 < NUM_OF_TABLES; tableNum1++) {
                    if (tableDetails.get(tableNum).usesCam && tableDetails.get(tableNum1).usesCam) {

                        IloConstraint forTablesPlacedInSameSOM = cplex.eq(tablePlacement[tableNum][somNum], tablePlacement[tableNum1][somNum]);

                        List<IloConstraint> constraints = new ArrayList<>();
                        for (int k = 0; k < tableDetails.get(tableNum).depthInTcamUnits; k++) {
                            for (int l = 0; l < tableDetails.get(tableNum).getWidthInTcamUnits(); l++) {
                                for (int k1 = 0; k1 < tableDetails.get(tableNum1).depthInTcamUnits; k1++) {
                                    for (int l1 = 0; l1 < tableDetails.get(tableNum1).getWidthInTcamUnits(); l1++) {
                                        IloConstraint rowNotEqual = cplex.not(cplex.eq(R[tableNum][somNum][k][l], R[tableNum1][somNum][k1][l1]));
                                        IloConstraint colNotEqual = cplex.not(cplex.eq(C[tableNum][somNum][k][l], C[tableNum1][somNum][k1][l1]));
                                        IloConstraint rowNotEqualOrColNotEqual = cplex.or(rowNotEqual, colNotEqual);
                                        constraints.add(rowNotEqualOrColNotEqual);
                                    }
                                }
                            }
                        }
                        IloConstraint noBlockOfTablesAssignedToSameTcamInGrid = cplex.and(constraints.toArray(new IloConstraint[0]));
                        cplex.add(cplex.ifThen(forTablesPlacedInSameSOM, noBlockOfTablesAssignedToSameTcamInGrid));
                    }
                }
            }
        }

        return new Pair<>(R, C);
    }

    protected IloIntVar[][] addFullCrossbarConstraints() throws IloException {
        //Indicator variable to say ith table can be placed in jth som
        IloIntVar[][] tablePlacement = new IloIntVar[NUM_OF_TABLES][];
        for (int tableNum = 0; tableNum < NUM_OF_TABLES; tableNum++) {
            String[] tablePlacementNames = new String[NUM_OF_SOMS];
            for (int somNum = 0; somNum < NUM_OF_SOMS; somNum++) {
                tablePlacementNames[somNum] = "tablePlacement[" + tableDetails.get(tableNum).tableId + "][" + somNum + "]";
            }
            tablePlacement[tableNum] = cplex.boolVarArray(NUM_OF_SOMS, tablePlacementNames);
        }

        //Indicator vairable to say numOfTables in a SOM
        String[] noOfTablesInSOMNames = new String[NUM_OF_SOMS];
        for (int somNum = 0; somNum < NUM_OF_SOMS; somNum++) {
            noOfTablesInSOMNames[somNum] = "noOfTablesInSOM[" + somNum + "]";
        }
        IloIntVar[] noOfTablesInSOM = cplex.intVarArray(NUM_OF_SOMS, 0, tableDetails.size(), noOfTablesInSOMNames);


        //constraint between tablePlacement and noOfTablesInSOM variables
        for (int somNum = 0; somNum < NUM_OF_SOMS; somNum++) {
            IloLinearIntExpr constraintNumOfTablesInSOM = cplex.linearIntExpr();
            for (int tableNum = 0; tableNum < NUM_OF_TABLES; tableNum++) {
                constraintNumOfTablesInSOM.addTerm(tablePlacement[tableNum][somNum], 1);
            }
            constraintNumOfTablesInSOM.addTerm(noOfTablesInSOM[somNum], -1);
            cplex.addEq(constraintNumOfTablesInSOM, 0, "constraintNumOfTablesInSom_" + somNum);
        }

        //every table should be placed in one and only one som
        for (int tableNum = 0; tableNum < NUM_OF_TABLES; tableNum++) {
            IloLinearIntExpr constraintTablePlacementInOneSOM = cplex.linearIntExpr();
            for (int somNum = 0; somNum < NUM_OF_SOMS; somNum++) {
                constraintTablePlacementInOneSOM.addTerm(tablePlacement[tableNum][somNum], 1);
            }
            cplex.addEq(constraintTablePlacementInOneSOM, 1, "constraintTable_" + tableNum + "_placement");
        }


        //constraint on table placement such that their total sram/tcams do not exceed available resources in SOM
        for (int somNum = 0; somNum < NUM_OF_SOMS; somNum++) {
            IloLinearIntExpr constraintTotalSrams = cplex.linearIntExpr();
            IloLinearIntExpr constraintTotalTcams = cplex.linearIntExpr();
            for (int tableNum = 0; tableNum < NUM_OF_TABLES; tableNum++) {
                constraintTotalSrams.addTerm(tablePlacement[tableNum][somNum], tableDetails.get(tableNum).numSrams);
                constraintTotalTcams.addTerm(tablePlacement[tableNum][somNum], tableDetails.get(tableNum).numTcams);
            }
            cplex.addLe(constraintTotalSrams, somSpecs.get(somNum).numSram, "constraintTotalSramsForSom_" + somNum);
            cplex.addLe(constraintTotalTcams, somSpecs.get(somNum).numTcam, "constraintTotalTcamsForSom_" + somNum);
        }


        //For every SOM, summation of numKSeg/numDSeg in particular disjoint set should not exceed maxKSeg/maxDseg
        for (int somNum = 0; somNum < NUM_OF_SOMS; somNum++) {

            int disjointSetIndex = -1;
            for (List<TableDetail> disjointTableDetail : disjointTableDetails) {
                disjointSetIndex++;
                if (!disjointTableDetail.isEmpty()) {
                    IloLinearIntExpr constraintMaxKSeg = cplex.linearIntExpr();
                    IloLinearIntExpr constraintMaxDSeg = cplex.linearIntExpr();

                    for (TableDetail tableDetail : disjointTableDetail) {
                        int tableNum = tableIdToIndex.get(tableDetail.tableId);
                        constraintMaxKSeg.addTerm(tablePlacement[tableNum][somNum], tableDetails.get(tableNum).numKSeg);
                        constraintMaxDSeg.addTerm(tablePlacement[tableNum][somNum], tableDetails.get(tableNum).numDseg);

                    }

                    cplex.addLe(constraintMaxKSeg, somSpecs.get(somNum).numKSeg, "constraintMaxKSegForSom_" + somNum + "_forDisjointSet_" + disjointSetIndex);
                    cplex.addLe(constraintMaxDSeg, somSpecs.get(somNum).numDSeg, "constraintMaxDSegForSom_" + somNum + "_forDisjointSet_" + disjointSetIndex);
                }

            }

        }


        //For every controllerType in a SOM, summation of that controllerType usage in particular disjoint set should not exceed available controllerTypes
        for (ControllerType controllerType : ControllerType.values()) {
            for (int somNum = 0; somNum < NUM_OF_SOMS; somNum++) {
                int disjointSetIndex = -1;
                for (List<TableDetail> disjointTableDetail : disjointTableDetails) {
                    disjointSetIndex++;
                    if (!disjointTableDetail.isEmpty()) {
                        IloLinearIntExpr constraintOnControllerType = cplex.linearIntExpr();
                        for (TableDetail tableDetail : disjointTableDetail) {
                            int tableNum = tableIdToIndex.get(tableDetail.tableId);
                            constraintOnControllerType.addTerm(tablePlacement[tableNum][somNum], tableDetails.get(tableNum).getControllerTypeNum(controllerType));
                        }
                        cplex.addLe(constraintOnControllerType, somSpecs.get(somNum).getControllerTypeNum(controllerType), "constraintOnControllerType_" + controllerType.getControllerType() + "_onSom_" + somNum + "_forDisjointSet_" + disjointSetIndex);
                    }
                }
            }
        }


        //Number of tables(which uses CAM) that can be placed in a SOM is less than or equal to number of available CAM Controllers
        for (int somNum = 0; somNum < NUM_OF_SOMS; somNum++) {
            for (int tableNum = 0; tableNum < NUM_OF_TABLES; tableNum++) {
                IloLinearIntExpr camToTablesConstraint = cplex.linearIntExpr();
                if (tableDetails.get(tableNum).usesCam) {
                    camToTablesConstraint.addTerm(tablePlacement[tableNum][somNum], 1);
                }
                cplex.addLe(camToTablesConstraint, somSpecs.get(somNum).getControllerTypeNum(ControllerType.NUM_CAM_CONTROLLERS), "constraintOnNumberOfCAMControllerInSOM_" + somNum);
            }
        }


        return tablePlacement;
    }

    protected void printCamPlacementSolution() {

        for (SomStore somStore : somStores) {
            SomSpec currentSomSpec = somStore.getSomSpec();
            int currentSomId = somStore.getSomSpec().somId;
            System.out.println("For Som : " + currentSomId);

            PrettyTablePrinter tablePrinter = new PrettyTablePrinter(System.out);
            TableDetail[][] tcamAssignmentToTable = somStore.getTcamAssignmetToTable();
            String[][] tCamAssignment = new String[currentSomSpec.numTcamRow][currentSomSpec.numTcamColumn];
            Map<Integer, Integer> tableBlockCounter = new HashMap<>();
            for (int i = 0; i < currentSomSpec.numTcamRow; i++) {
                for (int j = 0; j < currentSomSpec.numTcamColumn; j++) {
                    TableDetail tableDetail = tcamAssignmentToTable[i][j];
                    if (tableDetail == null) {
                        tCamAssignment[i][j] = "X";
                    } else {
                        Integer tableId = tableDetail.tableId;
                        int counter = -1;
                        if (tableBlockCounter.containsKey(tableId)) {
                            counter = tableBlockCounter.get(tableId);
                        }
                        counter = (counter + 1) % tableDetail.numKSeg == 0 ? 0 : counter + 1;

                        tableBlockCounter.put(tableId, counter);
                        tCamAssignment[i][j] = tableId + "(" + counter + ")";
                    }
                }
            }

            tablePrinter.print(tCamAssignment);
        }

    }

    @Override
    public List<SomStore> getSomStores() {
        return somStores;
    }

    @Override
    public void printSolution() {
        printCamPlacementSolution();
    }

    @Override
    public boolean containsSolution() {
        return solved;

    }
}