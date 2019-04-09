package com.p4.stepper.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Assignment {
	private String lhs;
	private String rhs;
	private String predicate;
	
	public Assignment(Assignment.UnNormalized un) {
		lhs = un.lhs;
		rhs = un.rhs;
		predicate = un.predicate;
	}
	
	@NoArgsConstructor
	public static class UnNormalized {
		public String lhs;
		public String rhs;
		public String predicate;
	}
}
