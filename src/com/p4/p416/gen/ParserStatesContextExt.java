package com.p4.p416.gen;

import java.util.ArrayList;
import java.util.List;

import  org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;

import com.p4.drmt.parser.P4Headers;
import com.p4.drmt.parser.P4State;
import com.p4.p416.gen.P416Parser.ParserStateContext;
import com.p4.p416.gen.P416Parser.ParserStatesContext;
import com.p4.p416.iface.IParserState;

public class ParserStatesContextExt extends AbstractBaseExt {

	public ParserStatesContextExt(ParserStatesContext ctx) {
		super(ctx);
	}

	@Override
	public  ParserStatesContext getContext(){
		return (ParserStatesContext)contexts.get(contexts.size()-1);
	}

	@Override
	public ParserStatesContext getContext(String str){
		return (ParserStatesContext)new PopulateExtendedContextVisitor().visit(getParser(str).parserStates());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ParserStatesContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ParserStatesContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}

	public void getStates(List<P4State> states, P4Headers hdr) {		
		ParserStatesContext ctx = getContext();
		for(ParserStateContext pctx : ctx.parserState()){
			states.add(((ParserStateContextExt) getExtendedContext(pctx)).getState(hdr));
		}
	}

	public IParserState getStartParserState(){
		ParserStatesContext ctx = (ParserStatesContext)getContext();
		List<ParserStateContext> parserStateContext = ctx.parserState();
		for(ParserStateContext pctx : parserStateContext){
			if(getExtendedContext(pctx.name()).getName().equals("start")){
				return (IParserState) getExtendedContext((ParseTree) pctx);
			}
		}
		return null;
	}

	@Override
	public void inlineParserStates(List<IParserState> inlinedParserStates, List<List<IParserState>> loopPaths, List<String> parseStatesInUnionStack){

		inlinedParserStates = new ArrayList<IParserState>();
		List<IParserState> parserStatesWithLoop = new ArrayList<IParserState>();
		List<String> nameOfParserStateWithLoop = new ArrayList<String>();
		for(List<IParserState>  loopParsePath : loopPaths){
			if(loopParsePath.size()>1){
				for(IParserState loopElement : loopParsePath){
					if(parseStatesInUnionStack!=null){
						if(!parseStatesInUnionStack.contains(loopElement.getNameString())){
							throw new RuntimeException("invalid loop with start parseState which is not breakable for "+ loopElement.getNameString());
						}
					}
				}
			}else{
				parserStatesWithLoop.add(loopParsePath.get(0));
				nameOfParserStateWithLoop.add(loopParsePath.get(0).getNameString());
			}
		}

		for(ParserStateContext parserState : getContext().parserState()){
			List<IParserState> newInlinedParseStates = new ArrayList<IParserState>();
			if(parserStatesWithLoop.contains(getExtendedContext(parserState))){
				getExtendedContext(parserState).inlineParserStates(newInlinedParseStates, loopPaths, parseStatesInUnionStack);
				inlinedParserStates.addAll(newInlinedParseStates);
			}
			else{
				inlinedParserStates.add((IParserState) getExtendedContext(parserState));
			}
		}


		List<IParserState> fullyInlinedParseStates = new ArrayList<IParserState>();
		for(IParserState parserState : inlinedParserStates){
			((ParserStateContextExt) parserState).inlineParentNameInTransition(fullyInlinedParseStates, nameOfParserStateWithLoop);
		}

		StringBuilder sb = new StringBuilder();
		for(IParserState parserState : fullyInlinedParseStates){
			sb.append(parserState.getFormattedText()+"\n");
		}

		ParserStatesContext newParserStatesContext= getContext(sb.toString());
		addToContexts(newParserStatesContext);
	}

}
