package com.p4.drmt.schd;

import com.p4.drmt.alu.CMatchNode;
import com.p4.drmt.alu.ConditionalComplexRow;
import com.p4.drmt.cfg.DRMTConstants;
import com.p4.drmt.cfg.KeyMeta;
import com.p4.drmt.compiler.ConfigEmitUnit;
import com.p4.drmt.ilp.IUnitDAGVertex;
import com.p4.ds.ListMap;
import com.p4.drmt.parser.FW;
import com.p4.drmt.struct.*;
import com.p4.drmt.xbar.XBarUnit;
import com.p4.p416.gen.ControlDeclarationContextExt;
import com.p4.packetgen.runner.CommandLineParser;
import com.p4.pktgen.SOMUtils;
import com.p4.utils.FileUtils;
import com.p4.utils.Utils;
import com.p4.utils.Utils.Pair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

import lombok.Getter;

public class NewScheduler {
    private static final Logger L = LoggerFactory.getLogger(NewScheduler.class);


    @Getter private static Map<Integer,Map<String,Integer>> tableToActionInstPtrMap = new HashMap<Integer,Map<String,Integer>>();
    @Getter private static Set<List<Integer>> disjointTables = new HashSet<List<Integer>>();

    //List holding Consolidated Configurations for Scheduler
    private static List<SchLengthUnit> schList = new ArrayList<SchLengthUnit>();
    public static List<SchLengthUnit> getSchList(){
        return Collections.unmodifiableList(schList);
    }

    //Resource Constraints

    public static void buildSolverSchedule(ListMap<Integer, IUnitDAGVertex> schedule, CommandLineParser cp){
        List<TimeQuanta> timeQuantas = getTimeQuantas(schedule);
        //validateSchedule(timeQuantas);
        //initializeDelays(timeQuantas,cp);
        disjointTables = gatherDisjointSets(timeQuantas);
        Map<Integer, Integer> xtdms = xtdmMap(disjointTables);
        SOMUtils.placeTablesInSOMs(disjointTables);
        Map<Integer, Pair<Integer, Set<Integer>>> tableSOMMap = SOMUtils.getTableToSomKsegMap(); //MAP: tableId -> Pair(somId, List of ksegs)
        int[][] dsegMap = SOMUtils.getDsegMap();
        XBarUnit.generateXBarConfig(tableSOMMap, xtdms, dsegMap, cp.getOutputDir().getAbsolutePath());
        int scheduleLength = schdLength(timeQuantas,cp);
        //assert (scheduleLength<=DRMTConstants.MAXSCHDLNGTH);
        generateScheduleMetaInfo(cp, scheduleLength);
        generateScheduleConfig(timeQuantas,cp);

    }

    private static Map<Integer, Integer> xtdmMap(Set<List<Integer>> disjointTables){
        Map<Integer, Integer> ret = new LinkedHashMap<>();
        int xtdm = 0;
        for(List<Integer> s: disjointTables){
            for(int tid: s){
                ret.put(tid, xtdm);
            }
            xtdm++;
        }
        return ret;
    }
    private static List<TimeQuanta> getTimeQuantas(ListMap<Integer, IUnitDAGVertex> schedule) {
        List<TimeQuanta> timeQuantas = initialize();
        for(Map.Entry<Integer, List<IUnitDAGVertex>> entry:schedule.entrySet()){
            if(entry.getValue().size()>0) {
                IUnitDAGVertex v0 = entry.getValue().get(0);
                if(v0.isMatch()) {
                    MatchQuanta m = new MatchQuanta(false);
                    for (IUnitDAGVertex vertex : entry.getValue()) {
                        IUnit unit = vertex.getUnit();
                        if (unit instanceof IMatchNode) {
                            m.getMatchNodes().add((IMatchNode) unit);
                        }
                    }
                    timeQuantas.set(entry.getKey(), m);
                }
                else {
                    String fElem = entry.getValue().get(0).getUnit().getClass().getName();
                    for(IUnitDAGVertex vertex : entry.getValue()){
                        if(!vertex.getUnit().getClass().getName().equals(fElem)){
                            L.info(Utils.summary(entry.getValue()));
                            throw new IllegalStateException();
                        }
                    }

                    if(entry.getValue().get(0).getUnit() instanceof IMatchNode){
                        MatchQuanta m = new MatchQuanta(true);
                        for (IUnitDAGVertex vertex : entry.getValue()) {
                            IUnit unit = vertex.getUnit();
                            if (unit instanceof IMatchNode) {
                                m.getMatchNodes().add((IMatchNode) unit);
                            } else {
                                throw new UnsupportedOperationException("Can't club match and action quanta");
                            }
                        }
                        timeQuantas.set(entry.getKey(), m);
                    }else{
                        ActionQuanta a = new ActionQuanta();
                        for (IUnitDAGVertex vertex : entry.getValue()) {
                            IUnit unit = vertex.getUnit();
                            if (unit instanceof IALUOperation) {
                                a.aluOperations.add((IALUOperation) unit);
                            } else {
                                throw new UnsupportedOperationException("Can't club match and action quanta");
                            }
                        }
                        timeQuantas.set(entry.getKey(), a);
                    }



                }
            }
        }
        return timeQuantas;
    }

    public static void buildNaiveSchedule(List<Set<IUnit>> parallelNodes, CommandLineParser cp){
        List<TimeQuanta> timeQuantas = initialize();
        int count = 0;
        for(Set<IUnit> p:parallelNodes){
            if(count>DRMTConstants.MAXSCHDLNGTH){
                throw new IllegalStateException("Naive Greedy Scheduling Not Possible ");
            }
            MatchQuanta m = getMatchQuanta(p);

            ActionQuanta a = getActionQuanta(p);
            if(m.size()>0 && a.size()>0){
                timeQuantas.set(count, m);
                timeQuantas.set(count+1, a);
                timeQuantas.set(count+DRMTConstants.matchDelay+1, m.getActionOf());
                count+=(DRMTConstants.matchDelay+1+DRMTConstants.actionDelay+1);
                throw new UnsupportedOperationException("Not Supported");
            }else if(m.size()>0){
                List<MatchQuanta> matches = m.split(DRMTConstants.numkseg);

                for(MatchQuanta segMatch:matches){
                    timeQuantas.set(count, segMatch);
                    timeQuantas.set(count+DRMTConstants.matchDelay+1, segMatch.getActionOf());
                    count+=(DRMTConstants.matchDelay+1+DRMTConstants.actionDelay+1);
                }
                //timeQuantas.set(count, m);
                //timeQuantas.set(count+cp.getMatchDelay()+1, m.getActionOf());
                //count+=(cp.getMatchDelay()+1+cp.getActionDelay()+1+i);
            }else if(a.size()>0){
                timeQuantas.set(count, a);
                count+=DRMTConstants.actionDelay+1;
            }
        }
        L.info("Schedule:\n"+Utils.summary(timeQuantas));
        Schedule sch = new Schedule(timeQuantas,DRMTConstants.actionDelay, DRMTConstants.matchDelay, DRMTConstants.PACKETRATE, DRMTConstants.NUMDRMTPROCS);
        //sch.adjust();
        //timeQuantas = sch.getSchedule();
        validateSchedule(timeQuantas);
        //initializeDelays(timeQuantas,cp);
        disjointTables = gatherDisjointSets(timeQuantas);
        SOMUtils.placeTablesInSOMs(disjointTables);
        Map<Integer, Pair<Integer, Set<Integer>>> tableSOMMap = SOMUtils.getTableToSomKsegMap(); //MAP: tableId -> Pair(somId, List of ksegs)
        int[][] dsegMap = SOMUtils.getDsegMap();
        XBarUnit.generateXBarConfig(tableSOMMap, xtdmMap(disjointTables), dsegMap, cp.getOutputDir().getAbsolutePath());
        int scheduleLength = schdLength(timeQuantas,cp);
        assert (scheduleLength<=DRMTConstants.MAXSCHDLNGTH);
        generateScheduleMetaInfo(cp, scheduleLength);
        generateScheduleConfig(timeQuantas,cp);
    }

   /* private static void initializeDelays(List<TimeQuanta> timeQuantas, CommandLineParser cp) {
        for(TimeQuanta t: timeQuantas){
            if(t instanceof MatchQuanta){
                MatchQuanta m = (MatchQuanta)t;
                for(IMatchNode mn:m.getMatchNodes()){
                    mn.setMatchDelay(matchDelay);
                    for(IAction ia:mn.getTable().getActions()) {
                        for(IALUOperation ialuOperation: ia.getInstructions()){
                            ialuOperation.setActionDelay(actionDelay);
                        }
                    }
                }
            }else if(t instanceof ActionQuanta){
                ActionQuanta a = (ActionQuanta)t;
                for(IALUOperation ialuOperation:a.aluOperations){
                    ialuOperation.setActionDelay(actionDelay);
                }
            }
        }
    }*/

    private static Set<List<Integer>> gatherDisjointSets(List<TimeQuanta> timeQuantas) {
        List<List<TimeQuanta>> sameProc = ScheduleUtils.segregateMod(timeQuantas, DRMTConstants.PACKETRATE);
        Set<List<Integer>> disjoint = new LinkedHashSet<>();
        for(int i=0;i<sameProc.size();i++){
            L.info("At Mod ["+i+"]:"+ Utils.summary(sameProc.get(i)));
            List<Integer> ids = new ArrayList<>();
            for(TimeQuanta t: sameProc.get(i)){
                if(t.isMatchType() && !((MatchQuanta)t).isAction()){
                    MatchQuanta m =(MatchQuanta)t;
                    ids.addAll(m.tableIds());
                }
            }
            disjoint.add(ids);
        }
        L.info(Utils.summary(disjoint));
        return disjoint;
    }

    private static void validateSchedule(List<TimeQuanta> timeQuantas) {
        for(TimeQuanta t:timeQuantas){
            if(t.isMatchType()){
                MatchQuanta m = (MatchQuanta)t;
                if(m.getMatchNodes().size()>DRMTConstants.numkseg){//NUMKSEG check
                    throw new IllegalStateException("Can't schedule more than "+ DRMTConstants.numkseg + " matches in 1 cycle");
                }
            }
        }
        List<List<TimeQuanta>> sameProc = ScheduleUtils.segregateMod(timeQuantas, DRMTConstants.NUMDRMTPROCS*DRMTConstants.PACKETRATE);
        for(int i=0;i<sameProc.size();i++){
            //L.info("At Mod ["+i+"]:"+ Utils.summary(sameProc.get(i)));
            if(!atMostOneMatchOrAction(sameProc.get(i))){
                L.error("Can't satisfy constraint. More than one Match or action in same Processor");
                assert(false);
            }
        }
    }

    private static boolean atMostOneMatchOrAction(List<TimeQuanta> timeQuantas) {
        int count = 0;
        for(TimeQuanta t:timeQuantas){
            if(!(t instanceof NOOPTimeQuanta)){
                count++;
            }
        }
        return count<=1?true:false;
    }

    private static void generateScheduleConfig(List<TimeQuanta> timeQuantas, CommandLineParser cp){
        Map<Integer, List<Integer>> matchSchedule = new LinkedHashMap<>();
        int matchCount = 0;
        int actionCount = 0;
        int instructionCount = 0;
        //TODO PlaceHolder
        ConditionalComplexRow c = new ConditionalComplexRow();
        c.pad2D();
        File schFile = new File(cp.getOutputFile().getAbsolutePath()+File.separator+"sch"+File.separator+"sch_cfg.txt");
        File matchFile = new File(cp.getOutputFile().getAbsolutePath()+File.separator+"sch"+File.separator+"match_cfg.txt");
        for(int i=0; i<timeQuantas.size();i++){
            int wait = waitTillNextQuanta(timeQuantas, i, cp);
            boolean last = isLast(timeQuantas, i);
            if(timeQuantas.get(i) instanceof MatchQuanta || timeQuantas.get(i) instanceof ActionQuanta) {
                FileUtils.AppendToFile(schFile, "Operations at cycle " + i + "\n");
                FileUtils.AppendToFile(schFile, "-------------------------" + "\n");
            }
            if(timeQuantas.get(i) instanceof MatchQuanta){
                MatchQuanta m = (MatchQuanta)timeQuantas.get(i);
                if(!m.isAction()){
                    SchLengthUnit sc = SchLengthUnit.constructScheduleConfigNodeMatch(0, matchCount, matchCount==0?1:0, wait);
                    FileUtils.AppendToFile(schFile, "Match at cycle ["+i+"], for table(s) "+Utils.summary(m.tableIds())+"\n");
                    for(IMatchNode matchNode: m.getMatchNodes()){
                        CMatchNode cMatchNode= ((CMatchNode)matchNode);
                        String predString =  cMatchNode.getPredicate()!=null?cMatchNode.getPredicate().offsetSummary() :"";
                        FileUtils.AppendToFile(matchFile, "At time t = "+i +", Match for table "+cMatchNode.summary()+" with id "+ cMatchNode.getTable().getTableId() + " has predicate "+predString+"\n");
                    }
                    matchSchedule.put(matchCount, m.tableIds());
                    matchCount++;
                    sc.padAll();
                    sc.emitAll(SchLengthUnit.schDirName(cp.getOutputDir().getAbsolutePath()));
                    schList.add(sc);

                }else{
                    SchLengthUnit sc = SchLengthUnit.constructScheduleConfigNodeAction(0,actionCount, last?1:0, wait );
                    FileUtils.AppendToFile(schFile, "Action for matches at cycle ["+i+"], for table(s) "+Utils.summary(m.tableIds())+"\n");
                    sc.padAll();
                    sc.emitAll(SchLengthUnit.schDirName(cp.getOutputDir().getAbsolutePath()));
                    schList.add(sc);
                    Map<Integer, List<Integer>> cpuMap = m.aluAssigns();
                    for(IMatchNode matchNode: m.getMatchNodes()){
                        int tableId = matchNode.getTable().getTableId();
                        int predicateOffset = matchNode.getPredicate()==null?0:matchNode.getPredicate().getOffset();
                        L.info(matchNode.getTable().getName()+" with id "+tableId);
                        List<Integer> cpus = cpuMap.get(tableId);
                        Map<String, Integer> actionToInstPtrMap = new HashMap<String, Integer>();
                        for(IAction action: matchNode.getTable().getActions()){
                            if(action.getName().startsWith("NoAction"))
                                continue;
                            List<IALUOperation> operations = action.getInstructions();
                            assert(operations.size()==cpus.size());
                            L.info(Utils.summary(cpus.size()));
                            L.info(Utils.summary(operations));
                            actionToInstPtrMap.put(action.getName(), actionCount);
                            for(int j=0; j<operations.size(); j++){
                                ALUConfigWriter.addALUEntryForTable(tableId, instructionCount, actionCount,
                                        cpus.get(j),operations.get(j), //CPU to Operations count
                                        matchNode.getPredicate()!=null, predicateOffset,c, cp);//Match Predicate
                            }
                        }
                        tableToActionInstPtrMap.put(tableId, actionToInstPtrMap);
                    }
                    actionCount++;
                    instructionCount++;
                }
            }else if(timeQuantas.get(i) instanceof ActionQuanta){
                SchLengthUnit sc = SchLengthUnit.constructScheduleConfigNodeAction(0,actionCount, last?1:0, wait );

                sc.padAll();
                sc.emitAll(SchLengthUnit.schDirName(cp.getOutputDir().getAbsolutePath()));
                schList.add(sc);
                ActionQuanta a = (ActionQuanta) timeQuantas.get(i);

                int cpucount=0;
                for(IALUOperation ialuOperation:a.aluOperations){
                    FileUtils.AppendToFile(schFile, "Direct Action at cycle ["+i+"], for instruction on cpu["+cpucount+"] "+ialuOperation.summary()+"\n");
                    ALUConfigWriter.addALUEntryDirect(instructionCount,actionCount, cpucount, ialuOperation, c, cp);
                    cpucount++;
                }
                actionCount++; instructionCount++;
            }else{

            }
            if(timeQuantas.get(i) instanceof MatchQuanta || timeQuantas.get(i) instanceof ActionQuanta) {
                FileUtils.AppendToFile(schFile, "************************" + "\n");
            }
        }
        ALUConfigWriter.emit(cp.getOutputDir().getAbsolutePath());
        c.emitAll(ConditionalComplexRow.conddirName(cp.getOutputDir().getAbsolutePath()));
        L.info("%%%");
        L.info(Utils.summary(matchSchedule));
        KeyMeta.schedule = matchSchedule;

    }

    private static boolean isLast(List<TimeQuanta> timeQuantas, int index) {
        boolean nextFound = false;
        int i=index+1;
        for(;i<timeQuantas.size();i++){
            if(!(timeQuantas.get(i) instanceof NOOPTimeQuanta)){
                nextFound = true;
                break;
            }
        }
        return !nextFound;
    }

    private static int waitTillNextQuanta(List<TimeQuanta> timeQuantas, int index, CommandLineParser cp) {
        boolean nextFound = false;
        int i=index+1;
        for(;i<timeQuantas.size();i++){
            if(!(timeQuantas.get(i) instanceof NOOPTimeQuanta)){
                nextFound = true;
                break;
            }
        }
        if(nextFound)
            return i-index-1;
        else{
            return DRMTConstants.actionDelay;
        }
    }

    private static void generateScheduleMetaInfo(CommandLineParser cp, int length){
        String dir = cp.getOutputDir().getAbsolutePath();
        File f = new File(dir+File.separator+"sch");
        File schF = new File(f.getAbsolutePath()+File.separator+"sch_meta.txt");

        f.mkdirs();
        ConfigEmitUnit pu_proc_max = new ConfigEmitUnit(8, 1, "pu_proc_max.cfg");
        pu_proc_max.addItem(DRMTConstants.NUMDRMTPROCS*DRMTConstants.PACKETRATE-1);
        emit(pu_proc_max, dir+ File.separator+"sch"+File.separator+pu_proc_max.getFileName());
        FileUtils.AppendToFile(schF, "Round Robin Rollover time (pu_proc_max) = "+(DRMTConstants.NUMDRMTPROCS*DRMTConstants.PACKETRATE-1)+"\n");
        ConfigEmitUnit pu_proc_time = new ConfigEmitUnit(8, 16, "pu_proc.cfg");
        //int interval = Utils.ceil(lwoD, NUMDRMTPROCS);
        int count = 0;
        for(int i=0;i<DRMTConstants.NUMDRMTPROCS;i++){
            pu_proc_time.addItem(count);
            FileUtils.AppendToFile(schF, "Packet for proc["+i+"] at = "+ count+"\n");
            count+=DRMTConstants.PACKETRATE;
        }
        emit(pu_proc_time, dir+ File.separator+"sch"+File.separator+pu_proc_time.getFileName());
        L.info("Total Schedule length = "+length);
        ConfigEmitUnit sch_vals = new ConfigEmitUnit(8, 16, "sch_vals.cfg");

        sch_vals.addItem(length);
        FileUtils.AppendToFile(schF, "Total schedule length = "+ length+"\n");
        int stallDelay = (length-DRMTConstants.matchDelay)<0? 0 : (length-DRMTConstants.matchDelay);
        sch_vals.addItem(stallDelay);
        FileUtils.AppendToFile(schF, "Stall delay  = "+ (stallDelay)+"\n");
        sch_vals.addItem(DRMTConstants.BP_THR);
        FileUtils.AppendToFile(schF, "Back Pressure Threshold  = "+ (DRMTConstants.BP_THR)+"\n");
        FileUtils.AppendToFile(schF, "Match Delay  = "+ (DRMTConstants.matchDelay)+"\n");
        FileUtils.AppendToFile(schF, "Action Delay  = "+ (DRMTConstants.actionDelay)+"\n");
        emit(sch_vals,dir+ File.separator+"sch"+File.separator+sch_vals.getFileName());

    }

    private static int schdLength(List<TimeQuanta> timeQuantas, CommandLineParser cp){
        int i = timeQuantas.size()-1;
        for(; i>=0;i--){
            if(!(timeQuantas.get(i) instanceof NOOPTimeQuanta))
                break;
        }
        //1 extra for 0 based index
        return i+1+DRMTConstants.actionDelay;//LAST Node is always action
    }

    private static MatchQuanta getMatchQuanta(Set<IUnit> p){
        MatchQuanta m = new MatchQuanta(false);
        for(IUnit unit:p){
            if(unit instanceof IMatchNode)
            m.getMatchNodes().add((IMatchNode) unit);
        }
        return m;
    }
    private static ActionQuanta getActionQuanta(Set<IUnit> p){
        ActionQuanta a = new ActionQuanta();
        for(IUnit unit:p){
            if(unit instanceof IALUOperation)
                a.aluOperations.add((IALUOperation) unit);
        }
        return a;
    }

    private static List<TimeQuanta> initialize(){
        List<TimeQuanta> timeQuantas = new ArrayList<>();
        for(int i=0;i<2* DRMTConstants.MAXSCHDLNGTH;i++){
            timeQuantas.add(NOOPTimeQuanta.NOOP);
        }
        return timeQuantas;
    }

    public static void scheduleTest(List<ControlDeclarationContextExt> gNodes, CommandLineParser cp){
        String dir = cp.getOutputDir().getAbsolutePath();
        List<MANode> maNodes = new ArrayList<>();

        for(ControlDeclarationContextExt controlDeclarationContextExt:gNodes){
            L.warn("***"+controlDeclarationContextExt.getName());
            maNodes.addAll(controlDeclarationContextExt.getContext().controlBody().extendedContext.getAllMANodes());
        }
        Utils.Pair<Integer,Integer> l = scheduleLength(maNodes, cp);
        int length = l.first();
        assert(length<DRMTConstants.MAXSCHDLNGTH);
        int lwoD= l.second();
        ConfigEmitUnit pu_proc_max = new ConfigEmitUnit(8, 1, "pu_proc_max.cfg");
        pu_proc_max.addItem(DRMTConstants.NUMDRMTPROCS*DRMTConstants.PACKETRATE-1);
        emit(pu_proc_max, dir+ File.separator+"sch"+File.separator+pu_proc_max.getFileName());

        L.info("Total Schedule length ="+length);
        ConfigEmitUnit sch_vals = new ConfigEmitUnit(8, 16, "sch_vals.cfg");

        sch_vals.addItem(length);
        sch_vals.addItem(length-DRMTConstants.matchDelay);
        sch_vals.addItem(DRMTConstants.BP_THR);
        emit(sch_vals,dir+ File.separator+"sch"+File.separator+sch_vals.getFileName());

        ConfigEmitUnit pu_proc_time = new ConfigEmitUnit(8, 16, "pu_proc.cfg");
        //int interval = Utils.ceil(lwoD, NUMDRMTPROCS);
        int count = 0;
        for(int i=0;i<DRMTConstants.NUMDRMTPROCS;i++){
            pu_proc_time.addItem(count);
            count+=DRMTConstants.PACKETRATE;
        }
        emit(pu_proc_time, dir+ File.separator+"sch"+File.separator+pu_proc_time.getFileName());
    }

    private static Utils.Pair<Integer, Integer> scheduleLength(List<MANode> maNodes, CommandLineParser cp){
        String dir = cp.getOutputDir().getAbsolutePath();
        int length = 0;
        int lengthWODelay = 0;
        int totalMatches = 0;
        int totalActions = 0;
        for(int i=0; i< maNodes.size();i++){
            int flag = (i==maNodes.size()-1)? 1:0;
            MANode maNode = maNodes.get(i);
            if(maNode instanceof IALUOperation){
                SchLengthUnit sc = SchLengthUnit.constructScheduleConfigNodeAction(length, totalActions,flag, DRMTConstants.actionDelay-1);
                totalActions++;
                length+=DRMTConstants.actionDelay;
                lengthWODelay++;
                sc.padAll();
                sc.emitAll(SchLengthUnit.schDirName(dir));
                //NOOP for rest of the time
                //emitWait(ACTION_DELAY-1, dir);

            }else if(maNode instanceof IMatchNode){
                SchLengthUnit sc = SchLengthUnit.constructScheduleConfigNodeMatch(length, totalMatches, totalMatches==0?1:0, DRMTConstants.matchDelay-1);
                totalMatches++;
                length+=DRMTConstants.matchDelay;
                lengthWODelay++;
                sc.padAll();
                sc.emitAll(SchLengthUnit.schDirName(dir));
                //NOOP for rest of the time
                //emitWait(MATCH_DELAY-1, dir);
                sc = SchLengthUnit.constructScheduleConfigNodeAction(length,totalActions, flag, DRMTConstants.actionDelay-1 );
                totalActions++;
                length+=DRMTConstants.actionDelay;
                lengthWODelay++;
                sc.padAll();
                sc.emitAll(SchLengthUnit.schDirName(dir));
                //NOOP for rest of the time
                // emitWait(ACTION_DELAY-1, dir);

            }
        }
        return Utils.Pair.of(length,lengthWODelay);
    }

    private static void emit(ConfigEmitUnit configEmitUnit, String file){
        File absoluteFile = new File(file);
        FileUtils.createNewFile(absoluteFile, true);
        StringBuilder sb = new StringBuilder();
        for(List<FW> item:configEmitUnit.getItems()){
            for(FW fw:item){
                sb.append(""+fw.toHSizeNibbles()+"\n");
            }
        }
        FileUtils.AppendToFile(absoluteFile, sb.toString());
    }
    @SuppressWarnings("ununsed")
    private static void emitWait(int cycles, String dir){
        for(int j=0; j<cycles;j++){
            SchLengthUnit.NOOPSCH.emitAll(SchLengthUnit.schDirName(dir));
        }
    }

}
