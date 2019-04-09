package com.p4.pktgen.model.memory.camconfigurations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.p4.utils.Utils.Pair;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
public class RowConfiguration implements Serializable {
	private Integer id;
	private Integer numTables;
	private List<Integer> widths;
	private List<WidthInfo> tcamConfig;
	private List<List<Integer>> andEnable;
	
	private List<List<Integer>> andTree;
	private int[] levelSelect;
	private int[] nodeSelect;
	private Map<Integer, List<Integer>> searchEnable;
	@Setter private boolean isValid;
	
	public RowConfiguration(RowConfiguration.UnNormalized rUn) {
		id = rUn.id;
		numTables = rUn.numTables;
		widths = rUn.widths;
		tcamConfig = new ArrayList<WidthInfo>();
		for(WidthInfo.UnNormalized wUn : rUn.tcamConfig) {
			tcamConfig.add(new WidthInfo(wUn));
		}
		andEnable = rUn.andEnable;
		andTree = rUn.andTree;
	}
	
	@NoArgsConstructor
	public static class UnNormalized {
		public Integer id;
		public Integer numTables;
		public List<Integer> widths;
		public List<WidthInfo.UnNormalized> tcamConfig;
		public List<List<Integer>> andTree;
		public List<List<Integer>> andEnable;
	}
	
	public void computeAndTreeLevelSelectNodeSelect(List<Pair<Integer, List<List<Integer>>>> tableAndTcamIds) {
		int[] tableWidths = new int[numTables];
		Map<Integer, List<Integer>> tableTcamMap = new HashMap<Integer, List<Integer>>();
		Map<Integer, List<Integer>> widthTableMap = new HashMap<Integer, List<Integer>>();
		Integer idx = 0;
		for(Integer tableId : andEnable.get(0)) {
			tableWidths[tableId]++;
			if(tableTcamMap.get(tableId) == null) {
				tableTcamMap.put(tableId, new ArrayList<Integer>());
			}
			tableTcamMap.get(tableId).add(idx);
			idx++;
		}
		for(int i=0; i<tableWidths.length; i++) {
			if(widthTableMap.get(tableWidths[i]) == null)
				widthTableMap.put(tableWidths[i], new ArrayList<Integer>());
			widthTableMap.get(tableWidths[i]).add(i);
		}
		
		Map<Integer, List<Integer>> realToDupIdMap = new HashMap<Integer, List<Integer>>();
		BitSet activeTables = new BitSet(numTables);
		for(Pair<Integer, List<List<Integer>>> pair : tableAndTcamIds) {
			for(List<Integer> list : pair.second()) {
				List<Integer> possibleMatches = widthTableMap.get(list.size());
				boolean matchFound = false;
				for(Integer matchingId : possibleMatches) {
					List<Integer> tcamsList = tableTcamMap.get(matchingId);
					if(isListsMatching(tcamsList, list)) {
						if(realToDupIdMap.get(pair.first()) == null)
							realToDupIdMap.put(pair.first(), new ArrayList<Integer>());
						realToDupIdMap.get(pair.first()).add(matchingId);
						activeTables.set(matchingId);
						matchFound = true;
						break;
					}
				}
				if(!matchFound)
					throw new RuntimeException("Unable to find a match in AndTree");
			}
		}
		
		Map<Integer, Integer> levels = new HashMap<Integer, Integer>();
		Map<Integer, List<Integer>> columns = new HashMap<Integer, List<Integer>>();
		for(Integer id : realToDupIdMap.keySet()) {
			boolean stop = false;
			for(int i=andEnable.size()-1; i>=0; i--) {
				for(int j=0; j<andEnable.get(i).size(); j++) {
					for(Integer dupId : realToDupIdMap.get(id)) {
						if(andEnable.get(i).get(j).equals(dupId)) {
							levels.put(id, i);
							if(columns.get(id) == null)
								columns.put(id, new ArrayList<Integer>());
							columns.get(id).add(j);
							stop = true;
						}
					}
				}
				if(stop)
					break;
			}
		}
		
		levelSelect = new int[andEnable.get(0).size()];
		nodeSelect = new int[andEnable.get(0).size()];
		searchEnable = new HashMap<Integer, List<Integer>>();
		
		for(Integer tableId : columns.keySet()) {
			for(Integer colId : columns.get(tableId)) {
				levelSelect[colId] = levels.get(tableId);
				nodeSelect[colId] = colId;
				if(searchEnable.get(tableId) == null)
					searchEnable.put(tableId, new ArrayList<Integer>());
				searchEnable.get(tableId).add(colId);
			}
		}
	}
	
	private boolean isListsMatching(List<Integer> first, List<Integer> second) {
		int[] camIds = new int[widths.size()];
		for(Integer id : first) {
			camIds[id] = 1;
		}
		for(Integer id : second) {
			if(camIds[id] != 1)
				return false;
		}
		return true;
	}
	
	public List<Integer> getSearchEnableColumn(Integer tableId) {
		return searchEnable.get(tableId);
	}
}
