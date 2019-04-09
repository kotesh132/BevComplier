package com.p4.stepper.runner;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;

import com.p4.p416.gen.AbstractBaseExt;
import com.p4.p416.gen.ExpressionContextExt;
import com.p4.stepper.Constants;
import com.p4.stepper.EmitUtils;
import com.p4.stepper.SOM;
import com.p4.stepper.SOM.ActionPtr;
import com.p4.stepper.data.Action;
import com.p4.stepper.data.Assignment;
import com.p4.stepper.data.Context;
import com.p4.stepper.data.Field;
import com.p4.stepper.data.Key;
import com.p4.stepper.data.ParserState;
import com.p4.stepper.data.Step;
import com.p4.stepper.data.Table;
import com.p4.stepper.data.Transition;
import com.p4.stepper.types.StepType;
import com.p4.utils.Utils;

public class StepperSession {
	private Context context;
	private BitSet packetByteVector;
	private BitSet packetBitVector;
	private BitSet prevByV;
	private BitSet prevBiV;
	@Getter private BitSet packetOut;
	private SOM som;
	
	private final Logger L = LoggerFactory.getLogger(StepperSession.class);
	
	public StepperSession(Context c, BitSet by, BitSet bi, SOM s) {
		context = c;
		packetByteVector = (BitSet) by.clone();
		packetBitVector = (BitSet) bi.clone();
		prevByV = (BitSet) by.clone();
		prevBiV = (BitSet) bi.clone();
		som = s;
	}
	
	public void run() {
		L.info("Starting schedule");
		L.info("==========================================================");
		for(Step step: context.getSchedule()) {
			L.info("\n");
			switch(StepType.valueOf(step.getType())) {
				case match: execMatch(step); break;
				case action: execAssignment(step.getAssignment(), new HashMap<String, BigInteger>());break;
				default: throw new RuntimeException("unrecognized type " + step.getType());
			}
		}
		assemblePacket();
	}
	
	private ParserState getNextState(String currentState, int headerIdx) {
		ParserState currState = context.getParserMap().get(currentState);
		if(currState.getTransitions() != null && currState.getTransitions().size() > 0) {
			for(Transition transition: currState.getTransitions()) {
				String nextState = transition.getNextState();
				if(nextState.equals(Constants.ACCEPT))
					return null;
				ParserState pstate = context.getParserMap().get(nextState);
				Integer validLoc = pstate.getValidLoc();
				if(packetBitVector.get(validLoc))
					return pstate;
			}
		}
		//something wrong, stop here
		throw new RuntimeException("Unexpected error: Transition is null");
	}
	
	private void assemblePacket() {
		String current = Constants.START;
		int headerIdx = 0;
		int prevHeaderIdx = 0;
		packetOut = new BitSet(Constants.PACKETLEN);
		while(true) {
			ParserState next = getNextState(current, prevHeaderIdx);
			if(next == null) break;
			//System.out.println("extracting - " + next.getName());
			for(Field f: next.getFields()) {
				for(int i=0; i<f.getSize(); i++) {
					if(packetByteVector.get(f.getDstOffset() + i))
						packetOut.set(headerIdx + f.getSrcOffset() + i);
				}
					
			}
			prevHeaderIdx = headerIdx;
			headerIdx += next.getHeaderSize();
			current = next.getName();
		}
	}
	
	private void execMatch(Step step) {
		if(step.getPredicate() != null && !packetBitVector.get(context.getPredicateOffsets().get(step.getPredicate()))) {
			return;
		}
		String tableName = step.getTable();
		L.info("MATCH : " + tableName);
		Table table = context.getTablesMap().get(tableName);
		List<Key> keys = new ArrayList<Key>();
		for(Key k : table.getKeys()) {
			Key newkey = new Key(k.getName(), k.getType(), k.getPvOffset(), k.getSize(), packetByteVector.get(k.getPvOffset(), k.getPvOffset() + k.getSize()));
			newkey.summary();
			keys.add(newkey);
		}
		ActionPtr actionPtr = som.tableMatch(tableName, keys, table.getScope());
		if(actionPtr != null) {
			L.info("Table hit, action to be executed - " + actionPtr.getActionName());
			execAction(actionPtr);
		} else {
			L.info("Table miss");
		}
	}
	
	private void execAction(ActionPtr ptr) {
		List<String> actionData = som.getActionData(ptr);
		L.info("Action data returned by SOM:");
		StringBuilder sb = new StringBuilder();
		for(String s: actionData) {
			if(sb.length() > 0) sb.append(",");
			sb.append(s);
		}
		L.info(sb.toString());
		Action action = context.getActionsMap().get(ptr.getActionName());
		Map<String, BigInteger> actionDataMap = new HashMap<String, BigInteger>();
		for(int i=0; i<actionData.size(); i++) {
			actionDataMap.put(action.getActionParams().get(i), new BigInteger(actionData.get(i).replaceAll("0x", ""), 16));
		}
		for(Assignment assign : action.getAssignments()) {
			execAssignment(assign, actionDataMap);
		}
	}
	
	private void execAssignment(Assignment assign, Map<String, BigInteger> actionDataMap) {
		if(assign.getPredicate() != null && !packetBitVector.get(context.getPredicateOffsets().get(assign.getPredicate()))) {
			return;
		}
		L.info("ACTION : " + assign.getLhs() + " = " + assign.getRhs());
		ExpressionContextExt expr = AbstractBaseExt.getExpression(assign.getRhs());
		BigInteger value = expr.eval(packetByteVector, packetBitVector, context.getHeaderOffsetAndSize(), context.getPredicateOffsets(), actionDataMap);
		String lhs = assign.getLhs();
		if(actionDataMap.containsKey(lhs))
			actionDataMap.put(lhs, value);
		else if(context.getPredicateOffsets().containsKey(lhs)) {
			if(value.intValue() == 0)
				packetBitVector.clear(context.getPredicateOffsets().get(lhs));
			else
				packetBitVector.set(context.getPredicateOffsets().get(lhs));
		}
		else if(context.getHeaderOffsetAndSize().containsKey(lhs)) {
			BitSet bs = Utils.toBitSet(value);
			Integer[] offsetAndSize = context.getHeaderOffsetAndSize().get(lhs);
			for(int i=0; i<offsetAndSize[1]; i++) {
				if(bs.get(i))
					packetByteVector.set(offsetAndSize[0] + i);
				else
					packetByteVector.clear(offsetAndSize[0] + i);
			}
		}
		else 
			throw new RuntimeException("could not find lhs value anywhere - " + lhs);
		EmitUtils.emit(context, packetByteVector, packetBitVector);
		L.info("=============================== DIFF ====================================");
		EmitUtils.emitByteVectorDiff(packetByteVector, prevByV);
		EmitUtils.emitBitVectorDiff(packetBitVector, prevBiV);
		prevByV = (BitSet) packetByteVector.clone();
		prevBiV = (BitSet) packetBitVector.clone();
		L.info("\n");
	}
}
