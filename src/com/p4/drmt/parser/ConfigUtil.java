package com.p4.drmt.parser;

import com.p4.drmt.cfg.DRMTConstants;
import com.p4.drmt.parser.ExtractorLogic.EXB;
import com.p4.drmt.parser.StateMeta.DM;
import com.p4.p416.expressions.ParserALUOp;
import com.p4.p416.gen.AbstractBaseExt;
import com.p4.p416.gen.ExtractMethodCallContextExt;
import com.p4.p416.gen.P416Parser;
import com.p4.pktgen.PacketGenerator;
import com.p4.tools.graph.Graph;
import com.p4.utils.FileUtils;
import com.p4.utils.Utils;
import com.p4.utils.Utils.fn1;

import lombok.Data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.BufferOverflowException;
import java.util.*;
import java.util.stream.Collectors;

public class ConfigUtil {
	protected static final Logger L = LoggerFactory.getLogger(ConfigUtil.class);
	//Emitting parser and deparser rows to be used by Config Generator.
	private static List<ParserRow> parserrows = new ArrayList<>();
	//private static List<ParserRow> deparserrows = new ArrayList<>();

	//Temp. extractor de-extractor rows to be used to emit current Config.
    private static ExtractorConfigUnit extractor = new ExtractorConfigUnit();
    private static ExtractorConfigUnit deextractor = new ExtractorConfigUnit();
     

    //Getter for parser rows
	public static List<ParserRow> getParserRows(){

	    return Collections.unmodifiableList(parserrows);
    }
    //Getter for deparser rows.
    /*public static List<ParserRow> getDeparserRows(){
	    return Collections.unmodifiableList(deparserrows);
    }*/
    //Getter for extractor
    public static ExtractorConfigUnit getExtractorObj(){
	    return extractor;
    }
    //Getter for de extractor
    public static ExtractorConfigUnit getDeextractorObj(){
	    return deextractor;
    }
    //Collection for Holding consolidated extractor Configurations
    private static List<ExtractorConfigUnit> exList = new ArrayList<ExtractorConfigUnit>();
    //Collection for Holding consolidated de extractor configurations.
	private static List<ExtractorConfigUnit> dexList = new ArrayList<ExtractorConfigUnit>();
    //Getter for Collection List consolidated extractor configurations.
    public static List<ExtractorConfigUnit> getExList(){
        return Collections.unmodifiableList(exList);
    }
    //Getter for Collection List consolidated dextractor configurations.
    public static List<ExtractorConfigUnit> getDexList(){
        return Collections.unmodifiableList(dexList);
    }

    //Init Extractor and Dextractor Handle.
    private static void init(){

        extractor = new ExtractorConfigUnit();
        deextractor = new ExtractorConfigUnit();
	}

    public static void gatherAllStates(P4Headers header, Graph<P4State> graph, List<Utils.Pair<String, Integer>> isValidInstances, AbstractBaseExt abstractBaseExt){
		final Map<P4State, StateMeta> map = new LinkedHashMap<>();
		for(P4State st:graph.getNodes()){
			map.put(st, new StateMeta());
		}
		List<P4State> dummyStates = calculateDummyStates(map, isValidInstances, abstractBaseExt, header);
		for(P4State st: dummyStates){
			map.put(st, new StateMeta());
		}
		StateMeta.assignStateIds(map);
		StateMeta.assignExtractBytes(map,header);
		StateMeta.assignSelectsAndTransitions(map,header);
		for(P4State p4s : map.keySet())
			PacketGenerator.allStates.add(p4s);
    }
    

	public static void generateParserRows(String parsername, P4Headers header, Graph<P4State> graph, File OutputDir, Utils.Pair<Integer, Integer> min_max, List<Utils.Pair<String, Integer>> isValidInstances, AbstractBaseExt abstractBaseExt){
		final Map<P4State, StateMeta> map = new LinkedHashMap<>();
		for(P4State st:graph.getNodes()){
			map.put(st, new StateMeta());
		}
		List<P4State> dummyStates = calculateDummyStates(map, isValidInstances, abstractBaseExt, header);
		for(P4State st: dummyStates){
			map.put(st, new StateMeta());
		}
		//List<P4State> dummyStates = calculateDummyStates();
		//Populate MetaFields for compiler
		StateMeta.assignStateIds(map);
		StateMeta.assignExtractBytes(map,header);
		StateMeta.assignSelectsAndTransitions(map,header);
		for(P4State p4s : map.keySet())
			PacketGenerator.allStates.add(p4s);



		//
		List<ParserRow> rows = new ArrayList<>();
		List<ParserRow> dprows = new ArrayList<>();
		StringBuilder sb = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();
		L.info("Starting Parser config Generation");
		//DeparserAtom.deparserHelper(graph, map);
		//DPH dph = DeparserAtom.deparserAnalysis(graph, map);
		for(P4State s: graph.topologicalSort()){
			
			for(P4State d:graph.adj(s)){
				ParserRow row = new ParserRow();
				//ParserRow dprow = new ParserRow();
				//TCAM 
				List<DM> transitions = map.get(s).getTransitions().get(d.getName());
				if(transitions==null || transitions.isEmpty()){
					transitions = StateMeta.getDefaultTransition();
				}
				row.setTcam_vld(FW.ONE);
				row.setTcam_mask_flag(FW.ZERO_BYTES);
				row.setTcam_mask_curr(FW.ZERO_BYTES);
				row.setTcam_mask_key(DM.onlyMask(transitions));
				row.setTcam_data_flag(FW.ZERO_BYTES);
				row.setTcam_data_curr(new FW(map.get(s).getStateID(),7));
				row.setTcam_data_key(DM.onlyData(transitions));
				//SRAM
				row.setSram_done((d.getName().equals("accept")||d.getName().equals("reject"))?
						FW.ONE:
						FW.ZERO);
				
				row.setSram_next(new FW(map.get(d).getStateID(), 7));
				row.setSram_outr(FW.ZERO);
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
				List<DM> extracts = map.get(d).getExtracts();
				if(extracts==null||extracts.size()==0){//Should only happen for final 
					extracts = StateMeta.getDefaultTransition();
				}
				List<FW> doffs1 = Utils.map(new fn1<FW, FW>() {
					@Override
					public FW invoke(FW p1) {
						return new FW(p1.getValue(), 8);
					}
				}, DM.onlyData(extracts));
				
				row.setSram_doff(doffs1);
				row.setSram_dvld(DM.onlyValid(extracts));
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
		ParserRow.writeParserFile(OutputDir, "pa", "parser_config.txt", sb.toString());

		emitParserStats(graph, dummyStates, map, OutputDir);
		
		L.info("Starting Extractor config Generation");
		Comparator<P4State> c = new Comparator<P4State>() {
			@Override
			public int compare(P4State o1, P4State o2) {
				Integer oi1 = map.get(o1).getStateID();
				Integer oi2 = map.get(o2).getStateID();
				return oi1.compareTo(oi2);
			}
		};

		DeparserConfigUnit dpc = new DeparserConfigUnit();
		int deparserCount = 0;
		dpc.getSram_min_max().addItem(min_max.first());
		int max = min_max.second()+1;
		dpc.getSram_min_max().addItem(max);
		dpc.padAll();
		List<P4State> nodes = graph.topologicalSort();
		nodes.addAll(dummyStates);
		nodes = nodes.stream().filter(x -> x.getExtracts().size()>0).collect(Collectors.toList());
		nodes.sort(new Comparator<P4State>() {
			@Override
			public int compare(P4State o1, P4State o2) {
				return Integer.compare(o1.getExtracts().get(0).getValidLoc(), o2.getExtracts().get(0).getValidLoc());
			}
		});
		for(P4State s: nodes){
			List<P4Extract> exts = s.getExtracts();
			if(!exts.isEmpty()){
				P4Extract aluExtract = map.get(s).getALUExtract();
				dpc.getSram_next().addItem(map.get(s).getStateID(),deparserCount);
				if (aluExtract == null) {
					dpc.getSres_alue().addItem(0, deparserCount);
					dpc.getSram_shift().addItem(new FW(map.get(s).getTotalTransitionsInBytes(), 9), deparserCount);
				} else {
					dpc.getSres_alue().addItem(1, deparserCount);
					dpc.getSram_shift().addItem(new FW(0, 9), deparserCount);
					ParserALUOp aluOp = aluExtract.getParserALUOp();
					dpc.getSres_alui_op0().addItem(aluOp.getDpOffset(), deparserCount);
					dpc.getSres_alui_mask().addItem(aluOp.getMask(), deparserCount);
					dpc.getSres_alui_op1().addItem(aluOp.getA(), deparserCount);
					dpc.getSres_alui_op2().addItem(aluOp.getB(), deparserCount);
					dpc.getSres_alui_opcode().addItem(aluOp.getOpCode(), deparserCount);
				}
				deparserCount++;
			}
		}
		dpc.getSram_done().addItem(FW.ONE, deparserCount);
		dpc.getSram_next().addItem(new FW(DRMTConstants.ACCEPT_STATE, 7), deparserCount);
		dpc.emitAll(OutputDir+File.separator+DeparserConfigUnit.dirName);

		sb = new StringBuilder();
		sb1 = new StringBuilder();
		nodes = graph.customSort(c);
		nodes.addAll(dummyStates);
		for(P4State s: nodes){
			List<P4Extract> exts = s.getExtracts();
			List<P4Assign> assigns = s.getAssigns();
			if(!exts.isEmpty() || !assigns.isEmpty()){
				List<P4Extract> ects = map.get(s).getTransitionExtracts();
				init();
				L.debug("************************************************************");
				List<SourceDestinationSize> fields = P4Extract.getFields(ects);
				L.debug("Header: "+ects.get(0).getHeaderActualName());
				int count = 0;
				for(SourceDestinationSize fieldCopy :fields){
					if(fieldCopy.emit) {
						L.debug("field = " + fieldCopy.fullName);
						L.info("field = " + fieldCopy.fullName + " = " + fieldCopy.destBit + "," + fieldCopy.sourceBit);
						sb.append("field = " + fieldCopy.fullName + ", Source bit in input Packet = " + fieldCopy.destBit + ", Dest bit in PHV = " + fieldCopy.sourceBit+"\n");
						//ga.getMap().put(fieldCopy.fullName, Pair.of(fieldCopy.destBit, fieldCopy.size));
						boolean isExtractor = true;
						List<EXB> s2d = ExtractorLogic.getSMDOffsetsBE(fieldCopy.sourceBit, fieldCopy.destBit, fieldCopy.size, isExtractor);
						count += s2d.size();
						L.debug(Utils.summary(s2d));
						extractor.getYbyt().addItems(EXB.onlyYbyt(s2d));
						extractor.getXbyt().addItems(EXB.onlyXbyt(s2d));
						extractor.getVbyt().addItems(EXB.onlyVld(s2d));
						extractor.getXsft().addItems(EXB.onlySft(s2d));
						extractor.getXdir().addItems(EXB.onlyDir(s2d));
						extractor.getYmsk().addItems(EXB.onlyMask(s2d));
						//Reverse
						isExtractor=!isExtractor;
						s2d = ExtractorLogic.getSMDOffsetsBE(fieldCopy.destBit, fieldCopy.sourceBit, fieldCopy.size, isExtractor);
						L.debug(Utils.summary(s2d));
						deextractor.getYbyt().addItems(EXB.onlyYbyt(s2d));
						deextractor.getXbyt().addItems(EXB.onlyXbyt(s2d));
						deextractor.getVbyt().addItems(EXB.onlyVld(s2d));
						deextractor.getXsft().addItems(EXB.onlySft(s2d));
						deextractor.getXdir().addItems(EXB.onlyDir(s2d));
						deextractor.getYmsk().addItems(EXB.onlyMask(s2d));
					}
				}
				L.info("Number of fields to extract for "+ects.get(0).getHeaderActualName()+" = "+count);
				if(count> DRMTConstants.NUMCBYT){
					throw new BufferOverflowException();
				}
				for(P4Extract p: ects){
					extractor.getXbit().addItem(1);
					extractor.getCbit().addItem(1);
					extractor.getVbit().addItem(1);
					extractor.getYbit().addItem(p.getValidLoc());

					//for last bit in dpa

					extractor.getXbit().addItem(1);
					extractor.getCbit().addItem(1);
					extractor.getVbit().addItem(1);
					extractor.getYbit().addItem(max);
				}
				
				extractor.padAll();
				extractor.emitAll(ExtractorConfigUnit.schDirName(OutputDir.getAbsolutePath(), "ex"));
				deextractor.padAll();
				deextractor.emitAll(ExtractorConfigUnit.schDirName(OutputDir.getAbsolutePath(), "dex"));
				exList.add(extractor);
				dexList.add(deextractor);
				sb.append("============\n");
				sb.append(s.getName()+"["+map.get(s).getStateID()+"]\n");
				sb.append(extractor.summary());
				sb.append("============\n");
				
				sb1.append("============\n");
				sb1.append(s.getName()+"["+map.get(s).getStateID()+"]\n");
				sb1.append(deextractor.summary());
				sb1.append("============\n");
			}
		}
		ParserRow.writeParserFile(OutputDir, "ex", "extractor_config.txt", sb.toString());
		ParserRow.writeParserFile(OutputDir, "dex", "dextractor_config.txt", sb1.toString());
	}

	private static List<P4State> calculateDummyStates(Map<P4State, StateMeta> map, List<Utils.Pair<String,Integer>> isValidInstances, AbstractBaseExt abstractBaseExt, P4Headers header) {
    	List<P4State> ret = new ArrayList<>();
    	for(Utils.Pair<String,Integer> v:isValidInstances){

    		boolean found  = false;
    		for(P4State s:map.keySet()){
    			if(s.getExtracts().size()>0 && s.getExtracts().get(0).getValidField().equals(v.first())){
    				found =true;
				}
			}
			if(!found){
				L.info("Found header which is not in the parser"+Utils.summary(v));
				P4State p = new P4State("_dummy_parse"+v.first());
				String headerName = v.first().replaceAll(".isValid", "");
				P4Extract e = ExtractMethodCallContextExt.getP4Extract(headerName, abstractBaseExt, header);
				p.getExtracts().add(e);
				ret.add(p);
			}
		}



    	return ret;
	}

	private static void emitParserStats(Graph<P4State> graph, List<P4State> dummystates, Map<P4State, StateMeta> map, File OutputDir){
		File parserStatsFile = new File(OutputDir+File.separator+"pa"+File.separator+"parser_stats.txt");
		FileUtils.AppendToFile(parserStatsFile, "State ID/Valid Locations\n");
		FileUtils.AppendToFile(parserStatsFile, "===============\n");
		List<P4State> nodes = graph.topologicalSort();
		nodes.addAll(dummystates);
		for(P4State s: nodes){
			StateMeta sm = map.get(s);
			if(s.getExtracts()!=null && !s.getExtracts().isEmpty()){
				FileUtils.AppendToFile(parserStatsFile, s.getExtracts().get(0).getHeader()+ "[State Id = " +sm.getStateID()+", validLoc = "+s.getExtracts().get(0).getValidLoc()
						+", Max Size = "+ s.getExtracts().get(0).getTotalSize()+", Min Size = "+ s.getExtracts().get(0).getMinSize()+"]\n");
			}
		}
		P4State startState = graph.getNodes().stream().filter(x -> x.getName().equals("start")).findFirst().get();
		P4State endState = graph.getNodes().stream().filter(x -> x.getName().equals("accept")).findFirst().get();
		FileUtils.AppendToFile(parserStatsFile, "===============\n");
		List<List<P4State>> allPaths = graph.allpaths(startState, endState);
		FileUtils.AppendToFile(parserStatsFile, "Paths\n");
		FileUtils.AppendToFile(parserStatsFile, "===============\n");
		FileUtils.AppendToFile(parserStatsFile, "Total Paths = "+ allPaths.size()+"\n");
		FileUtils.AppendToFile(parserStatsFile, "Longest Path Size = "+ allPaths.stream().reduce( (x, y) -> x.size() > y.size()? x : y).get().size()+"\n");
		FileUtils.AppendToFile(parserStatsFile, "Shortest Path Size = "+ allPaths.stream().reduce( (x, y) -> x.size() < y.size()? x : y).get().size()+"\n");
		FileUtils.AppendToFile(parserStatsFile, "Longest Packet Size = "+ new GraphPath(allPaths.stream().reduce( (x, y) -> new GraphPath(x).getSize() > new GraphPath(y).getSize()? x : y).get()).getSize()+" bits\n");
		FileUtils.AppendToFile(parserStatsFile, "Shortest Packet Size = "+ new GraphPath(allPaths.stream().reduce( (x, y) -> new GraphPath(x).getSize() < new GraphPath(y).getSize()? x : y).get()).getSize()+" bits\n");
		FileUtils.AppendToFile(parserStatsFile, "===============\n");
		int count = 0;
		for(List<P4State> path: allPaths ){
			GraphPath gp = new GraphPath(path);
			FileUtils.AppendToFile(parserStatsFile, "[Id = "+count+", Hops = "+gp.hops()+", Min = "+ gp.getMinSize()+"] "+gp.sum(map)+"\n");
			count++;
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

	public static void main(String[] args) {
		for(int i:ByteUtils.calcByteParts(4)){
			System.out.println(i);
		}
		//System.out.println(Utils.summary(calcParts(4)));
	}
}
