package com.p4.drmt.parser;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Parameter {

	private final String name;
	private final Integer value;
	
	public static class ParameterSet{
		private final Map<String,Integer> map = new HashMap<>();
		
		public void addParameter(Parameter p){
			map.put(p.name,p.value);
		}
	}
}
