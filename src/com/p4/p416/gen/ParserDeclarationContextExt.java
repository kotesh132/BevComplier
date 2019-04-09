package com.p4.p416.gen;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import com.p4.p416.iface.IParameter;
import com.p4.p416.iface.IParser;
import com.p4.p416.iface.IParserState;

import com.p4.tools.graph.Edge;
import  org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.stringtemplate.v4.ST;

import com.p4.drmt.parser.P4Headers;
import com.p4.drmt.parser.P4Parameter;
import com.p4.drmt.parser.P4Parser;
import com.p4.drmt.parser.P4Parsers;
import com.p4.drmt.parser.P4State;
import com.p4.p416.applications.Scope;
import com.p4.p416.applications.Type;
import com.p4.p416.exceptions.NameCollisionException;
import com.p4.p416.gen.P416Parser.ParserDeclarationContext;
import com.p4.p416.gen.P416Parser.ParserTypeDeclarationContext;

public class ParserDeclarationContextExt extends AbstractBaseExt implements IParser {

	private static final Logger L = LoggerFactory.getLogger(ParserDeclarationContextExt.class);

	public ParserDeclarationContextExt(ParserDeclarationContext ctx) {
		super(ctx);
	}

	@SuppressWarnings("unchecked")
	@Override
	public  ParserDeclarationContext getContext(){
		return (ParserDeclarationContext)contexts.get(contexts.size()-1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public ParserDeclarationContext getContext(String str){
		return (ParserDeclarationContext)new PopulateExtendedContextVisitor().visit(getParser(str).parserDeclaration());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ParserDeclarationContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ParserDeclarationContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}

	/*========================================================================
	 * Symbol, Scope & Type Interface START
	 * ======================================================================*/

	@Override
	public String getSymbolName()
	{
		ParserDeclarationContext ctx = getContext();
		ParserTypeDeclarationContextExt parserTypeDeclarationContextExt = (ParserTypeDeclarationContextExt)getExtendedContext(ctx.parserTypeDeclaration());
		return parserTypeDeclarationContextExt.getSymbolName();
	}


	@Override
	public String getTypeName()
	{
		ParserDeclarationContext ctx = getContext();
		ParserTypeDeclarationContextExt parserTypeDeclarationContextExt = (ParserTypeDeclarationContextExt)getExtendedContext(ctx.parserTypeDeclaration());
		return parserTypeDeclarationContextExt.getTypeName();
	}

	@Override
	public Boolean isSame(Type thatType)
	{
		ParserDeclarationContext ctx = getContext();
		ParserTypeDeclarationContextExt parserTypeDeclarationContextExt = (ParserTypeDeclarationContextExt)getExtendedContext(ctx.parserTypeDeclaration());
		return parserTypeDeclarationContextExt.isSame(thatType);
	}

	@Override
	public Type getType()
	{
		return this;
	}


	@Override
	public void setBitOffset(AtomicInteger bitOffset)
	{
		return;
	}

	@Override
	public void setByteOffset(AtomicInteger byteOffset)
	{
		return;
	}

	@Override
	public void setAlignedByteOffset(AtomicInteger byteOffset)
	{
		return;
	}

	protected int sizeInBits;
	protected int sizeInBytes;
	protected int alignedSizeInBytes;

	@Override
	public void defineSymbol(AtomicReference<Scope>  enclosingScopeRef) throws NameCollisionException
	{
		this.enclosingScope = enclosingScopeRef.get();
		this.enclosingScope.add(this);
		Scope localScope = createScope();
		ParserStatesContextExt parserStatesContextExt = (ParserStatesContextExt)getExtendedContext(getContext().parserStates());

		if (this.isSemanticChecksPass()){
			//			super.defineSymbol(new AtomicReference<Scope>(localScope));
			super.defineSymbol(new AtomicReference<Scope>(localScope));
		}else{
			parserStatesContextExt.defineSymbol(new AtomicReference<Scope>(localScope));//old implementation
		}
	}
	/*========================================================================
	 * Symbol, Scope & Type Interface END
	 * ======================================================================*/

	@Override
	public void getVariablesForHeaderClass(List<ST> sts){
		ParserDeclarationContext ctx = (ParserDeclarationContext) getContext();
		((ParserTypeDeclarationContextExt) getExtendedContext(ctx.parserTypeDeclaration())).getSt(sts);
	}

	@Override
	public void buildParsers(P4Headers headers, P4Parsers parsers){
		ParserDeclarationContext ctx = (ParserDeclarationContext) getContext();
		ParserTypeDeclarationContext ptctx = ctx.parserTypeDeclaration();
		String name = getExtendedContext(ptctx.name()).getFormattedText();
		List<P4Parameter> parameters = ((ParameterListContextExt) getExtendedContext(ptctx.parameterList())).getParameters(headers);
		P4Parser p = new P4Parser(name);
		p.getParameters().addAll(parameters);
		if(ctx.parserStates()!=null){
			List<P4State> states = new ArrayList<>();
			((ParserStatesContextExt) getExtendedContext(ctx.parserStates())).getStates(states,headers);
			p.getStates().addAll(states);
		}
		parsers.getLastparser().push(p);
		//System.out.println(ctx.getText() );
		super.buildParsers(headers, parsers);
		p = parsers.getLastparser().pop();
		parsers.getAllParsers().add(p);
	}

	//GL - ControlBlock
	public void getControlBlocks(Map<String, ControlDeclarationContextExt> controlBlocks) {
		return;
	}
	public void getTables(Map<String, TableDeclarationContextExt> tables) {
		return;
	}
	public void getActions(Map<String, ActionDeclarationContextExt> actions) {
		return;
	}
	//GL - ControlBlock END
	@Override
	public void quadrupalize(boolean probe, List<StatementOrDeclarationContextExt> statementOrDeclarationContextExts){
		return;
	}

	@Override
	public List<IParameter> getParameters() {

		ParserTypeDeclarationContext parserTypeDeclarationContext = (ParserTypeDeclarationContext)getExtendedContext(getContext().parserTypeDeclaration()).getContext();
		ParameterListContextExt parameterListContextExt = (ParameterListContextExt) getExtendedContext(parserTypeDeclarationContext.parameterList());
		return parameterListContextExt.getParameters();
	}

	@Override
	public String getNameString() {
		ParserTypeDeclarationContextExt parserDeclarationContextExt = (ParserTypeDeclarationContextExt) getExtendedContext(getContext().parserTypeDeclaration());
		return getExtendedContext(parserDeclarationContextExt.getContext().name()).getFormattedText();
	}

	@Override
	public List<IParserState> getParserStates() {
		List<IParserState> parserStates = new ArrayList<>();

		visitNode(node -> {
			if (node instanceof IParserState) {
				parserStates.add((IParserState) node);
			}
		});
		return parserStates;
	}

	@Override
	public HashMap<String, IParserState> getParserStatesMap(){
		HashMap<String, IParserState>  parserStateMap = new HashMap<String, IParserState>();
		ParserDeclarationContext ctx = (ParserDeclarationContext) getContext();
		List<IParserState> parserStates = ((IParser) getExtendedContext(ctx)).getParserStates();
		for(IParserState parserState : parserStates){
			parserStateMap.put(parserState.getNameString(), parserState);
		}
		ParserStateContextExt parserStateContextExt = new ParserStateContextExt(null);
		parserStateMap.put("accept", parserStateContextExt.getAcceptParseState());
		return parserStateMap;
	}

	@Override
	public IParserState getStartParserState(){
		ParserDeclarationContext ctx = (ParserDeclarationContext) getContext();
		if(ctx.parserStates().getChildCount()!=0){
			IParserState parserState = ((ParserStatesContextExt)getExtendedContext(ctx.parserStates())).getStartParserState();
			return parserState;
		}
		return null;
	}
	@Override
	public void parserStats(String startState, LinkedHashSet<Edge<String>> edges, P4Headers hdrs, Map<String, Integer> stateKeySizes){
		ParserDeclarationContext ctx = (ParserDeclarationContext) getContext();
		ParserTypeDeclarationContext ptctx = ctx.parserTypeDeclaration();
		String name = getExtendedContext(ptctx.name()).getFormattedText();
		List<P4Parameter> parameters = ((ParameterListContextExt) getExtendedContext(ptctx.parameterList())).getParameters(hdrs);
		super.parserStats(startState, edges, hdrs, stateKeySizes);
	}

}
