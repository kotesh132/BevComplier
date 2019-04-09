package com.p4.quadpeaks.parser;

import com.p4.drmt.cfg.DRMTConstants;
import com.p4.drmt.parser.*;
import com.p4.p416.expressions.ParserALUOp;
import com.p4.pktgen.PacketGenerator;
import com.p4.quadpeaks.extractor.MemoryMap;
import com.p4.quadpeaks.extractor.QPExtractorConfigUnit;
import com.p4.quadpeaks.parameters.QPConstants;
import com.p4.quadpeaks.signature.ParserAluConfigUnit;
import com.p4.quadpeaks.signature.SignatureConfigUnit;
import com.p4.tools.graph.Graph;
import com.p4.utils.FileUtils;
import com.p4.utils.Utils;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class ParserConfigUtil {
    protected static final Logger L = LoggerFactory.getLogger(ParserConfigUtil.class);
    private static List<ParserRow> parserrows = new ArrayList<>();
    public static void generateParserRows(String parsername, P4Headers header, Graph<P4State> graph, File OutputDir, boolean dump, MemoryMap memoryMap, Set<String> outer){
        final Map<P4State, StateMeta> map = new LinkedHashMap<>();
        for(P4State st:graph.getNodes()){
            map.put(st, new StateMeta());
            PacketGenerator.allStates.add(st);
        }

        //Populate MetaFields for compiler
        StateMeta.assignStateIds(map);
        StateMeta.assignExtractBytes(map,header);
        StateMeta.assignSelectsAndTransitions(map,header);

        //
        List<ParserRow> rows = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        /*for(P4State s: graph.topologicalSort()){
            System.out.println(((s.getExtracts().size()>0? (s.getExtracts().get(0).getHeaderActualName()+" "+ s.getExtracts().get(0).getHeader())+";":"")+"//"+s.getName()));
        }*/
        for(P4State s: graph.topologicalSort()){
            for(P4State d:graph.adj(s)){
                ParserRow row = new ParserRow();
                //TCAM
                List<StateMeta.DM> transitions = map.get(s).getTransitions().get(d.getName());
                if(transitions==null || transitions.isEmpty()){
                    transitions = StateMeta.getDefaultTransition();
                }
                row.setTcam_vld(FW.ONE);
                row.setTcam_mask_flag(FW.ZERO_BYTES);
                row.setTcam_mask_curr(FW.ZERO_BYTES);
                row.setTcam_mask_key(StateMeta.DM.onlyMask(transitions));
                row.setTcam_data_flag(FW.ZERO_BYTES);
                row.setTcam_data_curr(new FW(map.get(s).getStateID(),7));
                row.setTcam_data_key(StateMeta.DM.onlyData(transitions));
                //SRAM
                row.setSram_done((d.getName().equals("accept")||d.getName().equals("reject"))?
                        FW.ONE:
                        FW.ZERO);

                row.setSram_next(new FW(map.get(d).getStateID(), 7));
                row.setSram_outr(outer.contains(d.getName())? FW.ONE: FW.ZERO);

                //NO ALU Needed
                P4Extract aluExtract = map.get(s).getALUExtract();
                if(aluExtract==null){
                    row.setSram_shift(new FW(map.get(s).getTotalTransitionsInBytes(), 8));
                    row.setSres_alue(FW.ZERO);
                    row.setSres_alui_op0(ParserALUOp.defaultALUOP.getOffset());
                    row.setSres_alui_mask(ParserALUOp.defaultALUOP.getMask());
                    row.setSres_alui_op1(ParserALUOp.defaultALUOP.getA());
                    row.setSres_alui_op2(ParserALUOp.defaultALUOP.getB());
                    row.setSres_alui_opcode(ParserALUOp.defaultALUOP.getOpCode());
                }else{
                    row.setSram_shift(FW.ZERO_BYTES);
                    ParserALUOp aluOp = aluExtract.getParserALUOp();
                    row.setSres_alue(FW.ONE);
                    row.setSres_alui_op0(aluOp.getOffset());
                    row.setSres_alui_mask(aluOp.getMask());
                    row.setSres_alui_op1(aluOp.getA());
                    row.setSres_alui_op2(aluOp.getB());
                    row.setSres_alui_opcode(aluOp.getOpCode());

                }




                List<StateMeta.DM> extracts = map.get(d).getExtracts();
                if(extracts==null||extracts.size()==0){//Should only happen for final
                    extracts = StateMeta.getDefaultTransition();
                }
                List<FW> doffs1 = Utils.map(new Utils.fn1<FW, FW>() {
                    @Override
                    public FW invoke(FW p1) {
                        return new FW(p1.getValue(), 8);
                    }
                }, StateMeta.DM.onlyData(extracts));

                row.setSram_doff(doffs1);
                row.setSram_dvld(StateMeta.DM.onlyValid(extracts));
                row.setSram_flag(FW.ZERO_BYTES);


                sb.append("============\n");
                sb.append(s.getName()+"["+map.get(s).getStateID()+"]->"+d.getName()+"["+map.get(d).getStateID()+"]\n");
                sb.append(row.summary());
                sb.append("============\n");
                rows.add(row);
                //Parser row added to another structure, needs to be consumed by config generator.
                parserrows.add(row);

                //Scratch
                row.setSres_svld(FW.ZERO);
                row.setSres_soff(new FW(0, 12));
                row.setSres_woff(new FW(0, 2));
                row.setSres_smsk(FW.ZERO_BYTES);
                row.setSres_rvld(ByteUtils.repeat(FW.ZERO, DRMTConstants.NUMDOFF));
                row.setSres_sconst(FW.ZERO);

            }

        }
        ParserRow.writeParserConfig(OutputDir, rows, "pa");
        //ParserRow.writeParserConfig(OutputDir, dprows, "dpa");
        ParserRow.writeParserFile(OutputDir, "pa", "parser_config.txt", sb.toString());
        //ParserRow.writeParserFile(OutputDir, "dpa", "deparser_config.txt", sb1.toString());
        L.info("Total Edges = "+graph.getEdges().size());
        L.info("Total Nodes = "+graph.getNodes().size());
        emitParserStats(graph, map, OutputDir, dump);

        L.info("Starting Extractor config Generation");
        Comparator<P4State> c = new Comparator<P4State>() {
            @Override
            public int compare(P4State o1, P4State o2) {
                Integer oi1 = map.get(o1).getStateID();
                Integer oi2 = map.get(o2).getStateID();
                return oi1.compareTo(oi2);
            }
        };


        sb = new StringBuilder();
        List<P4State> nodes = graph.customSort(c);
        for(P4State s: nodes){
            List<P4Extract> exts = s.getExtracts();
            if(!exts.isEmpty()){
                List<P4Extract> ects = map.get(s).getTransitionExtracts();
                QPExtractorConfigUnit extractor = new QPExtractorConfigUnit();

                List<SourceDestinationSize> fields = P4Extract.getFields(ects);
                L.info("Header: "+ects.get(0).getHeaderActualName());
                int byteCount = 0;
                int bitCount = 0;
                for(SourceDestinationSize fieldCopy :fields){
                    if(memoryMap.containsField(fieldCopy.fullName)) {
                        int destBit = memoryMap.getOffset(fieldCopy.fullName);
                        if(fieldCopy.size>1) {
                            L.info(fieldCopy.fullName + ":" + fieldCopy.sourceBit + ", " + fieldCopy.size + ", " + destBit);
                            sb.append("field = " + fieldCopy.fullName + ", Source bit in input Packet = " + fieldCopy.sourceBit + ", Dest bit in PHV = " + destBit + "\n");
                            List<ExtractorLogic.EXB> s2d = ExtractorLogic.getSMDOffsetsBE(fieldCopy.sourceBit, destBit, fieldCopy.size, true);
                            byteCount += s2d.size();
                            L.info(Utils.summary(s2d));
                            if(byteCount<=QPConstants.NUMCBYT) {
                                extractor.getYbyt().addItems(ExtractorLogic.EXB.onlyYbyt(s2d));
                                extractor.getXbyt().addItems(ExtractorLogic.EXB.onlyXbyt(s2d));
                                extractor.getVbyt().addItems(ExtractorLogic.EXB.onlyVld(s2d));
                                extractor.getXsft().addItems(ExtractorLogic.EXB.onlySft(s2d));
                                extractor.getXdir().addItems(ExtractorLogic.EXB.onlyDir(s2d));
                                extractor.getYmsk().addItems(ExtractorLogic.EXB.onlyMask(s2d));
                            }else{
                                L.error("Can't extract field "+fieldCopy.fullName +" as extractor byte copy instructions are exhausted");
                                sb.append("ERROR: Can't extract field "+fieldCopy.fullName +" as extractor byte copy instructions are exhausted\n");
                            }
                        }else{
                            L.info(fieldCopy.fullName + ":" + fieldCopy.sourceBit + ", " + fieldCopy.size + ", " + destBit);
                            sb.append("field = " + fieldCopy.fullName + ", Source bit in input Packet = " + fieldCopy.sourceBit + ", Dest bit in PHV = " + destBit + "\n");
                            bitCount++;
                            if(bitCount<=QPConstants.NUMCBIT) {
                                extractor.getYbit().addItem(destBit);
                                extractor.getXbit().addItem(fieldCopy.sourceBit);
                                extractor.getVbit().addItem(1);
                                extractor.getCbit().addItem(0);
                            }else{
                                L.error("Can't extract field "+fieldCopy.fullName +" as extractor bit copy instructions are exhausted");
                                sb.append("ERROR: Can't extract field "+fieldCopy.fullName +" as extractor bit copy instructions are exhausted\n");
                            }

                        }
                    }
                }
                L.debug("Number of fields to extract for "+ects.get(0).getHeaderActualName()+" = "+byteCount);
                if(byteCount> QPConstants.NUMCBYT){
                    L.warn("NUMCBYT exceeds for "+ects.get(0).getHeaderActualName()+","+byteCount);
                }
                if(bitCount> QPConstants.NUMCBIT){
                    L.warn("NUMCBIT exceeds for "+ects.get(0).getHeader()+","+bitCount);
                }
                extractor.padAll();
                extractor.emitAll(QPExtractorConfigUnit.schDirName(OutputDir.getAbsolutePath(), "ex"));
                sb.append("============\n");
                sb.append(s.getName()+"["+map.get(s).getStateID()+"]\n");
                sb.append(extractor.summary());
                sb.append("============\n");


                ParserRow.writeParserFile(OutputDir, "ex", "extractor_config.txt", sb.toString());
            }
        }
        L.info("Generating Signature configs");
        emitSignatureConfig(graph, OutputDir, outer,map);
        L.info("Done Generating Signature configs");

    }

    private static void emitSignatureConfig(Graph<P4State> graph, File outputDir, Set<String> outer, Map<P4State, StateMeta> map) {
        P4State startState = graph.getNodes().stream().filter(x -> x.getName().equals("start")).findFirst().get();
        P4State endState = graph.getNodes().stream().filter(x -> x.getName().equals("accept")).findFirst().get();
        Map<Integer, Boolean> isAluNeeded = new LinkedHashMap<>();
        ParserAluConfigUnit palu = new ParserAluConfigUnit();
        palu.pad2D();
        for(P4State s: graph.getNodes()){
            if(s.getExtracts().size()>0 && s.getExtracts().get(0).isALUneeded()){
                L.info("State "+ s.getName()+ " needs alu");
                int stateId = map.get(s).getStateID();
                isAluNeeded.put(stateId, true);
                ParserALUOp parserALUOp = s.getExtracts().get(0).getParserALUOp();
                palu.getSres_alui_opcode().setItem(parserALUOp.getOpCode(), stateId, 0);
                palu.getSres_alui_op0().setItem(parserALUOp.getOffset(), stateId, 0);
                palu.getSres_alui_mask().setItem(parserALUOp.getMask(), stateId, 0);
                palu.getSres_alui_op1().setItem(parserALUOp.getA(), stateId, 0);
                palu.getSres_alui_op2().setItem(parserALUOp.getB(), stateId, 0);
            }
        }

        palu.emitAll(ParserAluConfigUnit.schDirName(outputDir.getAbsolutePath(), "sig"));



        List<List<P4State>> allPaths = graph.allpaths(startState, endState);
        Set<List<Utils.Pair<Integer, Integer>>> outr = new LinkedHashSet<>();

        Set<List<Utils.Pair<Integer, Integer>>> innr = new LinkedHashSet<>();
        for(List<P4State> p:allPaths){
            List<P4State> path = p.stream().filter(x -> !x.getName().equals("start")).collect(Collectors.toList());

            //su.padAll();
            List<P4State> o = path.stream().filter(x-> outer.contains(x.getName())).collect(Collectors.toList());
            List<Utils.Pair<Integer, Integer>> oi = o.stream().map(x -> Utils.Pair.of(map.get(x).getStateID(), map.get(x).getTotalTransitionsInBytes())).collect(Collectors.toList());
            outr.add(oi);

            List<P4State> i = path.stream().filter(x-> !outer.contains(x.getName())).collect(Collectors.toList());
            List<Utils.Pair<Integer, Integer>> ii = i.stream().map(x -> Utils.Pair.of(map.get(x).getStateID(), map.get(x).getTotalTransitionsInBytes())).collect(Collectors.toList());
            innr.add(ii);
            //su.emitAll(SignatureConfigUnit.schDirName(outputDir.getAbsolutePath(), "sig"));
        }

        SignatureConfigUnit su = new SignatureConfigUnit();
        su.pad2D();
        int count  = 0;
        for(List<Utils.Pair<Integer, Integer>> o : outr){
            for(int k=0; k<o.size();k++){
                su.getOutr().setItem(o.get(k).first(), count, k);
                su.getOutrsz().setItem(o.get(k).second(), count, k);
                if(isAluNeeded.containsKey(o.get(k).first())){
                    su.getOutrvbit().setItem(1, count, k);
                }

            }
            su.getOutrcnt().setItem(o.size(), count, 0);
            count++;
        }
        count = 0;
        for(List<Utils.Pair<Integer, Integer>> i : innr){
            for(int k=0; k<i.size();k++){
                su.getInnr().setItem(i.get(k).first(), count, k);
                su.getInnrsz().setItem(i.get(k).second(), count, k);
                if(isAluNeeded.containsKey(i.get(k).first())){
                    su.getInnrvbit().setItem(1, count, k);
                }
            }
            su.getInnrcnt().setItem(i.size(), count, 0);
            count++;
        }
        su.emitAll(SignatureConfigUnit.schDirName(outputDir.getAbsolutePath(), "sig"));

        L.info(""+outr.size());
        L.info(""+innr.size());
    }

    private static void emitParserStats(Graph<P4State> graph, Map<P4State, StateMeta> map, File OutputDir, boolean dump){
        File parserStatsFile = new File(OutputDir+File.separator+"pa"+File.separator+"parser_stats.txt");
        FileUtils.AppendToFile(parserStatsFile, "State ID/Valid Locations\n");
        FileUtils.AppendToFile(parserStatsFile, "===============\n");
        List<P4State> sorted = graph.topologicalSort();
        P4State startState = graph.getNodes().stream().filter(x -> x.getName().equals("start")).findFirst().get();
        P4State endState = graph.getNodes().stream().filter(x -> x.getName().equals("accept")).findFirst().get();
        List<List<P4State>> allPaths = graph.allpaths(startState, endState);
        if (false) {
            for( int skew = 0; skew <8; skew++) {
                L.info("Skew Size = "+skew);
                File f1 = new File(OutputDir+File.separator+"pa"+File.separator+"g1_skew_"+skew+"_1.txt");
                File f2 = new File(OutputDir+File.separator+"pa"+File.separator+"g1_skew_"+skew+"_2.txt");
                Set<P4State> group1 = new LinkedHashSet<>();
                Set<P4State> group2 = new LinkedHashSet<>();
                int c = 0;
                for (P4State s : sorted) {
                    if (c < (sorted.size() / 2 - skew))
                        group1.add(s);
                    else
                        group2.add(s);
                    c++;
                }
                FileUtils.AppendToFile(f1, Utils.summary(", ", group1));
                FileUtils.AppendToFile(f1, "\n***********\n");
                FileUtils.AppendToFile(f2, Utils.summary(", ", group2));
                FileUtils.AppendToFile(f2, "\n***********\n");
                //L.info(Utils.summary("\n", group1));
                //L.info(Utils.summary("\n", group2));


                Set<List<P4State>> comb1 = new LinkedHashSet<>();
                Set<List<P4State>> comb2 = new LinkedHashSet<>();
                for (List<P4State> path : allPaths) {
                    List<P4State> l1 = new ArrayList<>();
                    List<P4State> l2 = new ArrayList<>();
                    boolean second = false;
                    for (P4State s : path) {
                        if (group1.contains(s)) {
                            if (second)
                                throw new RuntimeException();
                            l1.add(s);
                        } else if (group2.contains(s)) {
                            l2.add(s);
                            second = true;
                        } else
                            throw new RuntimeException();

                    }
                    comb1.add(l1);
                    comb2.add(l2);
                }
                L.info("Set 1 size = " + comb1.size());
                FileUtils.AppendToFile(f1, Utils.summary("\n", comb1));

                L.info("Set 2 size = " + comb2.size());
                FileUtils.AppendToFile(f2, Utils.summary("\n", comb2));
            }
        }

        for(P4State s: sorted){
            StateMeta sm = map.get(s);
            if(s.getExtracts()!=null && !s.getExtracts().isEmpty()){
                FileUtils.AppendToFile(parserStatsFile, s.getExtracts().get(0).getHeader()+ "[State Id = " +sm.getStateID()+", validLoc = "+s.getExtracts().get(0).getValidLoc()
                        +", Max Size = "+ s.getExtracts().get(0).getTotalSize()+", Min Size = "+ s.getExtracts().get(0).getMinSize()+"]\n");
            }
        }





        FileUtils.AppendToFile(parserStatsFile, "===============\n");

        FileUtils.AppendToFile(parserStatsFile, "Paths\n");
        FileUtils.AppendToFile(parserStatsFile, "===============\n");
        FileUtils.AppendToFile(parserStatsFile, "Total Edges = "+graph.getEdges().size()+"\n");
        FileUtils.AppendToFile(parserStatsFile, "Total Paths = "+ allPaths.size()+"\n");
        FileUtils.AppendToFile(parserStatsFile, "Longest Path Size = "+ allPaths.stream().reduce( (x, y) -> x.size() > y.size()? x : y).get().size()+"\n");
        FileUtils.AppendToFile(parserStatsFile, "Shortest Path Size = "+ allPaths.stream().reduce( (x, y) -> x.size() < y.size()? x : y).get().size()+"\n");
        FileUtils.AppendToFile(parserStatsFile, "Longest Packet Size = "+ new GraphPath(allPaths.stream().reduce( (x, y) -> new GraphPath(x).getSize() > new GraphPath(y).getSize()? x : y).get()).getSize()+" bits\n");
        FileUtils.AppendToFile(parserStatsFile, "Shortest Packet Size = "+ new GraphPath(allPaths.stream().reduce( (x, y) -> new GraphPath(x).getSize() < new GraphPath(y).getSize()? x : y).get()).getSize()+" bits\n");
        FileUtils.AppendToFile(parserStatsFile, "===============\n");
        int count = 0;
        StringBuilder sb = new StringBuilder();
        if(dump) {
            for (List<P4State> path : allPaths) {
                GraphPath gp = new GraphPath(path);
                sb.append("[Id = " + count + ", Hops = " + gp.hops() + ", Min = " + gp.getMinSize() +", Max = "+ gp.getSize() +"] " + gp.sum(map) + "\n");
                //FileUtils.AppendToFile(parserStatsFile, "[Id = " + count + ", Hops = " + gp.hops() + ", Min = " + gp.getMinSize() +", Max = "+ gp.getSize() +"] " + gp.sum(map) + "\n");
                count++;
            }
            FileUtils.AppendToFile(parserStatsFile, sb.toString());
        }
    }

    @Data
    private static class GraphPath{
        private final List<P4State> path;
        private String sum(Map<P4State, StateMeta> map){
            StringBuilder sb = new StringBuilder();
            List<Utils.Pair<String, Integer>> m = path.stream().map(x -> Utils.Pair.of(x.getName(), map.get(x).getStateID())).collect(Collectors.toList());
            return Utils.summary("->", m);
        }
        public int getSize(){
            int ret = 0;
            for(P4State s: path){
                if(s.getExtracts()!=null && s.getExtracts().size()>0){
                    ret+=s.getExtracts().get(0).getTotalSize();
                }
            }
            return ret;
        }
        public int getMinSize(){
            int ret = 0;
            for(P4State s: path){
                if(s.getExtracts()!=null && s.getExtracts().size()>0){
                    ret+=s.getExtracts().get(0).getMinSize();
                }
            }
            return ret;
        }
        public int hops(){
            return path.size();
        }
    }


}
