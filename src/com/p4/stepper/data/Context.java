package com.p4.stepper.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties({"tablesMap", "actionsMap", "parserMap"})
public class Context {

	private Map<String, Integer[]> headerOffsetAndSize;
	private Map<String, Integer> predicateOffsets;
	private List<ParserState> parser;
	private List<Action> actions;
	private List<Table> tables;
	private List<Step> schedule;
	
	private Map<String, Table> tablesMap = new HashMap<String, Table>();
	private Map<String, Action> actionsMap = new HashMap<String, Action>();
	private Map<String, ParserState> parserMap = new HashMap<String, ParserState>();
	
	public Context(Context.UnNormalized un) {
		headerOffsetAndSize = un.headerOffsetAndSize;
		predicateOffsets = un.predicateOffsets;
		if(un.parser != null) {
			parser = new ArrayList<ParserState>();
			for(ParserState.UnNormalized pUn: un.parser) {
				parser.add(new ParserState(pUn));
			}
		}
		if(un.actions != null) {
			actions = new ArrayList<Action>();
			for(Action.UnNormalized aUn: un.actions) {
				actions.add(new Action(aUn));
			}
		}
		if(un.tables != null) {
			tables = new ArrayList<Table>();
			for(Table.UnNormalized tUn: un.tables) {
				tables.add(new Table(tUn));
			}
		}
		if(un.schedule != null) {
			schedule = new ArrayList<Step>();
			for(Step.UnNormalized sUn: un.schedule) {
				schedule.add(new Step(sUn));
			}
		}
		init();
	}
	
	@NoArgsConstructor
	public static class UnNormalized {
		public Map<String, Integer[]> headerOffsetAndSize;
		public Map<String, Integer> predicateOffsets;
		public List<ParserState.UnNormalized> parser;
		public List<Action.UnNormalized> actions;
		public List<Table.UnNormalized> tables;
		public List<Step.UnNormalized> schedule;
	}
	
	private void init() {
		if(tables != null) {
			for(Table t : tables) {
				tablesMap.put(t.getName(), t);
			}
		}
		
		if(actions != null) {
			for(Action a : actions) {
				actionsMap.put(a.getName()+"_"+a.getScope(), a);
			}
		}
		
		if(parser != null) {
			for(ParserState state: parser) {
				parserMap.put(state.getName(), state);
			}
		}
	}
}
