package com.p4.p416.gen;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.p4.p416.iface.IGraphNode;
import com.p4.p416.iface.IParserState;
import com.p4.p416.iface.ITransitionStatement;

import com.p4.tools.graph.Edge;
import  org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.drmt.parser.P4Assign;
import com.p4.drmt.parser.P4Extract;
import com.p4.drmt.parser.P4Headers;
import com.p4.drmt.parser.P4State;
import com.p4.drmt.parser.P4Transition;
import com.p4.drmt.Transitions;
import com.p4.p416.applications.Scope;
import com.p4.p416.applications.Symbol;
import com.p4.p416.applications.Type;
import com.p4.p416.exceptions.NameCollisionException;
import com.p4.p416.gen.P416Parser.ParserStateContext;
import com.p4.packetgen.utils.HeadersInfo;
import com.p4.packetgen.utils.StatesInfo;



public class ParserStateContextExt extends AbstractBaseExt implements IParserState {

	private static final Logger L = LoggerFactory.getLogger(ParserStateContextExt.class);



	public ParserStateContextExt(ParserStateContext ctx) {
		super(ctx);

	}

	@Override
	public  ParserStateContext getContext(){
		return (ParserStateContext)contexts.get(contexts.size()-1);
	}

	@Override
	public ParserStateContext getContext(String str){
		return (ParserStateContext)new PopulateExtendedContextVisitor().visit(getParser(str).parserState());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ParserStateContext){
				
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be cased to "+ParserStateContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}

	@Override
	public void collectInfo(StatesInfo si,HeadersInfo hi){
		ParserStateContext ctx = (ParserStateContext) getContext();
		si.add(getExtendedContext(ctx.name()).getName(), ctx);
	}

	@Override
	public void populateParser(Transitions tm){
		ParserStateContext ctx = (ParserStateContext)getContext();
		tm.getCurrentState().push(getExtendedContext(ctx.name()).getFormattedText());
		super.populateParser(tm);
		tm.getCurrentState().pop();
	}

	public P4State getState(P4Headers hdr) {
		ParserStateContext ctx = (ParserStateContext)getContext();
		P4State ps = new P4State(getExtendedContext(ctx.name()).getFormattedText());
		if(ctx.transitionStatement()!=null){
			P4Transition tr = ((TransitionStatementContextExt) getExtendedContext(ctx.transitionStatement())).getTransitions();
			ps.setTransition(tr);
		}
		if(ctx.parserStatements()!=null) {
			L.debug(getExtendedContext(ctx.parserStatements()).getFormattedText());
			List<P4Extract> extracts  = new ArrayList<>();
			//List<P4Extract> extracts = ctx.parserStatements().extendedContext.getExtracts();
			super.getExtracts(extracts, hdr);
			ps.getExtracts().addAll(extracts);
			List<P4Assign> assigns  = new ArrayList<>();
			//List<P4Assign> assigns = ctx.parserStatements().extendedContext.getAssigns();
			super.getAssigns(assigns);
			ps.getAssigns().addAll(assigns);
		}
		return ps;
	}


	@Override
	public String getNameString() {
		return getExtendedContext(getContext().name()).getName();
	}

	private List<IGraphNode> children = new ArrayList<IGraphNode>();
	private Boolean isTempVisited = false;
	private Boolean isPermVisited = false;

	@Override
	public void insertChildrenNode(IGraphNode childrenNode) {
		this.children.add(childrenNode);

	}

	@Override
	public List<IGraphNode> getChildrenNodes() {
		return this.children;
	}

	@Override
	public IParserState getAcceptParseState(){
		String acceptStateInput = "@name(\"accept\") state accept{}";
		ParserStateContext acceptParserStateContext = getContext(acceptStateInput);
		return (IParserState) getExtendedContext(acceptParserStateContext);
	}

	
	
	@Override
	public void inlineParserStates(List<IParserState> inlinedParserStates,  List<List<IParserState>>  loopPaths, List<String> parseStatesInUnionStack){
		ParserStateContextExt parserStateContextExt = (ParserStateContextExt) getExtendedContext(getContext());
		String parseStateName = parserStateContextExt.getNameString();
		if(parserStateContextExt.hasNextInStateExpression()){
			if(parserStateContextExt.getTransitionStatement().hasNextInStateExpression()){// we are only handling next in transitionStatements
				String varWithNext = parserStateContextExt.getTransitionStatement().getNextVariable();
				String[] var ;
				if(varWithNext.contains(".last")){
					var = varWithNext.split(".last");
				}
				else{
					var = varWithNext.split(".next");
				}
				Symbol symbol = parserStateContextExt.resolve(var[0]);
				Integer headerCount = symbol.getType().getTypeSize();
				String text = getFormattedText();
				for(int i =0 ; i<headerCount ; i++){
					ParserStateContext copiedCtx = (ParserStateContext) new PopulateExtendedContextVisitor().visit(getParser(text).parserState());
					getExtendedContext(copiedCtx).inlineParserStateForNewInstances(i, parseStateName, headerCount, parseStatesInUnionStack);
					((ParserStateContextExt) getExtendedContext(copiedCtx)).appendStateNameWithInstanceNumber(i, inlinedParserStates);
				}
			}
		}
	}
	

	/*
	 parserState
     :	optAnnotations STATE name LCURL parserStatements? transitionStatement? RCURL;
	 */
	public void appendStateNameWithInstanceNumber(Integer instance, List<IParserState> parseStatesList){
		ParserStateContext ctx = (ParserStateContext) getContext();
		StringBuilder sb = new StringBuilder();
		sb.append(getExtendedContext(ctx.optAnnotations()).getFormattedText());
		sb.append(ctx.STATE().getText()+" ");
		sb.append(getExtendedContext(ctx.name()).getFormattedText()+"_"+instance+" ");
		sb.append(ctx.LCURL().getText()+" ");
		sb.append("\n");
		if(ctx.parserStatements()!=null)
			sb.append(getExtendedContext(ctx.parserStatements()).getFormattedText()+"\n");
		if(ctx.transitionStatement()!=null)
			sb.append(getExtendedContext(ctx.transitionStatement()).getFormattedText()+"\n");
		sb.append(ctx.RCURL().getText()+" ");
		ParserStateContext parserStateContext = getContext(sb.toString());
		parseStatesList.add((IParserState) getExtendedContext(parserStateContext));
	}

	public void inlineParentNameInTransition(List<IParserState> inlinedParseStates, List<String> parseStatesWithLoop){
		ParserStateContext ctx = (ParserStateContext) getContext();
		StringBuilder sb = new StringBuilder();
		sb.append(getExtendedContext(ctx.optAnnotations()).getFormattedText());
		sb.append(ctx.STATE().getText()+" ");
		sb.append(getExtendedContext(ctx.name()).getFormattedText());
		sb.append(ctx.LCURL().getText()+" ");
		sb.append("\n");
		if(ctx.parserStatements()!=null)
			sb.append(getExtendedContext(ctx.parserStatements()).getFormattedText()+"\n");
		if(ctx.transitionStatement()!=null)
			getExtendedContext(ctx.transitionStatement()).inlineParentNameInTransition(inlinedParseStates, parseStatesWithLoop);
		sb.append(getExtendedContext(ctx.transitionStatement()).getFormattedText()+"\n");
		sb.append(ctx.RCURL().getText()+" ");
		ParserStateContext parserStateContext = getContext(sb.toString());
		inlinedParseStates.add((IParserState) getExtendedContext(parserStateContext));
	}

	@Override
	public ITransitionStatement getTransitionStatement() {
		return (ITransitionStatement)getExtendedContext(getContext().transitionStatement());
	}

	@Override
	public void parserStats(String startState, LinkedHashSet<Edge<String>> edges, P4Headers hdrs, Map<String, Integer> stateKeySizes){
		String start = getContext().name().getText();
		System.out.println(getContext().name().getText());
		super.parserStats(start, edges, hdrs, stateKeySizes);
	}
	
	@Override
	public void defineSymbol(AtomicReference<Scope>  enclosingScopeRef) throws NameCollisionException{
		if (isSemanticChecksPass()){
			this.enclosingScope = enclosingScopeRef.get();
			this.enclosingScope.add(this);
			Scope localScope = createScope();
			super.defineSymbol(new AtomicReference<Scope>(localScope));
		}
		super.defineSymbol(enclosingScopeRef);
	}
	
	@Override
	public String getSymbolName(){
		return this.getContext().name().getText();
	}
	
	@Override
	public Type getType(){
		return this;
	}
	
	@Override
	public String getTypeName(){
		return this.getContext().STATE().getText();
	}
	
}
