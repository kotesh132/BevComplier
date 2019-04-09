package com.p4.stepper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.map.ObjectMapper;

import com.p4.drmt.GlobalAddress;
import com.p4.drmt.parser.P4Extract;
import com.p4.drmt.parser.P4KeySet;
import com.p4.drmt.parser.P4Parser;
import com.p4.drmt.parser.P4Parsers;
import com.p4.drmt.parser.P4State;
import com.p4.drmt.cfg.CFGBuildingBlock;
import com.p4.drmt.cfg.CFGMap;
import com.p4.drmt.parser.SourceDestinationSize;
import com.p4.p416.applications.CFGNode;
import com.p4.p416.applications.TargetSymbol;
import com.p4.p416.applications.Target.MemoryType;
import com.p4.p416.gen.AbstractBaseExt;
import com.p4.p416.gen.ActionDeclarationContextExt;
import com.p4.p416.gen.ApplyMethodCallContextExt;
import com.p4.p416.gen.AssignmentStatementContextExt;
import com.p4.p416.gen.ControlDeclarationContextExt;
import com.p4.p416.gen.P416Parser.BitSizeTypeContext;
import com.p4.p416.gen.P416Parser.KeyContext;
import com.p4.p416.gen.P416Parser.KeyElementContext;
import com.p4.p416.gen.P416Parser.ParameterContext;
import com.p4.p416.gen.P416Parser.ParameterListContext;
import com.p4.p416.gen.P416Parser.TablePropertyContext;
import com.p4.p416.gen.TableDeclarationContextExt;
import com.p4.p416.gen.TypeRefContextExt;
import com.p4.p416.gen.VariableDeclarationContextExt;
import com.p4.stepper.data.Action;
import com.p4.stepper.data.Assignment;
import com.p4.stepper.data.Context;
import com.p4.stepper.data.Field;
import com.p4.stepper.data.Key;
import com.p4.stepper.data.ParserState;
import com.p4.stepper.data.Step;
import com.p4.stepper.data.Table;
import com.p4.stepper.data.Transition;
import com.p4.stepper.types.MatchType;
import com.p4.stepper.types.StepType;
import com.p4.tools.graph.Graph;

public class StepperUtils {
	
	private static final String STEPPER_JSON = "stepper.json";

	public static void emitJson(File outputDir, List<String> fields, GlobalAddress ga, List<CFGNode> cfg, P4Parsers parser, Map<String,ControlDeclarationContextExt> controlBlocks) {
		
		Map<String, Integer[]> headerOffsetAndSize = getHeaderOffsetAndSize(fields, ga);
		Map<String, Integer> predicateOffsets = getPredicateOffsets(cfg);
		List<Step> schedule = getSchedule(cfg);
		List<ParserState> parsers = getParser(parser, predicateOffsets);
		List<Table> tables = getTables(controlBlocks, headerOffsetAndSize);
		List<Action> actions = getActions(controlBlocks);
		
		Context context = new Context();
		context.setHeaderOffsetAndSize(headerOffsetAndSize);
		context.setPredicateOffsets(predicateOffsets);
		context.setSchedule(schedule);
		context.setParser(parsers);
		context.setTables(tables);
		context.setActions(actions);
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.writeValue(new File(outputDir.getAbsolutePath() + File.separator + STEPPER_JSON), context);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static List<Action> getActions(Map<String,ControlDeclarationContextExt> controlBlocks) {
		List<Action> actions = new ArrayList<Action>();
		
		for (Map.Entry<String,ControlDeclarationContextExt> controlEntry : controlBlocks.entrySet()) {
			String scope = controlEntry.getKey();
			Map<String,ActionDeclarationContextExt> controlActions = controlEntry.getValue().getActions();
			for(Entry<String,ActionDeclarationContextExt> actionEntry : controlActions.entrySet()) {
				ParameterListContext plc = actionEntry.getValue().getContext().parameterList();
				List<Integer> actionDataWidths = new ArrayList<Integer>();
				List<String> actionDataParamNames = new ArrayList<String>();
				if(plc != null && plc.parameter() != null) {
					for(ParameterContext param : plc.parameter()) {
						String paramName = AbstractBaseExt.getExtendedContext(param.name()).getFormattedText();
						
						String num = AbstractBaseExt.getExtendedContext( ((BitSizeTypeContext)AbstractBaseExt.getExtendedContext(((TypeRefContextExt)AbstractBaseExt.getExtendedContext(param.typeRef())).getContext().baseType()).getContext()).number()).getFormattedText();
						Integer size = Integer.parseInt(num);
						//Integer size = Integer.parseInt(((BitSizeTypeContext)param.typeRef().baseType().extendedContext.getContext()).number().extendedContext.getFormattedText());
						actionDataWidths.add(size);
						actionDataParamNames.add(paramName);
					}
				}
				CFGMap cfgmap = CFGMap.noInline();
				CFGBuildingBlock actionCfg = actionEntry.getValue().buildNGetCFG(cfgmap);
				Graph<CFGNode> actionGraph = actionCfg.getGraph();
				List<CFGNode> nodes = actionGraph.topologicalSort();
				List<Assignment> assignments = new ArrayList<Assignment>();
				for(CFGNode pn: nodes) {
					if(pn instanceof AssignmentStatementContextExt) {
						AssignmentStatementContextExt instruction = (AssignmentStatementContextExt) pn;
						String lhs = instruction.getContext().lvalue().getText();
						String rhs = instruction.getContext().expression().getText();
						String predicate = instruction.getPredicateSymbol();
						assignments.add(new Assignment(lhs, rhs, predicate));
					}
				}
				actions.add(new Action(actionEntry.getKey(), actionEntry.getValue().getActionId(), scope, actionDataParamNames, actionDataWidths, assignments));
			}
		}
		return actions;
	}
	
	private static List<Table> getTables(Map<String,ControlDeclarationContextExt> controlBlocks, Map<String, Integer[]> headerOffsetAndSize) {
		List<Table> tables = new ArrayList<Table>();
		
		for (Map.Entry<String,ControlDeclarationContextExt> controlEntry : controlBlocks.entrySet()) {
			String scope = controlEntry.getKey();
			Map<String,TableDeclarationContextExt> controlTables = controlEntry.getValue().getTables();
			for(Entry<String,TableDeclarationContextExt> tableEntry : controlTables.entrySet()) {
				TableDeclarationContextExt t = tableEntry.getValue();
				TablePropertyContext propertyContext = t.getKeyTablePropertyContext().getContext();
				List<Key> keys = new ArrayList<Key>();
				if(propertyContext instanceof KeyContext) {
					KeyContext keyContext = (KeyContext) propertyContext;
					if(keyContext.keyElementList() != null) {
						for(KeyElementContext keyElementContext : keyContext.keyElementList().keyElement()) {
							String keyName = AbstractBaseExt.getExtendedContext(keyElementContext.expression()).getFormattedText();
							Integer[] offsetAndSize = headerOffsetAndSize.get(keyName);
							keys.add(new Key(AbstractBaseExt.getExtendedContext(keyElementContext.expression()).getFormattedText(), 
									MatchType.valueOf( AbstractBaseExt.getExtendedContext(keyElementContext.name()).getFormattedText()), 
									offsetAndSize[0], offsetAndSize[1]));
						}
					}
				}
				tables.add(new Table(t.getTableName(), t.getTableId(), scope, keys));
			}
		}
		return tables;
	}
	
	private static List<ParserState> getParser(P4Parsers parser, Map<String, Integer> predicateOffsets) {
		List<ParserState> parsers = new ArrayList<ParserState>();
		P4Parser p4parser = parser.getAllParsers().get(0);
		for(P4State state : p4parser.getStates()) {
			List<String> select = state.getTransition().getSelect() != null ? state.getTransition().getSelect().getExpressions() : null;
			List<Transition> transitions = new ArrayList<Transition>();
			if(state.getTransition().getSelect() != null) {
				for(P4KeySet keyset : state.getTransition().getSelect().getTransitions()){
					List<String> values = new ArrayList<String>();
					for(String c: keyset.getCasest()) {
						c = c.replaceFirst("\\d+w", "");
						values.add(c);
					}
					transitions.add(new Transition(select, values, keyset.getState()));
				}
			}
			else {
				transitions.add(new Transition(null, null, state.getTransition().getNextState()));
			}
			String name = state.getName();
			String validField = null;
			Integer validLoc = -1;
			List<Field> fields = new ArrayList<Field>();
			int size = 0;
			for(P4Extract extract:state.getExtracts()){
				
				for(SourceDestinationSize field : extract.getFields()) {
					fields.add(new Field(field.getFullName(), field.getSourceBit(), field.getDestBit(), field.getSize()));
					size += field.getSize();
				}
				validField = extract.getValidField();
				validLoc = extract.getValidLoc();
				predicateOffsets.put(validField, validLoc);
			}
			parsers.add(new ParserState(name, validField, validLoc, size, fields, transitions));
		}
		return parsers;
	}
	
	private static Map<String, Integer[]> getHeaderOffsetAndSize(List<String> fields, GlobalAddress ga) {
		Map<String, Integer[]> headerOffsetAndSize = new HashMap<String, Integer[]>();
		for(String field: fields) {
			headerOffsetAndSize.put(field, new Integer[]{ga.getFields().get(field), ga.getSizes().get(field)});
		}
		return headerOffsetAndSize;
	}
	
	private static List<Step> getSchedule(List<CFGNode> cfg) {
		List<Step> schedule = new ArrayList<Step>();
		for(CFGNode pn : cfg) {
			if(pn instanceof AssignmentStatementContextExt) {
				AssignmentStatementContextExt instruction = (AssignmentStatementContextExt) pn;
				String lhs = instruction.getContext().lvalue().getText();
				String rhs = instruction.getContext().expression().getText();
				String predicate = instruction.getPredicateSymbol();
				Assignment assignment = new Assignment(lhs, rhs, predicate);
				Step step = new Step(StepType.action.name(), predicate, null, assignment);
				schedule.add(step);
			}
			else if(pn.isTableApplyNode()) {
				TableDeclarationContextExt t = (TableDeclarationContextExt) pn.getTableApplyNode();
				
				ApplyMethodCallContextExt applyMethodCallContextExt = (ApplyMethodCallContextExt) pn;
				String predicate = null;
				if(applyMethodCallContextExt.getPredicateSymbol()!=null){
					predicate = AbstractBaseExt.getExpression(applyMethodCallContextExt.getPredicateSymbol()).TerminalValue();
				}
				Step step = new Step(StepType.match.name(), predicate, t.getTableName(), null);
				schedule.add(step);
			}
		}
		return schedule;
	}
	
	private static Map<String, Integer> getPredicateOffsets(List<CFGNode> cfg) {
		Map<String, Integer> predicateOffsets = new HashMap<String, Integer>();
		
		for(CFGNode pn : cfg) {
			if(pn instanceof VariableDeclarationContextExt) {
				VariableDeclarationContextExt var = (VariableDeclarationContextExt) pn;
				if(var.getContext() == null) continue;
				String variable = var.getContext().name().getText();
				TargetSymbol ts = (TargetSymbol) var.resolve(variable);
				int offset = Integer.MIN_VALUE;
				if(ts.getMemoryType().equals(MemoryType.TypePktBit)){
					offset = var.getAlignedByteOffset(variable);
				}
				predicateOffsets.put(variable, offset);
			}
		}
		
		return predicateOffsets;
	}
}
