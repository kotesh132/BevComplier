package com.p4.tools.graph.runner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.drmt.parser.P4Headers;
import com.p4.drmt.parser.P4Parser;
import com.p4.drmt.parser.P4Parsers;
import com.p4.drmt.parser.P4State;
import com.p4.drmt.Transitions;
import com.p4.drmt.cfg.KeyMeta;
import com.p4.drmt.parser.ConfigUtil;
import com.p4.drmt.parser.DumbSchedule;
import com.p4.drmt.parser.KeyRow;
import com.p4.p416.gen.P416Parser.P4programContext;
import com.p4.p416.gen.P4programContextExt;
import com.p4.packetgen.runner.CommandLineParser;
import com.p4.packetgen.runner.P416ParserUtil;
import com.p4.tools.graph.DrawGraph;
import com.p4.tools.graph.Edge;
import com.p4.tools.graph.Graph;
import com.p4.tools.graph.structs.ControlMetaData;
import com.p4.tools.graph.structs.Node;
import com.p4.tools.graph.structs.StructUtil;
import com.p4.tools.graph.structs.TableMap;
import com.p4.utils.FileUtils;
import com.p4.utils.Utils;

import lombok.Data;
@Data
public class GraphRunnerSession {
	private static final Logger L = LoggerFactory.getLogger(GraphRunnerSession.class);

	public final CommandLineParser cp;

	public void run(){
		P416ParserUtil mp = new P416ParserUtil();
		File dir = cp.getOutputDir();
		List<File> allFiles = FileUtils.allFilesInDir(dir.toString(), ".p4");

		for(File f :allFiles){
			String text = FileUtils.readFileIntoString(f,"\n");
			P4programContext ctx = mp.getP416Context(text, f.getName());
			TableMap tm = new TableMap();
			P4programContextExt.getExtendedContext(ctx).buildST(tm);
			System.out.println(tm.tmToString());
			Set<Edge<Node>> edges = new HashSet<>();
			for(ControlMetaData cm: tm.getProcessed()){

				StructUtil.populateEdges(cm.cg.root, edges);
				if(edges.size()>0)
					System.out.println(Utils.summary(edges));
			}
			DrawGraph.draw(edges, cp.getOutputDir().getAbsolutePath());
		}
	}

	public void run2(){
		P416ParserUtil mp = new P416ParserUtil();
		File dir = cp.getOutputDir();
		List<File> allFiles = FileUtils.allFilesInDir(dir.toString(), ".p4");
		for(File f :allFiles){
			String text = FileUtils.readFileIntoString(f,"\n");
			P4programContext ctx = mp.getP416Context(text, f.getName());
			Transitions tm = new Transitions();
			P4programContextExt.getExtendedContext(ctx).populateParser(tm);
			System.out.println(tm.summary());
			DrawGraph.draw(tm.getEdges(), cp.getOutputDir().getAbsolutePath());
			Graph<String> g = new Graph<>(tm.getEdges());
			System.out.println(g.containsSelfLoops());
			Graph<String> g1 = g.removeSelfLoops();
			System.out.println(g1.getNodes().size());
			System.out.println(g1.getEdges().size());
			if(!g1.hasCycle()){
				List<List<String>> paths =  g1.allpaths("start", "accept");
				File op = new File(cp.getOutputDir()+File.separator+"paths.txt");
				FileUtils.Delete(op, false);
				for(List<String> p:paths ){
					FileUtils.AppendToFile(op, Utils.summary(p));
					FileUtils.AppendToFile(op, "\n");
				}
			}
		}
	}

	public void run3(){
		P416ParserUtil mp = new P416ParserUtil();
		File dir = cp.getOutputDir();
		List<File> allFiles = FileUtils.allFilesInDir(dir.toString(), ".p4");
		DumbSchedule.writeScheduleDat(cp.getOutputDir());
		for(File f :allFiles){
			String text = FileUtils.readFileIntoString(f,"\n");
			P4programContext ctx = mp.getP416Context(text, f.getName());
			P4Headers hdrs = new P4Headers();
			P4programContextExt.getExtendedContext(ctx).defineSymbol(null);
			P4programContextExt.getExtendedContext(ctx).allocateGlobalAddress();
			P4programContextExt.getExtendedContext(ctx).setIds(new AtomicInteger(1), new AtomicInteger(1), new AtomicInteger(1), new AtomicInteger(1));
			KeyMeta km = new KeyMeta();
			P4programContextExt.getExtendedContext(ctx).setKeys(km);
			KeyRow.WriteKeygenConfig(km, cp.getOutputDir());
			P4programContextExt.getExtendedContext(ctx).buildTypes(hdrs);
			//System.out.println(hdrs.summary());
			P4Parsers parsers = new P4Parsers();
			P4programContextExt.getExtendedContext(ctx).buildParsers(hdrs, parsers);
			for(P4Parser parser:parsers.getAllParsers()){
				L.info("*************");
				L.info(parser.getName());
				L.info("*************");
				/*for(P4State state: parser.getStates()){
					L.info(state.getName());
				}
				L.info("------------");
				Graph<P4State> graph = parser.getParseGraph();
				List<P4State> toposort = graph.topologicalSort();
				for(P4State s: toposort){
					System.out.println(s.getName());
				}
				Set<Edge<P4State>> edges = graph.topoEdges();
				for(Edge<P4State> edge:edges){
					System.out.println(edge.s.getName()+"->"+edge.d.getName());
				}*/
				Graph<P4State> graph = parser.getParseGraph();
				ConfigUtil.generateParserRows(parser.getName(),hdrs,graph, cp.getOutputDir(), Utils.Pair.of(0,0), new ArrayList<>(), ctx.extendedContext);

			}
			//ctx.extendedContext.encode();

		}
	}

	public void run4(){
		/*
		P416ParserUtil mp = new P416ParserUtil();
		File dir = cp.getOutputDir();
		List<File> allFiles = FileUtils.allFilesInDir(dir.toString(), ".p4");
		for(File f :allFiles){
			String text = FileUtils.readFileIntoString(f,"\n");
			P4programContext ctx = mp.getP416Context(text, f.getName());
			//P4Headers hdrs = new P4Headers();
			AtomicReference<AbstractBaseExt> scope = new AtomicReference<AbstractBaseExt>();
			scope.set(ctx.extendedContext);
			ctx.extendedContext.defineSymbol_depricated(scope);
			ctx.extendedContext.allocateGlobalAddress();
			ctx.extendedContext.setIds(new AtomicInteger(1), new AtomicInteger(1), new AtomicInteger(1), new AtomicInteger(1));

			Map<String,ControlDeclarationContextExt> controlBlocks  = new HashMap<String,ControlDeclarationContextExt>();
			ctx.extendedContext.getControlBlocks(controlBlocks);
			Map<String, ActionDeclarationContextExt> actions = new HashMap<String, ActionDeclarationContextExt>();
			Map<String,TableDeclarationContextExt> temp_tables = new HashMap<String, TableDeclarationContextExt>();
			for (Map.Entry<String,ControlDeclarationContextExt> controlEntry : controlBlocks.entrySet()) {
				System.out.println(controlEntry.getKey());
				controlEntry.getValue().getTables(temp_tables);
				controlEntry.getValue().getActions(actions);
			}

			Map<Integer,TableDeclarationContextExt> tables = new HashMap<Integer, TableDeclarationContextExt>();
			for(Entry<String,TableDeclarationContextExt> entry : temp_tables.entrySet())
			{
				tables.put(entry.getValue().getTableId(), entry.getValue());
			}


			CompilerFileUtils compilerFileUtils = new CompilerFileUtils();
			for (Map.Entry<String,ControlDeclarationContextExt> controlEntry : controlBlocks.entrySet()) {

				List<TableDeclarationContextExt> tableApplySequence = controlEntry.getValue().getTableApplySequence();
				if(tableApplySequence != null && tableApplySequence.size() > 0){
					for (int i=0; i< tableApplySequence.size(); i++)
					{
						if(tableApplySequence.get(i) != null)
						{
							//Table Definition is must for instruction config generation.
							compilerFileUtils.addEntry(tableApplySequence.get(i),controlEntry.getValue());
						}
					}
				}
			}
			//compilerFileUtils.writeToFiles(dir);
			compilerFileUtils.writeToFilesUsingDummyScheduler(dir,actions,tables);
		}
		*/
	}
	
	public void run5(){
	/*
		P416ParserUtil mp = new P416ParserUtil();
		File dir = cp.getOutputDir();
		List<File> allFiles = FileUtils.allFilesInDir(dir.toString(), ".p4");
		DumbSchedule.writeScheduleDat(cp.getOutputDir());
		for(File f :allFiles){
			String text = FileUtils.readFileIntoString(f,"\n");
			P4programContext ctx = mp.getP416Context(text, f.getName());
			P4Headers hdrs = new P4Headers();
			AtomicReference<AbstractBaseExt> scope = new AtomicReference<AbstractBaseExt>();
			scope.set(ctx.extendedContext);
			ctx.extendedContext.defineSymbol_depricated(scope);
			ctx.extendedContext.allocateGlobalAddress();
			ctx.extendedContext.setIds(new AtomicInteger(1), new AtomicInteger(1), new AtomicInteger(1), new AtomicInteger(1));

			KeyMeta km = new KeyMeta();
			ctx.extendedContext.setKeys(km);
			KeyRow.WriteKeygenConfig(km, cp.getOutputDir());
			ctx.extendedContext.buildTypes(hdrs);
			//System.out.println(hdrs.summary());
			P4Parsers parsers = new P4Parsers();
			ctx.extendedContext.buildParsers(hdrs, parsers);
			Map<String,Integer[]> selectOffsetsMap = new HashMap<String,Integer[]>();
			ctx.extendedContext.setSelectOffsets(selectOffsetsMap);
			Map<String,List<Integer>> actionsAndDataMap = new HashMap<String,List<Integer>>();
			ctx.extendedContext.populateTableActionsAndData(actionsAndDataMap);
			Map<String,TableMeta> tableAndActionsMap = new HashMap<String, TableMeta>();
			ctx.extendedContext.populateTableActionsMap(tableAndActionsMap);
			
			Map<String,ControlDeclarationContextExt> controlBlocks  = new HashMap<String,ControlDeclarationContextExt>();
			ctx.extendedContext.getControlBlocks(controlBlocks);
			Map<String, ActionDeclarationContextExt> actions = new HashMap<String, ActionDeclarationContextExt>();
			Map<String,TableDeclarationContextExt> temp_tables = new HashMap<String, TableDeclarationContextExt>();
			for (Map.Entry<String,ControlDeclarationContextExt> controlEntry : controlBlocks.entrySet()) {
				System.out.println(controlEntry.getKey());
				controlEntry.getValue().getTables(temp_tables);
				controlEntry.getValue().getActions(actions);
			}

			Map<Integer,TableDeclarationContextExt> tables = new HashMap<Integer, TableDeclarationContextExt>();
			for(Entry<String,TableDeclarationContextExt> entry : temp_tables.entrySet())
			{
				tables.put(entry.getValue().getTableId(), entry.getValue());
			}


			CompilerFileUtils compilerFileUtils = new CompilerFileUtils();
			for (Map.Entry<String,ControlDeclarationContextExt> controlEntry : controlBlocks.entrySet()) {

				List<TableDeclarationContextExt> tableApplySequence = controlEntry.getValue().getTableApplySequence();
				if(tableApplySequence != null && tableApplySequence.size() > 0){
					for (int i=0; i< tableApplySequence.size(); i++)
					{
						if(tableApplySequence.get(i) != null)
						{
							//Table Definition is must for instruction config generation.
							compilerFileUtils.addEntry(tableApplySequence.get(i),controlEntry.getValue());
						}
					}
				}
			}
			//compilerFileUtils.writeToFiles(dir);
			compilerFileUtils.writeToFilesUsingDummyScheduler(dir,actions,tables);
			
			for(P4Parser parser:parsers.getAllParsers()){
				L.info("*************");
				L.info(parser.getName());
				L.info("*************");
				Graph<P4State> graph = parser.getParseGraph();
				ConfigUtil.generateParserRows(parser.getName(),hdrs,graph, cp.getOutputDir());
				PacketUtils.generatePacketsAndTableConfig(cp.getOutputDir(), graph, km, selectOffsetsMap, actionsAndDataMap, tableAndActionsMap, compilerFileUtils.getActionToInstPtrMap(), compilerFileUtils.getCycleToTableMap(), compilerFileUtils.getTableToCycleMap(), hdrs);
			}
		}
		*/
	}

	
	public void run7(){
		/*
		P416ParserUtil mp = new P416ParserUtil();
		File dir = cp.getOutputDir();
		List<File> allFiles = FileUtils.allFilesInDir(dir.toString(), ".p4");
		for(File f :allFiles){
			String text = FileUtils.readFileIntoString(f,"\n");
			P4programContext ctx = mp.getP416Context(text, f.getName());
			AtomicReference<AbstractBaseExt> scope = new AtomicReference<AbstractBaseExt>();
			scope.set(ctx.extendedContext);
			ctx.extendedContext.defineSymbol_depricated(scope);
			ctx.extendedContext.setIds(new AtomicInteger(1), new AtomicInteger(1), new AtomicInteger(1), new AtomicInteger(1));

			final CFGMap cfgmap = CFGMap.noInline();
			ctx.extendedContext.buildNGetCFG(cfgmap);
			List<String> cnodes = Utils.newArrList();
			ctx.extendedContext.invokeProgramFlow(cnodes);
			cnodes = Utils.filter(new fn1<Boolean, String>() {
				@Override
				public Boolean invoke(String p1) {
					return cfgmap.getCfgmap().containsKey(p1);
				}
			}, cnodes);
			CFGBuildingBlock  s = cfgmap.getCfgmap().get(cnodes.get(0));
			for(int i=1;i<cnodes.size();i++){
				s = CFGBuildingBlock.linkDisjoint(s, cfgmap.getCfgmap().get(cnodes.get(i)));
			}
			Graph<CFGNode> g = s.getGraph();
			System.out.println(s.getMarker().getEnds().size());
			CFGNode start = s.getMarker().getStart();
			CFGNode end = null;
			if(s.getMarker().getEnds().size()!=1){
				throw new IllegalStateException();
			}else{
				for(CFGNode n: s.getMarker().getEnds()){
					end = n;
				}
			}
			List<CFGNode> nodes =g.allpaths(start, end).get(0); 
			for(CFGNode pn:nodes ){
				if(pn.isTableApplyNode()){
					TableDeclarationContextExt t = (TableDeclarationContextExt) pn.getTableApplyNode();
					System.out.println(pn.cFGNodeSummary()+", TableId = "+t.getTableId());
				}
					
			}
		}
		*/
	}
}
