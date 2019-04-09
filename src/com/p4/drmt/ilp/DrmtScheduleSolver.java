package com.p4.drmt.ilp;

import com.p4.drmt.alu.ALUType;
import com.p4.drmt.cfg.DRMTConstants;
import com.p4.ds.ListMap;
import com.p4.ids.IDAGEdge;
import com.p4.ids.IDAGGraph;
import com.p4.utils.Utils.Pair;
import ilog.concert.IloAnd;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class DrmtScheduleSolver implements IDrmtScheduleSolverSolution {

    private static final Logger L = LoggerFactory.getLogger(DrmtScheduleSolver.class);

    private IDAGGraph<IUnitDAGVertex, IDAGEdge> dagGraph;
    private InputSpec inputSpec;
    private final int period_duration;
    private final int Q_MAX;

    private Map<IUnitDAGVertex, Integer> vertexToCycleMapSolution;

    @Override
    public ListMap<Integer, IUnitDAGVertex> getModuloSchedule() {
        return moduloSchedule;
    }

    @Override
    public ListMap<Integer, IUnitDAGVertex> getSchedule() {
        return schedule;
    }

    @Override
    public ListMap<Integer, IUnitDAGVertex> getDisjointMatches() {
        return disjointMatches;
    }

    @Override
    public Map<Integer, Integer> getModuloMatchUnitsConsumed() {
        return moduloMatchUnitsConsumed;
    }

    @Override
    public Map<Integer, Integer> getModuloActionFieldsConsumed() {
        return moduloActionFieldsConsumed;
    }

    @Override
    public Map<IUnitDAGVertex, ALUType> getActionVertexToAluUseMap() {
        return actionVertexToAluUseMap;
    }

    private ListMap<Integer, IUnitDAGVertex> moduloSchedule;
    private ListMap<Integer, String> moduloSchedulePktsAgo;
    private ListMap<Integer, IUnitDAGVertex> schedule;
    private ListMap<Integer, IUnitDAGVertex> disjointMatches;
    private Map<Integer, Integer> moduloMatchUnitsConsumed;
    private Map<Integer, Integer> moduloActionFieldsConsumed;
    private ListMap<Integer, String> moduloActionFieldsConsumedByALUType;
    private Map<IUnitDAGVertex, ALUType> actionVertexToAluUseMap;
    private Map<Integer, Integer> scratchDataUnitsConsumedMap;


    public DrmtScheduleSolver(IDAGGraph<IUnitDAGVertex, IDAGEdge> dagGraph, InputSpec inputSpec, int period_duration, int q_max) {
        this.dagGraph = dagGraph;
        this.inputSpec = inputSpec;
        this.period_duration = period_duration;

        vertexToCycleMapSolution = new LinkedHashMap<>();
        moduloSchedule = new ListMap<>(LinkedHashMap.class, ArrayList.class);
        moduloSchedulePktsAgo = new ListMap<>(LinkedHashMap.class, ArrayList.class);
        schedule = new ListMap<>(LinkedHashMap.class, ArrayList.class);
        disjointMatches = new ListMap<>(LinkedHashMap.class, ArrayList.class);
        moduloMatchUnitsConsumed = new LinkedHashMap<>();
        moduloActionFieldsConsumed = new LinkedHashMap<>();
        moduloActionFieldsConsumedByALUType = new ListMap<>(LinkedHashMap.class, ArrayList.class);
        actionVertexToAluUseMap = new LinkedHashMap<>();
        scratchDataUnitsConsumedMap = new LinkedHashMap<>();
        Q_MAX = q_max;

    }

    public boolean solve() {
        int numOfVertices = dagGraph.getVertexCount();

//        L.info("Running randon sieve");
//        int timeLimit = 5;
//        RandomizedSieve randomizedSieve = new RandomizedSieve(dagGraph, inputSpec, period_duration, timeLimit);
//        Map<IUnitDAGVertex, Integer> initialGreedySchedule = randomizedSieve.randomSieve();

        Map<IUnitDAGVertex, Integer> initialGreedySchedule = null;

        int R_MAX = period_duration; // number of equivalence classes

        L.info("Q_MAX = " + Q_MAX);
        L.info("R_MAX = " + R_MAX);

        int MATCH_UNIT_SIZE = inputSpec.getMatchUnitSize();
        int MATCH_UNIT_LIMIT = inputSpec.getMatchUnitLimit();
        int DATA_UNIT_SIZE = inputSpec.getActionDataSize();
        int DATA_UNIT_LIMIT = inputSpec.getActionDataLimit();
        int ACTION_FIELDS_LIMIT = inputSpec.getActionFieldsLimit();
        int MATCH_PROC_LIMIT = inputSpec.getMatchProcLimit();
        int ACTION_PROC_LIMIT = inputSpec.getActionProcLimit();
        int NUMBER_OF_MATCHES_ACTION_CAN_BE_POSTPONED = 1; //This is assuming scratch space 2*DataBus and ping pong mode
        int DATA_UNIT_LIMIT_IN_SCRATCH = 2 * DATA_UNIT_LIMIT;
        boolean SCRATCH_PING_PONG_MODE = false;
        boolean SCRATCH_ROUND_ROBIN_MODE = true;

        L.info("Running DRMT ILP Solver");

        Set<IUnitDAGVertex> verticesSet = dagGraph.getVertices();
        List<IUnitDAGVertex> vertices = new ArrayList<>(verticesSet); //This is to ensure order
        Map<Integer, String> idToVertexNameMap = new HashMap<>();
        Map<Integer, IUnitDAGVertex> idToVertexMap = new HashMap<>();

        for (IUnitDAGVertex vertex : vertices) {
            idToVertexNameMap.put(vertex.getId(), vertex.getName());
            idToVertexMap.put(vertex.getId(), vertex);
        }

        List<IUnitDAGVertex> match_vertices = vertices.stream()
                .filter(vertex -> "match".equals(vertex.getType()))
                .collect(Collectors.toList());

        List<IUnitDAGVertex> action_vertices = vertices.stream()
                .filter(vertex -> "action".equals(vertex.getType()))
                .collect(Collectors.toList());

        List<IUnitDAGVertex> action_p4_vertices = vertices.stream()
                .filter(IUnitDAGVertex::isP4Action)
                .collect(Collectors.toList());

        List<IUnitDAGVertex> action_alu_vertices = vertices.stream()
                .filter(IUnitDAGVertex::isAluAction)
                .collect(Collectors.toList());




        int MAX_ACTION_DELAY = action_vertices.stream()
                .mapToInt(IUnitDAGVertex::getTimeNeededToComplete)
                .max()
                .orElse(0);

        try {

            IloCplex cplex = new IloCplex();
            if (!L.isDebugEnabled()) {
                cplex.setOut(null);
            }
            cplex.setParam(Param.TimeLimit, 100 * 6);

            // Create variables
            // t is the start time for each DAG node in the first scheduling period
            String[] tNames = new String[numOfVertices];
            for (int i = 0; i < numOfVertices; i++) {
                tNames[i] = "t[" + idToVertexNameMap.get(i) + "]";
            }
            IloIntVar[] t = cplex.intVarArray(numOfVertices, 0, Integer.MAX_VALUE, tNames);

            // The quotients and remainders when dividing by R_MAX (see below)
            // qr[v][q][r] is 1 when t[v]
            // leaves a quotient of q and a remainder of r, when divided by R_MAX.
            IloIntVar[][][] qr = new IloIntVar[numOfVertices][][];
            for (IUnitDAGVertex vertex : dagGraph.getVertices()) {
                qr[vertex.getId()] = new IloIntVar[Q_MAX][];

                for (int q = 0; q < Q_MAX; q++) {
                    String[] qrNames = new String[R_MAX];
                    for (int r = 0; r < R_MAX; r++) {
                        qrNames[r] = "qr[" + vertex.getName() + "][" + q + "][" + r + "]";
                    }
                    qr[vertex.getId()][q] = cplex.boolVarArray(R_MAX, qrNames);
                }
            }

            //Is there any match/action from packet q in time slot r?
            //This is required to enforce limits on the number of packets that
            //can be performing matches or actions concurrently on any processor.
            IloIntVar[][] any_match = new IloIntVar[Q_MAX][];
            IloIntVar[][] any_action = new IloIntVar[Q_MAX][];
            IloIntVar[][] any_p4_action = new IloIntVar[Q_MAX][];
            IloIntVar[][] any_alu_action = new IloIntVar[Q_MAX][];
            for (int q = 0; q < Q_MAX; q++) {
                String[] any_match_names = new String[R_MAX];
                String[] any_action_names = new String[R_MAX];
                String[] any_p4_action_names = new String[R_MAX];
                String[] any_alu_action_names = new String[R_MAX];
                for (int r = 0; r < R_MAX; r++) {
                    any_match_names[r] = "any_match[" + q + "][" + r + "]";
                    any_action_names[r] = "any_action[" + q + "][" + r + "]";
                    any_p4_action_names[r] = "any_p4_action[" + q + "][" + r + "]";
                    any_alu_action_names[r] = "any_alu_action[" + q + "][" + r + "]";
                }
                any_match[q] = cplex.boolVarArray(R_MAX, any_match_names);
                any_action[q] = cplex.boolVarArray(R_MAX, any_action_names);
                any_p4_action[q] = cplex.boolVarArray(R_MAX, any_p4_action_names);
                any_alu_action[q] = cplex.boolVarArray(R_MAX, any_alu_action_names);
            }

            //The length of the schedule
            IloIntVar length = cplex.intVar(0, Integer.MAX_VALUE, "length");

            //Set objective:minimize length of schedule
            if (!inputSpec.isSolveForPacketRate()) {
                cplex.addMinimize(length);
            }


            //Set constraints

            //Constraint on maximum length a schedule can be (used to solve for a given packet rate)
            if (inputSpec.isSolveForPacketRate()) {
                int maxCyclesPerPacket = inputSpec.getNumOfProcessors() * inputSpec.getNumOfContexts() * inputSpec.getPacketRate();
                int maxTimeNeededToCompleteByAnyAction = action_vertices.stream()
                        .map(IUnitDAGVertex::getTimeNeededToComplete)
                        .max(Comparator.naturalOrder())
                        .orElse(DRMTConstants.actionDelay);
                int maxLengthAtWhichAnyNodeCanBeScheduled = maxCyclesPerPacket - maxTimeNeededToCompleteByAnyAction;
                cplex.addLe(length, maxLengthAtWhichAnyNodeCanBeScheduled, "constraint_cap_on_schedule_length");
            }

            //The length is the maximum of all t's
            for (IUnitDAGVertex vertex : vertices) {
                cplex.addLe(t[vertex.getId()], length, "constr_length_is_max_" + vertex.getId());
            }

            //Given v, qr[v, q, r] is 1 for exactly one q, r, i.e., there's a unique quotient and remainder
            L.info("Adding constraint: unique quotient(q) and reminder(r)");
            for (IUnitDAGVertex vertex : vertices) {
                IloLinearIntExpr constr_unique_quotient_remainder = cplex.linearIntExpr();
                for (int q = 0; q < Q_MAX; q++) {
                    for (int r = 0; r < R_MAX; r++) {
                        constr_unique_quotient_remainder.addTerm(qr[vertex.getId()][q][r], 1);
                    }
                }
                cplex.addEq(constr_unique_quotient_remainder, 1, "constr_unique_quotient_remainder_" + vertex.getId());
            }

            //This is just a way to write dividend = quotient * divisor + remainder
            L.info("Adding constraint: t[v] can be represted as q*P + r");
            for (IUnitDAGVertex vertex : vertices) {
                IloLinearIntExpr quotientXdivisor = cplex.linearIntExpr();
                IloLinearIntExpr reminder = cplex.linearIntExpr();
                for (int q = 0; q < Q_MAX; q++) {
                    for (int r = 0; r < R_MAX; r++) {
                        quotientXdivisor.addTerm(qr[vertex.getId()][q][r], q * R_MAX);
                        reminder.addTerm(qr[vertex.getId()][q][r], r);
                    }
                }
                cplex.addEq(t[vertex.getId()], cplex.sum(quotientXdivisor, reminder), "constr_division_" + vertex.getId());
            }

            //Respect dependencies in DAG
            L.info("Adding constraint: delay constraints");
            for (IDAGEdge edge : dagGraph.getEdges()) {
                IUnitDAGVertex u = (IUnitDAGVertex) edge.getSource();
                IUnitDAGVertex v = (IUnitDAGVertex) edge.getDestination();

                cplex.addGe(cplex.diff(t[v.getId()], t[u.getId()]), edge.getDelay(), "constr_dag_dependencies_" + v.getId() + "_" + u.getId());
            }


            //Number of match units does not exceed match_unit_limit
            //for every time step(j) < R_MAX, check the total match unit requirements
            //across all nodes(v) that can be "rotated" into this time slot.
            L.info("Adding constraint: number of matches does not exceed match unit limit");
            List<IloLinearIntExpr> match_units_consumed = new ArrayList<>();
            for (int r = 0; r < R_MAX; r++) {

                IloLinearIntExpr match_units = cplex.linearIntExpr();
                for (IUnitDAGVertex match : match_vertices) {
                    for (int q = 0; q < Q_MAX; q++) {
                        match_units.addTerm(qr[match.getId()][q][r], (int) Math.ceil((1.0 * match.getKeyWidth()) / MATCH_UNIT_SIZE));
                    }
                }
                match_units_consumed.add(match_units);
                cplex.addLe(match_units, MATCH_UNIT_LIMIT, "constr_match_units_" + r);
            }


            //Consider availability of Data Segments in a time slot r. Number of matches that we can trigger in a cycle is
            // limited by the data they generate and availability of data segments for the same.
            //Consider availability of Data Segments in a time slot r
            L.info("Adding constraint: number of matches does not exceed data unit limit");
            List<IloLinearIntExpr> data_units_consumed = new ArrayList<>();
            for (int r = 0; r < R_MAX; r++) {

                IloLinearIntExpr data_units = cplex.linearIntExpr();
                for (IUnitDAGVertex match : match_vertices) {
                    for (int q = 0; q < Q_MAX; q++) {
                        data_units.addTerm(qr[match.getId()][q][r], (int) Math.ceil((1.0 * match.getDataWidth()) / DATA_UNIT_SIZE));
                    }
                }
                data_units_consumed.add(data_units);
                cplex.addLe(data_units, DATA_UNIT_LIMIT, "constr_data_units_" + r);
            }

            //The action field resource constraint (similar comments to above)
            List<IloLinearIntExpr> action_fields_consumed = new ArrayList<>();
            ListMap<Integer, IloConstraint> aluLimitEquations = null;
            List<List<ALUType>> allCombinations = new ArrayList<>();
            List<Map<ALUType, IloLinearIntExpr>> moduloAluFieldsUsageConstraints = new ArrayList<>();
            if (inputSpec.isSolveForDifferentAluTypes()) {
                if (inputSpec.isHandleAluTypesAtInstructionLevel()) {
                    //For different possibilities of choosing alus for an action
                    //This section of code can be enabled/used only if have data flow graph at instruction level instead of action level
                    allCombinations = getAllCombinationsOfAlus(action_vertices);
                    aluLimitEquations = prepareAluLimitEquations(action_vertices, allCombinations, qr, cplex, Q_MAX, R_MAX);
                    for (int r = 0; r < R_MAX; r++) {
                        IloConstraint[] constraints = aluLimitEquations.get(r).toArray(new IloConstraint[0]);
                        cplex.add(cplex.or(constraints));
                    }
                } else {
                    //by default handle ALUs at action block level.
                    L.info("Adding constraint: availability of different types of ALUs at action block level");
                    for (int r = 0; r < R_MAX; r++) {
                        Map<ALUType, IloLinearIntExpr> constraintOnAluType = new HashMap<>();
                        moduloAluFieldsUsageConstraints.add(constraintOnAluType);
                        List<IloConstraint> constraints = new ArrayList<>();
                        for (ALUType aluType : ALUType.values()) {
                            constraintOnAluType.put(aluType, cplex.linearIntExpr());
                        }

                        for (IUnitDAGVertex action : action_vertices) {
                            for (int q = 0; q < Q_MAX; q++) {
                                for (ALUType aluType : action.getAluTypeFieldsLimitMap().keySet()) {
                                    IloLinearIntExpr constraint = constraintOnAluType.get(aluType);
                                    constraint.addTerm(qr[action.getId()][q][r], action.getAluTypeFieldsLimit(aluType));
                                }
                            }
                        }

                        for (ALUType aluType : ALUType.values()) {
                            constraints.add(cplex.le(constraintOnAluType.get(aluType), inputSpec.getAluTypeFieldsLimit(aluType), "constr_alu_fields_" + aluType.summary() + "_" + r));
                        }

                        cplex.add(constraints.toArray(new IloConstraint[0]));
                    }
                }
            } else {
                for (int r = 0; r < R_MAX; r++) {
                    IloLinearIntExpr action_fields = cplex.linearIntExpr();

                    for (IUnitDAGVertex action : action_vertices) {
                        for (int q = 0; q < Q_MAX; q++) {
                            action_fields.addTerm(qr[action.getId()][q][r], action.getNumFields());
                        }
                    }
                    action_fields_consumed.add(action_fields);
                    cplex.addLe(action_fields, ACTION_FIELDS_LIMIT, "constr_action_fields_" + r);
                }
            }

            L.info("Adding constraint: IPC constraint");
            // Any time slot (r) can have match or action operations
            // from only match_proc_limit/action_proc_limit packets
            // We do this in two steps.

            // First, detect if there is any(at least one) match / action operation from packet q in time slot r
            // if qr[v, q, r] =1 for any match node, then any_match[q, r]must = 1 (same for actions)
            // Notice that any_match[q, r]may be 1 even if all qr[ v, q, r]are zero in the commented code approach
            for (int q = 0; q < Q_MAX; q++) {
                for (int r = 0; r < R_MAX; r++) {
                    IloLinearIntExpr any_match1 = cplex.linearIntExpr();
                    IloLinearIntExpr any_action1 = cplex.linearIntExpr();
                    IloLinearIntExpr any_p4_action1 = cplex.linearIntExpr();
                    IloLinearIntExpr any_alu_action1 = cplex.linearIntExpr();
                    for (IUnitDAGVertex match : match_vertices) {
                        any_match1.addTerm(qr[match.getId()][q][r], 1);
                    }
                    for (IUnitDAGVertex action : action_vertices) {
                        any_action1.addTerm(qr[action.getId()][q][r], 1);
                        if (action.isAluAction()) {
                            any_alu_action1.addTerm(qr[action.getId()][q][r], 1);
                        } else if (action.isP4Action()) {
                            any_p4_action1.addTerm(qr[action.getId()][q][r], 1);
                        }
                    }

                    IloLinearIntExpr any_match_len = cplex.linearIntExpr();
                    any_match_len.addTerm(any_match[q][r], match_vertices.size());
                    cplex.addLe(any_match1, any_match_len, "constr_any_match1_" + q + "_" + r);

                    IloLinearIntExpr any_action_len = cplex.linearIntExpr();
                    any_action_len.addTerm(any_action[q][r], action_vertices.size());
                    cplex.addLe(any_action1, any_action_len, "constr_any_action1_" + q + "_" + r);

                    IloLinearIntExpr any_p4_action_len = cplex.linearIntExpr();
                    any_p4_action_len.addTerm(any_p4_action[q][r], action_p4_vertices.size());
                    cplex.addLe(any_p4_action1, any_p4_action_len, "constr_any_p4_action1_" + q + "_" + r);

                    IloLinearIntExpr any_alu_action_len = cplex.linearIntExpr();
                    any_alu_action_len.addTerm(any_alu_action[q][r], action_alu_vertices.size());
                    cplex.addLe(any_alu_action1, any_alu_action_len, "constr_any_alu_action1_" + q + "_" + r);
                }
            }

            // Second, check that,for any r, the summation over q of any_match[q, r]is under proc_limits
            for (int r = 0; r < R_MAX; r++) {
                IloLinearIntExpr any_match_sum = cplex.linearIntExpr();
                IloLinearIntExpr any_action_sum = cplex.linearIntExpr();
                for (int q = 0; q < Q_MAX; q++) {
                    any_match_sum.addTerm(any_match[q][r], 1);
                    any_action_sum.addTerm(any_action[q][r], 1);
                }
                cplex.addLe(any_match_sum, MATCH_PROC_LIMIT, "constr_match_proc_" + r);
                cplex.addLe(any_action_sum, ACTION_PROC_LIMIT, "constr_action_proc_" + r);
            }

            //At any time slot (q,r) either match or action can be schedule, but not both.
            L.info("Adding constraint: In a cycle either match or action can be scheduled per processor per context");
            for (int q = 0; q < Q_MAX; q++) {
                for (int r = 0; r < R_MAX; r++) {
                    IloLinearIntExpr matchOrAction = cplex.linearIntExpr();
                    matchOrAction.addTerm(any_match[q][r], 1);
                    matchOrAction.addTerm(any_action[q][r], 1);
                    cplex.addLe(matchOrAction, 1, "constr_matchOrActionOnly_" + q + "_" + r);
                }
            }

            //At any time slot (q,r) either alu_action or p4_action can be schedule, but not both.
            L.info("Adding constraint: In a cycle either alu_action or p4_action can be scheduled per processor per context");
            for (int q = 0; q < Q_MAX; q++) {
                for (int r = 0; r < R_MAX; r++) {
                    IloLinearIntExpr aluOrP4Action = cplex.linearIntExpr();
                    aluOrP4Action.addTerm(any_p4_action[q][r], 1);
                    aluOrP4Action.addTerm(any_alu_action[q][r], 1);
                    cplex.addLe(aluOrP4Action, 1, "constr_aluOrP4ActionOnly_" + q + "_" + r);
                }
            }

            //Introduce forced action delay if actions are executing in different cycles and there is no data dependency between those actions
            //Update from Hardware Team: Below constraint is no more needed.
//            L.info("Adding constraint: forced delay between two actions if not scheduled in same cycle");
//            for (int i = 0; i < action_vertices.size(); i++) {
//                for (int j = 0; j < action_vertices.size(); j++) {
//                    IUnitDAGVertex srcVertex = action_vertices.get(i);
//                    IUnitDAGVertex dstVertex = action_vertices.get(j);
//
//                    //Below condition checks for existence of data dependency
//                    if (!dagGraph.isReachable(srcVertex, dstVertex) && !dagGraph.isReachable(dstVertex, srcVertex)) {
//                        IloLinearIntExpr timeDiffBtnActions = cplex.linearIntExpr();
//
//                        timeDiffBtnActions.addTerm(t[dstVertex.getId()], 1);
//                        timeDiffBtnActions.addTerm(t[srcVertex.getId()], -1);
//
//                        //t(v) - t(u) <= 0 or t(v)-t(u) >= u.timeNeededToComplete
//                        cplex.add(cplex.or(cplex.le(timeDiffBtnActions, 0), cplex.ge(timeDiffBtnActions, srcVertex.getTimeNeededToComplete())));
//
//                    }
//                }
//            }

            List<IloLinearIntExpr> holdScratchDataUnitConsumedExpr = new ArrayList<>();
            if (SCRATCH_ROUND_ROBIN_MODE) {
                L.info("Adding constraint: scratch space in round robin mode");
                holdScratchDataUnitConsumedExpr = addScratchDataUnitRoundRobinModeConstraints(R_MAX, Q_MAX, DATA_UNIT_SIZE, DATA_UNIT_LIMIT_IN_SCRATCH,
                        vertices, cplex, qr, MAX_ACTION_DELAY, match_vertices);
            } else if (SCRATCH_PING_PONG_MODE) {
                L.info("Adding constraint: scratch space in ping pong mode");
                addScratchDataUnitPingPongModeConstraints(R_MAX, Q_MAX, NUMBER_OF_MATCHES_ACTION_CAN_BE_POSTPONED, cplex, qr, any_match);
            }


            // Number of actionFields consumed in a cycle
//            String[] actionFieldsConsumedNames = new String[Q_MAX * R_MAX];
//            for (int i = 0; i < (Q_MAX * R_MAX); i++) {
//                actionFieldsConsumedNames[i] = "actionFieldConsumed[" + i + "]";
//            }
//            IloIntVar[] actionFieldsConsumed = cplex.intVarArray(Q_MAX * R_MAX, 0, inputSpec.getActionFieldsLimit(), actionFieldsConsumedNames);
//            //Hold the action fields consumed by an action till the time it will be completed for a particular packet context
//            for (int q = 0; q < Q_MAX; q++) {
//                for (int r = 0; r < R_MAX; r++) {
//                    IloLinearIntExpr holdActionNumFields = cplex.linearIntExpr();
//                    for (IUnitDAGVertex action_vertex : action_vertices) {
//                        int lookUpTo = q * R_MAX + r;
//                        int lookFrom = lookUpTo - action_vertex.getTimeNeededToComplete() + 1;
//                        for (int cellNum = lookFrom; cellNum <= lookUpTo; cellNum++) {
//                            if (cellNum >= 0) {
//                                int cellNumQ = cellNum / R_MAX;
//                                int cellNumR = cellNum % R_MAX;
//                                holdActionNumFields.addTerm(qr[action_vertex.getId()][cellNumQ][cellNumR], action_vertex.getNumFields());
//                            }
//                        }
//                    }
//                    cplex.addEq(actionFieldsConsumed[q * R_MAX + r], holdActionNumFields, "constr_num_of_action_fields_consumed_in_timeslot_" + q + "_" + r);
//                    cplex.addLe(actionFieldsConsumed[q * R_MAX + r], ACTION_FIELDS_LIMIT, "constr_num_of_action_fields_in_timeslot_" + q + "_" + r);
//                }
//            }

//            String[] actionFieldsConsumedNewNames = new String[Q_MAX * R_MAX];
//            for (int i = 0; i < R_MAX; i++) {
//                actionFieldsConsumedNewNames[i] = "actionFieldConsumedNew[" + i + "]";
//            }
//            IloIntVar[] actionFieldsConsumedNew = cplex.intVarArray(R_MAX, 0, inputSpec.getActionFieldsLimit(), actionFieldsConsumedNewNames);

            //Hold the action fields consumed by an action till the time it will be completed
//            List<IloLinearIntExpr> holdActionNumFieldsList = new ArrayList<>();
//            for (int r = 0; r < R_MAX; r++) {
//                holdActionNumFieldsList.add(cplex.linearIntExpr());
//            }
//            for (int r = 0; r < R_MAX; r++) {
//
//                for (IUnitDAGVertex action_vertex : action_vertices) {
//                    for (int q = 0; q < Q_MAX; q++) {
//                        int blockNumCycles = action_vertex.getTimeNeededToComplete();
//                        for (int i = 0; i < blockNumCycles; i++) {
//                            int effectiveR = (r + i) % R_MAX;
//                            holdActionNumFieldsList.get(effectiveR).addTerm(qr[action_vertex.getId()][q][r], action_vertex.getNumFields());
//                        }
//                    }
//
//                }
//            }
//            for (int r = 0; r < R_MAX; r++) {
//                cplex.addEq(actionFieldsConsumedNew[r], holdActionNumFieldsList.get(r), "constr_num_of_action_fields_" + r);
//                cplex.addLe(actionFieldsConsumedNew[r], ACTION_FIELDS_LIMIT, "constr_num_of_action_fields_in_timeslot_" + r);
//            }

//            if (initialGreedySchedule != null && !initialGreedySchedule.isEmpty()) {
//                Map<String, Integer> tNamedRandomSchedule = new HashMap<>();
//                initialGreedySchedule.forEach((key, value) -> tNamedRandomSchedule.put("t[" + key.getName() + "]", value));
//
//                double[] tStartValue = new double[numOfVertices];
//                for (int i = 0; i < numOfVertices; i++) {
//                    tStartValue[i] = tNamedRandomSchedule.get(t[i].getName());
//                }
//                cplex.addMIPStart(t, tStartValue);
//            }


            L.debug(cplex.getModel().toString());

            L.info("CPLEX solving...");
            long startTime = System.currentTimeMillis();
            long endTime = 0l;
            if (cplex.solve()) {
                endTime = System.currentTimeMillis();

                printTimeForWhichCplexRan(startTime, endTime);

                populateSchedule(R_MAX, idToVertexMap, cplex, t);

                populateMatchUnitsConsumed(R_MAX, cplex, match_units_consumed, action_fields_consumed);

                populateActionFieldsUsage(R_MAX, action_vertices, cplex, aluLimitEquations, allCombinations, moduloAluFieldsUsageConstraints);

                populateDisjointMatches(R_MAX);

                populateScratchDataUnitsConsumed(holdScratchDataUnitConsumedExpr, cplex);

                return true;

            } else {
                endTime = System.currentTimeMillis();
                L.info("unable to solve");
            }

            printTimeForWhichCplexRan(startTime, endTime);


        } catch (IloException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void printTimeForWhichCplexRan(long startTime, long endTime) {
        long timeTakenToSolveInMillis = endTime - startTime;
        String formattedTime = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(timeTakenToSolveInMillis),
                TimeUnit.MILLISECONDS.toMinutes(timeTakenToSolveInMillis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(timeTakenToSolveInMillis) % TimeUnit.MINUTES.toSeconds(1));
        L.info("Time taken by cplex to solve (hh:mm:ss) = " + formattedTime);
    }

    private void addScratchDataUnitPingPongModeConstraints(int R_MAX, int Q_MAX, int NUMBER_OF_MATCHES_ACTION_CAN_BE_POSTPONED, IloCplex cplex, IloIntVar[][][] qr, IloIntVar[][] any_match) throws IloException {
        //If we are using scratch space in ping-pong mode ie., every alternative Match(hardware) uses same scratch space.
        int T = Q_MAX * R_MAX;
        for (IDAGEdge edge : dagGraph.getEdges()) {
            IUnitDAGVertex src = (IUnitDAGVertex) edge.getSource();
            IUnitDAGVertex dst = (IUnitDAGVertex) edge.getDestination();

            if (src.isMatch() && !dst.isMatch())
                for (int i = 0; i < T; i++) {
                    int qm = i / R_MAX;
                    int rm = i % R_MAX;

                    for (int j = i + edge.getDelay(); j < T; j++) {
                        int qa = j / R_MAX;
                        int ra = j % R_MAX;

                        IloLinearIntExpr ifExpr = cplex.linearIntExpr();
                        ifExpr.addTerm(qr[src.getId()][qm][rm], 1);
                        ifExpr.addTerm(qr[dst.getId()][qa][ra], 1);

                        IloRange ifMatchActionScheduledConstraint = cplex.eq(ifExpr, 2);

                        IloLinearIntExpr thenExpr = cplex.linearIntExpr();
                        for (int k = i + 1; k < j; k++) {
                            int q = k / R_MAX;
                            int r = k % R_MAX;

                            thenExpr.addTerm(any_match[q][r], 1);
                        }

                        IloRange thenNumberOfMatchesConstraint = cplex.le(thenExpr, NUMBER_OF_MATCHES_ACTION_CAN_BE_POSTPONED);

                        IloConstraint ifThenConstraint = cplex.ifThen(ifMatchActionScheduledConstraint, thenNumberOfMatchesConstraint);
                        cplex.add(ifThenConstraint);

                    }
                }
        }
    }

    private List<IloLinearIntExpr> addScratchDataUnitRoundRobinModeConstraints(int R_MAX, int Q_MAX, int DATA_UNIT_SIZE,
                                                                               int DATA_UNIT_LIMIT_IN_SCRATCH, List<IUnitDAGVertex> vertices,
                                                                               IloCplex cplex, IloIntVar[][][] qr,
                                                                               int MAX_ACTION_DELAY,
                                                                               List<IUnitDAGVertex> match_vertices) throws IloException {

        int T = Q_MAX * R_MAX;
        //If we are using scratch space in round robin mode, then block data segemnts needed for match till its corresponding action is completed
        //Number of data units consumed (inside scratch space) in a cycle per processor per context

        //qrm[vm][qm][rm][qa][ra] denotes variable such that vm node is schedule at (qm,rm) and va node is scheduled at (qa, ra) where (vm, va) is a Match Action pair
        IloIntVar[][][][][] qrm = new IloIntVar[vertices.size()][Q_MAX][R_MAX][Q_MAX][R_MAX];
        for (IUnitDAGVertex vertex : dagGraph.getVertices()) {
            for (int j = 0; j < Q_MAX; j++) {
                for (int k = 0; k < R_MAX; k++) {
                    for (int l = 0; l < Q_MAX; l++) {
                        for (int m = 0; m < R_MAX; m++) {
                            qrm[vertex.getId()][j][k][l][m] = cplex.boolVar("qrm[" + vertex.getName() + "]" + "[" + j + "]" + "[" + k + "]" + "[" + l + "]" + "[" + m + "]");
                        }
                    }
                }
            }
        }

        //constraint qr[vm][qm][rm] == 1 && qr[va][qa][ra] == 1 ? qrm[vm][qm][rm][qa][ra] = 1 : qrm[vm][qm][rm][qa][ra] = 0 ,
        //for all (vm, va) in ODG , (qm, rm) in (Q_MAX, R_MAX), (qa, ra) in (Q_MAX, R_MAX)
        for (IDAGEdge edge : dagGraph.getEdges()) {
            IUnitDAGVertex src = (IUnitDAGVertex) edge.getSource();
            IUnitDAGVertex dst = (IUnitDAGVertex) edge.getDestination();

            if (src.isMatch() && !dst.isMatch()) {
                for (int i = 0; i < T; i++) {
                    int qm = i / R_MAX;
                    int rm = i % R_MAX;

                    for (int j = i + edge.getDelay(); j < T; j++) {
                        int qa = j / R_MAX;
                        int ra = j % R_MAX;

                        IloLinearIntExpr ifMAExpr = cplex.linearIntExpr();
                        ifMAExpr.addTerm(qr[src.getId()][qm][rm], 1);
                        ifMAExpr.addTerm(qr[dst.getId()][qa][ra], 1);

                        IloLinearIntExpr thenQMAExpr = cplex.linearIntExpr();
                        thenQMAExpr.addTerm(qrm[src.getId()][qm][rm][qa][ra], 1);

                        IloRange ifMatchActionScheduledConstraint = cplex.eq(ifMAExpr, 2);
                        IloRange setQMAExprToOneConstraint = cplex.eq(thenQMAExpr, 1);

                        IloRange ifNotMatchActionScheduledConstraint = cplex.le(ifMAExpr, 1);
                        IloRange setQMAExprToZeroConstraint = cplex.eq(thenQMAExpr, 0);

                        cplex.add(cplex.ifThen(ifMatchActionScheduledConstraint, setQMAExprToOneConstraint));
                        cplex.add(cplex.ifThen(ifNotMatchActionScheduledConstraint, setQMAExprToZeroConstraint));

                    }
                }
            }
        }

        //Hold the data unit fields consumed by an match-action till the time it will be completed for a particular packet context
        List<IloLinearIntExpr> holdScratchDataUnitConsumedExpr = new ArrayList<>();
        for (int i = 0; i < T + MAX_ACTION_DELAY; i++) {
            holdScratchDataUnitConsumedExpr.add(cplex.linearIntExpr());
        }

        for (IDAGEdge edge : dagGraph.getEdges()) {
            IUnitDAGVertex src = (IUnitDAGVertex) edge.getSource();
            IUnitDAGVertex dst = (IUnitDAGVertex) edge.getDestination();

            if (src.isMatch() && !dst.isMatch()) {
                for (int i = 0; i < T; i++) {
                    int qm = i / R_MAX;
                    int rm = i % R_MAX;

                    for (int j = i + edge.getDelay(); j < T; j++) {
                        int qa = j / R_MAX;
                        int ra = j % R_MAX;

                        int start = i;
                        int end = j + dst.getTimeNeededToComplete();

                        for (int k = start; k < end; k++) {
                            IloLinearIntExpr expr = holdScratchDataUnitConsumedExpr.get(k);
                            expr.addTerm(qrm[src.getId()][qm][rm][qa][ra], (int) Math.ceil(1.0 * src.getDataWidth() / DATA_UNIT_SIZE));
                        }
                    }
                }
            }
        }
        for (IloLinearIntExpr holdScratchDataUnitExpr : holdScratchDataUnitConsumedExpr) {
            cplex.addLe(holdScratchDataUnitExpr, DATA_UNIT_LIMIT_IN_SCRATCH);
        }


        //if (qmr[vm][qm][rm][qa][ra] == 1) then sum(qmr[vm][qmk][rmk][qal][ral]) = 0 where (qm, rm) < (qmk,rmk) and (qal, ral) < (qa, ra)
        //There should not exist schedule of Match-Action pair in the time line of another Match-Action pair
        //This is to ensure, scratch space memory is consumed in the same order, it is filled up.
        for (IDAGEdge edge : dagGraph.getEdges()) {
            IUnitDAGVertex src = (IUnitDAGVertex) edge.getSource();
            IUnitDAGVertex dst = (IUnitDAGVertex) edge.getDestination();

            if (src.isMatch() && !dst.isMatch()) {
                for (int i = 0; i < T; i++) {
                    int qm = i / R_MAX;
                    int rm = i % R_MAX;

                    for (int j = i + edge.getDelay(); j < T; j++) {
                        int qa = j / R_MAX;
                        int ra = j % R_MAX;

                        IloLinearIntExpr ifMAExpr = cplex.linearIntExpr();
                        ifMAExpr.addTerm(qrm[src.getId()][qm][rm][qa][ra], 1);
                        IloRange ifMatchActionScheduledConstraint = cplex.eq(ifMAExpr, 2);

                        IloLinearIntExpr thenOtherMANotScheduleExpr = cplex.linearIntExpr();


                        for (IUnitDAGVertex matchVertex : match_vertices) {
                            if (!dagGraph.isReachable(src, matchVertex)) {
                                for (int k = i + 1; k < j; k++) {
                                    int qmk = k / R_MAX;
                                    int rmk = k % R_MAX;
                                    for (int l = k + edge.getDelay(); l < j; l++) {

                                        int qal = i / R_MAX;
                                        int ral = i % R_MAX;

                                        thenOtherMANotScheduleExpr.addTerm(qrm[matchVertex.getId()][qmk][rmk][qal][ral], 1);
                                    }
                                }
                            }
                        }

                        IloRange thenOtherMANotScheduledConstraint = cplex.eq(thenOtherMANotScheduleExpr, 0);
                        cplex.add(cplex.ifThen(ifMatchActionScheduledConstraint, thenOtherMANotScheduledConstraint));

                    }
                }
            }
        }

        return holdScratchDataUnitConsumedExpr;
    }

    private void populateScratchDataUnitsConsumed(List<IloLinearIntExpr> holdScratchDataUnitConsumedExpr, IloCplex cplex) throws IloException {
        for (int i = 0; i < holdScratchDataUnitConsumedExpr.size(); i++) {
            scratchDataUnitsConsumedMap.put(i, (int) Math.round(cplex.getValue(holdScratchDataUnitConsumedExpr.get(i))));
        }
    }

    private void populateMatchUnitsConsumed(int R_MAX, IloCplex cplex, List<IloLinearIntExpr> match_units_consumed, List<IloLinearIntExpr> action_fields_consumed) throws IloException {
        for (int i = 0; i < R_MAX; i++) {
            moduloMatchUnitsConsumed.put(i, (int) Math.round(cplex.getValue(match_units_consumed.get(i))));
            if (!inputSpec.isSolveForDifferentAluTypes()) {
                moduloActionFieldsConsumed.put(i, (int) Math.round(cplex.getValue(action_fields_consumed.get(i))));
            }
        }
    }

    private void populateSchedule(int R_MAX, Map<Integer, IUnitDAGVertex> idToVertexMap, IloCplex cplex, IloIntVar[] t) throws IloException {

        for (int i = 0; i < t.length; i++) {
            int cycleNum = (int) Math.round(cplex.getValue(t[i]));
            int cycleNumQ = cycleNum / R_MAX;
            int cycleNumR = cycleNum % R_MAX;
            moduloSchedule.add(cycleNumR, idToVertexMap.get(i));
            moduloSchedulePktsAgo.add(cycleNumR, "p[" + (cycleNumQ) + "]." + idToVertexMap.get(i).getName());
            schedule.add(cycleNum, idToVertexMap.get(i));
            vertexToCycleMapSolution.put(idToVertexMap.get(i), (int) Math.round(cplex.getValue(t[i])));
        }

        sortMatchesInAParticularCycleBasedOnActionOrder();
    }

    private void sortMatchesInAParticularCycleBasedOnActionOrder() {
        //In a cycle, order matches in the same order of actions being triggered
        //This ordering is needed and helpful during scratch space memory consumption.
        //Also, cross bar has to consider this order such that scratch memory is consumed(by alus/actions) in the same order it is filled up,
        //such that there is always contiguous memory available in scratch space
        for (Integer scheduleCycle : schedule.keySet()) {
            List<IUnitDAGVertex> vertices = schedule.get(scheduleCycle);
            if (!vertices.isEmpty()) {
                //in a cycle, it will either contain matches or actions.
                //So if first element is match, then all the vertices in that cycle should be matches.
                boolean areMatchNodes = vertices.get(0).isMatch();
                if (areMatchNodes) {
                    List<IUnitDAGVertex> orderedMatches = vertices.stream()
                            .sorted(getActionOrderComparator())
                            .collect(Collectors.toList());

                    schedule.put(scheduleCycle, orderedMatches);
                }
            }
        }

        //Sort schedule based on cycle
        schedule = schedule.entrySet()
                .stream()
                .sorted(Entry.comparingByKey())
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue,
                        (oldValue, newValue) -> newValue,
                        () -> new ListMap<>(LinkedHashMap.class, ArrayList.class)));
    }

    private Comparator<IUnitDAGVertex> getActionOrderComparator() {
        return (match1, match2) -> {
            //assumption is that adjacentOutGoingVertex of match is always its corresponding match.
            //TODO May be we can store list of Match-Action pairs (edge), or mark a particular edge as match - action pair
            List<IUnitDAGVertex> match1Out = dagGraph.adjacentOutGoingVertices(match1);
            List<IUnitDAGVertex> match2Out = dagGraph.adjacentOutGoingVertices(match2);

            if (match1Out.size() == 1 && match2Out.size() == 1) {
                IUnitDAGVertex actionOfMatch1 = match1Out.get(0);
                IUnitDAGVertex actionOfMatch2 = match2Out.get(0);

                if (!actionOfMatch1.isMatch() && !actionOfMatch2.isMatch()) {
                    return vertexToCycleMapSolution.get(actionOfMatch1) - vertexToCycleMapSolution.get(actionOfMatch2);
                }
            }
            return 0;
        };
    }

    private void populateActionFieldsUsage(int R_MAX, List<IUnitDAGVertex> action_vertices, IloCplex cplex, ListMap<Integer, IloConstraint> aluLimitEquations, List<List<ALUType>> allCombinations, List<Map<ALUType, IloLinearIntExpr>> moduloAluFieldsUsageConstraints) throws IloException {
        if (inputSpec.isSolveForDifferentAluTypes()) {

            if (inputSpec.isHandleAluTypesAtInstructionLevel() && aluLimitEquations != null) {

                List<Map<ALUType, Integer>> noOfAlusNeeded = new ArrayList<>();
                for (int i = 0; i < R_MAX; i++) {
                    noOfAlusNeeded.add(new HashMap<>());
                }

                Map<IUnitDAGVertex, Integer> actionVetexToId = new HashMap<>();
                for (int i = 0; i < action_vertices.size(); i++) {
                    actionVetexToId.put(action_vertices.get(i), i);
                }


                for (int r = 0; r < R_MAX; r++) {
                    List<IloConstraint> aluConstraints = aluLimitEquations.get(r);
                    Map<ALUType, Integer> noOfAlusNeededAtCycleNumR = noOfAlusNeeded.get(r);

                    List<IUnitDAGVertex> verticesScheduled = moduloSchedule.get(r);
                    List<IUnitDAGVertex> actionVerticesScheduled = verticesScheduled.stream()
                            .filter(vertex -> "action".equals(vertex.getType()))
                            .collect(Collectors.toList());
                    if (!actionVerticesScheduled.isEmpty()) {
                        for (int j = 0; j < aluConstraints.size(); j++) {

                            if ((int) Math.round(cplex.getValue((aluConstraints.get(j)))) == 1) {
                                List<ALUType> ALUTypes = allCombinations.get(j);

                                for (IUnitDAGVertex actionVertex : actionVerticesScheduled) {
                                    ALUType ALUType = ALUTypes.get(actionVetexToId.get(actionVertex));
                                    actionVertexToAluUseMap.put(actionVertex, ALUType);
                                    if (noOfAlusNeededAtCycleNumR.containsKey(ALUType)) {
                                        noOfAlusNeededAtCycleNumR.put(ALUType, noOfAlusNeededAtCycleNumR.get(ALUType) + actionVertex.getAluTypeFieldsLimit(ALUType));
                                    } else {
                                        noOfAlusNeededAtCycleNumR.put(ALUType, actionVertex.getAluTypeFieldsLimit(ALUType));
                                    }
                                }
                                break;
                            }
                        }
                    }
                }

                for (int r = 0; r < R_MAX; r++) {
                    Map<ALUType, Integer> ALUTypeIntegerMap = noOfAlusNeeded.get(r);
                    for (Entry<ALUType, Integer> type : ALUTypeIntegerMap.entrySet()) {
                        moduloActionFieldsConsumedByALUType.add(r, type.getKey() + ":" + type.getValue());
                    }
                }
            } else {
                for (int r = 0; r < R_MAX; r++) {
                    for (ALUType aluType : ALUType.values()) {
                        int value = (int) Math.round(cplex.getValue(moduloAluFieldsUsageConstraints.get(r).get(aluType)));
                        if (value > 0) {
                            moduloActionFieldsConsumedByALUType.add(r, aluType.summary() + ":" + value);
                        }
                    }
                }
            }

        }
    }

    private void populateDisjointMatches(int R_MAX) {
        int packetRate = inputSpec.isSolveForPacketRate() ? inputSpec.getPacketRate() : 1;
        for (int bucket = 0; bucket < packetRate; bucket++) {
            for (int i = bucket; i < R_MAX; i = i + packetRate) {
                List<IUnitDAGVertex> matchTypeVertices = moduloSchedule.getList(i)
                        .stream()
                        .filter(vertex -> "match".equals(vertex.getType()))
                        .collect(Collectors.toList());
                disjointMatches.addAll(bucket, matchTypeVertices);
            }
        }
    }


    private ListMap<Integer, IloConstraint> prepareAluLimitEquations(List<IUnitDAGVertex> actionVertices,
                                                                     List<List<ALUType>> allCombinations,
                                                                     IloIntVar[][][] qr,
                                                                     IloCplex cplex,
                                                                     int Q_MAX,
                                                                     int R_MAX) throws IloException {
        List<ListMap<ALUType, IUnitDAGVertex>> equationsList = new ArrayList<>();
        //for each combination prepare a equation
        for (List<ALUType> combination : allCombinations) {
            assert combination.size() == actionVertices.size();
            ListMap<ALUType, IUnitDAGVertex> equation = new ListMap<>(LinkedHashMap.class, ArrayList.class);
            for (int i = 0; i < combination.size(); i++) {
                equation.add(combination.get(i), actionVertices.get(i));
            }
            equationsList.add(equation);
        }

        return prepareEquations(qr, equationsList, cplex, Q_MAX, R_MAX);
    }

    private List<List<ALUType>> getAllCombinationsOfAlus(List<IUnitDAGVertex> actionVertices) {
        List<List<ALUType>> ALUTypeLists = new ArrayList<>();
        for (IUnitDAGVertex actionVertex : actionVertices) {
            Set<ALUType> ALUTypes = actionVertex.getAluTypeFieldsLimitMap().keySet();
            ALUTypeLists.add(new ArrayList<ALUType>(ALUTypes));
        }

        List<List<ALUType>> allCombinations = new ArrayList<>();

        doAllCombinations(ALUTypeLists, 0, allCombinations, new Stack<ALUType>());
        return allCombinations;
    }

    private ListMap<Integer, IloConstraint> prepareEquations(IloIntVar[][][] qr,
                                                             List<ListMap<ALUType, IUnitDAGVertex>> equationsList,
                                                             IloCplex cplex, int q_max, int r_max) throws IloException {


        ListMap<Integer, IloConstraint> aluConstraints = new ListMap<>(LinkedHashMap.class, ArrayList.class);
        for (int r = 0; r < r_max; r++) {
            for (ListMap<ALUType, IUnitDAGVertex> equation : equationsList) {
                IloConstraint[] andConstraints = new IloConstraint[equation.keySet().size()];
                int index = 0;
                for (ALUType ALUType : equation.keySet()) {
                    List<IUnitDAGVertex> actionVerticesToUseALUType = equation.get(ALUType);
                    IloLinearIntExpr expr = cplex.linearIntExpr();
                    for (IUnitDAGVertex actionVertex : actionVerticesToUseALUType) {
                        for (int q = 0; q < q_max; q++) {
                            expr.addTerm(qr[actionVertex.getId()][q][r], actionVertex.getAluTypeFieldsLimit(ALUType));
                        }
                    }
                    andConstraints[index++] = cplex.le(expr, inputSpec.getAluTypeFieldsLimit(ALUType), ALUType.summary() + "r_" + r + "_" + index);
                }
                IloAnd andExpr = cplex.and(andConstraints);
                aluConstraints.add(r, andExpr);
            }
        }

        return aluConstraints;

    }

    void doAllCombinations(List<List<ALUType>> ALUTypeList, int index, List<List<ALUType>> allCombinations, Stack<ALUType> stack) {
        if (index == ALUTypeList.size()) {
            allCombinations.add(new ArrayList<>(stack));
            return;
        }

        List<ALUType> ALUTypes = ALUTypeList.get(index);

        for (ALUType ALUType : ALUTypes) {
            stack.push(ALUType);
            doAllCombinations(ALUTypeList, index + 1, allCombinations, stack);
            stack.pop();
        }

    }


    public void printSolution() {

        printScheduleSolutionFormatted(schedule, inputSpec);
        printModuloScheduleSolutionFormatted(moduloSchedule, inputSpec);
        printModuloScheduleSolutionPktsAgoFormatted(moduloSchedulePktsAgo, inputSpec);

        if (inputSpec.isSolveForDifferentAluTypes()) {
            if (inputSpec.isHandleAluTypesAtInstructionLevel()) {
                printAluFieldsUsageByAction(moduloSchedule, actionVertexToAluUseMap);
            }
            printModuloActionFieldsConsumedByAlu(moduloActionFieldsConsumedByALUType);
        } else {
            printModuloActionFieldsConsumed(moduloActionFieldsConsumed, inputSpec);
        }

        printDisjointMatchesFormatted(disjointMatches, inputSpec);
        printModuloMatchUnitsConsumed(moduloMatchUnitsConsumed, inputSpec);

        printScratchDataUnitConsumed(scratchDataUnitsConsumedMap);

    }

    private void printScratchDataUnitConsumed(Map<Integer, Integer> scratchDataUnitsConsumedMap) {
        int numberOfEntriesPerLine = 8;
        List<Integer> values = new ArrayList<>(scratchDataUnitsConsumedMap.values());
        printFormattedTextSingleLine(values, numberOfEntriesPerLine, "Scratch Data Units consumed");
    }

    private void printAluFieldsUsageByAction(ListMap<Integer, IUnitDAGVertex> moduloSchedule, Map<IUnitDAGVertex, ALUType> actionVertexToAluUseMap) {
        ListMap<Integer, String> moduloAluUsagePerAction = new ListMap<>(LinkedHashMap.class, ArrayList.class);
        for (Integer integer : moduloSchedule.keySet()) {
            List<String> names = moduloSchedule.get(integer)
                    .stream()
                    .filter(vertex -> "action".equals(vertex.getType()))
                    .map(vertex -> vertex.getName() + ":" + actionVertexToAluUseMap.get(vertex).summary() + ":" + vertex.getAluTypeFieldsLimit(actionVertexToAluUseMap.get(vertex)))
                    .collect(Collectors.toList());
            moduloAluUsagePerAction.addAll(integer, names);
        }
        int numberOfBuckets = inputSpec.isSolveForPacketRate() ? inputSpec.getNumOfProcessors() * inputSpec.getPacketRate() : period_duration;
        int maxNumberOfLinesPerBucket = inputSpec.isSolveForPacketRate() ? inputSpec.getNumOfContexts() : 100; //instead of 100 it would be totalScheduleLength/R_MAX
        int numberOfBucketsPerLine = inputSpec.getNumOfProcessors();
        int widthOfTextInABucket = 35;
        String tableHeading = "Alu usages instruction wise instructionName:ALUTypeUsed:NumberOfAlusOfThatTypeUsed";
        printFormattedText(moduloAluUsagePerAction, numberOfBuckets, maxNumberOfLinesPerBucket, numberOfBucketsPerLine, widthOfTextInABucket, "t", tableHeading);

    }

    private void printModuloActionFieldsConsumedByAlu(ListMap<Integer, String> moduloActionFieldsConsumedByALUType) {
        int numberOfBuckets = inputSpec.isSolveForPacketRate() ? inputSpec.getNumOfProcessors() * inputSpec.getPacketRate() : period_duration;
        int maxNumberOfLinesPerBucket = inputSpec.getAluTypeFieldsLimitMap().keySet().size(); //instead of 100 it would be totalScheduleLength/R_MAX
        int numberOfBucketsPerLine = inputSpec.getNumOfProcessors();
        int widthOfTextInABucket = 25;
        String tableHeading = "Modulo Action Fields Consume by Alu Type";
        printFormattedText(moduloActionFieldsConsumedByALUType, numberOfBuckets, maxNumberOfLinesPerBucket, numberOfBucketsPerLine, widthOfTextInABucket, "t", tableHeading);

    }

    private void printScheduleSolutionFormatted(ListMap<Integer, IUnitDAGVertex> schedule, InputSpec inputSpec) {
        ListMap<Integer, String> scheduleNames = new ListMap<>(LinkedHashMap.class, ArrayList.class);
        for (Integer integer : schedule.keySet()) {
            List<String> names = schedule.get(integer)
                    .stream()
                    .map(IUnitDAGVertex::getName)
                    .collect(Collectors.toList());
            scheduleNames.addAll(integer, names);
        }

        int numberOfBuckets = scheduleNames.keySet().stream().max(Comparator.naturalOrder()).orElse(-1) + 1;
        int maxNumberOfLinesPerBucket = inputSpec.isSolveForPacketRate() ? inputSpec.getNumOfContexts() : 100; //instead of 100 it would be totalScheduleLength/R_MAX
        int numberOfBucketsPerLine = inputSpec.getNumOfProcessors();
        int widthOfTextInABucket = 25;
        String tableHeading = "Schedule";
        printFormattedText(scheduleNames, numberOfBuckets, maxNumberOfLinesPerBucket, numberOfBucketsPerLine, widthOfTextInABucket, "t", tableHeading);


    }

    private void printModuloScheduleSolutionFormatted(ListMap<Integer, IUnitDAGVertex> moduloSchedule, InputSpec inputSpec) {

        ListMap<Integer, String> moduloScheduleNames = new ListMap<>(LinkedHashMap.class, ArrayList.class);
        for (Integer integer : moduloSchedule.keySet()) {
            List<String> names = moduloSchedule.get(integer)
                    .stream()
                    .map(IUnitDAGVertex::getName)
                    .collect(Collectors.toList());
            moduloScheduleNames.addAll(integer, names);
        }
        int numberOfBuckets = inputSpec.isSolveForPacketRate() ? inputSpec.getNumOfProcessors() * inputSpec.getPacketRate() : period_duration;
        int maxNumberOfLinesPerBucket = inputSpec.isSolveForPacketRate() ? inputSpec.getNumOfContexts() : 100; //instead of 100 it would be totalScheduleLength/R_MAX
        int numberOfBucketsPerLine = inputSpec.getNumOfProcessors();
        int widthOfTextInABucket = 25;
        String tableHeading = "Modulo Schedule";
        printFormattedText(moduloScheduleNames, numberOfBuckets, maxNumberOfLinesPerBucket, numberOfBucketsPerLine, widthOfTextInABucket, "t", tableHeading);
    }

    private void printModuloScheduleSolutionPktsAgoFormatted(ListMap<Integer, String> moduloScheduleNames, InputSpec inputSpec) {
        int numberOfBuckets = inputSpec.isSolveForPacketRate() ? inputSpec.getNumOfProcessors() * inputSpec.getPacketRate() : period_duration;
        int maxNumberOfLinesPerBucket = inputSpec.isSolveForPacketRate() ? inputSpec.getNumOfContexts() : 100; //instead of 100 it would be totalScheduleLength/R_MAX
        int numberOfBucketsPerLine = inputSpec.getNumOfProcessors();
        int widthOfTextInABucket = 25;
        String tableHeading = "Modulo Schedule Packets Ago";
        printFormattedText(moduloScheduleNames, numberOfBuckets, maxNumberOfLinesPerBucket, numberOfBucketsPerLine, widthOfTextInABucket, "t", tableHeading);
    }

    private void printDisjointMatchesFormatted(ListMap<Integer, IUnitDAGVertex> disjointMatchs, InputSpec inputSpec) {

        ListMap<Integer, String> disJointMatchesNames = new ListMap<>(LinkedHashMap.class, ArrayList.class);
        for (Integer integer : disjointMatchs.keySet()) {
            List<String> names = disjointMatchs.get(integer)
                    .stream()
                    .map(IUnitDAGVertex::getName)
                    .collect(Collectors.toList());
            disJointMatchesNames.addAll(integer, names);
        }

        int numberOfBuckets = inputSpec.isSolveForPacketRate() ? inputSpec.getPacketRate() : 1;
        int maxNumberOfLinesPerBucket = inputSpec.isSolveForPacketRate() ? inputSpec.getNumOfProcessors() * inputSpec.getNumOfContexts() * inputSpec.getPacketRate() : 1000;
        int numberOfBucketsPerLine = inputSpec.getPacketRate();
        int widthOfTextInABucket = 25;
        String tableHeading = "Disjoint Matches across processors";

        printFormattedText(disJointMatchesNames, numberOfBuckets, maxNumberOfLinesPerBucket, numberOfBucketsPerLine, widthOfTextInABucket, "b", tableHeading);
    }

    private void printFormattedText(ListMap<Integer, String> moduloScheduleNames, int numberOfBuckets,
                                    int maxNumberOfLinesPerBucket, int numberOfBucketsPerLine,
                                    int widthOfTextInABucket, String cellHeading, String tableHeading) {
        StringBuilder message = new StringBuilder();
        message.append(tableHeading).append("\n");


        String header = "";
        List<String> lines = new ArrayList<>();
        String horizontalLine = "|" + String.join("", Collections.nCopies(widthOfTextInABucket, "-")) + "|";
        int maxLinesFoundInaRow = 0;

        int index = 0;
        while (index < numberOfBuckets) {
            if (index % numberOfBucketsPerLine == 0) {
                header = "";
                lines.clear();
                for (int j = 0; j < maxNumberOfLinesPerBucket; j++) {
                    lines.add(j, "");
                }
            }
            header += "|" + fixedLengthString(cellHeading + "=" + index, widthOfTextInABucket) + "|";

            List<String> idagVertices = moduloScheduleNames.get(index);

            for (int j = 0; j < maxNumberOfLinesPerBucket; j++) {
                String fixedLengtString;
                if (j < idagVertices.size()) {
                    fixedLengtString = lines.get(j) + "|" + fixedLengthString(idagVertices.get(j), widthOfTextInABucket) + "|";
                    if (maxLinesFoundInaRow <= j) {
                        maxLinesFoundInaRow = j + 1;
                    }
                } else {
                    fixedLengtString = lines.get(j) + "|" + fixedLengthString("", widthOfTextInABucket) + "|";
                }
                lines.set(j, fixedLengtString);
            }

            index++;
            if (index % numberOfBucketsPerLine == 0 || index == numberOfBuckets) {
                message.append(header).append("\n");
                for (int j = 0; j < maxLinesFoundInaRow; j++) {
                    if (j < lines.size()) {
                        message.append(lines.get(j)).append("\n");
                    }
                }
                message.append(String.join("", Collections.nCopies(numberOfBucketsPerLine, horizontalLine))).append("\n");
                maxLinesFoundInaRow = 0;
            }
        }

        printToConsole(message.toString());
    }

    private void printActionFieldsConsumedPerCyclePerProcessor(IloIntVar[] actionFieldsConsumed, IloCplex cplex) throws IloException {

        List<Integer> values = new ArrayList<>();
        for (IloIntVar iloIntVar : actionFieldsConsumed) {
            values.add((int) Math.round(cplex.getValue(iloIntVar)));
        }

        int numberOfEntriesPerLine = 8;
        printFormattedTextSingleLine(values, numberOfEntriesPerLine, "Action fields consumed in a cycle per context per processor");

    }

    private void printModuloMatchUnitsConsumed(Map<Integer, Integer> moduloMatchUnitsConsumed, InputSpec inputSpec) {

        int numberOfEntriesPerLine = 8;
        List<Integer> values = new ArrayList<>(moduloMatchUnitsConsumed.values());
        printFormattedTextSingleLine(values, numberOfEntriesPerLine, "Match units consumed in a modulo cycle");
    }

    private void printModuloActionFieldsConsumed(Map<Integer, Integer> moduloActionFieldsConsumed, InputSpec inputSpec) {
        int numberOfEntriesPerLine = 8;
        List<Integer> values = new ArrayList<>(moduloActionFieldsConsumed.values());
        printFormattedTextSingleLine(values, numberOfEntriesPerLine, "Action Fields consumed in a modulo cycle");
    }

    private void printFormattedTextSingleLine(List<Integer> values, int numberOfEntriesPerLine, String tableHeading) {
        int index = 0;
        List<String> lines = new ArrayList<>();
        StringBuilder headerString = new StringBuilder();
        StringBuilder actionsConsumedString = new StringBuilder();
        StringBuilder message = new StringBuilder();
        message.append(tableHeading).append("\n");
        for (Integer value : values) {
            headerString.append("    t=").append(String.format("%02d", index)).append("    |");
            String val = String.format("%02d", value);
            actionsConsumedString.append("      ").append(val).append("    |");
            index++;
            if (index % numberOfEntriesPerLine == 0) {
                lines.add(headerString.toString() + "\n" + actionsConsumedString.toString());
                headerString.setLength(0);
                actionsConsumedString.setLength(0);
            }
        }
        if (index % numberOfEntriesPerLine != 0) {
            lines.add(headerString.toString() + "\n" + actionsConsumedString.toString());
            headerString.setLength(0);
            actionsConsumedString.setLength(0);
        }


        for (String line : lines) {
            message.append(line).append("\n").append("\n");
        }
        message.append("");
        printToConsole(message.toString());
    }

    private String fixedLengthString(String string, int length) {
        return String.format("%1$" + length + "s", string);
    }

    private void printToConsole(String message) {
        L.info("\n" + message);
    }

}
