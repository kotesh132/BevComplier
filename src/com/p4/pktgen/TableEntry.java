package com.p4.pktgen;

import java.util.BitSet;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TableEntry {

	private String tableName;
	private String actionName;
	private Map<String,BitSet> keys;
	private BitSet mask;
	private Map<String,BitSet> actionParams;
	private BitSet instptr;
	
}
