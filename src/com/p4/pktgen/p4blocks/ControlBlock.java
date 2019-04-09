package com.p4.pktgen.p4blocks;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import lombok.Getter;

import com.p4.drmt.cfg.KeyMeta;
import com.p4.p416.gen.ActionDeclarationContextExt;
import com.p4.p416.gen.ControlDeclarationContextExt;
import com.p4.p416.gen.TableDeclarationContextExt;

public class ControlBlock {

	private ControlDeclarationContextExt controlBlock;
	
	private Map<String, ActionDeclarationContextExt> actions = new HashMap<String, ActionDeclarationContextExt>();
	private Map<String,TableDeclarationContextExt> tables = new HashMap<String, TableDeclarationContextExt>();
	
	@Getter private Map<String, ActionBlock> actionsExtracted = new HashMap<String, ActionBlock>();
	@Getter private Map<String, TableBlock> tablesExtracted = new HashMap<String, TableBlock>();
	
	private Set<Integer> tableIds = new HashSet<Integer>();
	
	public ControlBlock(ControlDeclarationContextExt controlBlock, KeyMeta km) {
		this.controlBlock = controlBlock;
		System.out.println(controlBlock.getFormattedText());
		controlBlock.getTables(tables);
		controlBlock.getActions(actions);
		
		for(Map.Entry<String,TableDeclarationContextExt> tableEntry: tables.entrySet()) {
			tablesExtracted.put(tableEntry.getKey(), new TableBlock(tableEntry.getValue(), km));
			tableIds.add(tableEntry.getValue().getTableId());
		}
		
		for(Map.Entry<String,ActionDeclarationContextExt> actionEntry: actions.entrySet()) {
			actionsExtracted.put(actionEntry.getKey(), new ActionBlock(actionEntry.getValue()));
		}
	}
	
	public boolean isKnownTableId(Integer tableId) {
		return tableIds.contains(tableId);
	}
}
