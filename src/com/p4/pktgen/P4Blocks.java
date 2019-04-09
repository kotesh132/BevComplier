package com.p4.pktgen;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

import com.p4.drmt.cfg.KeyMeta;
import com.p4.p416.gen.ControlDeclarationContextExt;
import com.p4.pktgen.p4blocks.ControlBlock;

public class P4Blocks {

	private Map<String,ControlDeclarationContextExt> controlBlocks;
	
	@Getter private Map<String, ControlBlock> controlBlocksExtracted = new HashMap<String, ControlBlock>();
	
	private static P4Blocks instance = null;
	
	public static void createInstance(Map<String,ControlDeclarationContextExt> blocks, KeyMeta km) {
		instance = new P4Blocks(blocks, km);
	}
	
	public static P4Blocks getInstance() {
		if(instance == null)
			throw new RuntimeException("Error. P4Blocks class is not yet initialized");
		return instance;
	}
	
	private P4Blocks(Map<String,ControlDeclarationContextExt> blocks, KeyMeta km) {
		controlBlocks = blocks;
		for (Map.Entry<String,ControlDeclarationContextExt> controlEntry : controlBlocks.entrySet()) {
			controlBlocksExtracted.put(controlEntry.getKey(), new ControlBlock(controlEntry.getValue(), km));
		}
	}
	
	public String getControlBlockOfTable(Integer tableId) {
		for (Map.Entry<String,ControlBlock> controlEntry : controlBlocksExtracted.entrySet()) {
			if(controlEntry.getValue().isKnownTableId(tableId))
				return controlEntry.getKey();
		}
		throw new RuntimeException("unable to figure out the conrtol block of given table id : " + tableId);
	}
}
