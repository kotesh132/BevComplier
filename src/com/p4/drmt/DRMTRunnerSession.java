package com.p4.drmt;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.p4.drmt.cfg.CFGBuildingBlock;
import com.p4.drmt.cfg.CFGMap;
import com.p4.drmt.cfg.DRMTConstants;
import com.p4.drmt.cfg.KeyMeta;
import com.p4.drmt.cnst.ConstConfig;
import com.p4.drmt.hints.Hints;
import com.p4.drmt.ilp.IDrmtScheduleSolverSolution;
import com.p4.drmt.ilp.ScheduleSolver;
import com.p4.drmt.memoryManager.IMemoryInstance;
import com.p4.drmt.memoryManager.MemoryInstance;
import com.p4.drmt.memoryManager.MemoryManager;
import com.p4.drmt.parser.*;
import com.p4.drmt.schd.DFGHelper;
import com.p4.drmt.schd.NewScheduler;
import com.p4.drmt.struct.IUnit;
import com.p4.drmt.struct.MANode;
import com.p4.drmt.struct.graph.DFGBuilder;
import com.p4.p416.applications.CFGNode;
import com.p4.p416.applications.IMemoryRequest;
import com.p4.p416.applications.Symbol;
import com.p4.p416.applications.Type;
import com.p4.p416.archModel.GraphBuilderVisitor;
import com.p4.p416.gen.*;
import com.p4.p416.gen.P416Parser.P4programContext;
import com.p4.p416.iface.IParserState;
import com.p4.packetgen.runner.CommandLineParser;
import com.p4.packetgen.runner.P416ParserUtil;
import com.p4.pktgen.P4Blocks;
import com.p4.pktgen.PacketGenerator;
import com.p4.pktgen.SOMUtils;
import com.p4.pktgen.config.Config;
import com.p4.pktgen.model.SOMModel;
import com.p4.proto.pi.Pi;
import com.p4.tools.graph.Edge;
import com.p4.tools.graph.Graph;
import com.p4.utils.FileUtils;
import com.p4.utils.Utils;
import com.p4.utils.Utils.fn1;

import lombok.Data;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Data
public class DRMTRunnerSession {
	private static final Logger L = LoggerFactory.getLogger(DRMTRunnerSession.class);

	public final CommandLineParser cp;

	public void run12(){
        Hints hints = null;
        try {
            if(cp.getCompilerHints() !=null){
                InputStream is = FileUtils.getInputStream(cp.getCompilerHints());
                if(is != null) {
                    ObjectMapper mapper = new ObjectMapper();
                    mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    hints = new Hints(mapper.readValue(is, Hints.UnNormalized.class));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		P416ParserUtil mp = new P416ParserUtil();
		File dir = cp.getOutputDir();
		List<File> allFiles = FileUtils.allFilesInDir(dir.toString(), ".p4");
		MemoryManager.outputFile = new File(cp.getOutputFile()+File.separator+"memory.txt");
		for(File f :allFiles){
			if (cp.isRunSemanticChecks()){
				this.runSemanticChecks(f);
			}
			String text = FileUtils.readFileIntoString(f,"\n");
			P4programContext ctx = mp.getP416Context(text, f.getName());
            StandardTransforms.runStandard(ctx, cp, f);
			//TODO Fix this so we can detect these fields automatically
            Set<IMemoryInstance> extraInstances = new LinkedHashSet<>();
            for(String extraField :hints.getCompilerHints().getParser_extra_fields()){
            	Symbol instance = AbstractBaseExt.getExtendedContext(ctx).resolve(extraField);
                extraInstances.add(new MemoryInstance(extraField, (Type)instance));
            }

			MemoryManager.allocateMemory(P4programContextExt.getExtendedContext(ctx), null, extraInstances);
			MemoryManager.printMemoryAllocated();

			//ctx.extendedContext.transform2SSA();
			//L.info(ctx.extendedContext.getSSAFormattedText());
			//L.debug("======================================");
			//List<String> lstarr =ctx.extendedContext.getUsedStructFields();
			// print used struct fields
			//L.debug(Utils.summary(" => " + lstarr));

			//Set<IMemoryRequest> symbolSet = new LinkedHashSet<>();
			/*ctx.extendedContext.resetMemoryBuffer();
			ctx.extendedContext.preAllocateGlobalAddress(symbolSet);
			ctx.extendedContext.getMemoryManager().assignAddress(symbolSet);

			ctx.extendedContext.allocateGlobalAddress();
			*/
			Map<String,AbstractBaseExt> constants = P4programContextExt.getExtendedContext(ctx).getConstants();
			for(Entry<String,AbstractBaseExt> entry: constants.entrySet()) {
				L.debug(entry.getValue().getFormattedText() +"\t" + entry.getValue().getAlignedByteOffset());
			}

			P4programContextExt.getExtendedContext(ctx).setIds(new AtomicInteger(1), new AtomicInteger(1), new AtomicInteger(1), new AtomicInteger(1));
			//ctx.extendedContext.printInstruction();
			final CFGMap cfgmap = CFGMap.noInline();
			L.info("Building CFG");
			P4programContextExt.getExtendedContext(ctx).buildNGetCFG(cfgmap);
			List<String> cnodes = Utils.newArrList();
			L.info("Getting Program Order");
			P4programContextExt.getExtendedContext(ctx).invokeProgramFlow(cnodes);
			L.debug(Utils.summary(cnodes));
			cnodes = Utils.filter(new fn1<Boolean, String>() {
				@Override
				public Boolean invoke(String p1) {
					return cfgmap.getCfgmap().containsKey(p1);
				}
			}, cnodes);
			L.debug(Utils.summary(cnodes));

			CFGBuildingBlock  s = cfgmap.getCfgmap().get(cnodes.get(0));
			for(int i=1;i<cnodes.size();i++){
				s = CFGBuildingBlock.linkDisjoint(s, cfgmap.getCfgmap().get(cnodes.get(i)));
			}
			Graph<CFGNode> g = s.getGraph();
			if(g.containsSelfLoops()){
				L.info("Contains self loops");
			}
			List<CFGNode> nodes = g.topologicalSort();
			List<MANode> maNodes = DFGHelper.getMANodes(nodes);
			List<Set<IUnit>> parallelNodes = DFGHelper.filterMANodes(maNodes);
			CFGNode start = s.getMarker().getStart();
			CFGNode end = null;
			if(s.getMarker().getEnds().size()!=1){
				throw new IllegalStateException();
			}else{
				for(CFGNode n: s.getMarker().getEnds()){
					end = n;
				}
			}

			ConstConfig constConfig = new ConstConfig(AbstractBaseExt.getConstants());
			constConfig.emitAll(cp.getOutputDir().getAbsolutePath());

			//TEST NEW WAY OF CONSTRUCTING GRAPH
			List<ControlDeclarationContextExt> gNodes = new ArrayList<>();
			for(String cbName:cnodes){
				gNodes.addAll(((P4programContextExt) P4programContextExt.getExtendedContext(ctx)).getMatchingControlBlock(cbName));
			}

			Map<String,ControlDeclarationContextExt> controlBlocks  = new HashMap<String,ControlDeclarationContextExt>();
			P4programContextExt.getExtendedContext(ctx).getControlBlocks(controlBlocks);
			KeyMeta km = new KeyMeta();
			P4programContextExt.getExtendedContext(ctx).setKeys(km);
			L.info("KeyMeta = "+km.summary());

			//create SOM model instance
			Config config = null;
			try {
				InputStream is = FileUtils.getInputStream(cp.getPcfgFile());
				if(is != null) {
					ObjectMapper mapper = new ObjectMapper();
					mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					config = new Config(mapper.readValue(is, Config.UnNormalized.class));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			P4Blocks.createInstance(controlBlocks, km);
			SOMModel.createInstance(config.getSomConfig(), config.getCacheConfig());

			//NewScheduler.scheduleTest(gNodes, cp);
			//NewScheduler.buildNaiveSchedule(parallelNodes,cp);
			//NewScheduler

			//END
			int first=0, last=0;
			//ILP Solver
			List<String> isValidInstances = new ArrayList<>();
			if (cp.isUseIlp()) {
				Set<Edge<IUnit>> dfgEdges = new DFGBuilder().buildDFG(maNodes);
				IDrmtScheduleSolverSolution solution = ScheduleSolver.ilpSolveForPacketRate(dfgEdges, new HashSet<>(maNodes), DRMTConstants.matchDelay, DRMTConstants.actionDelay);
				//Consume solution further as needed
				MemoryManager.allocateMemory(P4programContextExt.getExtendedContext(ctx), solution.getSchedule(), extraInstances);
				MemoryManager.printMemoryAllocated();
				String fst = MemoryManager.fds.getFirstLastisValidInstances().first();
				Symbol fs = ctx.extendedContext.resolve(fst);
				first = MemoryManager.getOffset(fst, (Type)fs);
				String lst = MemoryManager.fds.getFirstLastisValidInstances().second();
				Symbol ls = ctx.extendedContext.resolve(lst);
				last = MemoryManager.getOffset(lst, (Type)ls);
				//ctx.extendedContext.defineSymbol(null);
				isValidInstances = MemoryManager.fds.allIsValidInstances();
				NewScheduler.buildSolverSchedule(solution.getSchedule(), cp);
			}else{
				throw new UnsupportedOperationException("Greedy Scheduler is not supported. Please use -useilp flag");
			}
			List<Utils.Pair<String,Integer>> instances = isValidInstances.stream().map(x -> Utils.Pair.of(x, MemoryManager.getOffset(x, (Type) ctx.extendedContext.resolve(x)))).collect(Collectors.toList());

			//End ILP Solver

			//L.info("Building Schedule");
			//ScheduleRunner.loopSchedule(nodes, cp.getOutputDir().getAbsolutePath());
			L.debug("********************");
			L.info("Building Headers for Parser Config");
			P4Headers hdrs = new P4Headers();
			P4programContextExt.getExtendedContext(ctx).buildTypes(hdrs);
			L.info("Generating keygen configs");

			KeyRow.WriteKeygenConfig(km, cp.getOutputDir());
			km.getKeysSchedule(KeyMeta.schedule);
			P4Parsers parsers = new P4Parsers();
			P4programContextExt.getExtendedContext(ctx).buildParsers(hdrs, parsers);

			//AluComplex aluComp = new AluComplex();
			GlobalAddress ga = new GlobalAddress();
			//AluMap aluMap = new AluMap();

			for(P4Parser parser:parsers.getAllParsers()){
				L.debug("*************");
				L.debug(parser.getName());
				L.debug("*************");
				Graph<P4State> graph = parser.getParseGraph();

				//ConfigUtil.gatherAllStates(hdrs, graph, instances, ctx.extendedContext);
				ConfigUtil.generateParserRows(parser.getName(),hdrs,graph, cp.getOutputDir(), Utils.Pair.of(first, last), instances, ctx.extendedContext);
				L.info("Done generating parser rows");
				//PacketUtils.generatePktNTblCfg(ctx.extendedContext, cp.getOutputDir(), cp.isUsebmv(), graph, g.allpaths(start, end), controlBlocks, hdrs, null, km, ScheduleRunner.getTableToActionInstPtrMap(), null,ScheduleRunner.getTableToCycleMap());
			}
			//aluComp.emitAll(cp.getOutputDir().getAbsolutePath());
			//aluMap.emitAll(cp.getOutputDir().getAbsolutePath());
			L.info("Done with Generating ALU configs");
			List<String> fields = hdrs.flattenAllTypes();
			System.out.println(fields.size());
//			ctx.extendedContext.listGlobalAddresses(ga, fields);
//			GlobalAddress.writeJSON(cp.getOutputDir(), "phvOffsets.json", ga.getFields());
			//ctx.extendedContext.printInstruction();
			L.info("*************************");
			SimScript.emitRunScript(cp.getOutputDir().getAbsolutePath());
			//StepperUtils.emitJson(cp.getOutputDir(), fields, ga, nodes, parsers, controlBlocks);
			
			if (cp.isP4Info()){
				// Kotesh: P4 Info Emit File
				Pi.P4Info.Builder p4InfoBuilder = Pi.P4Info.newBuilder();
				P4programContextExt.getExtendedContext(ctx).emitP4Info(p4InfoBuilder);
				Pi.P4Info p4info = p4InfoBuilder.build();
				try {
					L.info("Emitting p4 info");
					File p4File =  new File(cp.getOutputDir() +"/p4info.txt");
					FileUtils.WriteFile(p4File, JsonFormat.printer().print(p4info));
				} catch (InvalidProtocolBufferException ex){
					L.error(ex.getMessage());
				}
			}
           // DrmtConfigGenerator gen = new DrmtConfigGenerator(km);
			//gen.start();
			// End P4 Info
		}
	}

	public void run10(){
		P416ParserUtil mp = new P416ParserUtil();
		File dir = cp.getOutputDir();
		List<File> allFiles = FileUtils.allFilesInDir(dir.toString(), ".p4");
		for(File f :allFiles){
			String text = FileUtils.readFileIntoString(f,"\n");
			P4programContext ctx = mp.getP416Context(text, f.getName());
			Map<String,ControlDeclarationContextExt> controlBlocks  = new HashMap<String,ControlDeclarationContextExt>();
			P4programContextExt.getExtendedContext(ctx).getControlBlocks(controlBlocks);
			((P4programContextExt) P4programContextExt.getExtendedContext(ctx)).flatten(controlBlocks);
			File file = new File(dir+"/unrolled_"+f.getName());
			FileUtils.WriteFile(file, P4programContextExt.getExtendedContext(ctx).getFormattedText());
		}

	}

	//Semantic Checks
	public void run11(){
		P416ParserUtil mp = new P416ParserUtil();
		File dir = cp.getOutputDir();
		List<File> allFiles = FileUtils.allFilesInDir(dir.toString(), ".p4");
		for(File f :allFiles){
			String text = FileUtils.readFileIntoString(f,"\n");
			P4programContext ctx = mp.getP416Context(text, f.getName());
			L.info("Running semantic checks on "+f.getName());
			P4programContextExt.getExtendedContext(ctx).runSemanticChecks();
			L.info("Completed semantic checks on "+f.getName());
		}
	}

	public void inlining(){

		P416ParserUtil mp = new P416ParserUtil();
		File dir = cp.getOutputDir();
		List<File> allFiles = FileUtils.allFilesInDir(dir.toString(), ".p4");
		for(File f :allFiles){
			String text = FileUtils.readFileIntoString(f,"\n");
			P4programContext ctx = mp.getP416Context(text, f.getName());
			P4programContextExt.getExtendedContext(ctx).defineSymbol(null);

			List<String> pasrerStatesInUnion = null;
			Hints hints = null;
			try {
				if(cp.getCompilerHints() !=null){
					InputStream is = FileUtils.getInputStream(cp.getCompilerHints());
					if(is != null) {
						ObjectMapper mapper = new ObjectMapper();
						mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
						hints = new Hints(mapper.readValue(is, Hints.UnNormalized.class));
						pasrerStatesInUnion = hints.getCompilerHints().getInlining();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			GraphBuilderVisitor graphBuilderVisitor = new GraphBuilderVisitor();
			graphBuilderVisitor.visit(ctx);
			List<List<IParserState>> loopPaths = graphBuilderVisitor.getLoopPaths();

			P4programContextExt.getExtendedContext(ctx).inlineParserStates(null, loopPaths, pasrerStatesInUnion);
			System.out.println(P4programContextExt.getExtendedContext(ctx).getFormattedText());
		}
	}


	public void initializeSOMModel() {
		P416ParserUtil mp = new P416ParserUtil();
		File dir = cp.getOutputDir();
		List<File> allFiles = FileUtils.allFilesInDir(dir.toString(), ".p4");

		Config config = null;

		try {
			InputStream is = FileUtils.getInputStream(cp.getPcfgFile());
			if(is != null) {
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				config = new Config(mapper.readValue(is, Config.UnNormalized.class));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		for(File f :allFiles){
			String text = FileUtils.readFileIntoString(f,"\n");
			P4programContext ctx = mp.getP416Context(text, f.getName());
			AtomicReference<AbstractBaseExt> scope = new AtomicReference<AbstractBaseExt>();
			scope.set(P4programContextExt.getExtendedContext(ctx));
			P4programContextExt.getExtendedContext(ctx).defineSymbol(null);

			Set<IMemoryRequest> symbolSet = new LinkedHashSet<>();
			P4programContextExt.getExtendedContext(ctx).resetMemoryBuffer();
			P4programContextExt.getExtendedContext(ctx).preAllocateGlobalAddress(symbolSet);
			P4programContextExt.getExtendedContext(ctx).getMemoryManager().assignAddress(symbolSet);
			P4programContextExt.getExtendedContext(ctx).allocateGlobalAddress();
			//ctx.extendedContext.flattenIfStatements();
			//Map<String,AbstractBaseExt> constants = ctx.extendedContext.getConstants();
			/*for(Entry<String,AbstractBaseExt> entry: constants.entrySet()) 
			{
				L.info(entry.getValue().getFormattedText() +"\t" + entry.getValue().getAlignedByteOffset());
			}*/
			P4programContextExt.getExtendedContext(ctx).setIds(new AtomicInteger(1), new AtomicInteger(1), new AtomicInteger(1), new AtomicInteger(1));
			//ctx.extendedContext.printInstruction();
			final CFGMap cfgmap = CFGMap.noInline();
			P4programContextExt.getExtendedContext(ctx).buildNGetCFG(cfgmap);
			List<String> cnodes = Utils.newArrList();
			P4programContextExt.getExtendedContext(ctx).invokeProgramFlow(cnodes);
			L.info(Utils.summary(cnodes));
			cnodes = Utils.filter(new fn1<Boolean, String>() {
				@Override
				public Boolean invoke(String p1) {
					return cfgmap.getCfgmap().containsKey(p1);
				}
			}, cnodes);
			L.info(Utils.summary(cnodes));
			CFGBuildingBlock  s = cfgmap.getCfgmap().get(cnodes.get(0));
			for(int i=1;i<cnodes.size();i++){
				s = CFGBuildingBlock.linkDisjoint(s, cfgmap.getCfgmap().get(cnodes.get(i)));
			}
			Graph<CFGNode> g = s.getGraph();
			if(g.containsSelfLoops()){
				L.info("Contains self loops");
			}
			List<CFGNode> nodes = g.topologicalSort();
			CFGNode start = s.getMarker().getStart();
			CFGNode end = null;
			if(s.getMarker().getEnds().size()!=1){
				throw new IllegalStateException();
			}else{
				for(CFGNode n: s.getMarker().getEnds()){
					end = n;
				}
			}
			L.info("********************");
			P4Headers hdrs = new P4Headers();
			P4programContextExt.getExtendedContext(ctx).buildTypes(hdrs);
			KeyMeta km = new KeyMeta();
			P4programContextExt.getExtendedContext(ctx).setKeys(km);
			P4Parsers parsers = new P4Parsers();
			P4programContextExt.getExtendedContext(ctx).buildParsers(hdrs, parsers);

			Map<String,ControlDeclarationContextExt> controlBlocks  = new HashMap<String,ControlDeclarationContextExt>();
			P4programContextExt.getExtendedContext(ctx).getControlBlocks(controlBlocks);

			//P4Blocks p4blocks = new P4Blocks(controlBlocks, km);
			P4Blocks.createInstance(controlBlocks, km);
			SOMModel.createInstance(config.getSomConfig(), config.getCacheConfig());
			//SOMModel somModel = SOMModel.getInstance();
			//SOMUtils.placeTablesInSOMs();

		}

	}

	public void generateSOMConfigs(){
		Hints hints = null;
		try {
			if(cp.getCompilerHints() !=null){
				InputStream is = FileUtils.getInputStream(cp.getCompilerHints());
				if(is != null) {
					ObjectMapper mapper = new ObjectMapper();
					mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
					hints = new Hints(mapper.readValue(is, Hints.UnNormalized.class));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		P416ParserUtil mp = new P416ParserUtil();
		File dir = cp.getOutputDir();
		List<File> allFiles = FileUtils.allFilesInDir(dir.toString(), ".p4");
		for(File f :allFiles){
			String text = FileUtils.readFileIntoString(f,"\n");
			P4programContext ctx = mp.getP416Context(text, f.getName());
			AtomicReference<AbstractBaseExt> scope = new AtomicReference<AbstractBaseExt>();
			scope.set(P4programContextExt.getExtendedContext(ctx));
			P4programContextExt.getExtendedContext(ctx).defineSymbol(null);

			Set<IMemoryRequest> symbolSet = new LinkedHashSet<>();
			P4programContextExt.getExtendedContext(ctx).resetMemoryBuffer();

            //remove switch
//            Stack<ExpressionContextExt> stack = new Stack<>();
//            List<StatementOrDeclarationContextExt> lists = new ArrayList<>();
//            P4programContextExt.getExtendedContext(ctx).removeSwitch(lists, stack);

            P4programContextExt.getExtendedContext(ctx).preAllocateGlobalAddress(symbolSet);
			P4programContextExt.getExtendedContext(ctx).getMemoryManager().assignAddress(symbolSet);
			P4programContextExt.getExtendedContext(ctx).allocateGlobalAddress();
			P4programContextExt.getExtendedContext(ctx).flattenIfStatements();
            
			Set<IMemoryInstance> extraInstances = new LinkedHashSet<>();
			for(String extraField :hints.getCompilerHints().getParser_extra_fields()){
				Symbol instance = P4programContextExt.getExtendedContext(ctx).resolve(extraField);
				extraInstances.add(new MemoryInstance(extraField, (Type)instance));
			}
            MemoryManager.clear();
            MemoryManager.allocateMemory(P4programContextExt.getExtendedContext(ctx), null, extraInstances);
            MemoryManager.printMemoryAllocated();

            Map<String,AbstractBaseExt> constants = P4programContextExt.getExtendedContext(ctx).getConstants();
			for(Entry<String,AbstractBaseExt> entry: constants.entrySet()) 
			{
				L.info(entry.getValue().getFormattedText() +"\t" + entry.getValue().getAlignedByteOffset());
			}
			P4programContextExt.getExtendedContext(ctx).setIds(new AtomicInteger(1), new AtomicInteger(1), new AtomicInteger(1), new AtomicInteger(1));
			//ctx.extendedContext.printInstruction();
			final CFGMap cfgmap = CFGMap.noInline();
			P4programContextExt.getExtendedContext(ctx).buildNGetCFG(cfgmap);
			List<String> cnodes = Utils.newArrList();
			P4programContextExt.getExtendedContext(ctx).invokeProgramFlow(cnodes);
			L.info(Utils.summary(cnodes));
			cnodes = Utils.filter(new fn1<Boolean, String>() {
				@Override
				public Boolean invoke(String p1) {
					return cfgmap.getCfgmap().containsKey(p1);
				}
			}, cnodes);
			L.info(Utils.summary(cnodes));
			CFGBuildingBlock  s = cfgmap.getCfgmap().get(cnodes.get(0));
			for(int i=1;i<cnodes.size();i++){
				s = CFGBuildingBlock.linkDisjoint(s, cfgmap.getCfgmap().get(cnodes.get(i)));
			}
			Graph<CFGNode> g = s.getGraph();
			if(g.containsSelfLoops()){
				L.info("Contains self loops");
			}
			List<CFGNode> nodes = g.topologicalSort();
			CFGNode start = s.getMarker().getStart();
			CFGNode end = null;
			if(s.getMarker().getEnds().size()!=1){
				throw new IllegalStateException();
			}else{
				for(CFGNode n: s.getMarker().getEnds()){
					end = n;
				}
			}
			L.info("********************");
			P4Headers hdrs = new P4Headers();
			P4programContextExt.getExtendedContext(ctx).buildTypes(hdrs);
			KeyMeta km = new KeyMeta();
			P4programContextExt.getExtendedContext(ctx).setKeys(km);
			P4Parsers parsers = new P4Parsers();
			P4programContextExt.getExtendedContext(ctx).buildParsers(hdrs, parsers);
			Map<String,ControlDeclarationContextExt> controlBlocks  = new HashMap<String,ControlDeclarationContextExt>();
			P4programContextExt.getExtendedContext(ctx).getControlBlocks(controlBlocks);
			Map<String, ActionDeclarationContextExt> actions = new HashMap<String, ActionDeclarationContextExt>();
			Map<String,TableDeclarationContextExt> temp_tables = new HashMap<String, TableDeclarationContextExt>();
			for (Map.Entry<String,ControlDeclarationContextExt> controlEntry : controlBlocks.entrySet()) {
				L.info(controlEntry.getKey());
				controlEntry.getValue().getTables(temp_tables);
				controlEntry.getValue().getActions(actions);
			}

			for(P4Parser parser:parsers.getAllParsers()){
				L.info("*************");
				L.info(parser.getName());
				L.info("*************");
				Graph<P4State> graph = parser.getParseGraph();
				List<String> fields = hdrs.flattenAllTypes();
				Map<String, Integer> fieldSizes = new HashMap<String, Integer>();
				
				for(String field : fields) {
					MemoryInstance mi = new MemoryInstance(field, (Type) P4programContextExt.getExtendedContext(ctx).resolve(field));
					fieldSizes.put(field, mi.getType().getSizeInBits());
				}
				PacketGenerator pgen = new PacketGenerator();
				pgen.setConfigFile(cp.getPcfgFile());
				//pgen.setConfigFile(new File("/Users/sauppala/work/p4tree/sauppala_i_t2/sw/p4/p4jc/resources/pgen_config.json"));
				pgen.setParseGraph(graph);
				pgen.setControlBlocks(controlBlocks);
				pgen.setControlFlowPaths(g.allpaths(start, end));
				pgen.setKm(km);
				pgen.setActionsInstMap(NewScheduler.getTableToActionInstPtrMap());
				pgen.setOutputDir(cp.getOutputDir());
				pgen.setHeaders(hdrs);
				pgen.setPktFieldSizes(fieldSizes);
				//pgen.setAllHeaderValidFields(validBitOffsets);
				pgen.run();
				//PacketUtils.generatePktNTblCfg(ctx.extendedContext, cp.getOutputDir(), cp.getPacketsPerPath(), cp.isUsebmv(), graph, g.allpaths(start, end), controlBlocks, hdrs, null, km, ScheduleRunner.getTableToActionInstPtrMap(), null,ScheduleRunner.getTableToCycleMap());
			}

			L.info("*************************");
		}
	}

	public void generateIlpSchedule() {
		SOMUtils.getTableDetails(NewScheduler.getDisjointTables());
		SOMUtils.getSomSpec();
	}
	
	public void runSemanticChecks(File f){
		P416ParserUtil mp = new P416ParserUtil();
		String text = FileUtils.readFileIntoString(f,"\n");
		P4programContext ctx = mp.getP416Context(text, f.getName());
		L.info("Running semantic checks on "+f.getName());
		P4programContextExt.getExtendedContext(ctx).runSemanticChecks();
		L.info("Completed semantic checks on "+f.getName());
	}
}
