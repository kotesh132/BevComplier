package com.p4.pktgen;

import java.io.File;
import java.io.InputStream;
import java.util.*;

import com.p4.quadpeaks.parser.ParserConfigUtil;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import lombok.NoArgsConstructor;
import lombok.Setter;

import com.p4.drmt.parser.ConfigUtil;
import com.p4.drmt.parser.P4Assign;
import com.p4.drmt.parser.P4Extract;
import com.p4.drmt.parser.P4Headers;
import com.p4.drmt.parser.P4State;
import com.p4.drmt.parser.SourceDestinationSize;
import com.p4.drmt.cfg.KeyMeta;
import com.p4.p416.applications.CFGNode;
import com.p4.p416.applications.ExpressionNode;
import com.p4.p416.gen.ActionDeclarationContextExt;
import com.p4.p416.gen.AssignmentStatementContextExt;
import com.p4.p416.gen.CallWithoutTypeArgsContextExt;
import com.p4.p416.gen.ControlDeclarationContextExt;
import com.p4.p416.gen.ExpressionContextExt;
import com.p4.p416.gen.TableDeclarationContextExt;
import com.p4.packetgen.Packet;
import com.p4.packetgen.PacketMeta;
import com.p4.pktgen.config.Config;
import com.p4.pktgen.model.SOMModel;
import com.p4.pktgen.p4blocks.ActionBlock;
import com.p4.pktgen.p4blocks.ActionParams;
import com.p4.pktgen.p4blocks.ControlBlock;
import com.p4.pktgen.p4blocks.TableBlock;
import com.p4.pktgen.p4blocks.TableKey;
import com.p4.tools.graph.Graph;
import com.p4.utils.FileUtils;
import com.p4.utils.Utils;
import com.p4.utils.Utils.Pair;

@NoArgsConstructor
@Setter
public class PacketGenerator {
	public static Set<P4State> allStates = new LinkedHashSet<>();

	private File outputDir;
	private File configFile;
	private Graph<P4State> parseGraph;
	private List<List<CFGNode>> controlFlowPaths;
	private Map<String,ControlDeclarationContextExt> controlBlocks;
	private P4Headers headers;
	//private Map<String,Integer[]> selectOffsetsMap;
	private KeyMeta km;
	private Map<Integer,Map<String, Integer>> actionsInstMap;
	//private Map<Integer, Set<Integer>> cycleToTableMap;
	//private Map<Integer, Integer> tableToCycleMap;\
	private Map<String, Integer> pktFieldSizes;
	private List<Pair<Pair<String,String>, Pair<String,String>>> packets;
	private List<List<Pair<Integer,Integer>>> packetHeaderInfo;
	private List<Pair<List<Pair<String,String>>,List<Pair<String,String>>>> pktFieldValues;
	private Integer instructionPtrLen;
	private Map<String,Integer> allHeaderValidFields;
	
	private Map<Integer,Integer> tableKeySize = new HashMap<Integer,Integer>();
	private Map<Integer,Set<BitSet>> tableRandomValues = new HashMap<Integer,Set<BitSet>>();
	private Map<Integer,Set<BitSet>> tableFixedValues = new HashMap<Integer,Set<BitSet>>();
	
	private TableSpace tableSpace;
	
	Emitter emitter;
	
	public void run() {
		if(configFile == null) {
			throw new RuntimeException("Config file is not initialized.");
		}
		
		Config config = null;
		
		try {
			InputStream is = FileUtils.getInputStream(configFile);
			if(is != null) {
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				config = new Config(mapper.readValue(is, Config.UnNormalized.class));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		packets = new ArrayList<Pair<Pair<String,String>, Pair<String,String>>>();
		packetHeaderInfo = new ArrayList<List<Pair<Integer,Integer>>>();
		pktFieldValues = new ArrayList<Pair<List<Pair<String,String>>,List<Pair<String,String>>>>();
		allHeaderValidFields = new HashMap<String, Integer>();
		//P4Blocks p4blocks = new P4Blocks(controlBlocks, km);
		P4Blocks p4blocks = P4Blocks.getInstance();
		P4Parser p4parser = new P4Parser(parseGraph);
		SOMModel.createInstance(config.getSomConfig(),config.getCacheConfig());
		SOMModel somModel = SOMModel.getInstance();
		emitter = new Emitter(outputDir, somModel);
		//SOMModel somModel = new SOMModel(config.getSomConfig());
		//instructionPtrLen = getPtrLen();
		instructionPtrLen = config.getSomConfig().get(0).getInstPtrNumBits() != null ? config.getSomConfig().get(0).getInstPtrNumBits() : getPtrLen();
		
		tableSpace = new TableSpace(p4blocks, actionsInstMap, instructionPtrLen);
		
		if(!somModel.isTablesPlaced()) {
			for(Map.Entry<String, ControlBlock> controlEntry : p4blocks.getControlBlocksExtracted().entrySet()) {
				for(Map.Entry<String, TableBlock> tableEntry : controlEntry.getValue().getTablesExtracted().entrySet()) {
					
					if(actionsInstMap.get(tableEntry.getValue().getTableId()) == null) {
						System.out.println("Skipping SOM alloction for table " + tableEntry.getValue().getTableName());
						continue;
					}
					
					int maxSize = 0;
					for(String action: tableEntry.getValue().getTableActions()) {
						int actionDataSize = controlEntry.getValue().getActionsExtracted().get(action).getTotalWidthOfParams() + instructionPtrLen;
						if(actionDataSize > maxSize)
							maxSize = actionDataSize;
					}
					tableKeySize.put(tableEntry.getValue().getTableId(), tableEntry.getValue().getTotalWidthOfKeys());
					int somId = somModel.findSOMForTable(tableEntry.getValue().getTableKeys(), maxSize, tableEntry.getValue().getTableId());
					
					//System.out.println("som id for table " + tableEntry.getValue().getTableId() + " is " + somId);
					somModel.addTableToSOM(somId, tableEntry.getValue().getTableId(), tableEntry.getValue().getTableKeys(), null, maxSize, null, null, 0);
				}
			}
		}
		List<List<P4State>> acceptedPacketTypes =  p4parser.getParserAcceptedPacketTypes(config.getPktConfig());
		PacketEmitter pktemit = new PacketEmitter(outputDir);
		generatePackets(config, somModel, acceptedPacketTypes, p4blocks, p4parser, pktemit);
		if(!config.getPktConfig().isFullCoverage())
			addTableEntries(p4blocks, somModel);
		if(!config.getPktConfig().getParserOnlyPkts()) 
			emitter.emitModelConfig();
	}
	
	private void generatePackets(Config config, SOMModel somModel, List<List<P4State>> acceptedPacketTypes, P4Blocks p4blocks, P4Parser p4parser, PacketEmitter pktemit) {
		

		int maxPacketsPerPath = config.getPktConfig().getPacketsPerPath();
		
		Map<Integer, ArrayList<Pair<String,Pair<Integer,Integer>>>> allHeaderFieldOffsetsAndSizes = new HashMap<Integer, ArrayList<Pair<String,Pair<Integer,Integer>>>>();
		Map<Integer, P4State> allheaders = new HashMap<Integer, P4State>();
		Map<String, Integer> validLocationRemap = new HashMap<String, Integer>();
		int maxHeaderSize = 1000;
		
		for(P4State state : getAllStates()) {
			if(state.getExtracts() != null && state.getExtracts().size()>0) {
				if(allHeaderFieldOffsetsAndSizes.get(state.getName()) == null) {
					ArrayList<Pair<String, Pair<Integer, Integer>>> headerOffsetsAndSizes = new ArrayList<Pair<String, Pair<Integer, Integer>>>();
					for(SourceDestinationSize sds : state.getExtracts().get(0).getFields()) {
						headerOffsetsAndSizes.add(Pair.of(sds.getFullName(), Pair.of(state.getExtracts().get(0).getValidLoc() * maxHeaderSize + sds.getSourceBit(), sds.getSize())));
					}
					//System.out.println(state.getExtracts().get(0).getHeader() +" : "+state.getExtracts().get(0).getValidLoc());
					allHeaderValidFields.put(state.getExtracts().get(0).getValidField(), state.getExtracts().get(0).getValidLoc());
					validLocationRemap.put(state.getExtracts().get(0).getHeader(), state.getExtracts().get(0).getValidLoc());
					allHeaderFieldOffsetsAndSizes.put(state.getExtracts().get(0).getValidLoc(), headerOffsetsAndSizes);
					//headerId++;
				}
				allheaders.put(state.getExtracts().get(0).getValidLoc(), state);
			}
		}
		
		int finalHeaderOffset = (config.getPktConfig().getBitVectorLength()+1) * maxHeaderSize;
		ArrayList<Pair<String, Pair<Integer, Integer>>> headerOffsetsAndSizes = new ArrayList<Pair<String, Pair<Integer, Integer>>>();
		for(String field : pktFieldSizes.keySet()) {
			if(!p4parser.isHeaderVariable(field)) {
				headerOffsetsAndSizes.add(Pair.of(field, Pair.of(finalHeaderOffset, pktFieldSizes.get(field))));
				finalHeaderOffset += pktFieldSizes.get(field);
			}
		}
		//headerId++;
		allHeaderFieldOffsetsAndSizes.put(config.getPktConfig().getBitVectorLength()+1, headerOffsetsAndSizes);
		if(config.getPktConfig().getParserOnlyPkts()) {
			//PacketEmitter pktemit = new PacketEmitter(outputDir);
			for(List<P4State> parserPath: acceptedPacketTypes) {
				List<P4State> mappedParserPath = new ArrayList<P4State>();
				for(P4State p4s : parserPath) {
					
					if(p4s.getExtracts() != null && p4s.getExtracts().size() > 0) {
						P4State newP4State = new P4State(p4s.getName());
						newP4State.setTransition(p4s.getTransition());
						if(p4s.getAssigns() != null) {
							for(P4Assign p4a : p4s.getAssigns()) {
								newP4State.getAssigns().add(p4a);
							}
						}
						for(P4Extract p4e : p4s.getExtracts()) {
							P4Extract newp4Ext = new P4Extract(p4e.getHeaderActualName(), p4e.getHeader(), validLocationRemap.get(p4e.getHeader()), p4e.getValidField(), p4e.getParserALUOp(), p4e.isALUneeded());
							newp4Ext.getFields().addAll(p4e.getFields());
							newP4State.getExtracts().add(newp4Ext);
						}
						mappedParserPath.add(newP4State);
					}
					else {
						mappedParserPath.add(p4s);
					}
				}
				
				Packet packet = new Packet(mappedParserPath, p4parser.getHeaderIds(), p4parser.getParserStartState(), p4parser.getParserEndState(), config.getPktConfig().getPacketLength(), config.getPktConfig().getByteVectorLength(), config.getPktConfig().getBitVectorLength(), config.getPktConfig(), null, allHeaderFieldOffsetsAndSizes, finalHeaderOffset, new HashMap<String, String>(),allheaders);

				PacketMeta pktMeta = packet.getPacket();
				Pair<String,String> pktPair = Pair.of(Utils.bitSetToHex(pktMeta.getEndianFormatPkt(), pktMeta.getPktLen()),pktMeta.getPacketData());
				Pair<String,String> nwpktPair = Pair.of(Utils.bitSetToHex(pktMeta.getNwFormatPkt(), pktMeta.getPktLen()),pktMeta.getPacketData());
				//packets.add(Pair.of(pktPair, pktPair));
				pktemit.emitPacket(Pair.of(pktPair, pktPair),Pair.of(nwpktPair,nwpktPair));
			}
			return;
		}
		
		for(List<P4State> parserPath: acceptedPacketTypes) {
			List<P4State> mappedParserPath = new ArrayList<P4State>();
			for(P4State p4s : parserPath) {
				
				if(p4s.getExtracts() != null && p4s.getExtracts().size() > 0) {
					P4State newP4State = new P4State(p4s.getName());
					newP4State.setTransition(p4s.getTransition());
					if(p4s.getAssigns() != null) {
						for(P4Assign p4a : p4s.getAssigns()) {
							newP4State.getAssigns().add(p4a);
						}
					}
					for(P4Extract p4e : p4s.getExtracts()) {
						P4Extract newp4Ext = new P4Extract(p4e.getHeaderActualName(), p4e.getHeader(), validLocationRemap.get(p4e.getHeader()), p4e.getValidField(), p4e.getParserALUOp(), p4e.isALUneeded());
						newp4Ext.getFields().addAll(p4e.getFields());
						newP4State.getExtracts().add(newp4Ext);
					}
					mappedParserPath.add(newP4State);
				}
				else {
					mappedParserPath.add(p4s);
				}
			}
			for(List<CFGNode> cfgPaths : controlFlowPaths) {
				
				int packetsGeneratedInPath = 0;
				
				List<List<String>> allTableActions = new ArrayList<List<String>>();
				List<CFGNode> constraintsPath = new ArrayList<CFGNode>();
				List<CFGNode> executionPath = new ArrayList<CFGNode>();
				
				for(CFGNode node: cfgPaths) {
					if(node.isTableApplyNode()) {
						TableDeclarationContextExt t = (TableDeclarationContextExt) node.getTableApplyNode();
						String controlBlock = p4blocks.getControlBlockOfTable(t.getTableId());
						allTableActions.add(p4blocks.getControlBlocksExtracted().get(controlBlock).getTablesExtracted().get(t.getNameString()).getTableActions());
						executionPath.add(node);
					}
					else if(node instanceof ExpressionContextExt || node instanceof AssignmentStatementContextExt || node instanceof CallWithoutTypeArgsContextExt) {
						constraintsPath.add(node);
						if(node instanceof AssignmentStatementContextExt || node instanceof CallWithoutTypeArgsContextExt) 
							executionPath.add(node);
					}
				}
				
				List<List<String>> actionCombinations = allPossibleCombinationsOfActionsInAPath(allTableActions, 0);
				
				while(packetsGeneratedInPath < maxPacketsPerPath) {
					for(List<String> actionsPath: actionCombinations) {
						if(packetsGeneratedInPath >= maxPacketsPerPath * actionCombinations.size())
							break;
						
						boolean isConstraintsSpecified = config.getPktConfig().getConstraints() != null && config.getPktConfig().getConstraints().size() > 0;
						int numConstraintsLoop = isConstraintsSpecified ? config.getPktConfig().getConstraints().size() : 1;
						
						for(int i=0; i<numConstraintsLoop; i++) {
							//chnage the constructor later
							Map<String, String> solvedValues = new HashMap<String, String>();
							Packet packet = new Packet(mappedParserPath, p4parser.getHeaderIds(), p4parser.getParserStartState(), p4parser.getParserEndState(), config.getPktConfig().getPacketLength(), config.getPktConfig().getByteVectorLength(), config.getPktConfig().getBitVectorLength(), config.getPktConfig(), isConstraintsSpecified ? config.getPktConfig().getConstraints().get(i) : null, allHeaderFieldOffsetsAndSizes, finalHeaderOffset, solvedValues, allheaders);
							Map<String, Pair<Integer, Integer>> hdrOffsetsNSizes = packet.getfieldOffsetAndSize();
							Solver solver = new Solver(getUpdatedConstraintsPath(constraintsPath, packet, p4parser, hdrOffsetsNSizes), packet, hdrOffsetsNSizes, allHeaderValidFields);
							solver.solve();
							if(solver.isSolved()) {
								if(config.getPktConfig().isFullCoverage())
									generatePacketAndProgramTables(config, somModel, p4blocks, packet, executionPath, actionsPath, solvedValues);
								else
									generatePacketAndProgramTablesRandomly(config, somModel, p4blocks, packet, executionPath, actionsPath, solvedValues, pktemit, allHeaderFieldOffsetsAndSizes);
								packetsGeneratedInPath++;
							}
							else {
								//L.debug("cannot generate packet with this path. skipping.");
								packetsGeneratedInPath++;
							}
						}
					}
				}
			}
		}
	}

	private static Set<P4State> getAllStates() {
		return allStates;
	}

	private List<CFGNode> getUpdatedConstraintsPath(List<CFGNode> constraintsPath, Packet packet, P4Parser p4parser, Map<String, Pair<Integer, Integer>> hdrOffsetsNSizes) {
		List<CFGNode> updatedConstraintsPath = new LinkedList<CFGNode>();
		for(CFGNode node : constraintsPath) {
			if(node instanceof ExpressionContextExt) {
				Set<String> terminals = new HashSet<String>();
				getTerminalsInExpression((ExpressionContextExt) node, terminals);
				boolean addThisCondition = true;
				for(String terminal: terminals) {
					if(p4parser.isHeaderVariable(terminal)) {
						if(terminal.endsWith("isValid")) {
							if(!packet.isHeaderPartOfPacket(allHeaderValidFields.get(terminal))) {
								addThisCondition = false;
								break;
							}
						}
						else {
							//int offset = ((ExpressionContextExt) node).getAlignedByteOffset(terminal) * 8;
							int offset = hdrOffsetsNSizes.get(terminal).first();
							if(!packet.isHeaderVariable(offset)) {
								addThisCondition = false;
								break;
							}
						}
					}
				}
				if(addThisCondition)
					updatedConstraintsPath.add(node);
			}
			else {
				updatedConstraintsPath.add(node);
			}
		}
		return updatedConstraintsPath;
	}
	
	private void getTerminalsInExpression(ExpressionContextExt expCtx, Set<String> terminals){
		for(ExpressionNode node : expCtx.getOperands()){
			if(node.isTerminal() && !node.isNumber()) {
				terminals.add(node.TerminalValue());
			}
			else if(node.isNumber()) {
				//do nothing
			}
			else{
				ExpressionContextExt ctx = (ExpressionContextExt) node;
				getTerminalsInExpression(ctx, terminals);
			}
		}
	}
	
	private Integer getPtrLen() {
		Integer max = 0;
		if(actionsInstMap!=null) {
			for (Integer tableId : actionsInstMap.keySet()) {
				for (String action : actionsInstMap.get(tableId).keySet()) {
					if (actionsInstMap.get(tableId).get(action) > max)
						max = actionsInstMap.get(tableId).get(action);
				}
			}
		}
		return Integer.SIZE-Integer.numberOfLeadingZeros(max);
	}
	
	private void generatePacketAndProgramTables(Config config, SOMModel somModel, P4Blocks p4blocks, Packet packet, List<CFGNode> executionPath, List<String> actionsPath, Map<String,String> pktSolvedValues) {
		
		PacketMeta ipktMeta = packet.getPacket();
		Pair<String,String> ipktPair = Pair.of(Utils.bitSetToHex(ipktMeta.getEndianFormatPkt(), ipktMeta.getPktLen()),ipktMeta.getPacketData());
		
		BitSet inputPacketVector = (BitSet) packet.getPacketVector().clone();
		Map<String,String> initialValues = packet.getPacketFieldValues(ipktMeta.getDrmtFormatPkt());
		Map<String, Long> values = new HashMap<String, Long>();
		
		int actionIdx = 0;
		for(CFGNode cnode: executionPath) {
			if(cnode.isTableApplyNode()) {
				TableDeclarationContextExt t = (TableDeclarationContextExt) cnode.getTableApplyNode();
				Integer tableId = t.getTableId();
				String tableName = t.getTableName();
				String action = actionsPath.get(actionIdx++);
				TableBlock tableBlock = p4blocks.getControlBlocksExtracted().get(p4blocks.getControlBlockOfTable(tableId)).getTablesExtracted().get(tableName);
				ActionBlock actionBlock = p4blocks.getControlBlocksExtracted().get(p4blocks.getControlBlockOfTable(tableId)).getActionsExtracted().get(action);
				ActionDeclarationContextExt actionCtx = actionBlock.getAction();
				
				BitSet key = new BitSet(tableBlock.getTotalWidthOfKeys());
				BitSet mask = new BitSet(tableBlock.getTotalWidthOfKeys());
				BitSet data = new BitSet(actionBlock.getTotalWidthOfParams());
				BitSet ptr = Utils.longToBitset(actionsInstMap.get(tableId).get(action).longValue(), instructionPtrLen);
				mask.set(0, tableBlock.getTotalWidthOfKeys());
				
				int keyIndex = 0;
				int bitIndex = 0;
				for(TableKey tableKey : tableBlock.getTableKeys()) {
					BitSet bs = tableKey.isBit() ? packet.getBitVector().get(tableKey.getOffset(), tableKey.getOffset() + 1) : inputPacketVector.get(packet.getfieldOffsetAndSize().get(tableKey.getKeyName()).first(), packet.getfieldOffsetAndSize().get(tableKey.getKeyName()).first() + packet.getfieldOffsetAndSize().get(tableKey.getKeyName()).second());
					if(pktSolvedValues.get(tableKey.getKeyName()) == null)
						pktSolvedValues.put(tableKey.getKeyName(), Utils.bitSetToHex(bs));
					for(int i=0; i<tableKey.getSize(); i++) {
						if(bs.get(i))
							key.set(keyIndex);
						keyIndex++;
					}
					/*if(tableKey.isBit()) {
						key.set(bitIndex);
						mask.set(bitIndex);
						bitIndex++;
					}
					else
						for(int i=0; i<tableKey.getSize(); i++) {
							if(bs.get(i))
								key.set(keyIndex);
							mask.set(keyIndex);
							keyIndex++;
						}*/
				}
				
				int dataIndex = 0;
				Map<String, Long> randomActionParameterValues = new HashMap<String, Long>();
				for(ActionParams param : actionBlock.getActionParams()) {
					dataIndex = actionBlock.getParamOffset(param.getParamName());
					BitSet bs = Utils.randomBitSet(param.getParamWidth());
					Long randomValue = Utils.bitSetToLong(bs);
					randomActionParameterValues.put(param.getParamName(), randomValue);
					for(int i=0; i<param.getParamWidth(); i++) {
						if(bs.get(i))
							data.set(dataIndex);
						dataIndex++;
					}
				}
				
				somModel.addTableEntry(null, tableId, key, tableBlock.getTotalWidthOfKeys(), data, actionBlock.getTotalWidthOfParams(), mask, ptr);
				
				actionCtx.evaluateAction(inputPacketVector, packet.getBitVector(), packet.getHeaderFieldOffsetsAndSizes(), values, randomActionParameterValues, allHeaderValidFields, packet.getfieldOffsetAndSize());
			}
			else if(cnode instanceof AssignmentStatementContextExt) {
				AssignmentStatementContextExt assignCtx = (AssignmentStatementContextExt) cnode;
				assignCtx.evaluateAction(inputPacketVector, packet.getBitVector(), packet.getHeaderFieldOffsetsAndSizes(), values, new HashMap<String,Long>(), allHeaderValidFields, packet.getfieldOffsetAndSize());
			}
			else if(cnode instanceof CallWithoutTypeArgsContextExt) {
				CallWithoutTypeArgsContextExt callCtx = (CallWithoutTypeArgsContextExt) cnode;
				callCtx.evaluateAction(inputPacketVector, packet.getBitVector(), packet.getHeaderFieldOffsetsAndSizes(), values, new HashMap<String,Long>(), allHeaderValidFields, packet.getfieldOffsetAndSize());
			}
		}
		
		PacketMeta epktMeta = packet.getDpktFromBitset(inputPacketVector);
		Map<String,String> finalValues = packet.getPacketFieldValues(epktMeta.getDrmtFormatPkt());
		
		//packets.add(Pair.of(ipktPair, Pair.of(Utils.bitSetToHex(epktMeta.getEndianFormatPkt(), epktMeta.getPktLen()),epktMeta.getPacketData())));
		//packetHeaderInfo.add(ipktMeta.getHeaderIdAndOffsets());
		
		List<Pair<String,String>> initialFieldValues = new ArrayList<Pair<String,String>>();
		List<Pair<String,String>> finalFieldValues = new ArrayList<Pair<String,String>>();
		if(config.getPktConfig().getEnums() != null && config.getPktConfig().getEnumToP4() != null) {
			int index = 0;
			
			BitSet ibs = new BitSet(config.getPktConfig().getEnums().size());
			for(String field: config.getPktConfig().getEnums()) {
				if(pktSolvedValues.get(config.getPktConfig().getEnumToP4().get(field)) != null) {
					initialFieldValues.add(Pair.of(field,pktSolvedValues.get(config.getPktConfig().getEnumToP4().get(field))));
					ibs.set(index);
				}
				index++;
			}
			initialFieldValues.add(Pair.of("bitset", Utils.bitSetToHex(ibs)));
			index = 0;
			BitSet ebs = new BitSet(config.getPktConfig().getEnums().size());
			for(String field: config.getPktConfig().getEnums()) {
				if(finalValues.get(config.getPktConfig().getEnumToP4().get(field)) != null && !finalValues.get(config.getPktConfig().getEnumToP4().get(field)).equals(initialValues.get(config.getPktConfig().getEnumToP4().get(field)))) {
					finalFieldValues.add(Pair.of(field,finalValues.get(config.getPktConfig().getEnumToP4().get(field))));
					ebs.set(index);
				}
				index++;
			}
			finalFieldValues.add(Pair.of("bitset", Utils.bitSetToHex(ebs)));
		}
		
		emitter.emitInputOutputPkts(Pair.of(ipktPair, Pair.of(Utils.bitSetToHex(epktMeta.getEndianFormatPkt(), epktMeta.getPktLen()),epktMeta.getPacketData())), 
				Pair.of(initialFieldValues,finalFieldValues), ipktMeta.getHeaderIdAndOffsets());
		
	}
	
	private void addTableEntries(P4Blocks p4blocks, SOMModel somModel) {
		for(Integer tableId : tableSpace.getFixedTableEntries().keySet()) {
			int numEntries = 0;
			for(TableEntry entry : tableSpace.getTableEntries(tableId)) {
				//TableEntry entry = tableSpace.getFixedTableEntries().get(tableId).get(i);
				
				TableBlock tableBlock = p4blocks.getControlBlocksExtracted().get(p4blocks.getControlBlockOfTable(tableId)).getTablesExtracted().get(entry.getTableName());
				ActionBlock actionBlock = p4blocks.getControlBlocksExtracted().get(p4blocks.getControlBlockOfTable(tableId)).getActionsExtracted().get(entry.getActionName());
				
				BitSet key = new BitSet(tableBlock.getTotalWidthOfKeys());
				BitSet mask = entry.getMask();
				BitSet data = new BitSet(actionBlock.getTotalWidthOfParams());
				BitSet ptr = entry.getInstptr();
				
				int keyIndex = 0;
				for(TableKey tableKey : tableBlock.getTableKeys()) {
					BitSet partKey = entry.getKeys().get(tableKey.getKeyName());
					for(int j=0; j<tableKey.getSize(); j++) {
						if(partKey.get(j))
							key.set(keyIndex);
						keyIndex++;
					}
				}
				
				int dataIndex = 0;
				for(ActionParams param : actionBlock.getActionParams()) {
					dataIndex = actionBlock.getParamOffset(param.getParamName());
					BitSet bs = entry.getActionParams().get(param.getParamName());
					for(int j=0; j<param.getParamWidth(); j++) {
						if(bs.get(j))
							data.set(dataIndex);
						dataIndex++;
					}
				}
				
				somModel.addTableEntry(null, tableId, key, tableBlock.getTotalWidthOfKeys(), data, actionBlock.getTotalWidthOfParams(), mask, ptr);
				numEntries++;
				//System.out.println(tableId+"-"+numEntries);
			}
		}
	}
	
	private void generatePacketAndProgramTablesRandomly(Config config, SOMModel somModel, P4Blocks p4blocks, Packet packet, List<CFGNode> executionPath, List<String> actionsPath, 
			Map<String,String> pktSolvedValues, PacketEmitter pktemit, Map<Integer, ArrayList<Pair<String,Pair<Integer,Integer>>>> allHeaderFieldOffsetsAndSizes) {
		
		PacketMeta ipktMeta = packet.getPacket();
		Pair<String,String> ipktPair = Pair.of(Utils.bitSetToHex(ipktMeta.getEndianFormatPkt(), ipktMeta.getPktLen()),ipktMeta.getPacketData());
		Pair<String,String> inwpktPair = Pair.of(Utils.bitSetToHex(ipktMeta.getNwFormatPkt(), ipktMeta.getPktLen()),ipktMeta.getPacketData());
		
		BitSet inputPacketVector = (BitSet) packet.getPacketVector().clone();
		Map<String, List<BitSet>> valuesHistory = new HashMap<String, List<BitSet>>();
		for(Integer headerId: allHeaderFieldOffsetsAndSizes.keySet()) {
			for(Pair<String,Pair<Integer,Integer>> pair : allHeaderFieldOffsetsAndSizes.get(headerId)) {
				valuesHistory.put(pair.first(), new ArrayList<BitSet>());
				valuesHistory.get(pair.first()).add(inputPacketVector.get(pair.second().first(),pair.second().first()+pair.second().second()));
			}
		}
		Map<String, Long> values = new HashMap<String, Long>();
		
		int actionIdx = 0;
		for(CFGNode cnode: executionPath) {
			
			if(cnode.isTableApplyNode()) {
				TableDeclarationContextExt t = (TableDeclarationContextExt) cnode.getTableApplyNode();
				Integer tableId = t.getTableId();
				String tableName = t.getTableName();
				String action = actionsPath.get(actionIdx++);
				TableBlock tableBlock = p4blocks.getControlBlocksExtracted().get(p4blocks.getControlBlockOfTable(tableId)).getTablesExtracted().get(tableName);
				int fixedEntryIdx = tableSpace.getNumFixedEntries(tableId);
				Map<String,BitSet> probableEntryValues = new HashMap<String,BitSet>();
				boolean fixEntry = false;
				for(TableKey tableKey : tableBlock.getTableKeys()) {
					if(pktSolvedValues.get(tableKey.getKeyName()) != null) {
						probableEntryValues.put(tableKey.getKeyName(), Utils.stringToBitSet(pktSolvedValues.get(tableKey.getKeyName()), tableKey.getSize(), 16, false));
						fixEntry = true;
					}
					else if(valuesHistory.get(tableKey.getKeyName()).size() > 1) {
						probableEntryValues.put(tableKey.getKeyName(), valuesHistory.get(tableKey.getKeyName()).get(valuesHistory.get(tableKey.getKeyName()).size()-1));
						fixEntry = true;
					}
					else {
						probableEntryValues.put(tableKey.getKeyName(),null);
					}
				}
				
				Map<String, BitSet> keysToFix = new HashMap<String, BitSet>();
				TableEntry entryChosen = null;
				if(fixEntry) {
					if(!tableSpace.doesFixedEntryAlreadyExist(tableId, probableEntryValues)) {
						if(!tableSpace.isFixedEntriesFull(tableId)) {
							for(String key : probableEntryValues.keySet()) {
								if(probableEntryValues.get(key) != null)
									tableSpace.addKeyToFixedEntries(tableId, fixedEntryIdx, key, probableEntryValues.get(key));
							}
							keysToFix = tableSpace.getFixedTableEntryKeys(tableId, fixedEntryIdx);
							entryChosen = tableSpace.getFixedTableEntry(tableId, fixedEntryIdx);
						}
						else {
							break;
						}
					}
				}
				else {
					keysToFix = tableSpace.getRandomTableEntryKeys(tableId);
					entryChosen = tableSpace.getRandomTableEntry(tableId);
				}
				for(TableKey tableKey : tableBlock.getTableKeys()) {
					BitSet keyValue = keysToFix.get(tableKey.getKeyName());
					int start = tableKey.getOffset();
					int end = start + tableKey.getSize();
					for(int i=start,j=0; i<end; i++) {
						if(keyValue.get(j))
							packet.getPacketVector().set(i);
						else
							packet.getPacketVector().clear(i);
					}
				}
				
				ActionBlock actionBlock = p4blocks.getControlBlocksExtracted().get(p4blocks.getControlBlockOfTable(tableId)).getActionsExtracted().get(action);
				ActionDeclarationContextExt actionCtx = actionBlock.getAction();

				Map<String, Long> randomActionParameterValues = new HashMap<String, Long>();
				for(String key : entryChosen.getActionParams().keySet()) {
					randomActionParameterValues.put(key, Utils.bitSetToLong(entryChosen.getActionParams().get(key)));
				}
				
				actionCtx.evaluateAction(inputPacketVector, packet.getBitVector(), packet.getHeaderFieldOffsetsAndSizes(), values, randomActionParameterValues, allHeaderValidFields, packet.getfieldOffsetAndSize());
			}
			else if(cnode instanceof AssignmentStatementContextExt) {
				AssignmentStatementContextExt assignCtx = (AssignmentStatementContextExt) cnode;
				assignCtx.evaluateAction(inputPacketVector, packet.getBitVector(), packet.getHeaderFieldOffsetsAndSizes(), values, new HashMap<String,Long>(), allHeaderValidFields, packet.getfieldOffsetAndSize());
			}
			else if(cnode instanceof CallWithoutTypeArgsContextExt) {
				CallWithoutTypeArgsContextExt callCtx = (CallWithoutTypeArgsContextExt) cnode;
				callCtx.evaluateAction(inputPacketVector, packet.getBitVector(), packet.getHeaderFieldOffsetsAndSizes(), values, new HashMap<String,Long>(), allHeaderValidFields, packet.getfieldOffsetAndSize());
			}
			
			BitSet currentPacketVector = (BitSet) packet.getPacketVector().clone();
			for(Integer headerId: allHeaderFieldOffsetsAndSizes.keySet()) {
				for(Pair<String,Pair<Integer,Integer>> pair : allHeaderFieldOffsetsAndSizes.get(headerId)) {
					if(!currentPacketVector.get(pair.second().first(),pair.second().first()+pair.second().second()).equals(valuesHistory.get(pair.first()).get(valuesHistory.get(pair.first()).size()-1))) {
						valuesHistory.get(pair.first()).add(currentPacketVector.get(pair.second().first(),pair.second().first()+pair.second().second()));
					}
				}
			}
		}
	
		PacketMeta epktMeta = packet.getPacket();
		Pair<String,String> epktPair = Pair.of(Utils.bitSetToHex(epktMeta.getEndianFormatPkt(), epktMeta.getPktLen()),epktMeta.getPacketData());
		Pair<String,String> enwpktPair = Pair.of(Utils.bitSetToHex(epktMeta.getNwFormatPkt(), epktMeta.getPktLen()),epktMeta.getPacketData());
		pktemit.emitPacket(Pair.of(ipktPair, epktPair),Pair.of(inwpktPair,enwpktPair));
	}
	
	private List<List<String>> allPossibleCombinationsOfActionsInAPath(List<List<String>> allActions, int index){
		if(index == allActions.size()) {
			List<List<String>> allTableActions = new ArrayList<List<String>>();
			allTableActions.add(new LinkedList<String>());
			return allTableActions;
		}
		
		List<List<String>> allTableActions = new ArrayList<List<String>>();
	    List<String> actionsItems = allActions.get(index);
	    // Get combination from next index
	    List<List<String>> remainingList = allPossibleCombinationsOfActionsInAPath(allActions, index+1);
	    for (int i=0; i<actionsItems.size(); i++) {
	        String action = actionsItems.get(i);
	        if (remainingList != null) {
	            for (List<String> remaining : remainingList) {
	                List<String> nextCombination = new ArrayList<String>();
	                nextCombination.add(action);
	                nextCombination.addAll(remaining);
	                allTableActions.add(nextCombination);
	            }
	        }
	    }
	    return allTableActions;
	}
	
}
