package com.p4.stepper.data;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Action {
	private String name;
	private Integer id;
	private String scope;
	private List<String> actionParams;
	private List<Integer> paramWidths;
	private List<Assignment> assignments;
	
	public Action(Action.UnNormalized un) {
		name = un.name;
		id = un.id;
		scope = un.scope;
		actionParams = un.actionParams;
		paramWidths = un.paramWidths;
		if(un.assignments != null) {
			assignments = new ArrayList<Assignment>();
			for(Assignment.UnNormalized aUn: un.assignments) {
				assignments.add(new Assignment(aUn));
			}
		}
	}
	
	@NoArgsConstructor
	public static class UnNormalized {
		public String name;
		public Integer id;
		public String scope;
		public List<String> actionParams;
		public List<Integer> paramWidths;
		public List<Assignment.UnNormalized> assignments;
	}
}
