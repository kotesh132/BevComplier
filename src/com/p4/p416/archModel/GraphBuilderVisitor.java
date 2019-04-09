package com.p4.p416.archModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Getter;
import lombok.Setter;

import org.antlr.v4.runtime.ParserRuleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.p4.p416.gen.P416BaseVisitor;
import com.p4.p416.gen.P416Parser;
import com.p4.p416.gen.ParserDeclarationContextExt;
import com.p4.p416.gen.ParserStateContextExt;
import com.p4.p416.iface.IGraphNode;
import com.p4.p416.iface.IParser;
import com.p4.p416.iface.IParserState;

public class GraphBuilderVisitor extends P416BaseVisitor<ParserRuleContext>{

	protected static final Logger L = LoggerFactory.getLogger(GraphBuilderVisitor.class);

	private HashMap<String, IParserState>  parserStateMap = new HashMap<String, IParserState>();
	private IParserState parentState = null;
	@Getter @Setter public HashMap<IParserState, List<IParserState>> adjacencyList = new HashMap<IParserState, List<IParserState>>();
	private IParserState startParserState =null ;

	@Override
	public ParserRuleContext visitParserDeclaration(P416Parser.ParserDeclarationContext ctx){
		ParserDeclarationContextExt parserDeclarationContextExt = (ParserDeclarationContextExt) ParserDeclarationContextExt.getExtendedContext(ctx);
		List<IParserState> parserStates = parserDeclarationContextExt.getParserStates();
		parserStateMap = new HashMap<String, IParserState>();
		for(IParserState parserState : parserStates){
			parserStateMap.put(parserState.getNameString(), parserState);
		}
		ParserStateContextExt parserStateContextExt = new ParserStateContextExt(null);
		parserStateMap.put("accept", parserStateContextExt.getAcceptParseState());
		parentState = parserDeclarationContextExt.getStartParserState();
		startParserState = parentState;
		super.visitParserDeclaration(ctx);
		return ctx;
	}

	@Override
	public ParserRuleContext visitParserState(P416Parser.ParserStateContext ctx){
		ParserStateContextExt parserStateContextExt = (ParserStateContextExt) ParserStateContextExt.getExtendedContext(ctx);
		Map<String, String> transitionCases = new HashMap<String, String>();
		parserStateContextExt.getTransitionCases(transitionCases);
		List<IParserState> childrenParserStates = new ArrayList<IParserState>();
		for(Entry<String, String> entry : transitionCases.entrySet()){
			if(!entry.getValue().equals("accept")){
				IParserState childrenParserState = parserStateMap.get(entry.getValue());
				parserStateContextExt.insertChildrenNode(childrenParserState);
				childrenParserStates.add(childrenParserState);

			}else{
				IParserState acceptState = parserStateMap.get("accept");
				parserStateContextExt.insertChildrenNode(acceptState);
				childrenParserStates.add(acceptState);
			}
		}
		adjacencyList.put(parserStateContextExt, childrenParserStates);
		if(parserStateContextExt != startParserState)
			parentState = parserStateContextExt;
		return ctx;
	}

	public List<List<IParserState>> getLoopPaths(){
		List<List<IParserState>> loopPaths = new ArrayList<List<IParserState>>();
		List<IParserState> rootParseState = new ArrayList<IParserState>();
		Map<String, IParserState> parseStateVisitedMap = new HashMap<String, IParserState>();
		getLoopPaths((ParserStateContextExt) startParserState, rootParseState, loopPaths, parseStateVisitedMap);
		return loopPaths;
	}

	public void getLoopPaths(ParserStateContextExt startParserStateContextExt, List<IParserState> rootParseState, List<List<IParserState>> loopPaths, Map<String, IParserState> parseStateVisitedMap){
		ParserStateContextExt parserStateContextExt = startParserStateContextExt;
		if(!parserStateContextExt.getNameString().equals("accept")){
			if(!parseStateVisitedMap.containsKey(parserStateContextExt.getNameString())){
				if(!rootParseState.contains(parserStateContextExt)){
					rootParseState.add(parserStateContextExt);
					for(IGraphNode childrenParseState : parserStateContextExt.getChildrenNodes()){
						List<IParserState> childParseState = new ArrayList<IParserState>(rootParseState);
						getLoopPaths((ParserStateContextExt) childrenParseState, childParseState, loopPaths, parseStateVisitedMap);
					}
					parseStateVisitedMap.put(parserStateContextExt.getNameString(), parserStateContextExt);
				}else{
					rootParseState.add(parserStateContextExt);
					int start = rootParseState.indexOf(parserStateContextExt);
					int current = rootParseState.size()-1;
					List<IParserState> childParseState = rootParseState.subList(start, current);
					loopPaths.add(childParseState);
					rootParseState = rootParseState.subList(0, start);
				}
			}
		}
	}
	
	public List<List<IParserState>> getLongestPathInParser(IParser parserDeclaration){
		IParserState startParseState = parserDeclaration.getStartParserState();
		List<List<IParserState>> longestParsePaths = new ArrayList<List<IParserState>>();
		List<IParserState> rootParsePath = new ArrayList<IParserState>();
		getLongestParsePath(longestParsePaths, rootParsePath, startParseState);
		return longestParsePaths;
	}
	
	private static void getLongestParsePath(List<List<IParserState>> longestParsePaths,List<IParserState> rootParsePath, IGraphNode newChildrenParseState){
		ParserStateContextExt ctx = (ParserStateContextExt) newChildrenParseState;
		rootParsePath.add(ctx);
		for(IGraphNode childrenParseState : ctx.getChildrenNodes()){
			List<IParserState> childParsePath = new ArrayList<IParserState>(rootParsePath);
			getLongestParsePath(longestParsePaths,childParsePath,childrenParseState);
			if(longestParsePaths.size()>0){
				if(longestParsePaths.get(0).size() < childParsePath.size()){
					longestParsePaths.clear();
					longestParsePaths.add(childParsePath);
				}else if(longestParsePaths.get(0).size() == childParsePath.size()){
					longestParsePaths.add(childParsePath);
				}
			}else{
				longestParsePaths.add(childParsePath);
			}
		}
	}

}
