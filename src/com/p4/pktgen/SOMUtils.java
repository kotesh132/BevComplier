package com.p4.pktgen;

import java.io.File;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.drmt.ilp.som.SomSpec;
import com.p4.drmt.ilp.som.TableDetail;
import com.p4.pktgen.enums.ControllerType;
import com.p4.pktgen.enums.LayoutType;
import com.p4.pktgen.model.SOMModel;
import com.p4.pktgen.model.memory.SOMUnit;
import com.p4.pktgen.p4blocks.ControlBlock;
import com.p4.pktgen.p4blocks.TableBlock;
import com.p4.pktgen.p4blocks.TableKey;
import com.p4.utils.Utils;
import com.p4.utils.Utils.Pair;

public class SOMUtils {
	
	private static Logger L = LoggerFactory.getLogger(SOMUnit.class);

	public static void placeTablesInSOMs(Set<List<Integer>> disjointTables) {
		SOMModel somModel = SOMModel.getInstance();
		P4Blocks p4blocks = P4Blocks.getInstance();
		
		Map<Integer, Pair<List<TableKey>, Integer>> tableKeysAndActionWidths = new HashMap<Integer, Pair<List<TableKey>, Integer>>();
		
		for(List<Integer> disjointList : disjointTables) {
			for(Integer tableId : disjointList) {
				ControlBlock controlBlock = p4blocks.getControlBlocksExtracted().get(p4blocks.getControlBlockOfTable(tableId));
				for(Map.Entry<String, TableBlock> tableEntry : controlBlock.getTablesExtracted().entrySet()) {
					if(tableEntry.getValue().getTableId() == tableId) {
						int maxSize = 0;
						for(String action: tableEntry.getValue().getTableActions()) {
							int actionDataSize = controlBlock.getActionsExtracted().get(action).getTotalWidthOfParams();
							if(actionDataSize > maxSize)
								maxSize = actionDataSize;
						}
						tableKeysAndActionWidths.put(tableId, Pair.of(tableEntry.getValue().getTableKeys(), maxSize));
					}
				}
			}
		}
		
		somModel.placeTablesInSOMs(disjointTables, tableKeysAndActionWidths);
	}
	
	public static List<List<TableDetail>> getTableDetails(Set<List<Integer>> disjointTables) {
		SOMModel somModel = SOMModel.getInstance();
		List<List<TableDetail>> allTableDetails = new LinkedList<List<TableDetail>>();
		for(List<Integer> disjointList : disjointTables) {
			List<TableDetail> disjointDetails = new LinkedList<TableDetail>();
			for(Integer tableId : disjointList) {
				TableDetail td = new TableDetail(tableId, somModel.getKsegsOfTable(tableId).size(), somModel.getDsegsOfTable(tableId).size(), somModel.getSramsOfTable(tableId), somModel.getTcamsOfTable(tableId));
				for(ControllerType controller : somModel.getControllersAllocatedForTable(tableId).keySet()){
					switch(controller) {
						case read  : td.addControllerTypeNum(com.p4.drmt.ilp.som.SomSpec.ControllerType.NUM_READ_CONTROLLERS, somModel.getControllersAllocatedForTable(tableId).get(controller)); break;
						case write : td.addControllerTypeNum(com.p4.drmt.ilp.som.SomSpec.ControllerType.NUM_WRITE_CONTROLLERS, somModel.getControllersAllocatedForTable(tableId).get(controller)); break;
						case hash  : td.addControllerTypeNum(com.p4.drmt.ilp.som.SomSpec.ControllerType.NUM_HASH_CONTROLLERS, somModel.getControllersAllocatedForTable(tableId).get(controller)); break;
						case cam   : td.addControllerTypeNum(com.p4.drmt.ilp.som.SomSpec.ControllerType.NUM_CAM_CONTROLLERS, somModel.getControllersAllocatedForTable(tableId).get(controller)); break;
					}
				}
				disjointDetails.add(td);
			}
			allTableDetails.add(disjointDetails);
		}
		return allTableDetails;
	}
	
	public static SomSpec getSomSpec() {
		SOMModel somModel = SOMModel.getInstance();
		SomSpec spec = new SomSpec(somModel.getSomConfig().get(0).getNumKseg(), somModel.getSomConfig().get(0).getNumKseg(), somModel.getSomConfig().get(0).getSramConfig().getNumSram(), somModel.getSomConfig().get(0).getTcamConfig().getNumRow() * somModel.getSomConfig().get(0).getTcamConfig().getNumCol(), somModel.getSomConfig().size());
		spec.addControllerTypeNum(com.p4.drmt.ilp.som.SomSpec.ControllerType.NUM_READ_CONTROLLERS, somModel.getSomConfig().get(0).getNumReadControllers());
		spec.addControllerTypeNum(com.p4.drmt.ilp.som.SomSpec.ControllerType.NUM_WRITE_CONTROLLERS, somModel.getSomConfig().get(0).getNumWriteControllers());
		spec.addControllerTypeNum(com.p4.drmt.ilp.som.SomSpec.ControllerType.NUM_HASH_CONTROLLERS, somModel.getSomConfig().get(0).getNumHashControllers());
		spec.addControllerTypeNum(com.p4.drmt.ilp.som.SomSpec.ControllerType.NUM_CAM_CONTROLLERS, somModel.getSomConfig().get(0).getNumCamControllers());
		return spec;
	}
	
	public static Map<Integer, Pair<Integer, Set<Integer>>> getTableToSomKsegMap() {
		return SOMModel.getInstance().getTableToSomKsegMap();
	}
	
	public static void placeTablesInSOMs(List<List<TableDetail>> disjointTableDetails) {
		SOMModel somModel = SOMModel.getInstance();
		
		Map<Integer, Integer> tableToSomMap = new HashMap<Integer, Integer>();
		Set<List<Integer>> disjointTables = new HashSet<List<Integer>>();
		Map<Integer, Pair<List<TableKey>, Integer>> tableKeysAndActionWidths = new HashMap<Integer, Pair<List<TableKey>, Integer>>();
		Map<Integer, Integer> tableNumEntries = new HashMap<Integer, Integer>();
		
		for(List<TableDetail> disjointList : disjointTableDetails) {
			List<Integer> disjointSet = new LinkedList<Integer>();
			for(TableDetail table : disjointList) {
				tableToSomMap.put(table.getTableId(), table.getAssignedSomId());
				List<TableKey> tableKeys = new LinkedList<TableKey>();
				tableKeys.add(new TableKey("key_table" + table.getTableId(), table.getKeytype(), 0, table.getKeyWidth(),false));
				tableKeysAndActionWidths.put(table.getTableId(), Pair.of(tableKeys, table.getDataWidth()));
				disjointSet.add(table.getTableId());
				if(table.getNumEntries() > 0)
					tableNumEntries.put(table.getTableId(), table.getNumEntries());
			}
			disjointTables.add(disjointSet);
		}
		
		somModel.placeTablesInSOMs(disjointTables, tableKeysAndActionWidths, tableNumEntries, tableToSomMap);
	}
	
	public static void addTableEntry(Integer somId, Integer tableId, BitSet key, Integer keyLength, BitSet data, Integer dataLength, BitSet mask, BitSet actionPtr) {
		SOMModel somModel = SOMModel.getInstance();
		somModel.addTableEntry(somId, tableId, key,keyLength, data, dataLength, mask, actionPtr);
	}
	
	public static void emitSOMConfigAndData(File outputDir) {
		SOMModel somModel = SOMModel.getInstance();
		Emitter emitter = new Emitter(outputDir, somModel);
		emitter.emitSomUnitConfig();
	}
	
	public static void populateTables(List<List<TableDetail>> disjointTableDetails) {
		SOMModel somModel = SOMModel.getInstance();
		for(List<TableDetail> disjointList : disjointTableDetails) {
			for(TableDetail table : disjointList) {
				for(int i=0; i<table.getNumEntries()-1; i++) {
					BitSet mask = new BitSet(table.getKeyWidth());
					mask.set(0, table.getKeyWidth());
					BitSet data = Utils.randomBitSet(table.getDataWidth());
					//data.clear(0, 8);
					addTableEntry(table.getAssignedSomId(), table.getTableId(), Utils.randomBitSet(table.getKeyWidth()), table.getKeyWidth(), data, table.getDataWidth(), mask, Utils.randomBitSet(somModel.getSomConfig().get(0).getInstPtrNumBits() != null ? somModel.getSomConfig().get(0).getInstPtrNumBits() : somModel.getSomConfig().get(0).getSramConfig().getBits() - somModel.getSomConfig().get(0).getDsegWidth() - 1));
				}
				getTableInfo(table.getTableId());
			}
		}
	}
	
	public static void getTableInfo(Integer tableId) {
		SOMModel somModel = SOMModel.getInstance();
		Integer somId = somModel.getSomIdOfTable(tableId);
		LayoutType layout = somModel.getLayoutType(somId, tableId);
		List<List<Integer>> keysLayout = somModel.getKeyMemoryLayout(somId, tableId);
		List<List<Integer>> dataLayout = somModel.getDataMemoryLayout(somId, tableId);
		L.debug(tableId.toString());
		L.debug(layout.toString());
		L.debug(keysLayout == null ? null : keysLayout.toString());
		L.debug(dataLayout.toString());
	}
	
	public static int[][] getDsegMap() {
		SOMModel somModel = SOMModel.getInstance();
		return somModel.getDsegMap();
	}
}
