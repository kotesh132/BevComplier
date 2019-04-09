package com.p4.packetgen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;

import com.microsoft.z3.*;

public class Z3Solver {
	
	private static final Logger L = LoggerFactory.getLogger(Z3Solver.class);
	com.microsoft.z3.Solver solver = null;
	List<BoolExpr> constraints = new LinkedList<BoolExpr>();
	Map<String, List<IntExpr>> symbols = new HashMap<String, List<IntExpr>>();
	@Getter
	Context ctx = null;
	Model model = null;
	@Getter
	Status status = null;
	
	public Z3Solver() {
		HashMap<String, String> cfg = new HashMap<String, String>();
		cfg.put("model", "true");
		ctx = new Context(cfg);
		solver = ctx.mkSolver();
	}
	
	private IntExpr getNewSymbol(String name, Integer index) {
		return ctx.mkIntConst(name + "_" + index);
	}
	
	public IntExpr getIntSymbol(Integer number) {
		return ctx.mkInt(number);
	}
	
	public boolean isNewSymbol(String name) {
		return !symbols.containsKey(name);
	}
	
	public IntExpr getLatestContext(String name) {
		return symbols.get(name).get(symbols.get(name).size()-1);
	}
	
	public void addSymbol(String name) {
		if(!symbols.containsKey(name)) {
			List<IntExpr> list = new ArrayList<IntExpr>();
			list.add(getNewSymbol(name, 0));
			symbols.put(name, list);
		}
		else {
			symbols.get(name).add(getNewSymbol(name, symbols.get(name).size()));
		}
	}
	
	public void addConstraint(BoolExpr expr) {
		constraints.add(expr);
	}
	
	private void postConstraints() {
		if(constraints != null) {
			for(BoolExpr constraint: constraints)
				solver.add(constraint);
		}
	}
	
	public void solve() {
		postConstraints();
		status = solver.check();
		if(status == Status.SATISFIABLE) {
			model = solver.getModel();
		}
	}
	
	public boolean isSolved() {
		return status != null && status == Status.SATISFIABLE;
	}
	
	public Set<String> getListOfSymbols() {
		return symbols.keySet();
	}
	
	public Map<String, Integer> getSymbolInitialValues() {
		Map<String, Integer> symbolsAndValues = new HashMap<String, Integer>();
		L.info(model.toString());
		for(String symbol: symbols.keySet()) {
			symbolsAndValues.put(symbol, Integer.parseInt(model.getConstInterp(symbols.get(symbol).get(0)).toString()));
		}
		return symbolsAndValues;
	}
}
