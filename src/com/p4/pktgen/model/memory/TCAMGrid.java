package com.p4.pktgen.model.memory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.p4.pktgen.model.controller.TCAMController;
import com.p4.pktgen.model.controller.TCAMRowController;
import com.p4.pktgen.model.memory.camconfigurations.CAMUtils;
import com.p4.pktgen.model.memory.camconfigurations.RowConfiguration;
import com.p4.utils.FileUtils;
import com.p4.utils.Utils;
import com.p4.utils.Utils.Pair;

public class TCAMGrid {

	private int somId;
	private int gridRows;
	private int gridCols;
	private int rowsPerTcam;
	private int maxTables;
	
	private int activeRows;
	
	private List<RowConfiguration> currentTcamRowConfigs;
	
	private List<Map<Integer, List<List<Integer>>>> tableTcamAllocationMap;
	private Map<Integer, Pair<Integer, Integer>> tableTcamHeightWidthMap;
	private Map<Integer, Map<Pair<Integer, Integer>, Integer>> tableTcamOffsets;
	private Map<Integer, List<List<Pair<Integer, Integer>>>> tableTcamAllocationOrder;
	
	private TCAMController[][] tcamControllers;
	private TCAMRowController[] tcamRowControllers;
	private Map<Integer, Set<Integer>> tableKsegMap;
	
	public TCAMGrid(int numRows, int numCols, int sId, Map<Integer, Set<Integer>> tabKsegMap, int rowsPerCam, int maxTbls) {
		gridRows = numRows;
		gridCols = numCols;
		somId = sId;
		tableKsegMap = tabKsegMap;
		rowsPerTcam = rowsPerCam;
		maxTables = maxTbls;
		
		resetTcamControllers();
		tcamRowControllers = new TCAMRowController[gridRows];
	}
	
	public void updateTcamAvailability(Integer tableId, Integer tableRows, Integer tableCols) {
		int rowsAvailable = 0;
		
		List<Integer> segIdList = new ArrayList<Integer>(tableKsegMap.get(tableId));
		int rowId = 0;
		List<Integer> availabilityOrder = new ArrayList<Integer>();
		int[][] widthCounts = new int[gridRows][gridCols+1];
		
		while(true) {
			RowConfiguration rc = currentTcamRowConfigs.get(rowId);
			if(rc != null && rc.getWidths().get(tableCols) != 0 && rc.getWidths().get(tableCols) > widthCounts[rowId][tableCols]) {
				rowsAvailable++;
				availabilityOrder.add(rowId);
				widthCounts[rowId][tableCols]++;
				if(rowsAvailable >= tableRows) {
					break;
				}
			}
			
			rowId++;
			if(rowId == activeRows) {
				rowId = 0;
			}
		}
		
		tableTcamHeightWidthMap.put(tableId, Pair.of(tableRows, tableCols));
		int nextOffset = 0;
		for(int j=0; j<tableRows; j++) {
			int id = availabilityOrder.get(j);
			RowConfiguration rowConfig = currentTcamRowConfigs.get(id);
			for(int k=0; k<rowConfig.getTcamConfig().size(); k++) {
				
				if(rowConfig.getTcamConfig().get(k).getTableWidth().equals(tableCols)) {
					if(tableTcamAllocationMap.get(id).get(tableId) == null){
						tableTcamAllocationMap.get(id).put(tableId, new ArrayList<List<Integer>>());
					}
					tableTcamAllocationMap.get(id).get(tableId).add(rowConfig.getTcamConfig().get(k).getCamIds());
					
					if(tableTcamOffsets.get(tableId) == null){
						tableTcamOffsets.put(tableId, new HashMap<Pair<Integer, Integer>, Integer>());
					}
					if(tableTcamAllocationOrder.get(tableId) == null){
						tableTcamAllocationOrder.put(tableId, new ArrayList<List<Pair<Integer, Integer>>>());
					}
					List<Pair<Integer,Integer>> rowColPairs = new ArrayList<Pair<Integer,Integer>>();
					for(Integer camId : rowConfig.getTcamConfig().get(k).getCamIds()) {
						tableTcamOffsets.get(tableId).put(Pair.of(id, camId), nextOffset);
						rowColPairs.add(Pair.of(id, camId));
					}
					tableTcamAllocationOrder.get(tableId).add(rowColPairs);
					
					int ksegIdx = 0;
					for(Integer camId : rowConfig.getTcamConfig().get(k).getCamIds()) {
						BitSet tid = new BitSet(maxTables);
						tid.set(tableId);
						tcamControllers[id][camId] = new TCAMController(id, camId, somId, 1, tid, segIdList.get(ksegIdx++));
					}
					rowConfig.getTcamConfig().remove(k);
					rowConfig.getWidths().set(tableCols, rowConfig.getWidths().get(tableCols) - 1);
					rowConfig.setValid(true);
					break;
				}
				
			}
			nextOffset += rowsPerTcam;
		}
	}
	
	public boolean isTcamAvailable(Integer tableRows, Integer tableCols) {
		if(currentTcamRowConfigs == null) {
			currentTcamRowConfigs = new ArrayList<RowConfiguration>(gridRows);
			tableTcamHeightWidthMap = new HashMap<Integer, Pair<Integer, Integer>>();
			tableTcamAllocationMap = new ArrayList<Map<Integer, List<List<Integer>>>>(gridRows);
			tableTcamOffsets = new HashMap<Integer, Map<Pair<Integer,Integer>, Integer>>();
			tableTcamAllocationOrder = new HashMap<Integer, List<List<Pair<Integer,Integer>>>>();
			
			for(int i=0; i<gridRows; i++) {
				currentTcamRowConfigs.add(i, null);
				tableTcamAllocationMap.add(i, new HashMap<Integer, List<List<Integer>>>());
			}
			
			int rowsToAllocate = tableRows > gridRows ? gridRows : tableRows;
			currentTcamRowConfigs = CAMUtils.getSuitableConfiguration(gridCols, new LinkedList<Integer>(Arrays.asList(tableCols)), rowsToAllocate);
			activeRows = rowsToAllocate;
			return true;
		}
		else {
			//search tcamconfig for free space
			//if config doesn't suit then see if empty rows are available and allocate
			//if emptyrows are not available reshuffle and check
			//NOTE : This method doesn't utilize space properly. optimize it.
			boolean reallocate = (tableRows > activeRows);
			if(!reallocate) {
				//check if table fits in current config else reallocate
				
				boolean vacancyAvailableInCurrentSetting = false;
				int[][] widthCounts = new int[gridRows][gridCols+1];
				int rowsAvailable = 0;
				
				int rowId = 0;
				int unsuccessfulAttempts = 0;
				while(true) {
					
					RowConfiguration rc = currentTcamRowConfigs.get(rowId);
					if(rc == null || rc.getWidths().get(tableCols) == 0 || rc.getWidths().get(tableCols) == widthCounts[rowId][tableCols]) {
						unsuccessfulAttempts++;
					}
					else {
						rowsAvailable++;
						widthCounts[rowId][tableCols]++;
						if(rowsAvailable >= tableRows) {
							vacancyAvailableInCurrentSetting = true;
							break;
						}
					}
					
					rowId++;
					if(unsuccessfulAttempts == activeRows)
						break;
					if(rowId == activeRows) {
						rowId = 0;
						unsuccessfulAttempts = 0;
					}
				}
				if(vacancyAvailableInCurrentSetting)
					return true;
				else 
					reallocate = true;
			}
			if(reallocate) {
				//get all possible combinations and for each combination check all tables + new table fits in
				//if yes, allocate old tables and return true
				//else return false
				int rowsToAllocate = tableRows > activeRows ? (tableRows > gridRows ? gridRows : tableRows) : activeRows;
				for(List<RowConfiguration> rowCombination : CAMUtils.getAllPossibleRowConfigurations(gridCols, rowsToAllocate)) {
					
					List<Map<Integer, List<List<Integer>>>> tempTcamAllocationMap = new ArrayList<Map<Integer, List<List<Integer>>>>(rowsToAllocate);
				    Map<Integer, Map<Pair<Integer, Integer>, Integer>> tempTableTcamOffsets = new HashMap<Integer, Map<Pair<Integer, Integer>, Integer>>();
				    Map<Integer, List<List<Pair<Integer, Integer>>>> tempTableTcamAllocationOrder = new HashMap<Integer, List<List<Pair<Integer,Integer>>>>();
				    Map<Integer, Pair<Integer, Integer>> tempTcamHeightWidthMap = new HashMap<Integer, Pair<Integer, Integer>>();
				    int[][] widthCounts = new int[gridRows][gridCols+1];
					
					for(int i=0; i<rowsToAllocate; i++)
						tempTcamAllocationMap.add(new HashMap<Integer, List<List<Integer>>>());
					
					Map<Integer, Pair<Integer, Integer>> tcamHeightWidthMap = tableTcamHeightWidthMap;
					Map<Integer, List<Integer>> availabilityOrder = new HashMap<Integer, List<Integer>>();
					boolean suitableConfig = true;
					tcamHeightWidthMap.put(-1, Pair.of(tableRows, tableCols));
					for(Integer tableId : tcamHeightWidthMap.keySet()) {
						boolean vacancyAvailableInCurrentSetting = false;
						int rowsAvailable = 0;
						int startIdx = 0;
						
						int rowId = 0;
						int unsuccessfulAttempts = 0;
						
						while(true) {
							
							RowConfiguration rc = rowCombination.get(rowId);
							if(rc == null || rc.getWidths().get(tcamHeightWidthMap.get(tableId).second()) == 0 || rc.getWidths().get(tcamHeightWidthMap.get(tableId).second()) == widthCounts[rowId][tcamHeightWidthMap.get(tableId).second()]) {
								unsuccessfulAttempts++;
							}
							else {
								rowsAvailable++;
								if(availabilityOrder.get(tableId) == null)
									availabilityOrder.put(tableId, new ArrayList<Integer>());
								availabilityOrder.get(tableId).add(rowId);
								widthCounts[rowId][tcamHeightWidthMap.get(tableId).second()]++;
								if(rowsAvailable >= tcamHeightWidthMap.get(tableId).first()) {
									vacancyAvailableInCurrentSetting = true;
									break;
								}
							}
							
							rowId++;
							if(unsuccessfulAttempts == rowsToAllocate)
								break;
							if(rowId == rowsToAllocate) {
								rowId = 0;
								unsuccessfulAttempts = 0;
							}
						}

						if(!vacancyAvailableInCurrentSetting) {
							suitableConfig = false;
							break;
						}
						
						if(tableId != -1) {
							int nextOffset = 0;
							for(int j=0; j<tcamHeightWidthMap.get(tableId).first(); j++) {
								int id = availabilityOrder.get(tableId).get(j);
								RowConfiguration rowConfig = rowCombination.get(id);
								for(int k=0; k<rowConfig.getTcamConfig().size(); k++) {
									if(rowConfig.getTcamConfig().get(k).getTableWidth().equals(tcamHeightWidthMap.get(tableId).second())) {
										if(tempTcamAllocationMap.get(id).get(tableId) == null)
											tempTcamAllocationMap.get(id).put(tableId, new ArrayList<List<Integer>>());
										tempTcamAllocationMap.get(id).get(tableId).add(rowConfig.getTcamConfig().get(k).getCamIds());
										
										if(tempTableTcamOffsets.get(tableId) == null){
											tempTableTcamOffsets.put(tableId, new HashMap<Pair<Integer, Integer>, Integer>());
										}
										
										if(tempTableTcamAllocationOrder.get(tableId) == null){
											tempTableTcamAllocationOrder.put(tableId, new ArrayList<List<Pair<Integer, Integer>>>());
										}
										List<Pair<Integer,Integer>> rowColPairs = new ArrayList<Pair<Integer,Integer>>();
										for(Integer camId : rowConfig.getTcamConfig().get(k).getCamIds()) {
											tempTableTcamOffsets.get(tableId).put(Pair.of(id, camId), nextOffset);
											rowColPairs.add(Pair.of(id, camId));
										}
										tempTableTcamAllocationOrder.get(tableId).add(rowColPairs);
										
										rowConfig.getTcamConfig().remove(k);
										rowConfig.getWidths().set(tcamHeightWidthMap.get(tableId).second(), rowConfig.getWidths().get(tcamHeightWidthMap.get(tableId).second()) - 1);
										rowConfig.setValid(true);
										break;
									}
								}
								nextOffset += rowsPerTcam;
							}
						}
						
						tempTcamHeightWidthMap.put(tableId, Pair.of(tcamHeightWidthMap.get(tableId).first(), tcamHeightWidthMap.get(tableId).second()));
					}
					if(!suitableConfig)
						continue;
					
					activeRows = rowsToAllocate;
					currentTcamRowConfigs = rowCombination;
					tableTcamHeightWidthMap = tempTcamHeightWidthMap;
					tableTcamAllocationMap = tempTcamAllocationMap;
					tableTcamOffsets = tempTableTcamOffsets;
					tableTcamAllocationOrder = tempTableTcamAllocationOrder;
					updateTcamControllers();
					return true;
				}
			}
			return false;
		}
	}
	
	private void resetTcamControllers() {
		tcamControllers = new TCAMController[gridRows][gridCols];
		for(int i=0; i<gridRows; i++) {
			for(int j=0; j<gridCols; j++) {
				tcamControllers[i][j] = new TCAMController(i, j);
			}
		}
	}
	
	private void updateTcamControllers() {
		resetTcamControllers();
		int rowIndex = 0;
		for(Map<Integer, List<List<Integer>>> allocationMap : tableTcamAllocationMap) {
			for(Map.Entry<Integer, List<List<Integer>>> entry : allocationMap.entrySet()) {
				List<Integer> segIdList = new ArrayList<Integer>(tableKsegMap.get(entry.getKey()));
				for(List<Integer> camIdList : entry.getValue()) {
					int segIdx = 0;
					for(Integer camId : camIdList) {
						BitSet tid = new BitSet(maxTables);
						tid.set(entry.getKey());
						tcamControllers[rowIndex][camId] = new TCAMController(rowIndex, camId, somId, 1, tid, segIdList.get(segIdx++));
					}
				}
			}
			rowIndex++;
		}
	}
	
	private void computeAndTreeLevelSelectNodeSelect() {
		for(int i=0; i<currentTcamRowConfigs.size(); i++) {
			List<Pair<Integer, List<List<Integer>>>> list = new ArrayList<Pair<Integer, List<List<Integer>>>>();
			for(Integer tableId : tableTcamAllocationMap.get(i).keySet()) {
				list.add(Pair.of(tableId, tableTcamAllocationMap.get(i).get(tableId)));
			}
			currentTcamRowConfigs.get(i).computeAndTreeLevelSelectNodeSelect(list);
		}
	}
	
	private void updateTcamRowControllers() {
		resetTcamRowControllers();
		for(int i=0; i<currentTcamRowConfigs.size(); i++) {
			int[] offsets = new int[gridCols];
			for(Integer tableId : tableTcamAllocationMap.get(i).keySet()) {
				for(List<Integer> camIdList : tableTcamAllocationMap.get(i).get(tableId)) {
					for(Integer camId : camIdList) {
						offsets[camId] = tableTcamOffsets.get(tableId).get(Pair.of(i, camId));
					}
				}
			}
			TCAMRowController rowController = new TCAMRowController(i, somId, currentTcamRowConfigs.get(i).getAndTree(), 
					currentTcamRowConfigs.get(i).getLevelSelect(), 
					currentTcamRowConfigs.get(i).getNodeSelect(),  
					offsets, currentTcamRowConfigs.get(i).isValid());
			tcamRowControllers[i] = rowController;
		}
	}
	
	private void resetTcamRowControllers() {
		for(int row=0; row<gridRows; row++) {
			List<List<Integer>> andtree = new ArrayList<List<Integer>>();
			int[] levelSelects = new int[gridCols];
			int[] nodeSelects = new int[gridCols];
			int[] offsets = new int[gridCols];
			for(int i=0; i<=Math.log(gridCols); i++) {
				List<Integer> andi = new ArrayList<Integer>();
				for(int j=0; j<gridCols; j++)
					andi.add(0);
				andtree.add(andi);
			}
			tcamRowControllers[row] = new TCAMRowController(row, somId, andtree, levelSelects, nodeSelects, offsets, false);
		}
	}
	
	public void getControllersConfiguration() {
		if(currentTcamRowConfigs != null) {
			computeAndTreeLevelSelectNodeSelect();
			updateTcamControllers();
			updateTcamRowControllers();
		}
	}
	
	public List<Integer> getSearchEnable(Integer tableId) {
		List<Integer> searchEnable = new ArrayList<Integer>();
		for(int row=0; row<gridRows; row++) {
			BitSet bs = new BitSet(gridCols);
			if(row < activeRows && row < tableTcamHeightWidthMap.get(tableId).first())
				if(currentTcamRowConfigs.get(row).getSearchEnableColumn(tableId) != null)
					for(Integer colId : currentTcamRowConfigs.get(row).getSearchEnableColumn(tableId))
						bs.set(colId);
			searchEnable.add((int) Utils.bitSetToLong(bs));
		}
		return searchEnable;
	}
	
	public Map<Integer, List<List<Pair<Integer, Integer>>>> getTableTcamAllocation() {
		return tableTcamAllocationOrder;
	}
	
	public void emitRTLConfig(File output) {
		if(tcamRowControllers != null) {
			for(int i=0; i<activeRows; i++) {
				if(tcamRowControllers[i].isValid())
					tcamRowControllers[i].emitRTLConfig(output);
			}
		}
		if(tcamControllers != null) {
			for(int i=0; i<gridRows; i++) {
				for(int j=0; j<gridCols; j++) {
					tcamControllers[i][j].emitConfig(output);
				}
			}
		}
	}
	
	public void emitDebugInfo(File output) {
		for(int i=0; i<gridRows; i++) {
			for(int j=0; j<gridCols; j++) {
				if(tcamControllers[i][j].getEnable() == 1) {
					FileUtils.writeToFile(output, true, "SOM-" + somId + ", TCAM row:" + tcamControllers[i][j].getRowId() + " col: " + tcamControllers[i][j].getColId() + ", assigned to table : " + tcamControllers[i][j].getTid() + ", key segment : " + tcamControllers[i][j].getKseg() + "\n");
				}
			}
		}
	}
}
