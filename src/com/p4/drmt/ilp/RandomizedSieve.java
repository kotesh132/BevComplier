package com.p4.drmt.ilp;

import com.p4.ids.IDAGEdge;
import com.p4.ids.IDAGGraph;
import com.p4.utils.Utils.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

public class RandomizedSieve {

    private int time_limit;
    private IDAGGraph<IUnitDAGVertex, IDAGEdge> dagGraph;
    private InputSpec inputSpec;
    private int period_duration;
    private static final Logger L = LoggerFactory.getLogger(RandomizedSieve.class);

    RandomizedSieve(IDAGGraph<IUnitDAGVertex, IDAGEdge> dagGraph, InputSpec inputSpec, int period_duration, int time_limit) {
        this.dagGraph = dagGraph;
        this.inputSpec = inputSpec;
        this.period_duration = period_duration;
        this.time_limit = time_limit;
    }

    Map<IUnitDAGVertex, Integer> randomSieve() {

        Pair<Integer, List<IUnitDAGVertex>> criticalPath = dagGraph.getCriticalPath();
        int delay = criticalPath.first();
        int best = delay * 2;
        Map<IUnitDAGVertex, Integer> best_schedule = new HashMap<>();

       L.info("Random Sieve: Looking for greedy feasible solution for " + time_limit + " seconds");

        long currentTime = System.currentTimeMillis();
        long startTime = System.currentTimeMillis();
        int index = 0;

        int vertexCount = dagGraph.getVertexCount();

        while (((currentTime - startTime) / 1000) < time_limit) {

            Map<IUnitDAGVertex, Integer> schedule = indexDagSieve(index % vertexCount, 2 * delay);
            index += 1;

            if (schedule != null && !schedule.isEmpty()) {
                int max_val = schedule.values().stream().max(Comparator.naturalOrder()).orElse(Integer.MIN_VALUE);
                int min_val = schedule.values().stream().min(Comparator.naturalOrder()).orElse(Integer.MAX_VALUE);

                if (max_val - min_val < best) {
                    best = max_val - min_val;
                    best_schedule = schedule;
                   L.info("Found Feasible Solution with Latency " + (best + 1));
                }

            }
            currentTime = System.currentTimeMillis();

        }

        if (best_schedule.isEmpty()) {
            return null;
        }

        int min_val = best_schedule.values().stream().min(Comparator.naturalOrder()).orElse(Integer.MAX_VALUE);
        best_schedule.replaceAll((k, v) -> v - min_val);

//       L.info("*****");
//        best_schedule.forEach((k, v) ->L.info(k.getName() + ":" + v));
//       L.info("*****");


        return best_schedule;


    }

    private Map<IUnitDAGVertex, Integer> indexDagSieve(int index, int bound) {
        Map<IUnitDAGVertex, Integer> schedule = new HashMap<>();

        //wild card intensity
        int rfM = 10 ^ 4;
        int rfA = 10 ^ 4;

        //upper bound on nulls for each wild card
        int nullsM = 2;
        int nullsA = 2;

        // limit on keys of matches rounded to unit size - match_unit_limit
        List<Integer> match_limit = new ArrayList<>(period_duration);

        // limit on bits of actions - action_fields_limit
        List<Integer> action_limit = new ArrayList<>(period_duration);

        List<Set<Integer>> concurrent_match_limit = new ArrayList<>(period_duration);
        List<Set<Integer>> concurrent_action_limit = new ArrayList<>(period_duration);

        for (int i = 0; i < period_duration; i++) {
            concurrent_match_limit.add(new HashSet<Integer>());
            concurrent_action_limit.add(new HashSet<Integer>());
            match_limit.add(0);
            action_limit.add(0);
        }

        for (IUnitDAGVertex vertex : dagGraph.getVertices()) {
            vertex.setTime(0);
        }

        List<IUnitDAGVertex> ts = dagGraph.topologicalSort();

        Queue<IUnitDAGVertex> tsForwardQueue = new LinkedList<>();
        Queue<IUnitDAGVertex> tsReverseQueue = new LinkedList<>();


        IntStream.range(index, ts.size()).forEach(i -> tsForwardQueue.offer(ts.get(i)));
        IntStream.range(0, index).forEach(i -> tsReverseQueue.offer(ts.get((index-1)-i)));

        Random random = new Random();

        while (!(tsForwardQueue.isEmpty() && tsReverseQueue.isEmpty())) {
            boolean coin = random.nextBoolean();

            if (tsForwardQueue.isEmpty()) {
                coin = false;
            } else if (tsReverseQueue.isEmpty()) {
                coin = true;
            }

            if (!findSchedule(bound, schedule, rfM, rfA, nullsM, nullsA, match_limit, action_limit,
                    concurrent_match_limit, concurrent_action_limit, tsForwardQueue, tsReverseQueue, random, coin))
                return null;

        }

        return schedule;

    }

    private boolean findSchedule(int bound, Map<IUnitDAGVertex, Integer> schedule, int rfM, int rfA, int nullsM, int nullsA,
                                 List<Integer> match_limit, List<Integer> action_limit,
                                 List<Set<Integer>> concurrent_match_limit, List<Set<Integer>> concurrent_action_limit,
                                 Queue<IUnitDAGVertex> tsForwardQueue, Queue<IUnitDAGVertex> tsReverseQueue,
                                 Random random, boolean coin) {

        IUnitDAGVertex currentVertex = coin ? tsForwardQueue.remove() : tsReverseQueue.remove();
        List<IDAGEdge> edges = coin ? dagGraph.edgesComingInto(currentVertex) : dagGraph.edgesGoingOutOf(currentVertex);

        int time = 0;
        if (!edges.isEmpty()) {
            for (IDAGEdge edge : edges) {
                if (coin) {
                    if (edge.getDelay() + ((IUnitDAGVertex)edge.getSource()).getTime() > time) {
                        time = edge.getDelay() + ((IUnitDAGVertex)edge.getSource()).getTime();
                    }
                } else {
                    if (((IUnitDAGVertex)edge.getDestination()).getTime() - edge.getDelay() < time) {
                        time = ((IUnitDAGVertex)edge.getDestination()).getTime() - edge.getDelay();
                    }
                }
            }
        } else {
            time = currentVertex.getTime();
        }

        if ("match".equals(currentVertex.getType())) {

            boolean flag = true;

            while (flag) {
                //check resource availablity at time%period_duration
                int moduloTime = time % period_duration;
                moduloTime = moduloTime < 0 ? moduloTime + period_duration : moduloTime;
                int match_limit_at_currentVertex = (int) Math.ceil((1.0 * currentVertex.getKeyWidth()) / inputSpec.getMatchUnitSize());
                boolean match_limit_cond = match_limit.get(moduloTime) + match_limit_at_currentVertex <= inputSpec.getMatchUnitLimit();

                boolean con_match_limit_cond = concurrent_match_limit.get(moduloTime).size() < inputSpec.getMatchProcLimit();

                boolean same_con_match_limit_cond = concurrent_match_limit.get(moduloTime).contains(time);

                boolean wild_card = random.nextInt(rfM) > 0;

                if (match_limit_cond && wild_card && (con_match_limit_cond || same_con_match_limit_cond)) {
                    //Update node time
                    currentVertex.setTime(time);

                    //Update resource usage
                    match_limit.set(moduloTime, match_limit.get(moduloTime) + match_limit_at_currentVertex);

                    //Update different packet matches at that cycle
                    concurrent_match_limit.get(moduloTime).add(time);

                    schedule.put(currentVertex, time);

                    flag = false;
                } else {
                    if (!wild_card) {
                        time += coin ? random.nextInt(nullsM) : -random.nextInt(nullsM);
                    }
                    time += coin ? 1 : -1;

                    if (coin && time > bound) {
                        return false;
                    }

                    if (!coin && time < -bound) {
                        return false;
                    }

                }
            }

        }

        if ("action".equals(currentVertex.getType())) {

            boolean flag = true;

            while (flag) {
                int moduloTime = time % period_duration;
                moduloTime = moduloTime < 0 ? moduloTime + period_duration : moduloTime;
                boolean action_limit_cond = action_limit.get(moduloTime) + currentVertex.getNumFields() <= inputSpec.getActionFieldsLimit();

                boolean con_action_limit_cond = concurrent_action_limit.get(moduloTime).size() < inputSpec.getActionProcLimit();

                boolean same_con_action_limit_cond = concurrent_action_limit.get(moduloTime).contains(time);

                boolean wild_card = random.nextInt(rfA) > 0;

                if (action_limit_cond && (con_action_limit_cond || same_con_action_limit_cond)) {

                    currentVertex.setTime(time);

                    action_limit.set(moduloTime, action_limit.get(moduloTime) + currentVertex.getNumFields());

                    concurrent_action_limit.get(moduloTime).add(time);

                    schedule.put(currentVertex, time);

                    flag = false;
                } else {
                    if (!wild_card) {
                        time += coin ? random.nextInt(nullsA) : -random.nextInt(nullsA);
                    }
                    time += coin ? 1 : -1;

                    if (coin && time > bound) {
                        return false;
                    }

                    if (!coin && time < -bound) {
                        return false;
                    }
                }
            }
        }


        return true;
    }
}
