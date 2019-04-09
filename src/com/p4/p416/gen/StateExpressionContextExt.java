package com.p4.p416.gen;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import com.p4.drmt.parser.P4Headers;
import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.drmt.Transitions;
import com.p4.p416.gen.P416Parser.StateExpressionContext;
import com.p4.p416.iface.IParserState;
import com.p4.tools.graph.Edge;

public class StateExpressionContextExt extends AbstractBaseExt {

	public StateExpressionContextExt(StateExpressionContext ctx) {
		super(ctx);
	}

	@Override
	public  StateExpressionContext getContext(){
		return (StateExpressionContext)contexts.get(contexts.size()-1);
	}

	@Override
	public StateExpressionContext getContext(String str){
		return (StateExpressionContext)new PopulateExtendedContextVisitor().visit(getParser(str).stateExpression());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof StateExpressionContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ StateExpressionContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
/*
	@Override
	public void getNode(Node node,StatesInfo si){
		/*StateExpressionContext ctx = (StateExpressionContext) getContext();
		if(ctx.name() != null && !ctx.name().getText().equals("")){
			if(ctx.name().getText().equals("accept")){
				Node n = new Node();
				n.setAccept(true);
				node.addChild(n);
			} else if(ctx.name().getText().equals("reject")){
				Node n = new Node();
				n.setReject(true);
				node.addChild(n);
			} else {
				String name = ctx.name().getText();
				ParserStateContext parserStateContext = si.getState(name);
				Node n = new Node();
				parserStateContext.extendedContext.getNode(n, si);
				node.addChild(n);
			}
		} else {
			super.getNode(node,si);
		}
	}
	*/
	@Override 
	public void populateParser(Transitions tm){
		StateExpressionContext ctx = (StateExpressionContext) getContext();
		if(ctx.name() != null && !getExtendedContext(ctx.name()).getFormattedText().isEmpty()){
			tm.getEdges().add(new Edge<String>(tm.getCurrentState().peek(), getExtendedContext(ctx.name()).getFormattedText()));
		}
		super.populateParser(tm);
	}
	
	@Override
	public void getTransitionCases(Map<String, String> transitionCases){
		StateExpressionContext ctx = (StateExpressionContext) getContext();
		if(ctx.name() != null && !getExtendedContext(ctx.name()).getFormattedText().isEmpty()){
			transitionCases.put("transition",getExtendedContext(ctx.name()).getFormattedText());
		}else{
			super.getTransitionCases(transitionCases);
		}
	}
	
	@Override
	public void inlineParentNameInTransition(List<IParserState> inlinedParseStates, List<String> parseStatesWithLoop){
		StringBuilder sb = new StringBuilder();
		if(getContext().name()!=null && parseStatesWithLoop.contains(getExtendedContext(getContext().name()).getFormattedText())){
			sb.append(getExtendedContext(getContext().name()).getFormattedText()+"_0 ;");
			StateExpressionContext stateExpressionContext = getContext(sb.toString());
			setContext(stateExpressionContext);
		}else{
			super.inlineParentNameInTransition(inlinedParseStates, parseStatesWithLoop);
		}
		
	}

	@Override
	public void parserStats(String startState, LinkedHashSet<Edge<String>> edges, P4Headers hdrs, Map<String, Integer> stateKeySizes){
		if(getContext().name()!=null && !getContext().name().getText().isEmpty()){
			System.out.println(startState +"->"+getContext().name().getText());
			edges.add(new Edge<>(startState, getContext().name().getText()));
		}else {
			super.parserStats(startState, edges, hdrs, stateKeySizes);
		}
	}

}
