package com.p4.p416.gen;

import java.util.*;

import com.p4.drmt.parser.*;
import com.p4.tools.graph.Edge;
import com.p4.utils.Utils;
import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.applications.Symbol;
import com.p4.p416.gen.P416Parser.ExpressionListContext;
import com.p4.p416.gen.P416Parser.SelectCaseContext;
import com.p4.p416.gen.P416Parser.SelectCaseListContext;
import com.p4.p416.gen.P416Parser.SelectExpressionContext;

public class SelectExpressionContextExt extends AbstractBaseExt {

	public SelectExpressionContextExt(SelectExpressionContext ctx) {
		super(ctx);
	}

	@Override
	public  SelectExpressionContext getContext(){
		return (SelectExpressionContext)contexts.get(contexts.size()-1);
	}

	@Override
	public SelectExpressionContext getContext(String str){
		return (SelectExpressionContext)new PopulateExtendedContextVisitor().visit(getParser(str).selectExpression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof SelectExpressionContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ SelectExpressionContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	/*
	@Override
    public void getAddedVariablesForHeaderClass(List<ST> sts){
		/*
        for(String name : variablesForPacketHeader.keySet()){
            ST variable = Utils.getStgGrp().getInstanceOf("variable");
            variable.add("name", name);
            variable.add("type", variablesForPacketHeader.get(name));
            sts.add(variable);
        }
        
    }
    
	@Override
	public void getNode(Node node,StatesInfo si){
		
		SelectExpressionContext ctx = (SelectExpressionContext) getContext();
		String[] expressions = ctx.expressionList().getText().split(",");
		List<String> types = Arrays.asList(expressions);
		if(types.size() == 1 && types.get(0).contains("lookahead")){
            String size = ctx.expressionList().extendedContext.getLookaheadValue();
            node.setLookaheadValue(Integer.parseInt(size));
            node.setHasLookahead(true);
            types = new ArrayList<String>();
            String lookaheadVar = Utils.getLookaheadVar();
            types.add(lookaheadVar);
            node.setLookaheadVar(lookaheadVar);
            variablesForPacketHeader.put(lookaheadVar,"rand_var< sc_bv < "+size+" > >");
            Utils.increment();
        }
		List<Pair<List<String>,String>> cases = new ArrayList<Pair<List<String>,String>>();
		ctx.selectCaseList().extendedContext.getCases(cases);
		for(Pair<List<String>,String> case_ : cases){
			Node n = getNode(case_,types,si);
			node.addChild(n);
		}
	}

	private Node getNode(Pair<List<String>, String> case_, List<String> types,StatesInfo si) {
		if(case_.first().size() == 1 && case_.first().get(0).equals("default")){
			if(case_.second().equals("accept")){
				Node n = new Node();
				n.setAccept(true);
				return n;
			} else if(case_.second().equals("reject")){
				Node n = new Node();
				n.setReject(true);
				return n;
			} else {
				ParserStateContext parserStateContext = si.getState(case_.second());
				Node n = new Node();
				parserStateContext.extendedContext.getNode(n, si);
				return n;
			}
		} else {
			if(case_.first().size() != types.size()){
				L.error("in transition statement, the number of lhs variables != rhs");
			}
			if(case_.second().equals("accept")){
                Node n = new Node();
                if(case_.first().size() >0){
                    for(int i=0;i<case_.first().size();i++){
                        Constraint c = new Constraint(types.get(i), "==",case_.first().get(i));
                        n.addToConstraints(c);
                    }
                }
                n.setAccept(true);
                return n;
            } else if(case_.second().equals("reject")){
                Node n = new Node();
                if(case_.first().size() >0){
                    for(int i=0;i<case_.first().size();i++){
                        Constraint c = new Constraint(types.get(i), "==",case_.first().get(i));
                        n.addToConstraints(c);
                    }
                }
                n.setReject(true);
                return n;
            } else {
				ParserStateContext parserStateContext = si.getState(case_.second());
				Node n = new Node();
				parserStateContext.extendedContext.getNode(n, si);
				for(int i=0;i<case_.first().size();i++){
					Constraint c = new Constraint(types.get(i), "==",case_.first().get(i));
					n.addToConstraints(c);
				}
				return n;
			}
		}
	}
*/
	private List<String> getExpressionsInSelect(SelectExpressionContext ctx) {
		List<String> exps = new ArrayList<>();
		if(ctx.expressionList()!=null){
			ExpressionListContextExt expressionList = (ExpressionListContextExt) getExtendedContext(ctx.expressionList());
			expressionList.getExps(exps);
		}
		Collections.reverse(exps);
		return exps;
	}

	public P4Select getSelect() {
		P4Select select = new P4Select();
		SelectExpressionContext ctx = getContext();
		select.getExpressions().addAll(getExpressionsInSelect(ctx));
		List<P4KeySet> ks = new ArrayList<>();
		SelectCaseListContext selectCaseListContext = getExtendedContext(ctx.selectCaseList()).getContext();
		for(SelectCaseContext x:selectCaseListContext.selectCase()){
			SelectCaseContextExt selectCaseContext = (SelectCaseContextExt) getExtendedContext(x);
			ks.add(selectCaseContext.getKeySet());
		}
		select.getTransitions().addAll(ks);
		return select;
	}
	
	@Override
	public void setSelectOffsets(Map<String, Integer[]> map) {
		SelectExpressionContext ctx = getContext();
		for(String expr : getExpressionsInSelect(ctx)) {
			try{
				Symbol keySymbol = resolve(expr);
				if(keySymbol != null) {
					int offset = getBitOffset(expr);
					int size = keySymbol.getSizeInBytes()*8;
					Integer[] arr = new Integer[]{offset, size};
					map.put(expr, arr);
				}
			}catch(Exception e) {

			}
		}
	}

	@Override
	public void parserStats(String startState, LinkedHashSet<Edge<String>> edges, P4Headers hdrs, Map<String, Integer> stateKeySizes){

		for(P416Parser.ExpressionContext exp: ((ExpressionListContext)getExtendedContext(getContext().expressionList()).getContext()).expression()){
			String expStr = getExtendedContext(exp).getFormattedText();
			L.info("++"+expStr);
			if(expStr!=null && !expStr.isEmpty()) {
				Utils.Pair<P4Header, BitStringType> h = hdrs.resolveHeaderField(expStr);
				//System.out.println("++" + expStr + "," + h.second().getLength());
				int length = h.second().getLength();
				if(!stateKeySizes.containsKey(startState)){
					stateKeySizes.put(startState, 0);
				}
				stateKeySizes.put(startState, stateKeySizes.get(startState)+length);
			}
		}
		super.parserStats(startState, edges, hdrs, stateKeySizes);
	}
}
