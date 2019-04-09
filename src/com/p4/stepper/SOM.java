package com.p4.stepper;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.stepper.data.Action;
import com.p4.stepper.data.Context;
import com.p4.stepper.data.Key;
import com.p4.stepper.data.Table;
import com.p4.utils.Utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class SOM {

	private List<String> commands;
	private Map<String, Map<BitSet, ActionPtr>> keys = new HashMap<String, Map<BitSet, ActionPtr>>();
	private Map<String, Map<Integer, List<String>>> actionData = new HashMap<String, Map<Integer, List<String>>>();
	
	private final int KEY_SIZE = 160;
	
	private Logger L = LoggerFactory.getLogger(SOM.class);
	
	@AllArgsConstructor
	@Getter
	public class ActionPtr {
		private int rowPtr;
		private String actionName;
	}
	
	public SOM(List<String> cmds) {
		commands = cmds;
	}
	
	public void loadSOM(Context context) {
		
		for(String command : commands) {
			String[] cmdtokens = command.split("\\s+");
			try {
				String tableName = cmdtokens[1];
				String actionName = cmdtokens[2];
				String scopedActionName = actionName;
				if(!context.getTablesMap().containsKey(tableName))
					throw new RuntimeException("Required table missing");
				Table table = context.getTablesMap().get(tableName);
				String actionScope = table.getScope();
				
				Action action = null;
				if(!context.getActionsMap().containsKey(actionName+"_"+actionScope)) {
					if(!context.getActionsMap().containsKey(actionName+"_global")) {
						throw new RuntimeException("Required action missing");
					}
					else {
						action = context.getActionsMap().get(actionName+"_global");
						scopedActionName = actionName+"_global";
					}
				}
				else {
					action = context.getActionsMap().get(actionName+"_"+actionScope);
					scopedActionName = actionName+"_"+actionScope;
				}
					
				boolean isKey = true;
				int idx = 0;
				int keyIdx = 0;
				
				BitSet kbs = new BitSet(KEY_SIZE);
				List<String> aData = new ArrayList<String>();
				
				for(int i=3; i<cmdtokens.length; i++) {
					if(cmdtokens[i].equals("=>")) {
						isKey = false;
						continue;
					}
					if(isKey) {
						Key k = table.getKeys().get(idx++);
						BitSet keyValue = Utils.toBitSet(new BigInteger(cmdtokens[i].replaceAll("0x", ""), 16));
						for(int j=0; j<k.getSize(); j++) {
							if(keyValue.get(j)) {
								kbs.set(keyIdx);
							}
							keyIdx++;
						}
					}
					else {
						aData.add(cmdtokens[i]);
					}
				}
				
				if(!keys.containsKey(tableName))
					keys.put(tableName, new HashMap<BitSet, ActionPtr>());
				if(!actionData.containsKey(scopedActionName))
					actionData.put(scopedActionName, new HashMap<Integer, List<String>>());
				
				int rowPtr = keys.get(tableName).size() + 1;
				keys.get(tableName).put(kbs, new ActionPtr(rowPtr, scopedActionName));
				actionData.get(scopedActionName).put(rowPtr, aData);
			}
			catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException("somethign wrong with commands.txt file");
			}
		}
	}
	
	public ActionPtr tableMatch(String tableName, List<Key> tableKeys, String scope) {
		return tableMatchExact(tableName, tableKeys, scope);
	}
	
	private ActionPtr tableMatchExact(String tableName, List<Key> tableKeys, String scope) {
		BitSet fullKey = new BitSet(KEY_SIZE);
		int idx = 0;
		for(Key key: tableKeys) {
			for(int i=0; i<key.getSize(); i++) {
				if(key.getValue().get(i))
					fullKey.set(idx);
				idx++;
			}
		}
		
		if(keys.get(tableName).containsKey(fullKey)) {
			return keys.get(tableName).get(fullKey);
		}
		return null;
	}
	
	public List<String> getActionData(ActionPtr ptr) {
		return actionData.get(ptr.getActionName()).get(ptr.getRowPtr());
	}
}
