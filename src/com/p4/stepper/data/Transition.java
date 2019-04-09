package com.p4.stepper.data;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Transition {

	private List<String> select;
	private List<String> values;
	private String nextState; 
	
	public Transition(Transition.UnNormalized un) {
		select = un.select;
		values = un.values;
		nextState = un.nextState;
	}
	
	@NoArgsConstructor
	public static class UnNormalized {
		public List<String> select;
		public List<String> values;
		public String nextState;
	}
}
