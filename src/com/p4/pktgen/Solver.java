package com.p4.pktgen;

import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.microsoft.z3.BoolExpr;
import com.p4.p416.applications.CFGNode;
import com.p4.p416.gen.AssignmentStatementContextExt;
import com.p4.p416.gen.ExpressionContextExt;
import com.p4.packetgen.Packet;
import com.p4.packetgen.Z3Solver;
import com.p4.utils.Utils;
import com.p4.utils.Utils.Pair;

public class Solver {
	
	private List<CFGNode> constraints;
	private Packet packet;
	private Map<String, Pair<Integer, Integer>> hdrOffsetsNSizes;
	private Map<String, String> solvedHeaderValues;
	private Map<String,Integer> hdrValidFieldOffsets;
	private Z3Solver solver;
	
	public Solver(List<CFGNode> constraints, Packet packet, Map<String, Pair<Integer, Integer>> hdrOffsetsNSizes, Map<String,Integer> hdrValidFieldOffsets) {
		this.constraints = constraints;
		this.packet = packet;
		this.hdrOffsetsNSizes = hdrOffsetsNSizes;
		this.solvedHeaderValues = new HashMap<String, String>();
		this.hdrValidFieldOffsets = hdrValidFieldOffsets;
	}

	public void solve() {
		
		solver = new Z3Solver();
		BitSet bitVector = (BitSet) packet.getBitVector().clone();
		
		Map<String,Integer[]> symbolOffsetAndSize = new HashMap<String,Integer[]>();
		for(CFGNode cnode: constraints) {
			if(cnode instanceof ExpressionContextExt) {
				ExpressionContextExt expCtxE = (ExpressionContextExt) cnode;
				solver.addConstraint((BoolExpr) expCtxE.gatherSymbolsAndConstraints(solver, bitVector, hdrValidFieldOffsets, packet.getfieldOffsetAndSize()));
				for(String sym : solver.getListOfSymbols()) {
					if(!symbolOffsetAndSize.containsKey(sym) && hdrOffsetsNSizes.containsKey(sym)) {
						int offset = hdrOffsetsNSizes.get(sym).first();
						int size = hdrOffsetsNSizes.get(sym).second();
						symbolOffsetAndSize.put(sym, new Integer[]{offset, size});
					}
				}
			}
			else if(cnode instanceof AssignmentStatementContextExt) {
				AssignmentStatementContextExt assnCtxE = (AssignmentStatementContextExt) cnode;
				solver.addConstraint((BoolExpr) assnCtxE.gatherSymbolsAndConstraints(solver, bitVector, hdrValidFieldOffsets, packet.getfieldOffsetAndSize()));
				for(String sym : solver.getListOfSymbols()) {
					if(!symbolOffsetAndSize.containsKey(sym) && hdrOffsetsNSizes.containsKey(sym)) {
						int offset = hdrOffsetsNSizes.get(sym).first();
						int size = hdrOffsetsNSizes.get(sym).second();
						symbolOffsetAndSize.put(sym, new Integer[]{offset, size});
					}
				}
			}
		}
		solver.solve();
		if(solver.isSolved()) {
			Map<String,Integer> initialValues = solver.getSymbolInitialValues();
			for(Map.Entry<String, Integer> kv : initialValues.entrySet()) {
				//System.out.println(kv.getKey());
				//System.out.println(symbolOffsetAndSize.get(kv.getKey())[0]);
				if(symbolOffsetAndSize.get(kv.getKey()) != null && packet.isHeaderVariable(symbolOffsetAndSize.get(kv.getKey())[0])) {
					packet.setValue(Utils.stringToBitSet(kv.getValue().toString(), symbolOffsetAndSize.get(kv.getKey())[1], 10, false), symbolOffsetAndSize.get(kv.getKey())[0], symbolOffsetAndSize.get(kv.getKey())[1]);
					solvedHeaderValues.put(kv.getKey(), Integer.toHexString(kv.getValue()));
				}
			}
		}
	}
	
	public boolean isSolved() {
		return solver.isSolved();
	}
	
	public Packet getPacket() {
		return packet;
	}
	
	public Map<String, String> getSolvedFields() {
		return solvedHeaderValues;
	}
	
	public Map<String,Integer> getSolvedInitialValues() {
		return solver.getSymbolInitialValues();
	}
}
