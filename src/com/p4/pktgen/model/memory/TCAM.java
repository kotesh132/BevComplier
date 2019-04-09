package com.p4.pktgen.model.memory;

import java.io.File;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import lombok.Getter;

import com.p4.utils.FileUtils;
import com.p4.utils.Utils;
import com.p4.utils.Utils.Pair;

public class TCAM extends Memory {

	private Integer id;
	private Integer uid;
	private Integer startAddr;
	private Integer endAddr;
	private Integer tableId;
	private Integer segId;
	@Getter private Integer colId;
	@Getter private Integer rowId;
	private Integer somId;
	private Integer actualKeyWidth;
	
	private List<BitSet> memory;
	
	public TCAM(Integer depth, Integer width, Integer uid, Integer somId, Integer segId, Integer keyWidth) {
		super(depth, width);
		memory = new ArrayList<BitSet>(depth);
		this.uid = uid;
		this.somId = somId;
		this.segId = segId;
		actualKeyWidth = keyWidth;
		for(int i=0; i<depth; i++)
			memory.add(i, null);
	}
	
	@Override
	public void setAddrRange(Integer start, Integer end) {
		startAddr = start;
		endAddr = end;
	}

	@Override
	public void setTableId(Integer id) {
		tableId = id;
	}

	@Override
	public void setSegId(Integer id) {
		segId = id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public void addEntry(Integer index, BitSet value) {
		memory.set(index, value);
	}

	@Override
	public String emitModelConfig(Integer index) {
		return getWidth() + "'h" + Utils.bitSetToHex(memory.get(index), getWidth());
	}

	@Override
	public void emitRTLConfig() {
		
	}

	@Override
	public Pair<Integer, Integer> getAddrRange() {
		return Pair.of(startAddr, endAddr);
	}

	@Override
	public Integer getSegId() {
		return segId;
	}

	@Override
	public void emitKeyAndData(File outputDir) {
		
		File tcamFile1 = new File(outputDir.getAbsolutePath() + "/somTcamCfg.sv");
		File tcamFile2 = new File(outputDir.getAbsolutePath() + "/somTcamCfg.txt");
		File tcamFile3 = new File(outputDir.getAbsolutePath() + "/som"+ somId +"_AvagoTcamCfgX_row"+ rowId +"_col"+ colId +"_fd.txt");
		File tcamFile4 = new File(outputDir.getAbsolutePath() + "/som"+ somId +"_AvagoTcamCfgY_row"+ rowId +"_col"+ colId +"_fd.txt");
		File tcamFile5 = new File(outputDir.getAbsolutePath() + "/som"+ somId +"_AvagoTcamCfgX_row"+ rowId +"_col"+ colId +"_bd.txt");
		File tcamFile6 = new File(outputDir.getAbsolutePath() + "/som"+ somId +"_AvagoTcamCfgY_row"+ rowId +"_col"+ colId +"_bd.txt");
		File tcamFile7 = new File(outputDir.getAbsolutePath() + "/somAvagoTcamCfg.txt");
		
		try {
			int entry = 0;
			for(BitSet tcamMem : memory) {
				if(tcamMem != null) {
					FileUtils.writeToFile(tcamFile1, true, "MePpsTbTop.core.u_pps.u_som_cluster.u_som_core" + somId + ".tr_loop[" + rowId + "].tc_loop[" + colId + "].u_tcam.mem[" + entry + "] = " + getWidth() + "'h" + Utils.bitSetToHex(tcamMem) + ";\n");
					FileUtils.writeToFile(tcamFile2, true, somId + "\n" + rowId + "\n" + colId + "\n" + entry + "\n" + getWidth() + "'h" + Utils.bitSetToHex(tcamMem) + "\n");
					
					BitSet tcamMemNeg = (BitSet) tcamMem.clone();
					for(int i=0; i<actualKeyWidth; i++) {
						if(tcamMem.get(i))
							tcamMemNeg.clear(i);
						else
							tcamMemNeg.set(i);
					}
					FileUtils.writeToFile(tcamFile3, true, Utils.bitSetToHex(tcamMem, getWidth()) + "\n");
					FileUtils.writeToFile(tcamFile4, true, Utils.bitSetToHex(tcamMemNeg, getWidth()) + "\n");
					FileUtils.writeToFile(tcamFile5, true, Utils.bitSetToHex(tcamMem, getWidth()) + "\n");
					FileUtils.writeToFile(tcamFile6, true, Utils.bitSetToHex(tcamMemNeg, getWidth()) + "\n");
					FileUtils.writeToFile(tcamFile7, true, somId + "\n" + rowId + "\n" + colId + "\n" + entry + "\n" + getWidth() + "'h" + Utils.bitSetToHex(tcamMem) + "\n" + getWidth() + "'h" + Utils.bitSetToHex(tcamMemNeg) + "\n");
					
					BitSet bs = new BitSet(tcamMem.length());
					bs.set(0, tcamMem.length());
					FileUtils.writeToFile(tcamFile1, true, "MePpsTbTop.core.u_pps.u_som_cluster.u_som_core" + somId + ".tr_loop[" + rowId + "].tc_loop[" + colId + "].u_tcam.mem_mask[" + entry + "] = " + getWidth() + "'h" + Utils.bitSetToHex(bs) + ";\n");
					FileUtils.writeToFile(tcamFile2, true, somId + "\n" + rowId + "\n" + colId + "\n" + entry + "\n" + getWidth() + "'h" + Utils.bitSetToHex(bs) + "\n");
					entry++;
				}
			}
			/*for(int i=entry; i<getDepth(); i++) {
				BitSet tcamMem = new BitSet(getWidth());
				FileUtils.writeToFile(tcamFile5, true, Utils.bitSetToHex(tcamMem, getWidth()) + "\n");
				tcamMem.set(0,getWidth());
				FileUtils.writeToFile(tcamFile6, true, Utils.bitSetToHex(tcamMem, getWidth()) + "\n");
			}*/
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public void assignMemId(Pair<Integer, Integer> id) {
		rowId = id.first();
		colId = id.second();
	}

	@Override
	public Integer getDataWidth() {
		return getWidth();
	}
	
	@Override
	public String getData(Integer index, boolean isLogicalData) {
		return Utils.bitSetToHex(memory.get(index), getWidth());
	}

	@Override
	public String getNonData(Integer index) {
		return "";
	}
	
	@Override
	public BitSet getEntry(Integer index) {
		return memory.get(index);
	}
	
	@Override
	public Integer getId() {
		return uid;
	}
}
