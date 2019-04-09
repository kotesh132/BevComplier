package com.p4.stepper.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Step {

	private String type;
	private String predicate;
	private String table;
	private Assignment assignment;
	
	public Step(Step.UnNormalized un) {
		type = un.type;
		table = un.table;
		predicate = un.predicate;
		if(un.assignment != null) {
			assignment = new Assignment(un.assignment);
		}
	}
	
	@NoArgsConstructor
	public static class UnNormalized {
		public String type;
		public String predicate;
		public String table;
		public Assignment.UnNormalized assignment;
	}
}
