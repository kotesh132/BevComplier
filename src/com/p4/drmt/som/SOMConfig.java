package com.p4.drmt.som;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.STGroupFile;

import com.p4.drmt.cfg.KeyMeta.AF;
import com.p4.drmt.parser.FW;
import com.p4.utils.Utils;

public class SOMConfig {
	private final int TCAM_NUM_WORDS = 256;
	private final int TCAM_NUM_BITS = 80;
	private final int SRAM_NUM_WORDS = 256;
	private final int SRAM_NUM_BITS = 80;
	private final int MEM_KSEG = 2;
	private final int MEM_DSEG = 2;
	private final int NUM_KSEG = 4;
	private final int NUM_DSEG = 4;
	
	private int somId;
	private int tableId;
	private String tableName;
	private int totalKeyLength;
	private int maxActionsBits;
	private int tcamColumnCount;
	private int sramColumnCount;
	private int tcamTileRowIndex;
	private int sramTileRowIndex;
	private int tcamBucketCount;
	private int sramBucketCount;
	private List<AF> km;
	private ArrayList<LinkedList<BitSet>> tableKey;
	private ArrayList<LinkedList<BitSet>> tableData;
	private ArrayList<Integer> actionPtrs;
	private List<String> actions;
	private Map<String, Integer> actionAddrMap;
	private List<BitSet> keyMask;
	private int dummyCounter = 0;
	private boolean isInitialized = false;
	
	private final Logger L = LoggerFactory.getLogger(SOMConfig.class);
	
	private final String TASK_TEMPLATE = "tableapi.stg";
	
	public SOMConfig(int somId) {
		this.somId = somId;
	}
	
	public SOMConfig(List<AF> kmList, List<String> actions, Map<String, ArrayList<Integer>> actionsAndDataMap, int somId, int tableId, String tableName, Map<String, Integer> actAddrMap) {
		totalKeyLength = 0;
		maxActionsBits = 0;
		km = kmList;
		this.somId = somId;
		this.tableId = tableId;
		this.tableName = tableName;
		this.actions = actions;
		for(AF af: kmList) {
			totalKeyLength += af.getSize();
		}
		for(String action: this.actions) {
			int actionbits = 0;
			for(Integer width: actionsAndDataMap.get(action))
				actionbits += width;
			if (actionbits > maxActionsBits)
				maxActionsBits = actionbits;
		}
		tcamColumnCount = (int) Math.ceil((double)totalKeyLength/TCAM_NUM_BITS);
		sramColumnCount = (int) Math.ceil((double)maxActionsBits/SRAM_NUM_BITS);
		tcamTileRowIndex = 0;
		sramTileRowIndex = 0;
		tcamBucketCount = 0;
		sramBucketCount = 0;
		tableKey = new ArrayList<LinkedList<BitSet>>();
		tableData = new ArrayList<LinkedList<BitSet>>();
		BitSet b = new BitSet(MEM_KSEG * TCAM_NUM_BITS);
		b.set(totalKeyLength, MEM_KSEG * TCAM_NUM_BITS);
		keyMask = new LinkedList<BitSet>();
		for(int i=0; i<MEM_KSEG*TCAM_NUM_BITS; i+=TCAM_NUM_BITS){
			keyMask.add(b.get(i,i+TCAM_NUM_BITS));
		}
		actionPtrs = new ArrayList<Integer>();
		actionAddrMap = actAddrMap;
	}
	
	public void addDummyKeyAndData() {
		dummyCounter++;
	}
	
	public void addTableKey(BitSet pi) {
		LinkedList<BitSet> keyList = new LinkedList<BitSet>();
		for(int i=0; i<MEM_KSEG; i++)
			keyList.add(new BitSet(TCAM_NUM_BITS));
		int colIndex = 0;
		int total = 0;
		for(AF af: km) {
			int startAddr = af.getStartingAddress() * 8;
			int bound = startAddr + af.getSize();
			for(int i=startAddr; i<bound; i++) {
				if(pi.get(i))
					keyList.get(colIndex).set(total % TCAM_NUM_BITS);
				total++;
				if(total == TCAM_NUM_BITS) 
					colIndex++;
			}
		}
		tableKey.add(keyList);
	}
	
	public void addTableData(List<BitSet> tableDataBS, List<Integer> tableDataWidths, Integer actionPtr) {
		LinkedList<BitSet> dataList = new LinkedList<BitSet>();
		for(int i=0; i<MEM_DSEG; i++)
			dataList.add(new BitSet(SRAM_NUM_BITS));
		int colIndex = 0;
		int total = 0;
		for(int i=0; i<tableDataBS.size(); i++) {
			BitSet bs = tableDataBS.get(i);
			for(int j=0; j<tableDataWidths.get(i); j++) {
				if(bs.get(j))
					dataList.get(colIndex).set(total % SRAM_NUM_BITS);
				total++;
				if(total == SRAM_NUM_BITS)
					colIndex++;
			}
		}
		tableData.add(dataList);
		actionPtrs.add(actionPtr);
	}
	
	public String getLatestKeyAndMask() {
		StringBuilder latestKeyAndMask = new StringBuilder();
		LinkedList<BitSet> key = tableKey.get(tableKey.size()-1);
		for(int i=0; i<key.size(); i++) {
			BitSet b = key.get(i);
			StringBuilder sb = new StringBuilder();
			for(int j=TCAM_NUM_BITS-1; j>=0; j -= 4) {
				BitSet part = b.get(j-3,j+1);
				sb.append(Utils.bitSetToHex(part));
			}
			L.debug("sos_loop[" + somId + "].somModel.tcam_data[" + (tableKey.size() - 1) + "][" + i + "] = 80'h"+sb.toString());
			latestKeyAndMask.append("sos_loop[" + somId + "].somModel.tcam_data[" + (tableKey.size() - 1) + "][" + i + "] = 80'h"+sb.toString()+";\n");
		}
		
		for(int i=0; i<keyMask.size(); i++) {
			BitSet b = keyMask.get(i);
			StringBuilder sb = new StringBuilder();
			for(int j=TCAM_NUM_BITS-1; j>=0; j -= 4) {
				BitSet part = b.get(j-3,j+1);
				sb.append(Utils.bitSetToHex(part));
			}
			L.debug("sos_loop[" + somId + "].somModel.tcam_mask[" + (tableKey.size() - 1) + "][" + i + "] = 80'h"+sb.toString());
			latestKeyAndMask.append("sos_loop[" + somId + "].somModel.tcam_mask[" + (tableKey.size() - 1) + "][" + i + "] = 80'h"+sb.toString()+";\n");
		}
		return latestKeyAndMask.toString();
	}
	
	public String getKey() {
		return getKeyOrDataOrMask(tableKey.get(tableKey.size()-1));
	}
	
	public String getMask() {
		return getKeyOrDataOrMask(keyMask);
	}
	
	public String getData() {
		return getKeyOrDataOrMask(tableData.get(tableData.size()-1));
	}
	
	public String getKeyOrDataOrMask(List<BitSet> bsl) {
		int totSize = 0;
		StringBuilder sb = new StringBuilder();
		for(int i=bsl.size()-1; i>=0; i--) {
			BitSet b = bsl.get(i);
			for(int j=TCAM_NUM_BITS-1; j>=0; j -= 4) {
				BitSet part = b.get(j-3,j+1);
				sb.append(Utils.bitSetToHex(part));
			}
			totSize += TCAM_NUM_BITS;
		}
		//L.debug(totSize + "'h" + sb.toString());
		return totSize + "'h" + sb.toString();
	}
	
	public String getDataValid() {
		BitSet datVld = new BitSet(4);
		for(int i=0; i<MEM_DSEG; i++)
			if(i<sramColumnCount)
				datVld.set(i);
		return Utils.bitSetToHex(datVld);
	}
	
	public Integer getDataSelect(Map<Integer, Integer> tableToCycleMap, Set<Integer> tablesConfigured) {
		int datSel = 0;
		Set<Integer> tables = new HashSet<Integer>();
		int cycleId = tableToCycleMap.get(tableId);
		for(Integer tableId: tableToCycleMap.keySet()) {
			if(tableToCycleMap.get(tableId) == cycleId) {
				tables.add(tableId);
			}
		}
		//Set<Integer> tables = cycleToTableMap.get(tableToCycleMap.get(tableId));
		if(tables != null && tables.size() > 1) {
			int tablesAlreadyConfigured = 0;
			for(Integer tid : tables) {
				if(tid != tableId && tablesConfigured.contains(tid))
					tablesAlreadyConfigured++;
			}
			datSel = tablesAlreadyConfigured * sramColumnCount;
			if(datSel >= NUM_DSEG) {
				throw new RuntimeException("Conflict detected when driving the data out of SOM. Please check the configuration.");
			}
		}
		return datSel;
	}
	
	public String getActionPtr() {
		return (new FW(actionPtrs.get(tableKey.size()-1), 8)).summary();
	}
	
	public Integer getTableId() {
		return tableId;
	}
	
	public Integer getSomId() {
		return somId;
	}
	
	public Integer getEntryId() {
		return tableData.size() - 1;
	}
	
	public String getLatestData(Map<Integer, Set<Integer>> cycleToTableMap, Map<Integer, Integer> tableToCycleMap, Set<Integer> tablesConfigured) {
		StringBuilder latestData = new StringBuilder();
		LinkedList<BitSet> data = tableData.get(tableData.size()-1);
		for(int i=0; i<data.size(); i++) {
			BitSet b = data.get(i);
			StringBuilder sb = new StringBuilder();
			for(int j=SRAM_NUM_BITS-1; j>=0; j -= 4) {
				BitSet part = b.get(j-3,j+1);
				sb.append(Utils.bitSetToHex(part));
			}
			L.debug("sos_loop[" + somId + "].somModel.sram_dat[" + (tableKey.size() - 1) + "][" + i + "] = 80'h" + sb.toString());
			latestData.append("sos_loop[" + somId + "].somModel.sram_dat[" + (tableKey.size() - 1) + "][" + i + "] = 80'h" + sb.toString()+";\n");
		}
		BitSet datVld = new BitSet(4);
		for(int i=0; i<MEM_DSEG; i++)
			if(i<sramColumnCount)
				datVld.set(i);
		int datSel = 0;
		Set<Integer> tables = new HashSet<Integer>();
		int cycleId = tableToCycleMap.get(tableId);
		for(Integer tableId: tableToCycleMap.keySet()) {
			if(tableToCycleMap.get(tableId) == cycleId) {
				tables.add(tableId);
			}
		}
		//Set<Integer> tables = cycleToTableMap.get(tableToCycleMap.get(tableId));
		if(tables != null && tables.size() > 1) {
			int tablesAlreadyConfigured = 0;
			for(Integer tid : tables) {
				if(tid != tableId && tablesConfigured.contains(tid))
					tablesAlreadyConfigured++;
			}
			datSel = tablesAlreadyConfigured * sramColumnCount;
			if(datSel >= NUM_DSEG) {
				throw new RuntimeException("Conflict detected when driving the data out of SOM. Please check the configuration.");
			}
		}
		L.debug("sos_loop[" + somId + "].somModel.sram_ptr[" + (tableKey.size() - 1) + "] = " + (new FW(actionPtrs.get(tableKey.size()-1), 8)).summary() + ";\n");
		L.debug("sos_loop[" + somId + "].somModel.cfg_tbl_sel  = " + tableId);
		L.debug("sos_loop[" + somId + "].somModel.cfg_dat_sel  = " + datSel);
		L.debug("sos_loop[" + somId + "].somModel.cfg_dat_vld  = " + Utils.bitSetToHex(datVld));
		L.debug("sos_loop[" + somId + "].somModel.cfg_miss_ptr  = 0");
		latestData.append("sos_loop[" + somId + "].somModel.sram_ptr[" + (tableKey.size() - 1) + "] = " + (new FW(actionPtrs.get(tableKey.size()-1), 8)).summary() + ";\n");
		latestData.append("sos_loop[" + somId + "].somModel.cfg_tbl_sel  = " + tableId + ";\n");
		latestData.append("sos_loop[" + somId + "].somModel.cfg_dat_sel  = " + datSel + ";\n");
		latestData.append("sos_loop[" + somId + "].somModel.cfg_dat_vld  = " + Utils.bitSetToHex(datVld) + ";\n");
		latestData.append("sos_loop[" + somId + "].somModel.cfg_miss_ptr  = 0;\n");
		
		return latestData.toString();
	}
	
	public String getDummyData() {
		int idx = dummyCounter - 1;
		StringBuilder dummyData = new StringBuilder();
		BitSet key = new BitSet(TCAM_NUM_BITS);
		BitSet mask = new BitSet(TCAM_NUM_BITS);
		BitSet data = new BitSet(SRAM_NUM_BITS);
		mask.set(0, TCAM_NUM_BITS);
		for(int i=0; i<MEM_KSEG; i++) {
			StringBuilder sb = new StringBuilder();
			for(int j=TCAM_NUM_BITS-1; j>=0; j -= 4) {
				BitSet part = key.get(j-3,j+1);
				sb.append(Utils.bitSetToHex(part));
			}
			L.debug("sos_loop[" + somId + "].somModel.tcam_data[" + idx + "][" + i + "] = 80'h"+sb.toString());
			dummyData.append("sos_loop[" + somId + "].somModel.tcam_data[" + idx + "][" + i + "] = 80'h"+sb.toString()+";\n");
		}
		for(int i=0; i<MEM_KSEG; i++) {
			StringBuilder sb = new StringBuilder();
			for(int j=TCAM_NUM_BITS-1; j>=0; j -= 4) {
				BitSet part = mask.get(j-3,j+1);
				sb.append(Utils.bitSetToHex(part));
			}
			L.debug("sos_loop[" + somId + "].somModel.tcam_mask[" + idx + "][" + i + "] = 80'h"+sb.toString());
			dummyData.append("sos_loop[" + somId + "].somModel.tcam_mask[" + idx + "][" + i + "] = 80'h"+sb.toString()+";\n");
		}
		for(int i=0; i<MEM_DSEG; i++) {
			StringBuilder sb = new StringBuilder();
			for(int j=SRAM_NUM_BITS-1; j>=0; j -= 4) {
				BitSet part = data.get(j-3,j+1);
				sb.append(Utils.bitSetToHex(part));
			}
			L.debug("sos_loop[" + somId + "].somModel.sram_dat[" + idx + "][" + i + "] = 80'h"+sb.toString());
			dummyData.append("sos_loop[" + somId + "].somModel.sram_dat[" + idx + "][" + i + "] = 80'h"+sb.toString()+";\n");
		}
		L.debug("sos_loop[" + somId + "].somModel.cfg_tbl_sel  = 0");
		L.debug("sos_loop[" + somId + "].somModel.cfg_dat_sel  = 0");
		L.debug("sos_loop[" + somId + "].somModel.cfg_dat_vld  = 0");
		L.debug("sos_loop[" + somId + "].somModel.cfg_miss_ptr  = 0");
		dummyData.append("sos_loop[" + somId + "].somModel.sram_ptr[" + idx + "] = 8'h0;\n");
		dummyData.append("sos_loop[" + somId + "].somModel.cfg_tbl_sel  = 0;\n");
		dummyData.append("sos_loop[" + somId + "].somModel.cfg_dat_sel  = 0;\n");
		dummyData.append("sos_loop[" + somId + "].somModel.cfg_dat_vld  = 0;\n");
		dummyData.append("sos_loop[" + somId + "].somModel.cfg_miss_ptr  = 0;\n");
		
		return dummyData.toString();
	}
	
	public String getTableTasks(Map<Integer, Integer> tableToCycleMap, Set<Integer> tablesConfigured) {
		StringBuilder sb = new StringBuilder();
		STGroup stGroup = new STGroupFile(SOMConfig.class.getResource(TASK_TEMPLATE), "Cp1252", '<', '>');
		
		ST tableAddTask = stGroup.getInstanceOf("table_add");
		tableAddTask.add("somId", somId);
		tableAddTask.add("tableName", tableName);
		tableAddTask.add("ifStmt", getIfStmt(stGroup));
		sb.append(tableAddTask.render());
		
		ST tableInitTask = stGroup.getInstanceOf("table_init");
		tableInitTask.add("somId", somId);
		tableInitTask.add("tableName", tableName);
		tableInitTask.add("tableSelect", tableId);
		tableInitTask.add("dataSelect", getDataSelect(tableToCycleMap, tablesConfigured));
		tableInitTask.add("dataValid", getDataValid());
		sb.append(tableInitTask.render());
		
		return sb.toString();
	}
	
	private String getIfStmt(STGroup stGroup) {
		StringBuilder sb = new StringBuilder();
		ST task;
		for(String action: actions) {
			task = sb.length() == 0 ? stGroup.getInstanceOf("if_stmt") : stGroup.getInstanceOf("else_if_stmt");
			task.add("somId", somId);
			task.add("actionName", action);
			task.add("actionPtr", (new FW(actionAddrMap.get(action), 8)).summary());
			sb.append(task.render());
		}
		if(sb.length() > 0) {
			task = stGroup.getInstanceOf("else_stmt");
			sb.append(task.render());
		}
		return sb.toString();
	}
	
	public boolean isInitialized() {
		return isInitialized;
	}
	
	public void setInitialized() {
		isInitialized = true;
	}
}
