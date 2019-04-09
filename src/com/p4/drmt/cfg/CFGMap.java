package com.p4.drmt.cfg;

import java.util.LinkedHashMap;
import java.util.Map;

import com.p4.p416.gen.ControlDeclarationContextExt;

import lombok.Data;
@Data
public class CFGMap {
	private Map<String, GraphMarker> map = new LinkedHashMap<>();
	private Map<String, CFGBuildingBlock> cfgmap = new LinkedHashMap<>();
	private ControlDeclarationContextExt currentCntrlBlock = null;
	private final boolean inlineTableApply;
	
	public static CFGMap noInline(){
		return new CFGMap(false);
	}
}
