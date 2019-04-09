package com.p4.drmt.cfg;

import java.util.LinkedList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TableMeta {
	private String defaultAction;
	private List<String> actions;
	
	public void addAction(String action) {
		if(actions == null) {
			actions = new LinkedList<String>();
		}
		actions.add(action);
	}
}
