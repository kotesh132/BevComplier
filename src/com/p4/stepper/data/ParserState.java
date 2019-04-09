package com.p4.stepper.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ParserState {
	private String name;
	private String validField;
	private Integer validLoc;
	private Integer headerSize;
	private List<Field> fields;
	private List<Transition> transitions;
	
	public ParserState(ParserState.UnNormalized un) {
		name = un.name;
		validField = un.validField;
		validLoc = un.validLoc;
		headerSize = un.headerSize;
		if(un.fields != null) {
			fields = new ArrayList<Field>();
			for(Field.UnNormalized fUn : un.fields) {
				fields.add(new Field(fUn));
			}
		}
		if(un.transitions != null) {
			transitions = new ArrayList<Transition>();
			for(Transition.UnNormalized tUn: un.transitions) {
				transitions.add(new Transition(tUn));
			}
		}
	}
	
	@NoArgsConstructor
	public static class UnNormalized {
		public String name;
		public String validField;
		public Integer validLoc;
		public Integer headerSize;
		public List<Field.UnNormalized> fields;
		public List<Transition.UnNormalized> transitions;
	}
	
	public Map<String, Field> getFieldsMap() {
		Map<String, Field> fieldsMap = new HashMap<String, Field>();
		if(fields != null) {
			for(Field f: fields) {
				fieldsMap.put(f.getName(), f);
			}
		}
		return fieldsMap;
	}
}
