package com.p4.p416.gen;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.drmt.compiler.Temporaries;
import com.p4.p416.gen.P416Parser.IfElseStatementContext;
import com.p4.p416.gen.P416Parser.IfStatementContext;
import com.p4.p416.gen.P416Parser.StatementContext;
import com.p4.tools.graph.structs.TupleStatementSimplify;
import com.p4.utils.Utils.Pair;

public class IfElseStatementContextExt extends ConditionalStatementContextExt {

	public IfElseStatementContextExt(IfElseStatementContext ctx) {
		super(ctx);
	}

	@SuppressWarnings("unchecked")
	@Override
	public  IfElseStatementContext getContext(){
		return (IfElseStatementContext)contexts.get(contexts.size()-1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public IfElseStatementContext getContext(String str){
		return (IfElseStatementContext)new PopulateExtendedContextVisitor().visit(getParser(str).conditionalStatement());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof IfElseStatementContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ IfElseStatementContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	@Override
	public void quadrupalize(boolean probe,List<StatementOrDeclarationContextExt> statementOrDeclarationContextExts){
		IfElseStatementContext ifElseStatementContext = getContext();
		TupleStatementSimplify q = ((ExpressionContextExt) getExtendedContext(getExtendedContext(ifElseStatementContext.expression()).getContext())).simplify();
		q=q.terminalize();
		List<StatementOrDeclarationContextExt> s1 = new ArrayList<>();
		boolean ifBlockStatement = true;
		if(((StatementContext)getExtendedContext(ifElseStatementContext.statement(0)).getContext()).blockStatement() == null){
			ifBlockStatement = false;
		}
		getExtendedContext(ifElseStatementContext.statement(0)).quadrupalize(true, s1);
		StringBuilder sb = new StringBuilder();
		
		sb.append("if("+q.getSimpleExpressionText()+")"+(ifBlockStatement?"\n":"{\n"));
		for(StatementOrDeclarationContextExt statementOrDeclarationContextExt:s1){
			sb.append(statementOrDeclarationContextExt.getFormattedText()+"\n");
		}
		sb.append(ifBlockStatement?"else\n":"}else\n");
		s1 = new ArrayList<>();
		ifBlockStatement = true;
		if(((StatementContext)getExtendedContext(ifElseStatementContext.statement(1)).getContext()).blockStatement() == null){
			ifBlockStatement = false;
		}
		sb.append(ifBlockStatement?"\n":"{\n");
		getExtendedContext(ifElseStatementContext.statement(1)).quadrupalize(true, s1);
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
		IfElseStatementContext ctx = getContext();
		//PUSH EXPRESSION into Stack
		ExpressionContextExt originalExpr = (ExpressionContextExt) getExtendedContext(ctx.expression());
		branchStack.push((ExpressionContextExt) getExtendedContext(ctx.expression()));
		
		String expressionString = "";
		if(branchStack.size()==1){
			expressionString = getExtendedContext(ctx.expression()).getFormattedText();
		}else{
			String varName = Temporaries.nextTempIdByte();
			expressionString = varName;
			VariableDeclarationContextExt vd = AbstractBaseExt.getDeclaration("bit "+varName +";");
			AssignmentStatementContextExt assignmentStatementContextExt = AbstractBaseExt.getAssignment(varName +" = "+ExpressionContextExt.joinByAnd(branchStack)+";");
			statementOrDeclarationContextExts.add(AbstractBaseExt.getStatementOrDeclaration(vd.getFormattedText()));
			statementOrDeclarationContextExts.add(AbstractBaseExt.getStatementOrDeclaration(assignmentStatementContextExt.getFormattedText()));
		}
		
		StatementContextExt statementContextExt = (StatementContextExt)getExtendedContext(ctx.statement(0));
		List<StatementOrDeclarationContextExt> statementOrDeclarationContextExts2 = new ArrayList<>();
		statementContextExt.removeElse(statementOrDeclarationContextExts2, branchStack);
		StringBuilder sb = new StringBuilder();
		for (StatementOrDeclarationContextExt statementOrDeclarationContextExt : statementOrDeclarationContextExts2  )
		{
			sb.append(statementOrDeclarationContextExt.getFormattedText());
		}
		
		String firstIf = "if("+expressionString+")\n"+sb.toString();
		IfStatementContextExt ifStatementContextExt = new IfStatementContextExt(null);
		IfStatementContext ifStatementContext = ifStatementContextExt.getContext(firstIf);
		statementOrDeclarationContextExts.add(AbstractBaseExt.getStatementOrDeclaration(getExtendedContext(ifStatementContext).getFormattedText()));
		
		String complimentif = Temporaries.nextTempIdByte();
		VariableDeclarationContextExt newComplimentV = AbstractBaseExt.getDeclaration("bit "+ complimentif+";");
		AssignmentStatementContextExt newComplimentAssign = AbstractBaseExt.getAssignment(complimentif+"=!("+originalExpr.getFormattedText()+");");
		statementOrDeclarationContextExts.add(AbstractBaseExt.getStatementOrDeclaration(newComplimentV.getFormattedText()));
		statementOrDeclarationContextExts.add(AbstractBaseExt.getStatementOrDeclaration(newComplimentAssign.getFormattedText()));
		branchStack.pop();
		branchStack.push(AbstractBaseExt.getExpression(complimentif));
		
		String newif = Temporaries.nextTempIdByte();
		VariableDeclarationContextExt newV = AbstractBaseExt.getDeclaration("bit "+ newif+";");
		AssignmentStatementContextExt assignmentStatementContextExt = AbstractBaseExt.getAssignment(newif +"="+ExpressionContextExt.joinByAnd(branchStack)+";");
		statementOrDeclarationContextExts.add(AbstractBaseExt.getStatementOrDeclaration(newV.getFormattedText()));
		statementOrDeclarationContextExts.add(AbstractBaseExt.getStatementOrDeclaration(assignmentStatementContextExt.getFormattedText()));
		
		statementContextExt = (StatementContextExt)getExtendedContext(ctx.statement(1));
		statementOrDeclarationContextExts2 = new ArrayList<>();
		
		
		statementContextExt.removeElse(statementOrDeclarationContextExts2,branchStack);
		sb = new StringBuilder();
		for (StatementOrDeclarationContextExt statementOrDeclarationContextExt : statementOrDeclarationContextExts2  )
		{
			sb.append(statementOrDeclarationContextExt.getFormattedText());
		}
		String secondIf = "if("+newif+")\n"+sb.toString();
		IfStatementContextExt secondifStatementContextExt = new IfStatementContextExt(null);
		IfStatementContext secondifStatementContext = secondifStatementContextExt.getContext(secondIf);
		statementOrDeclarationContextExts.add(AbstractBaseExt.getStatementOrDeclaration(getExtendedContext(secondifStatementContext).getFormattedText()));
		branchStack.pop();
	}

	@Override
	public AbstractBaseExt replaceHeaderValid(){
		IfElseStatementContext ifElseStatementContext = getContext();
		StringBuilder sb  = new StringBuilder();
		sb.append("if(" + getExtendedContext(ifElseStatementContext.expression()).getFormattedText()+")\n" );
		sb.append(getExtendedContext(ifElseStatementContext.statement(0)).replaceHeaderValid().getFormattedText());
		sb.append("\nelse\n");
		sb.append(getExtendedContext(ifElseStatementContext.statement(1)).replaceHeaderValid().getFormattedText());
		IfElseStatementContext ifElseStatementContext1 = getContext(sb.toString());
		ifElseStatementContext.extendedContext.setPredicateSymbol(this.predicateSymbol);
		setContext(ifElseStatementContext1);
		return getExtendedContext(ifElseStatementContext1);
	}
}
