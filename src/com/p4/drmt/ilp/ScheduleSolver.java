package com.p4.drmt.ilp;

import com.p4.drmt.alu.ALUType;
import com.p4.drmt.alu.AluComplex;
import com.p4.drmt.cfg.ALUComplexConstants;
import com.p4.drmt.cfg.DRMTConstants;
import com.p4.drmt.cfg.KeyMeta;
import com.p4.drmt.schd.NewScheduler;
import com.p4.drmt.struct.IUnit;
import com.p4.ds.ListMap;
import com.p4.ids.IDAGEdge;
import com.p4.ids.IDAGGraph;
import com.p4.ids.IGraph;
import com.p4.tools.graph.Edge;
import com.p4.utils.Utils.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.System.exit;

public class ScheduleSolver {

    private static final Logger L = LoggerFactory.getLogger(ScheduleSolver.class);

    public static IDrmtScheduleSolverSolution ilpSolveForBestPeriod(Set<Edge<IUnit>> dfgEdges, Set<IUnit> allNodes) {
        IGraph<IUnitDAGVertex, IDAGEdge> dag = DAGBuilder.getDAG(dfgEdges, allNodes);
        InputSpec inputSpec = new InputSpec(32, 4, 80, 8, 80, 1, 1, 22, 2, 0, false, 4, 16, 4);

        return solveForBestPeriod(inputSpec, (IDAGGraph<IUnitDAGVertex, IDAGEdge>) dag);
    }


    public static IDrmtScheduleSolverSolution ilpSolveForPacketRate(Set<Edge<IUnit>> dfgEdges, Set<IUnit> allNodes, int matchDelay, int actionDelay) {
        int packetRate = DRMTConstants.PACKETRATE;
        int numcontexts = DRMTConstants.NUMCONTXT;
        int numProc = DRMTConstants.NUMDRMTPROCS;
        int numALUs = ALUComplexConstants.NUMALUS;
        int numkseg = DRMTConstants.numkseg;
        int segmentBits = 8 * DRMTConstants.maxBytes;


        IGraph<IUnitDAGVertex, IDAGEdge> dag = DAGBuilder.getDAG(dfgEdges, allNodes);
        InputSpec inputSpec = new InputSpec(numALUs, numkseg, segmentBits, numkseg, segmentBits, 1, 1, matchDelay, actionDelay, 0, false, numProc, 16, packetRate);
        inputSpec.addAluTypeFieldsLimit(ALUType.COMPLEX_BYTE_ALU, ALUComplexConstants.NUMALUS);
        inputSpec.addAluTypeFieldsLimit(ALUType.BIT_ALU, 1);
        inputSpec.setSolveForDifferentAluTypes(true);

        return solveForPacketRate(inputSpec, (IDAGGraph<IUnitDAGVertex, IDAGEdge>) dag);
    }


    //This is initial test code to convert existing constraints defined in (python using Gurobi - https://github.com/anirudhSK/drmt/)
    public static void main(String[] args) {

        DAGBuilder dagBuilder = new DAGBuilder();
        InputSpec inputSpec = new InputSpec(32, 4, 80, 8, 80, 1, 1, 22, 2, 0, false, 4, 16, 4);

        IDAGGraph<IUnitDAGVertex, IDAGEdge> dagGraph = (IDAGGraph<IUnitDAGVertex, IDAGEdge>) dagBuilder.getSampleDAG(inputSpec);

        solveForBestPeriod(inputSpec, dagGraph);

    }


    private static IDrmtScheduleSolverSolution solveForPacketRate(InputSpec inputSpec, IDAGGraph<IUnitDAGVertex, IDAGEdge> dagGraph) {
        Pair<Integer, List<IUnitDAGVertex>> criticalPath = dagGraph.getCriticalPath();

        printProblem(dagGraph, criticalPath, inputSpec);

        int period = inputSpec.getNumOfProcessors() * inputSpec.getPacketRate();

        int maxNumOfCyclesPerPacket = 2 * inputSpec.getNumOfProcessors() * inputSpec.getNumOfContexts() * inputSpec.getPacketRate();
        int Q_MAX = (int) Math.ceil((1.5 * criticalPath.first()) / period);

        while (Q_MAX <= maxNumOfCyclesPerPacket / inputSpec.getPacketRate()){
            DrmtScheduleSolver solver = new DrmtScheduleSolver(dagGraph, inputSpec, period, Q_MAX);
            if (solver.solve()) {
                solver.printSolution();
                return solver;
            }
            Q_MAX = Q_MAX + 1;
        }

        return null;
    }

    private static IDrmtScheduleSolverSolution solveForBestPeriod(InputSpec inputSpec, IDAGGraph<IUnitDAGVertex, IDAGEdge> dagGraph) {
        Pair<Integer, List<IUnitDAGVertex>> criticalPath = dagGraph.getCriticalPath();

        double tpt_upper_bound = printProblem(dagGraph, criticalPath, inputSpec);
        double tpt_lower_bound = 0.01;

        int period_lower_bound = (int) Math.ceil(1.0 / tpt_upper_bound);
        int period_upper_bound = (int) Math.ceil(1.0 / tpt_lower_bound);

        int period = period_upper_bound;
        ListMap<Integer, IUnitDAGVertex> last_good_solution = null;
        DrmtScheduleSolver last_good_solver = null;
        int last_good_period = Integer.MAX_VALUE;

        L.info("Searching between limits " + period_lower_bound + " and " + period_upper_bound + " cycles");
        int low = period_lower_bound;
        int high = period_upper_bound;

        int Q_MAX = (int) Math.ceil((1.5 * criticalPath.first()) / period);

        while (low <= high) {
            period = (int) Math.ceil((low + high) / 2.0);
            L.info("period = " + period + " cycles");
            L.info("*******Scheduling DRMT*******");

            DrmtScheduleSolver solver = new DrmtScheduleSolver(dagGraph, inputSpec, period, Q_MAX);

            if (solver.solve()) {
                last_good_period = period;
                last_good_solver = solver;
                high = period - 1;
                L.info("Found feasible soultion by ILP Solver at period " + period + " cycles");
            } else {
                low = period + 1;
                L.info("Soultion not found by ILP Solver at period " + period + " cycles");
            }
        }

        if (last_good_solver != null) {
            L.info("\nlast good solution period : " + last_good_period);
            last_good_solver.printSolution();
            return last_good_solver;
        }
        return null;
    }

    private static double printProblem(IDAGGraph<IUnitDAGVertex, IDAGEdge> dagGraph, Pair<Integer, List<IUnitDAGVertex>> criticalPath, InputSpec inputSpec) {

        L.info("# of nodes = " + dagGraph.getVertexCount());
        L.info("# of edges = " + dagGraph.getEdges().size());

        List<IUnitDAGVertex> match_vertices = dagGraph.getVertices()
                .stream()
                .filter(vertex -> "match".equals(vertex.getType()))
                .collect(Collectors.toList());

        List<IUnitDAGVertex> action_vertices = dagGraph.getVertices()
                .stream()
                .filter(vertex -> "action".equals(vertex.getType()))
                .collect(Collectors.toList());

        L.info("# of matches = " + match_vertices.size());
        L.info("# of actions = " + action_vertices.size());

        L.info("Match unit size = " + inputSpec.getMatchUnitSize());

        int match_units = match_vertices.stream()
                .map(v -> Math.ceil((1.0 * v.getKeyWidth())) / inputSpec.getMatchUnitSize())
                .reduce((a, b) -> a + b)
                .orElse(0.0)
                .intValue();
        L.info("# of match units = " + match_units);

        L.info("Match unit limit = " + inputSpec.getMatchUnitLimit());

        int max_match_key = match_vertices.stream()
                .map(IUnitDAGVertex::getKeyWidth)
                .max(Comparator.naturalOrder())
                .orElse(0);

        L.info("max size of match key in program = " + max_match_key);
        L.info("max size of match key in hw = " + inputSpec.getMatchUnitSize() * inputSpec.getMatchUnitLimit());

        if (max_match_key > inputSpec.getMatchUnitSize() * inputSpec.getMatchUnitLimit()) {
            L.info("max match key in program is larger than can be supported by hardware");
            exit(1);
        }

        int action_fields = action_vertices.stream()
                .mapToInt(IUnitDAGVertex::getNumFields)
                .reduce((a, b) -> a + b)
                .orElse(0);

        L.info("# of action fields = " + action_fields);
        L.info("action fields limit = " + inputSpec.getActionFieldsLimit());

        Integer max_action_fields = action_vertices.stream()
                .map(IUnitDAGVertex::getNumFields)
                .max(Comparator.naturalOrder())
                .orElse(0);

        L.info("max number of action fields in program = " + max_action_fields);
        L.info("max number of action fields in hw = " + inputSpec.getActionFieldsLimit());

        if (max_action_fields > inputSpec.getActionFieldsLimit()) {
            L.info("max number of action fields in program is larger than can be supported by hardware");
            exit(1);
        }

        L.info("match proc limit = " + inputSpec.getMatchProcLimit());
        L.info("action proc limit = " + inputSpec.getActionProcLimit());

        double throughput_upper_bound = Math.min((1.0 * inputSpec.getActionFieldsLimit()) / action_fields, (1.0 * inputSpec.getMatchUnitLimit()) / match_units);

        L.info("Upper bound on throughput = " + throughput_upper_bound);

        return throughput_upper_bound;

    }
}
