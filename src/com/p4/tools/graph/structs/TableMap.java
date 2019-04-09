package com.p4.tools.graph.structs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import com.p4.utils.Utils;

import lombok.Getter;

@Getter
public class TableMap {

	HashMap<String,String> map = new HashMap<>();
	
	Stack<ControlMetaData> control = new Stack<>();
	
	List<ControlMetaData> processed = new ArrayList<>();
	
	public void add(String control, String table){
		map.put(control, table);
	}
	
	public ControlMetaData currentControlScope(){
		return control.peek();
	}
	
	public void popCurrentControlScope(){
		processed.add(control.pop());
	}
	
	public void addControlScope(String item){
		control.push(new ControlMetaData(item, new ControlGraph(item), false));
	}
	
	public void markEnterApplyControl(){
		control.peek().setApplyTraverseStatus(true);
	}
	
	public void markExitApplyControl(){
		control.peek().setApplyTraverseStatus(false);
	}
	
	public String tmToString(){
		return Utils.summary(map);
	}
}
