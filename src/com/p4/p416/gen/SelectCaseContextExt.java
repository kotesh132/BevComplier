package com.p4.p416.gen;

import java.util.*;

import com.p4.drmt.parser.P4Headers;
import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.drmt.parser.P4KeySet;
import com.p4.drmt.Transitions;
import com.p4.p416.gen.P416Parser.KeySetExpressionContext;
import com.p4.p416.gen.P416Parser.SelectCaseContext;
import com.p4.p416.gen.P416Parser.SimpleKeySetExpressionContext;
import com.p4.p416.gen.P416Parser.TupleKeySetExpressionContext;
import com.p4.p416.iface.IParserState;
import com.p4.tools.graph.Edge;
import com.p4.utils.Utils.Pair;

public class SelectCaseContextExt extends AbstractBaseExt {

	public SelectCaseContextExt(SelectCaseContext ctx) {
		super(ctx);
	}

	@Override
	public  SelectCaseContext getContext(){
		return (SelectCaseContext)contexts.get(contexts.size()-1);
	}

	@Override
	public SelectCaseContext getContext(String str){
		return (SelectCaseContext)new PopulateExtendedContextVisitor().visit(getParser(str).selectCase());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof SelectCaseContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ SelectCaseContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}

	@Override
	protected void getCases(List<Pair<List<String>, String>> cases) {
		/*
		SelectCaseContext ctx = (SelectCaseContext) getContext();
		String name = ctx.name().extendedContext.getName();
		List<String> lhs = new ArrayList<String>();
		ctx.keysetExpression().extendedContext.getLhs(lhs);
		cases.add(Pair.of(lhs,name));
		 */
	}

	@Override
	public void populateParser(Transitions tm){
		SelectCaseContext ctx = (SelectCaseContext) getContext();
		tm.getEdges().add(new Edge<String>(tm.getCurrentState().peek(), getExtendedContext(ctx.name()).getFormattedText()));
		super.populateParser(tm);
	}

	public P4KeySet getKeySet() {
		SelectCaseContext ctx = (SelectCaseContext) getContext();
		P4KeySet ks = new P4KeySet(getExtendedContext(ctx.name()).getFormattedText());
		if(((KeySetExpressionContext)getExtendedContext(ctx.keySetExpression()).getContext()).simpleKeySetExpression()!=null){
			ks.getCasest().add(getExtendedContext(((KeySetExpressionContext)getExtendedContext(ctx.keySetExpression()).getContext()).simpleKeySetExpression()).getFormattedText());
		}
		if(ctx.keySetExpression().tupleKeySetExpression() !=null){
			List<String> keys = new ArrayList<>();
			for(SimpleKeySetExpressionContext simple:((TupleKeySetExpressionContext)getExtendedContext(((KeySetExpressionContext)getExtendedContext(ctx.keySetExpression()).getContext()).tupleKeySetExpression()).getContext()).simpleKeySetExpression()){
				keys.add(getExtendedContext(simple).getFormattedText());
			}
			Collections.reverse(keys);
			for(String k:keys){
				ks.getCasest().add(k);
			}
		}

		return ks;
	}

	@Override
	public void getTransitionCases(Map<String, String> transitionCases){
		SelectCaseContext ctx = (SelectCaseContext) getContext();
		transitionCases.put(getExtendedContext(ctx.keySetExpression()).getFormattedText(), getExtendedContext(ctx.name()).getFormattedText());
	}

	/*
	 selectCase
	locals [SelectCaseContextExt extendedContext]
	:	keySetExpression COLON name SEMI;
	 */
	@Override
	public void inlineParserStateForNewInstances(Integer instanceNumber, String parseStateName, Integer count, List<String> parseStatesInUnionStack){
		StringBuilder sb  = new StringBuilder();
		String transitionStateName = getExtendedContext(getContext().name()).getFormattedText();
		if(parseStateName.equals(transitionStateName)  | (parseStatesInUnionStack!=null && parseStatesInUnionStack.contains(transitionStateName))){
			if((instanceNumber+1)<count){
				sb.append(getExtendedContext(getContext().keySetExpression()).getFormattedText()+" : ");
				sb.append(transitionStateName+"_"+(instanceNumber+1)+" ;");
			}
		}else{
			sb.append(getExtendedContext(getContext().keySetExpression()).getFormattedText()+" : ");
			sb.append(transitionStateName+" ;");
		}
		if(sb.toString().length()>0){
			SelectCaseContext selectCaseContext = getContext(sb.toString());
			setContext(selectCaseContext);
		}else{
			setContext(null);
		}
	}

	@Override
	public void inlineParentNameInTransition(List<IParserState> inlinedParseStates, List<String> parseStatesWithLoop){
		StringBuilder sb = new StringBuilder();
		SelectCaseContext selectCaseContext = null;
		if(parseStatesWithLoop.contains(getExtendedContext(getContext().name()).getFormattedText())){
			sb.append(getExtendedContext(getContext().keySetExpression()).getFormattedText()+" : ");
			sb.append(getExtendedContext(getContext().name()).getFormattedText()+"_0"+" ; ");
			selectCaseContext = getContext(sb.toString());
		}else{
			selectCaseContext = getContext(getExtendedContext(getContext()).getFormattedText());
		}
		setContext(selectCaseContext);
	}

	@Override
	public void parserStats(String startState, LinkedHashSet<Edge<String>> edges, P4Headers hdrs, Map<String, Integer> stateKeySizes){
		if(getContext().name()!=null && !getContext().name().getText().isEmpty()){
			System.out.println(startState +"->"+getContext().name().getText());
			edges.add(new Edge<>(startState, getContext().name().getText()));
		}
	}

}
