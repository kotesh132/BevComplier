package com.p4.p416.gen;

import java.util.List;

import  org.antlr.v4.runtime.ParserRuleContext;

import com.p4.p416.gen.P416Parser.ReturnStatementContext;

public class ReturnStatementContextExt extends AbstractBaseExt {

	public ReturnStatementContextExt(ReturnStatementContext ctx) {
		super(ctx);
	}

	@Override
	public  ReturnStatementContext getContext(){
		return (ReturnStatementContext)contexts.get(contexts.size()-1);
	}

	@Override
	public ReturnStatementContext getContext(String str){
		return (ReturnStatementContext)new PopulateExtendedContextVisitor().visit(getParser(str).returnStatement());
	}

	@Override
	public void setContext(ParserRuleContext ctx){
		if(ctx != null){
			if(ctx instanceof ReturnStatementContext){
				addToContexts(ctx);
			} else {
				throw new ClassCastException(ctx.getClass().getSimpleName() + " cannot be casted to "+ ReturnStatementContext.class.getName());
			}
		} else {
			addToContexts(null);
		}
	}
	
	//GL - ControlBlock START

	@Override
	public StatementType getStatementType()
	{
		return StatementType.RETURN_STATEMENT;
	}
	//GL - ControlBlock END
	@Override
	public void quadrupalize(boolean probe,List<StatementOrDeclarationContextExt> statementOrDeclarationContextExts){
		statementOrDeclarationContextExts.add(AbstractBaseExt.getStatementOrDeclaration(getFormattedText()));
	}
	
}
