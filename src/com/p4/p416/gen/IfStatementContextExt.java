package com.p4.p416.gen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.Stack;

import com.p4.drmt.struct.IALUOperation;
import  org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;

import com.p4.drmt.compiler.Temporaries;
import com.p4.p416.applications.SsaForm;
import com.p4.p416.gen.P416Parser.IfStatementContext;
import com.p4.p416.gen.P416Parser.StatementContext;
import com.p4.tools.graph.structs.TupleStatementSimplify;
import com.p4.utils.Utils.Pair;
import com.p4.utils.Utils.Tuple3;

public class IfStatementContextExt extends ConditionalStatementContextExt {
	
    //SSA START
	// List of min and max versions that build phi for each if statement.
	Map<AbstractBaseExt,List< Tuple3<AbstractBaseExt,Integer,Integer>> > mapMinMax = new HashMap<>();
    //SSA END

	public IfStatementContextExt(IfStatementContext ctx) {
		super(ctx);
	}

	@Override
	public  IfStatementContext getContext(){
		return (IfStatementContext)contexts.get(contexts.size()-1);
	}

	@Override
	public IfStatementContext getContext(String str){
		return (IfStatementContext)new PopulateExtendedContextVisitor().visit(getParser(str).conditionalStatement());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof IfStatementContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ IfStatementContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	@Override
	public void quadrupalize(boolean probe,List<StatementOrDeclarationContextExt> statementOrDeclarationContextExts){
		IfStatementContext ifStatementContext = getContext();
		TupleStatementSimplify q = ((ExpressionContextExt) getExtendedContext(getExtendedContext(ifStatementContext.expression()).getContext())).simplify();
		q=q.terminalize();
		List<StatementOrDeclarationContextExt> s1 = new ArrayList<>();
		boolean ifBlockStatement = true;
		if(((StatementContext)getExtendedContext(ifStatementContext.statement()).getContext()).blockStatement() == null){
			ifBlockStatement = false;
		}
		getExtendedContext(ifStatementContext.statement()).quadrupalize(true, s1);
		StringBuilder sb = new StringBuilder();
		
		sb.append("if("+q.getSimpleExpressionText()+")"+(ifBlockStatement?"\n":"{\n"));
		for(StatementOrDeclarationContextExt statementOrDeclarationContextExt:s1){
			sb.append(statementOrDeclarationContextExt.getFormattedText()+"\n");
		}
		sb.append(ifBlockStatement?"\n":"}\n");
		for(Pair<VariableDeclarationContextExt, AssignmentStatementContextExt> p:q.getTempAssigns()){
			statementOrDeclarationContextExts.add(AbstractBaseExt.getStatementOrDeclaration(p.first().getFormattedText()));
			statementOrDeclarationContextExts.add(AbstractBaseExt.getStatementOrDeclaration(p.second().getFormattedText()));
		}
		statementOrDeclarationContextExts.add(AbstractBaseExt.getStatementOrDeclaration(sb.toString()));
	}
	
	@Override
	public void removeElse(List<StatementOrDeclarationContextExt> statementOrDeclarationContextExts, Stack<ExpressionContextExt> branchStack){
		IfStatementContext ifStatementContext = getContext();
		
		StatementContextExt statementContextExt = (StatementContextExt)getExtendedContext(getContext().statement());
		List<StatementOrDeclarationContextExt> statementOrDeclarationContextExts2 = new ArrayList<>();
		branchStack.push((ExpressionContextExt) getExtendedContext(ifStatementContext.expression()));
		statementContextExt.removeElse(statementOrDeclarationContextExts2,branchStack);
		StringBuilder sb = new StringBuilder();
		String expressionString = "";
		if(branchStack.size()==1){
			expressionString = getExtendedContext(ifStatementContext.expression()).getFormattedText();
		}else{
			String varName = Temporaries.nextTempIdByte();
			expressionString = varName;
			VariableDeclarationContextExt vd = AbstractBaseExt.getDeclaration("bit "+varName +";");
			AssignmentStatementContextExt assignmentStatementContextExt = AbstractBaseExt.getAssignment(varName +" = "+ExpressionContextExt.joinByAnd(branchStack)+";");
			statementOrDeclarationContextExts.add(AbstractBaseExt.getStatementOrDeclaration(vd.getFormattedText()));
			statementOrDeclarationContextExts.add(AbstractBaseExt.getStatementOrDeclaration(assignmentStatementContextExt.getFormattedText()));
		}
		
		sb.append("if("+expressionString+")");
		for (StatementOrDeclarationContextExt statementOrDeclarationContextExt : statementOrDeclarationContextExts2  )
		{
			sb.append(statementOrDeclarationContextExt.getFormattedText());
		}
		statementOrDeclarationContextExts.add(AbstractBaseExt.getStatementOrDeclaration(sb.toString()));
		branchStack.pop();
	}
	
	public void assignPredicate(Stack<ExpressionContextExt> stack){
		IfStatementContext ifStatementContext = getContext();
		stack.push((ExpressionContextExt) getExtendedContext(ifStatementContext.expression()));
		((StatementContextExt) getExtendedContext(ifStatementContext.statement())).assignPredicate(stack);
		stack.pop();
	}

	//SSA START
	@Override
	public void setVersions(SsaForm obj){
		Map<AbstractBaseExt,Integer> maxMap = new HashMap<>();
		List< Tuple3<AbstractBaseExt,Integer,Integer>> tuple3List = new ArrayList< Tuple3<AbstractBaseExt,Integer,Integer> > ();
		Map<AbstractBaseExt,Integer> oldminMap = obj.getMinMap();
		obj.setMinMap(new HashMap<AbstractBaseExt,Integer>());
		AbstractBaseExt oldParentCtx = obj.getParentCtx();
		obj.setParentCtx(getExtendedContext(getContext()));
		super.setVersions(obj);
		super.populateMaxMap(maxMap);
		for(Entry<AbstractBaseExt,Integer> entry:obj.getMinMap().entrySet()){
			AbstractBaseExt curCtx=entry.getKey();
			Integer minVer = entry.getValue();
			Integer maxVer= 0;
			if(maxMap.get(curCtx)!=null){
				maxVer=maxMap.get(curCtx);
			}
			tuple3List.add(new Tuple3<AbstractBaseExt,Integer,Integer>(curCtx,minVer,maxVer));
			Set<Integer> hashSet = obj.getPhiVer().get(curCtx);
			if(hashSet==null){
				hashSet = new HashSet<Integer>();
			}
			hashSet.add(minVer);
			hashSet.add(maxVer);
			obj.getPhiVer().put(curCtx,hashSet);
			curCtx.incVersion();
			obj.addPhiVer(new Pair(curCtx,maxVer+1));
			obj.hashSetRhsPhi.add(new Pair(curCtx,minVer));
			obj.hashSetRhsPhi.add(new Pair(curCtx,maxVer));
		}
		mapMinMax.put(getExtendedContext(getContext()),tuple3List);
		obj.setParentCtx(oldParentCtx);
		obj.setMinMap(oldminMap);
	}

	@Override
	public void getSSAFormattedText(Params params){
		Interval alignmentTextInterval = new Interval(params.getBeginingOfAlignemtText(),getContext().start.getStartIndex());
		String alignmentText = getContext().start.getInputStream().getText(alignmentTextInterval);
		params.getStringBuilder().append("\n\n");
		super.getSSAFormattedText(params);
		String printStr="\n";
		List< Tuple3<AbstractBaseExt,Integer,Integer>> listTuple3 = mapMinMax.get(getExtendedContext(getContext()));
		if(listTuple3!=null){
			for(Tuple3<AbstractBaseExt,Integer,Integer> tmpT3 :listTuple3){
				Integer phiVer=tmpT3.third()+1;
				printStr +=  tmpT3.first().getSymbolName()+"("+phiVer+")"+"<-phi("+tmpT3.second()+","+tmpT3.third()+")";
				if(ssaObject.hashSetRhsPhi.contains(new Pair(tmpT3.first(),phiVer))==false){
					printStr += "/*DEL*/";
				}
				printStr += "\n";
			}
		}
		params.getStringBuilder().append(printStr);
		params.setBeginingOfAlignemtText(getContext().stop.getStopIndex()+1);
	}
	//SSA END

	@Override
	public AbstractBaseExt replaceHeaderValid(){
		IfStatementContext ifStatementContext = getContext();
		StringBuilder sb  = new StringBuilder();
		sb.append("if(" + getExtendedContext(ifStatementContext.expression()).getFormattedText()+")\n" );
		sb.append(getExtendedContext(ifStatementContext.statement()).replaceHeaderValid().getFormattedText());
		IfStatementContext ifStatementContext1 = getContext(sb.toString());
		ifStatementContext1.extendedContext.setPredicateSymbol(this.predicateSymbol);
		setContext(ifStatementContext1);
		return getExtendedContext(ifStatementContext1);
	}

	@Override
	public void collectAllAssignmentOperations(List<IALUOperation> aluOperations){
		IfStatementContext ifStatementContext = getContext();
		if(((StatementContext)getExtendedContext(ifStatementContext.statement()).getContext()).blockStatement() != null){
			getExtendedContext(((StatementContext)getExtendedContext(ifStatementContext.statement()).getContext()).blockStatement()).collectAllAssignmentOperations(aluOperations);
		}else if(((StatementContext)getExtendedContext(ifStatementContext.statement()).getContext()).assignmentStatement() !=null){
			aluOperations.addAll(((AssignmentStatementContextExt) getExtendedContext(((StatementContext)getExtendedContext(ifStatementContext.statement()).getContext()).assignmentStatement())).allALUOperations());
		}else if(((StatementContext)getExtendedContext(ifStatementContext.statement()).getContext()).conditionalStatement() != null){
			getExtendedContext(((StatementContext)getExtendedContext(ifStatementContext.statement()).getContext()).conditionalStatement()).collectAllAssignmentOperations(aluOperations);
		}
	}
}
