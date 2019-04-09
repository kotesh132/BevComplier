package com.p4.p416.gen;

import java.util.ArrayList;
import java.util.List;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.drmt.parser.P4Transition;
import com.p4.p416.gen.P416Parser.ExpressionContext;
import com.p4.p416.gen.P416Parser.ExpressionListContext;
import com.p4.p416.gen.P416Parser.NameContext;
import com.p4.p416.gen.P416Parser.SelectExpressionContext;
import com.p4.p416.gen.P416Parser.StateExpressionContext;
import com.p4.p416.gen.P416Parser.TransitionStatementContext;
import com.p4.p416.iface.IExpression;
import com.p4.p416.iface.ITransitionStatement;

public class TransitionStatementContextExt extends AbstractBaseExt implements ITransitionStatement{

	public TransitionStatementContextExt(TransitionStatementContext ctx) {
		super(ctx);
	}

	@Override
	@SuppressWarnings("unchecked")
	public  TransitionStatementContext getContext(){
		return (TransitionStatementContext)contexts.get(contexts.size()-1);
	}

	@Override
	@SuppressWarnings("unchecked")
	public TransitionStatementContext getContext(String str){
		return (TransitionStatementContext)new PopulateExtendedContextVisitor().visit(getParser(str).transitionStatement());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof TransitionStatementContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ TransitionStatementContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}

	public P4Transition getTransitions() {
		TransitionStatementContext ctx = (TransitionStatementContext) getContext();
		P4Transition transition = new P4Transition();
		if(ctx.stateExpression().name()!=null){
			transition.setNextState(getExtendedContext(((StateExpressionContext)getExtendedContext(ctx.stateExpression()).getContext()).name()).getFormattedText());
		}else if(ctx.stateExpression().selectExpression()!=null){
			transition.setSelect(((SelectExpressionContextExt)getExtendedContext(((StateExpressionContext)getExtendedContext(ctx.stateExpression()).getContext()).selectExpression())).getSelect());
		}
		return transition;
	}

	@Override
	public Boolean hasNextInStateExpression(){
		if(super.hasNextInStateExpression()) return true;
		return false;
	}

	public String getNextVariable(){
		TransitionStatementContext ctx = (TransitionStatementContext) getContext();
		List<String> expWithNexts = new ArrayList<String>();
		if(getExtendedContext(ctx).hasNextInStateExpression()){
			if(((ITransitionStatement) getExtendedContext(ctx)).getSelectExpressionList().size()>0){
				List<IExpression> expressionList = ((ITransitionStatement) getExtendedContext(ctx)).getSelectExpressionList();
				for(IExpression exp : expressionList){
					if(((ExpressionContextExt) exp).hasNextInStateExpression()){
						expWithNexts.add(exp.getFormattedText());
					}
				}
			}
		}
		if(expWithNexts.size()>1){
			throw new RuntimeException("this transition state has variables with multiple nexts which we are not handling "+getExtendedContext(ctx).getFormattedText());
		}
		return expWithNexts.get(0);
	}

	public List<IExpression> getSelectExpressionList(){
		TransitionStatementContext ctx = (TransitionStatementContext) getContext();
		List<IExpression>  expressionList = new ArrayList<IExpression>();
		for(ExpressionContext exp : ((ExpressionListContext)getExtendedContext(((SelectExpressionContext)getExtendedContext(((StateExpressionContext)getExtendedContext(ctx.stateExpression()).getContext()).selectExpression()).getContext()).expressionList()).getContext()).expression()){
			expressionList.add((IExpression) getExtendedContext(exp));
		}
		return expressionList;
	}
	
	public Boolean hasSelectExpression(){
		TransitionStatementContext ctx = (TransitionStatementContext) getContext();
		if(((StateExpressionContext)getExtendedContext(ctx.stateExpression()).getContext()).selectExpression()!=null){
			return true;
		}
		return false;
	}

}
